from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.staticfiles import StaticFiles
import cloudinary
import cloudinary.uploader
import os, random, uuid, io, requests
import numpy as np
import onnxruntime as ort
from PIL import Image, ImageOps, ImageDraw, ImageChops, ImageFilter

app = FastAPI()
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"]
)

cloudinary.config(
    cloud_name = os.getenv("CLOUDINARY_CLOUD_NAME", ""),
    api_key    = os.getenv("CLOUDINARY_API_KEY", ""),
    api_secret = os.getenv("CLOUDINARY_API_SECRET", "")
)

WIG_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "wigs")
app.mount("/wigs", StaticFiles(directory=WIG_DIR), name="wigs")

GENDER_MODEL_PATH = os.path.join(os.path.dirname(os.path.abspath(__file__)), "models", "genderage.onnx")
_gender_session = None


def _get_gender_session():
    global _gender_session
    if _gender_session is None:
        _gender_session = ort.InferenceSession(GENDER_MODEL_PATH, providers=["CPUExecutionProvider"])
    return _gender_session


def detect_gender(img: Image.Image, face_bbox: list) -> str:
    """Detect 'male' or 'female' from a face crop using the InsightFace
    genderage.onnx model (96x96 aligned input, output[:2] = gender logits).
    Falls back to 'unisex' on any failure so style filtering never hard-blocks
    a user from seeing hair styles."""
    try:
        session    = _get_gender_session()
        input_name = session.get_inputs()[0].name
        size       = session.get_inputs()[0].shape[2]  # 96

        fx, fy, fw, fh = face_bbox[:4]
        x1, y1, x2, y2 = fx, fy, fx + fw, fy + fh
        center_x, center_y = (x1 + x2) / 2, (y1 + y2) / 2
        scale = size / (max(x2 - x1, y2 - y1) * 1.5)

        # Forward alignment is dst = scale*src + t. PIL's Image.transform
        # wants the inverse (dst -> src) mapping for AFFINE resampling.
        inv_scale = 1.0 / scale
        tx = size / 2 - center_x * scale
        ty = size / 2 - center_y * scale
        aligned = img.convert("RGB").transform(
            (size, size), Image.AFFINE,
            (inv_scale, 0, -tx * inv_scale, 0, inv_scale, -ty * inv_scale),
            resample=Image.BILINEAR
        )

        arr  = np.asarray(aligned, dtype=np.float32)   # (size, size, 3) RGB
        blob = arr.transpose(2, 0, 1)[None, ...]        # (1, 3, size, size)
        output = session.run(None, {input_name: blob})[0][0]
        gender_idx = int(output[:2].argmax())
        return "male" if gender_idx == 1 else "female"
    except Exception:
        return "unisex"

BACKEND_URL = os.getenv("BACKEND_URL", "https://styleme-api.onrender.com")

FACE_SHAPES = [
    {"id": 1, "name": "oval",    "label": "Oval"},
    {"id": 2, "name": "round",   "label": "Round"},
    {"id": 3, "name": "square",  "label": "Square"},
    {"id": 4, "name": "heart",   "label": "Heart"},
    {"id": 5, "name": "oblong",  "label": "Oblong"},
    {"id": 6, "name": "diamond", "label": "Diamond"},
]

HAIR_COLOURS = [
    {"id": 1,  "name": "jet_black",       "html_code": "#0A0A0A"},
    {"id": 2,  "name": "dark_brown",      "html_code": "#3B1F0E"},
    {"id": 3,  "name": "medium_brown",    "html_code": "#6B3A2A"},
    {"id": 4,  "name": "light_brown",     "html_code": "#A0522D"},
    {"id": 5,  "name": "dirty_blonde",    "html_code": "#C8A96E"},
    {"id": 6,  "name": "golden_blonde",   "html_code": "#F0C040"},
    {"id": 7,  "name": "platinum_blonde", "html_code": "#F5E6C8"},
    {"id": 8,  "name": "auburn",          "html_code": "#922B21"},
    {"id": 9,  "name": "copper_red",      "html_code": "#C0392B"},
    {"id": 10, "name": "bright_red",      "html_code": "#E74C3C"},
    {"id": 11, "name": "rose_gold",       "html_code": "#E8B4B8"},
    {"id": 12, "name": "pastel_pink",     "html_code": "#FFB6C1"},
    {"id": 13, "name": "electric_blue",   "html_code": "#1A73E8"},
    {"id": 14, "name": "violet_purple",   "html_code": "#8E44AD"},
    {"id": 15, "name": "silver_grey",     "html_code": "#95A5A6"},
]

