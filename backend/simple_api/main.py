from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.staticfiles import StaticFiles
import cloudinary
import cloudinary.uploader
import os, random, uuid, io, requests
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
    {"id": 1, "name": "messy_textured",    "label": "Messy Textured"},
    {"id": 2, "name": "classic_pompadour", "label": "Classic Pompadour"},
    {"id": 3, "name": "short_slick",       "label": "Short Slick"},
    {"id": 4, "name": "side_sweep",        "label": "Side Sweep"},
]

# Map style id -> wig PNG filename (served from /wigs/ static endpoint)
WIG_FILES = {
    1: "hair_messy.png",
    2: "hair_pompadour1.png",
    3: "hair_short_black.png",
    4: "hair_pompadour2.png",
}

MODEL_PICTURES = [
    {"id": 1, "file_name": "Messy Textured",    "file_path": None, "hair_style_id": 1, "face_shape_id": None, "hair_length_id": 1},
    {"id": 2, "file_name": "Classic Pompadour", "file_path": None, "hair_style_id": 2, "face_shape_id": None, "hair_length_id": 2},
    {"id": 3, "file_name": "Short Slick",       "file_path": None, "hair_style_id": 3, "face_shape_id": None, "hair_length_id": 1},
    {"id": 4, "file_name": "Side Sweep",        "file_path": None, "hair_style_id": 4, "face_shape_id": None, "hair_length_id": 2},
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
# "w"        = wig width as a multiple of the detected face width
# "forehead" = fraction of face height where the wig bottom edge sits
#              (0.15 = wig bottom lands at top 15% of face = upper forehead)
#              Anchoring from the wig-bottom avoids the wig floating off-frame
#              on passport / close-up photos where fy ≈ 0.
WIG_OVERLAY_PARAMS = {
    1: {"w": 1.20, "forehead": 0.32},  # Messy Textured
    2: {"w": 1.20, "forehead": 0.28},  # Classic Pompadour (confirmed working)
    3: {"w": 1.20, "forehead": 0.28},  # Short Slick
    4: {"w": 1.05, "forehead": 0.42},  # Side Sweep (large PNG, full-coverage hair)
}
DEFAULT_WIG_PARAMS = {"w": 1.15, "forehead": 0.28}


def hair_content_bottom_fraction(img_rgba: Image.Image) -> float:
    """Return the fraction (0-1) of the PNG height where the last opaque hair
    pixel is found.  Uses PIL getbbox() on a binarised alpha channel (C-level
    scan) for efficiency.  Falls back to 1.0 if nothing is found.

    Wig PNGs typically have the hair crown at the top and transparent space at
    the bottom.  Anchoring to this value (rather than the full image height)
    means the real hair bottom aligns with the target forehead line instead of
    the PNG's empty lower region.
    """
    alpha = img_rgba.split()[3]
    alpha_bin = alpha.point(lambda a: 255 if a > 30 else 0)
    bbox = alpha_bin.getbbox()  # (left, top, right, bottom) or None
    if bbox:
        return bbox[3] / img_rgba.height
    return 1.0


def apply_bottom_fade(img_rgba: Image.Image, fade_fraction: float = 0.35) -> Image.Image:
    """Fade the alpha channel to 0 over the bottom `fade_fraction` of the wig.

    This removes the hard horizontal edge that appears where the wig meets
    the face, making the hairstyle blend naturally into the skin rather than
    ending with a sharp line.  A fade_fraction of 0.35 means the bottom 35 %
    of the wig (e.g. sideburns / hair ends) gradually dissolves to transparent.
    """
    w, h = img_rgba.size
    fade_h = round(h * fade_fraction)
    fade_start = h - fade_h

    # Build a linear gradient mask: 255 above fade_start, 0 at image bottom
    gradient = Image.new("L", (w, h), 255)
    draw = ImageDraw.Draw(gradient)
    for dy in range(fade_h):
        value = round(255 * (1.0 - dy / fade_h))
        draw.line([(0, fade_start + dy), (w - 1, fade_start + dy)], fill=value)

    # Multiply existing alpha by the gradient (C-level op in PIL)
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
    # Erode alpha by 1 px: MinFilter(3) takes the minimum value in each 3×3
    # neighbourhood, which shrinks the opaque region by one pixel on every edge.
    # This clips the semi-transparent fringe pixels that would otherwise appear
    # as a grey halo when the wig is composited over the user's dark hair.
    new_alpha = new_alpha.filter(ImageFilter.MinFilter(3))
    img.putalpha(new_alpha)
    return img


def crop_to_face_zoom(img: Image.Image, face: list) -> Image.Image:
    """Return a gently zoomed crop (~1.25×) centered on the upper face / hair region.
    Crops to 80 % of the image dimensions, biased upward so the hair is central."""
    fx, fy, fw, fh = face[:4]
    cx = fx + fw // 2
    # Bias the vertical focus toward the top of the face so hair fills the frame
    cy = fy + fh // 4

    crop_w = int(img.width  * 0.80)
    crop_h = int(img.height * 0.80)

    x1 = max(0, cx - crop_w // 2)
    y1 = max(0, cy - crop_h // 3)
    x2 = x1 + crop_w
    y2 = y1 + crop_h

    # Clamp to image bounds
    if x2 > img.width:
        x2 = img.width
        x1 = max(0, x2 - crop_w)
    if y2 > img.height:
        y2 = img.height
        y1 = max(0, y2 - crop_h)

    return img.crop((x1, y1, x2, y2))


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

    # Strip white background if the wig has no meaningful transparency already
    alpha_vals = list(wig_raw.split()[3].getdata())
    has_transparency = any(a < 200 for a in alpha_vals)
    if not has_transparency:
        wig_raw = remove_white_background(wig_raw)

    # Find where the actual hair content ends vertically.
    # Wig PNGs have the crown at the top and transparent/white space below the
    # hair ends. Anchoring to the real hair bottom (not the PNG image bottom)
    # keeps the hair aligned with the forehead regardless of how much empty
    # space the PNG has beneath the hair.
    hair_bottom_frac = hair_content_bottom_fraction(wig_raw)

    # Compute overlay size from face bbox [x, y, w, h]
    fx, fy, fw, fh = face[:4]
    # Compute anchor first so we can use it to constrain overlay height.
    # forehead_y = where the actual hair BOTTOM should land on the face.
    forehead_y = round(fy + params["forehead"] * fh)

    overlay_w = round(fw * params["w"])
    overlay_w = min(overlay_w, round(user_img.width * 0.85))
    aspect    = wig_raw.height / wig_raw.width
    overlay_h = round(overlay_w * aspect)
    # Cap 1: don't dwarf the face
    overlay_h = min(overlay_h, round(fh * 0.85))
    # Cap 2: prevent the wig from floating above the image frame.
    # hair_bottom_frac × overlay_h must not exceed forehead_y, otherwise
    # y = forehead_y - actual_hair_bottom becomes deeply negative (wig above frame).
    if hair_bottom_frac > 0.01:
        overlay_h = min(overlay_h, round(forehead_y / hair_bottom_frac))

    wig_resized = wig_raw.resize((overlay_w, overlay_h), Image.LANCZOS)
    wig_resized = apply_bottom_fade(wig_resized, fade_fraction=0.15)

    # Anchor: align the ACTUAL HAIR BOTTOM (not the PNG image bottom) with forehead_y.
    actual_hair_bottom_px = round(hair_bottom_frac * overlay_h)
    x = round(fx + fw / 2 - overlay_w / 2)
    y = forehead_y - actual_hair_bottom_px

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

    # Flatten to RGB, gently zoom to face, encode as JPEG
    result_rgb = result_rgba.convert("RGB")
    result_rgb = crop_to_face_zoom(result_rgb, face)
    buf = io.BytesIO()
    result_rgb.save(buf, format="JPEG", quality=92)
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
def get_hair_styles():
    return HAIR_STYLES

@app.get("/face_shapes")
def get_face_shapes():
    return FACE_SHAPES

@app.get("/model_pictures")
def get_model_pictures():
    result = []
    for m in MODEL_PICTURES:
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
            "face":         faces[0] if faces else None
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
