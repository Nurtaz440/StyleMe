from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.staticfiles import StaticFiles
import cloudinary
import cloudinary.uploader
import os, random, uuid, io, requests
from PIL import Image

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
    {"id": 4, "name": "natural_dark",      "label": "Natural Dark"},
    {"id": 5, "name": "side_sweep",        "label": "Side Sweep"},
]

# Map style id -> wig PNG filename (served from /wigs/ static endpoint)
WIG_FILES = {
    1: "hair_messy.png",
    2: "hair_pompadour1.png",
    3: "hair_short_black.png",
    4: "hair_natural.png",
    5: "hair_pompadour2.png",
}

MODEL_PICTURES = [
    {"id": 1, "file_name": "Messy Textured",    "file_path": None, "hair_style_id": 1, "face_shape_id": None, "hair_length_id": 1},
    {"id": 2, "file_name": "Classic Pompadour", "file_path": None, "hair_style_id": 2, "face_shape_id": None, "hair_length_id": 2},
    {"id": 3, "file_name": "Short Slick",       "file_path": None, "hair_style_id": 3, "face_shape_id": None, "hair_length_id": 1},
    {"id": 4, "file_name": "Natural Dark",      "file_path": None, "hair_style_id": 4, "face_shape_id": None, "hair_length_id": 2},
    {"id": 5, "file_name": "Side Sweep",        "file_path": None, "hair_style_id": 5, "face_shape_id": None, "hair_length_id": 2},
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
# "w"  = wig width as a multiple of the detected face width
# "y"  = vertical offset of wig top edge from face-box top, as fraction of
#         face height (negative = higher, covering more scalp/forehead)
WIG_OVERLAY_PARAMS = {
    1: {"w": 1.4, "y": -0.30},  # Messy Textured
    2: {"w": 1.4, "y": -0.30},  # Classic Pompadour
    3: {"w": 1.5, "y": -0.35},  # Short Slick
    4: {"w": 1.4, "y": -0.28},  # Natural Dark
    5: {"w": 1.4, "y": -0.30},  # Side Sweep
}
DEFAULT_WIG_PARAMS = {"w": 1.4, "y": -0.30}


def remove_white_background(img: Image.Image,
                             hard_threshold: int = 240,
                             soft_threshold: int = 180) -> Image.Image:
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

    # Load user photo — go through RGB first to normalise any CMYK/P modes
    resp = requests.get(user_photo_url, timeout=20)
    resp.raise_for_status()
    user_img = Image.open(io.BytesIO(resp.content)).convert("RGB").convert("RGBA")

    # Load wig — convert to RGBA regardless of source mode (P, LA, RGB, …)
    wig_path = os.path.join(WIG_DIR, wig_filename)
    wig_raw  = Image.open(wig_path)
    # Preserve palette transparency before converting
    if wig_raw.mode == "P":
        wig_raw = wig_raw.convert("RGBA")
    else:
        wig_raw = wig_raw.convert("RGBA")

    # If the wig has no meaningful transparency, strip its white background
    alpha_vals = list(wig_raw.split()[3].getdata())
    has_transparency = any(a < 200 for a in alpha_vals)
    if not has_transparency:
        wig_raw = remove_white_background(wig_raw)

    # Compute overlay size and position from face bbox [x, y, w, h]
    fx, fy, fw, fh = face[:4]
    overlay_w = round(fw * params["w"])
    aspect    = wig_raw.height / wig_raw.width
    overlay_h = round(overlay_w * aspect)
    wig_resized = wig_raw.resize((overlay_w, overlay_h), Image.LANCZOS)

    x = round(fx + fw / 2 - overlay_w / 2)
    y = round(fy + params["y"] * fh)

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

    # Flatten to RGB and encode as JPEG (no transparency in final image)
    buf = io.BytesIO()
    result_rgba.convert("RGB").save(buf, format="JPEG", quality=92)
    return buf.getvalue()


@app.get("/health")
def health():
    return {"status": "ok", "pictures_in_memory": len(pictures_store)}

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