HAIR_STYLES = [
    {"id": 1,  "name": "messy_textured",    "label": "Messy Textured",    "gender": "male"},
    {"id": 2,  "name": "classic_pompadour", "label": "Classic Pompadour", "gender": "male"},
    {"id": 3,  "name": "short_slick",       "label": "Short Slick",       "gender": "male"},
    {"id": 4,  "name": "side_sweep",        "label": "Side Sweep",        "gender": "male"},
    {"id": 6,  "name": "blaze_crop",        "label": "Blaze Crop",        "gender": "male"},
    {"id": 7,  "name": "bangs_black",       "label": "Bangs Black Hair",  "gender": "male"},
    {"id": 8,  "name": "long_straight",     "label": "Long Straight",     "gender": "female"},
    {"id": 9,  "name": "curly",             "label": "Curly",             "gender": "female"},
    {"id": 10, "name": "wavy_luxurious",    "label": "Wavy Luxurious",    "gender": "female"},
]

# Map style id -> wig PNG filename (served from /wigs/ static endpoint)
WIG_FILES = {
    1: "hair_messy.png",
    2: "hair_pompadour1.png",
    3: "hair_short_black.png",
    4: "hair_pompadour2.png",
    6: "hair_blaze_crop.png",
    7: "hair_bangs_black.png",
    8: "hair_long_straight_w.png",
    9: "hair_curly_w.png",
    10: "hair_wavy_luxurious_w.png",
}

MODEL_PICTURES = [
    {"id": 1, "file_name": "Messy Textured",    "file_path": None, "hair_style_id": 1, "face_shape_id": None, "hair_length_id": 1},
    {"id": 2, "file_name": "Classic Pompadour", "file_path": None, "hair_style_id": 2, "face_shape_id": None, "hair_length_id": 2},
    {"id": 3, "file_name": "Short Slick",       "file_path": None, "hair_style_id": 3, "face_shape_id": None, "hair_length_id": 1},
    {"id": 4, "file_name": "Side Sweep",        "file_path": None, "hair_style_id": 4, "face_shape_id": None, "hair_length_id": 2},
    {"id": 6, "file_name": "Blaze Crop",        "file_path": None, "hair_style_id": 6, "face_shape_id": None, "hair_length_id": 1},
    {"id": 7, "file_name": "Bangs Black Hair",  "file_path": None, "hair_style_id": 7, "face_shape_id": None, "hair_length_id": 2},
    {"id": 8, "file_name": "Long Straight",     "file_path": None, "hair_style_id": 8, "face_shape_id": None, "hair_length_id": 2},
    {"id": 9, "file_name": "Curly",             "file_path": None, "hair_style_id": 9, "face_shape_id": None, "hair_length_id": 2},
    {"id": 10,"file_name": "Wavy Luxurious",    "file_path": None, "hair_style_id": 10, "face_shape_id": None, "hair_length_id": 2},
]

pictures_store = {}

def make_placeholder_pic(picture_id: int):
    return {
        "id": picture_id, "file_name": "photo.jpg",
        "file_path": None, "public_id": None,
        "file_size": "0", "height": None,
        "width": None, "date_created": "", "face": None
    }

# Per-style overlay tuning.
# "w" = wig width as a multiple of the detected face width.
# Positioning is driven by crown anchor (see composite_wig), not a per-style param.
WIG_OVERLAY_PARAMS = {
    1: {"w": 1.20},  # Messy Textured
    2: {"w": 1.20},  # Classic Pompadour
    3: {"w": 1.20},  # Short Slick
    4: {"w": 1.05},  # Side Sweep (large PNG)
    6: {"w": 1.20},  # Blaze Crop
    7: {"w": 1.20},  # Bangs Black Hair
    8: {"w": 1.20},  # Long Straight
    9: {"w": 1.20},  # Curly
    10: {"w": 1.20}, # Wavy Luxurious
}
DEFAULT_WIG_PARAMS = {"w": 1.15}


def hair_content_fractions(img_rgba: Image.Image):
    """Return (top_frac, bottom_frac) — where opaque hair content starts and
    ends vertically as fractions of the PNG height.  Uses getbbox() on a
    binarised alpha channel so the scan runs at C speed."""
    alpha    = img_rgba.split()[3]
    alpha_bin = alpha.point(lambda a: 255 if a > 30 else 0)
    bbox = alpha_bin.getbbox()          # (left, top, right, bottom) or None
    if bbox:
        return bbox[1] / img_rgba.height, bbox[3] / img_rgba.height
    return 0.0, 1.0


def apply_bottom_fade(img_rgba: Image.Image,
                      fade_fraction: float = 0.25,
                      side_fraction: float = 0.12) -> Image.Image:
    """Fade wig alpha at the bottom and sides for natural skin blending.

    fade_fraction — bottom N% fades to transparent (hairline / forehead edge).
    side_fraction — left/right N% fades to transparent (temple / sideburn edge).
    Both gradients are multiplied together so corners fade smoothly.
    """
    w, h = img_rgba.size

    # Horizontal gradient: 0 at left/right edges, 255 in centre
    fade_w = round(w * side_fraction)
    h_line = Image.new("L", (w, 1), 255)
    for dx in range(fade_w):
        v = round(255 * dx / fade_w)
        h_line.putpixel((dx, 0), v)
        h_line.putpixel((w - 1 - dx, 0), v)
    horiz_grad = h_line.resize((w, h), Image.NEAREST)

    # Vertical gradient: 255 at top, 0 at bottom edge
    fade_h = round(h * fade_fraction)
    v_line = Image.new("L", (1, h), 255)
    for dy in range(fade_h):
        v_line.putpixel((0, h - fade_h + dy), round(255 * (1.0 - dy / fade_h)))
    vert_grad = v_line.resize((w, h), Image.NEAREST)

    # Combine both gradients, then multiply into the wig's existing alpha
    gradient = ImageChops.multiply(horiz_grad, vert_grad)
    alpha = img_rgba.split()[3]
    new_alpha = ImageChops.multiply(alpha, gradient)
    r, g, b, _ = img_rgba.split()
    return Image.merge("RGBA", (r, g, b, new_alpha))


def remove_white_background(img: Image.Image,
                             hard_threshold: int = 240,
                             soft_threshold: int = 160) -> Image.Image:
    """
    Remove white/near-white background from a wig PNG using luminance-based
    graduated alpha so anti-aliased hair edges blend naturally.

    Pixels brighter than hard_threshold → fully transparent.
    Pixels between soft_threshold and hard_threshold → graduated alpha.
    Pixels darker than soft_threshold → fully opaque (hair).
    PIL's .point() runs in C so this stays fast even on large images.
    """
    img = img.convert("RGBA")
    luminance = img.convert("L")   # fast single-channel luminance

    span = hard_threshold - soft_threshold

    def lum_to_alpha(lum: int) -> int:
        if lum >= hard_threshold:
            return 0
        if lum <= soft_threshold:
            return 255
        return int(255 * (hard_threshold - lum) / span)

    new_alpha = luminance.point(lum_to_alpha)
    img.putalpha(new_alpha)
    return img


def composite_wig(user_photo_url: str, wig_filename: str,
                  face: list, style_id: int) -> bytes:
    """
    Download the user's photo, load the wig PNG, composite them with PIL,
    and return the result as JPEG bytes.

    Uses Image.alpha_composite so that transparent wig areas correctly reveal
    the user's face beneath (not a black hole or checkerboard).
    """
    params = WIG_OVERLAY_PARAMS.get(style_id, DEFAULT_WIG_PARAMS)

    # Load user photo.
    # Apply EXIF transpose so PIL's pixel grid matches the orientation that
    # Cloudinary's face detection analysed (avoids coordinate mismatch on
    # photos shot in portrait but stored as landscape bytes).
    resp = requests.get(user_photo_url, timeout=20)
    resp.raise_for_status()
    user_img = ImageOps.exif_transpose(
        Image.open(io.BytesIO(resp.content))
    ).convert("RGB").convert("RGBA")

    # Load wig — convert to RGBA regardless of source mode (P, LA, RGB, …)
    wig_path = os.path.join(WIG_DIR, wig_filename)
    wig_raw  = Image.open(wig_path)
    if wig_raw.mode == "P":
        wig_raw = wig_raw.convert("RGBA")
    else:
        wig_raw = wig_raw.convert("RGBA")

    # Downscale oversized source assets before any per-pixel work below.
    # Some wig PNGs (e.g. hair_bangs_black.png) ship at multi-thousand-pixel
    # resolution; the per-pixel alpha scan + erosion filter on the full-size
    # image is slow/memory-heavy enough to time out the request (502 on
    # Render). The final overlay is resized down to a few hundred px anyway,
    # so capping here costs no visible quality.
    MAX_WIG_DIM = 1200
    if max(wig_raw.size) > MAX_WIG_DIM:
        wig_raw.thumbnail((MAX_WIG_DIM, MAX_WIG_DIM), Image.LANCZOS)

    # Strip white background if the wig has no meaningful transparency already
    alpha_vals = list(wig_raw.split()[3].getdata())
    has_transparency = any(a < 200 for a in alpha_vals)
    if not has_transparency:
        wig_raw = remove_white_background(wig_raw)

    # Erode alpha by 1 px on ALL wigs (transparent-bg and white-bg alike).
    # Clips semi-transparent fringe pixels at the wig boundary that otherwise
    # appear as a bright/grey stripe where the wig meets the person's hair.
    _a = wig_raw.split()[3].filter(ImageFilter.MinFilter(3))
    _r, _g, _b, _ = wig_raw.split()
    wig_raw = Image.merge("RGBA", (_r, _g, _b, _a))

    # Find where actual hair content starts and ends in the PNG.
    hair_top_frac, hair_bottom_frac = hair_content_fractions(wig_raw)

    # Compute overlay size from face bbox [x, y, w, h]
    fx, fy, fw, fh = face[:4]

    overlay_w = round(fw * params["w"])
    overlay_w = min(overlay_w, round(user_img.width * 0.75))
    aspect    = wig_raw.height / wig_raw.width
    overlay_h = round(overlay_w * aspect)
    overlay_h = min(overlay_h, round(fh * 0.80))  # hard upper cap

    wig_resized = wig_raw.resize((overlay_w, overlay_h), Image.LANCZOS)
    wig_resized = apply_bottom_fade(wig_resized, fade_fraction=0.25, side_fraction=0.12)

    # Crown anchor: place wig so the first opaque hair pixel in the PNG lands
    # exactly at fy (the top of the detected face bbox).
    # crown_margin=0 lands the hair crown on fy; a positive value shifts it UP
    # (above fy), a negative value shifts it DOWN (into the face area).
    crown_margin  = 0
    hair_crown_px = round(hair_top_frac * overlay_h)
    x = round(fx + fw / 2 - overlay_w / 2)
    y = (fy - crown_margin) - hair_crown_px

    # Place the resized wig on a transparent canvas the same size as the photo,
    # clipping any parts that go outside the frame.
    wig_layer = Image.new("RGBA", user_img.size, (0, 0, 0, 0))
    paste_x = max(0, x)
    paste_y = max(0, y)
    crop_x  = paste_x - x   # >0 when wig extends left of frame
    crop_y  = paste_y - y   # >0 when wig extends above frame
    if crop_x > 0 or crop_y > 0:
        wig_resized = wig_resized.crop((crop_x, crop_y, overlay_w, overlay_h))
    wig_layer.paste(wig_resized, (paste_x, paste_y))

    # Alpha-composite: wig sits on top; transparent wig pixels → user photo shows
    result_rgba = Image.alpha_composite(user_img, wig_layer)

    # Flatten to RGB and encode as JPEG
    buf = io.BytesIO()
    result_rgba.convert("RGB").save(buf, format="JPEG", quality=92)
    return buf.getvalue()


@app.get("/health")
def health():
    return {"status": "ok", "pictures_in_memory": len(pictures_store)}

@app.get("/debug/composite_params")
def debug_composite_params(user_picture_id: int, model_picture_id: int):
    """Return face bbox, image size, wig bottom fraction, and y-placement
    without actually compositing.  Call after uploading a photo to diagnose
    positioning issues."""
    pic = pictures_store.get(user_picture_id)
    if not pic:
        raise HTTPException(status_code=404, detail="picture not found — upload first")
    face = pic.get("face")
    file_path = pic.get("file_path")
    if not face or not file_path:
        raise HTTPException(status_code=400, detail="no face detected or no photo URL")

    model    = next((m for m in MODEL_PICTURES if m["id"] == model_picture_id), None)
    style_id = model["hair_style_id"] if model else model_picture_id
    wig_file = WIG_FILES.get(style_id)
    if not wig_file:
        raise HTTPException(status_code=404, detail="unknown style")

    params = WIG_OVERLAY_PARAMS.get(style_id, DEFAULT_WIG_PARAMS)

    resp = requests.get(file_path, timeout=20)
    user_img = ImageOps.exif_transpose(
        Image.open(io.BytesIO(resp.content))
    ).convert("RGB")
    img_w, img_h = user_img.size

    wig_raw = Image.open(os.path.join(WIG_DIR, wig_file))
    wig_raw = wig_raw.convert("RGBA")
    alpha_vals = list(wig_raw.split()[3].getdata())
    if not any(a < 200 for a in alpha_vals):
        wig_raw = remove_white_background(wig_raw)
    hair_bottom_frac = hair_content_bottom_fraction(wig_raw)
    aspect = wig_raw.height / wig_raw.width

    fx, fy, fw, fh = face[:4]
    overlay_w = min(round(fw * params["w"]), round(img_w * 0.85))
    overlay_h = min(round(overlay_w * aspect), round(fh * 0.65))
    actual_hair_bottom_px = round(hair_bottom_frac * overlay_h)
    forehead_y = round(fy + params["forehead"] * fh)
    y = forehead_y - actual_hair_bottom_px

    return {
        "image_size":         {"w": img_w, "h": img_h},
        "face_bbox_raw":      face,
        "face_bbox":          {"fx": fx, "fy": fy, "fw": fw, "fh": fh},
        "face_pct_of_image":  {"fy": round(fy/img_h, 3), "fh": round(fh/img_h, 3),
                               "fx": round(fx/img_w, 3), "fw": round(fw/img_w, 3)},
        "wig":                {"file": wig_file, "aspect": round(aspect, 3),
                               "hair_bottom_frac": round(hair_bottom_frac, 3)},
        "overlay":            {"w": overlay_w, "h": overlay_h,
                               "actual_hair_bottom_px": actual_hair_bottom_px,
                               "forehead_y": forehead_y, "y": y,
                               "x": round(fx + fw/2 - overlay_w/2)},
        "params":             params,
    }

@app.get("/hair_colours")
def get_hair_colours():
    return HAIR_COLOURS

@app.get("/hair_styles")
def get_hair_styles(user_picture_id: int = None):
    """Return hair styles. If user_picture_id is given and that picture has a
    detected gender, only styles matching that gender (or 'unisex') are
    returned. Without it (or if detection was inconclusive), all styles
    are returned so older app builds keep working unchanged."""
    if user_picture_id is not None:
        pic = pictures_store.get(user_picture_id)
        gender = pic.get("gender") if pic else None
        if gender in ("male", "female"):
            return [s for s in HAIR_STYLES if s["gender"] in (gender, "unisex")]
    return HAIR_STYLES

@app.get("/face_shapes")
def get_face_shapes():
    return FACE_SHAPES

@app.get("/model_pictures")
def get_model_pictures(user_picture_id: int = None):
    allowed_ids = {s["id"] for s in get_hair_styles(user_picture_id)}
    result = []
    for m in MODEL_PICTURES:
        if m["hair_style_id"] not in allowed_ids:
            continue
        pic = dict(m)
        wig_file = WIG_FILES.get(m["hair_style_id"])
        if wig_file:
            pic["file_path"] = f"{BACKEND_URL}/wigs/{wig_file}"
        result.append(pic)
    return result

@app.post("/pictures")
async def upload_picture(file: UploadFile = File(...)):
    try:
        contents = await file.read()
        result = cloudinary.uploader.upload(
            contents,
            folder="styleme/user_photos",
            resource_type="image",
            faces=True
        )
        faces = result.get("faces") or []
        face = faces[0] if faces else None

        gender = "unisex"
        if face:
            upload_img = ImageOps.exif_transpose(Image.open(io.BytesIO(contents)))
            gender = detect_gender(upload_img, face)

        picture_id = abs(hash(result["public_id"])) % 1000000
        picture = {
            "id":           picture_id,
            "file_name":    result.get("original_filename", "photo") + ".jpg",
            "file_path":    result["secure_url"],
            "public_id":    result["public_id"],
            "file_size":    str(result.get("bytes", 0)),
            "height":       result.get("height"),
            "width":        result.get("width"),
            "date_created": str(result.get("created_at", "")),
            "face":         face,
            "gender":       gender
        }
        pictures_store[picture_id] = picture
        face_shape = random.choice(FACE_SHAPES)
        history_id = abs(hash(str(uuid.uuid4()))) % 1000000
        history = {
            "id":                  history_id,
            "picture_id":          picture_id,
            "original_picture_id": picture_id,
            "face_shape_id":       face_shape["id"],
            "date_created":        picture["date_created"]
        }
        return {
            "picture":       picture,
            "face_shape":    face_shape,
            "history_entry": history,
            "id":            picture_id,
            "file_name":     picture["file_name"],
            "message":       "Upload successful"
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/pictures/file/{picture_id}")
def get_picture_file(picture_id: int):
    pic = pictures_store.get(picture_id, make_placeholder_pic(picture_id))
    return {"url": pic.get("file_path"), "picture": pic}

@app.get("/pictures/change_hair_colour/{picture_id}")
def change_hair_colour(picture_id: int, colour: str, r: int, g: int, b: int):
    pic        = pictures_store.get(picture_id, make_placeholder_pic(picture_id))
    public_id  = pic.get("public_id")
    cloud_name = os.getenv("CLOUDINARY_CLOUD_NAME", "")
    hex_color  = f"{r:02X}{g:02X}{b:02X}"

    if public_id and cloud_name:
        transformed_url = (
            f"https://res.cloudinary.com/{cloud_name}/image/upload/"
            f"e_colorize:60,co_rgb:{hex_color}/{public_id}"
        )
    else:
        transformed_url = pic.get("file_path")

    new_id  = abs(hash(f"{picture_id}_{colour}_{hex_color}")) % 1000000
    new_pic = {**pic, "id": new_id, "file_path": transformed_url, "public_id": public_id}
    pictures_store[new_id] = new_pic

    hair_colour = next((c for c in HAIR_COLOURS if c["name"] == colour), None)
    return {
        "picture":       new_pic,
        "hair_colour":   hair_colour,
        "history_entry": {
            "id":                  abs(hash(str(uuid.uuid4()))) % 1000000,
            "picture_id":          new_id,
            "original_picture_id": picture_id,
            "hair_colour_id":      hair_colour["id"] if hair_colour else None
        }
    }

@app.get("/pictures/change_hair_style")
def change_hair_style(user_picture_id: int, model_picture_id: int):
    pic      = pictures_store.get(user_picture_id, make_placeholder_pic(user_picture_id))
    face     = pic.get("face")
    file_path = pic.get("file_path")

    model    = next((m for m in MODEL_PICTURES if m["id"] == model_picture_id), None)
    style_id = model["hair_style_id"] if model else model_picture_id
    wig_file = WIG_FILES.get(style_id)

    result_url = file_path or ""

    if file_path and wig_file and face:
        try:
            result_bytes = composite_wig(file_path, wig_file, face, style_id)
            upload_res   = cloudinary.uploader.upload(
                result_bytes,
                folder="styleme/styled",
                resource_type="image"
            )
            result_url = upload_res["secure_url"]
            new_pub_id = upload_res["public_id"]
        except Exception as e:
            result_url = file_path
            new_pub_id = pic.get("public_id")
    else:
        new_pub_id = pic.get("public_id")

    new_id  = abs(hash(f"{user_picture_id}_style_{style_id}")) % 1000000
    new_pic = {**pic, "id": new_id, "file_path": result_url, "public_id": new_pub_id}
    pictures_store[new_id] = new_pic

    return {
        "current_picture":  new_pic,
        "original_picture": pic,
        "history_entry": {
            "id":                  abs(hash(str(uuid.uuid4()))) % 1000000,
            "picture_id":          new_id,
            "original_picture_id": user_picture_id,
            "hair_style_id":       style_id
        }
    }

@app.delete("/pictures/discard_changes/{original_picture_id}")
def discard_changes(original_picture_id: int):
    pic = pictures_store.get(original_picture_id, make_placeholder_pic(original_picture_id))
    return {"current_picture": pic, "history": None}

@app.get("/history/latest")
def get_latest_history():
    return {"history_entry": None, "current_picture": None, "original_picture": None}

@app.get("/pictures/register")
def register_picture(picture_id: int, url: str, public_id: str):
    pictures_store[picture_id] = {
        "id": picture_id, "file_name": "photo.jpg",
        "file_path": url, "public_id": public_id,
        "file_size": "0", "height": None, "width": None,
        "date_created": "", "face": None
    }
    return {"status": "registered", "picture_id": picture_id}
