from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.middleware.cors import CORSMiddleware
import cloudinary
import cloudinary.uploader
import cloudinary.utils
import os, random, uuid

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
    {"id": 1, "name": "short_textured", "label": "Short Textured"},
    {"id": 2, "name": "long_straight",  "label": "Long Straight"},
    {"id": 3, "name": "curly_long",     "label": "Curly Long"},
]

# Map style id to Cloudinary public_id of wig image.
# hair1 = curly, hair2 = short/topper, hair3 = long straight.
# Assigned here so each wig matches its label/thumbnail in the app:
#   1 "Short Textured" -> hair2 (short)
#   2 "Long Straight"  -> hair3 (long straight)
#   3 "Curly Long"     -> hair1 (curly)
WIG_PUBLIC_IDS = {
    1: "styleme/wigs/hair2",
    2: "styleme/wigs/hair3",
    3: "styleme/wigs/hair1",
}

MODEL_PICTURES = [
    {"id": 1, "file_name": "Short Textured", "file_path": None, "hair_style_id": 1, "face_shape_id": None, "hair_length_id": 1},
    {"id": 2, "file_name": "Long Straight",  "file_path": None, "hair_style_id": 2, "face_shape_id": None, "hair_length_id": 3},
    {"id": 3, "file_name": "Curly Long",     "file_path": None, "hair_style_id": 3, "face_shape_id": None, "hair_length_id": 3},
]

pictures_store = {}

def make_placeholder_pic(picture_id: int):
    return {
        "id": picture_id, "file_name": "photo.jpg",
        "file_path": None, "public_id": None,
        "file_size": "0", "height": None,
        "width": None, "date_created": ""
    }

# Per-style overlay tuning. Each wig PNG has a different "face hole" shape /
# hairline coverage, so the scale (relative to the *detected face box*) and
# the vertical offset (relative to face height, measured from the top of the
# face box) need to be tuned per style for a natural fit.
#   - "w" is the overlay width as a multiple of the detected face width
#   - "y" is how far the overlay's top edge sits above the face box's top
#     edge, as a fraction of the face height (negative = higher / more
#     forehead & scalp coverage)
WIG_OVERLAY_PARAMS = {
    1: {"w": 1.5,  "y": -0.55},  # Short Textured -> hair2 (solid topper)
    2: {"w": 1.5,  "y": -0.35},  # Long Straight  -> hair3 (long, hollow)
    3: {"w": 1.7,  "y": -0.45},  # Curly Long     -> hair1 (curly, hollow)
}
DEFAULT_WIG_PARAMS = {"w": 1.5, "y": -0.4}


def apply_wig_overlay(user_public_id: str, wig_public_id: str, cloud_name: str,
                       style_id: int = 0, face=None) -> str:
    """
    Overlay a wig PNG onto the user's face photo.

    - e_make_transparent strips the wig's white background so only the hair
      pixels are composited (no white box over the face).
    - If a detected face bounding box (x, y, width, height in pixels) is
      available, the overlay is sized as a multiple of the face width and
      positioned with absolute pixel offsets (g_north_west) so it lines up
      with the actual face regardless of where it is in the photo.
    - Falls back to Cloudinary's g_face gravity (less reliable, but works
      without a stored face box) if no face was detected at upload time.
    - fl_no_overflow keeps the composited image the same size as the
      original photo (no extra white canvas).
    """
    wig_layer = wig_public_id.replace("/", ":")
    params = WIG_OVERLAY_PARAMS.get(style_id, DEFAULT_WIG_PARAMS)

    if face:
        fx, fy, fw, fh = face[:4]
        overlay_w = round(fw * params["w"])
        x = round(fx + fw / 2 - overlay_w / 2)
        y = round(fy + params["y"] * fh)
        transformation = (
            f"l_{wig_layer},e_make_transparent:30,"
            f"w_{overlay_w},x_{x},y_{y},g_north_west,fl_no_overflow,fl_layer_apply"
        )
    else:
        transformation = (
            f"l_{wig_layer},e_make_transparent:30,fl_relative,fl_no_overflow,"
            f"w_{params['w']},g_face,y_{params['y']},fl_layer_apply"
        )

    return (
        f"https://res.cloudinary.com/{cloud_name}"
        f"/image/upload/{transformation}/{user_public_id}"
    )

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
    cloud = os.getenv("CLOUDINARY_CLOUD_NAME", "")
    result = []
    for m in MODEL_PICTURES:
        pic = dict(m)
        wig_id = WIG_PUBLIC_IDS.get(m["hair_style_id"])
        if wig_id and cloud:
            pic["file_path"] = f"https://res.cloudinary.com/{cloud}/image/upload/{wig_id}"
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
    pic = pictures_store.get(picture_id, make_placeholder_pic(picture_id))
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
    pic        = pictures_store.get(user_picture_id, make_placeholder_pic(user_picture_id))
    user_pub   = pic.get("public_id")
    cloud_name = os.getenv("CLOUDINARY_CLOUD_NAME", "")

    model      = next((m for m in MODEL_PICTURES if m["id"] == model_picture_id), None)
    style_id   = model["hair_style_id"] if model else model_picture_id
    wig_pub_id = WIG_PUBLIC_IDS.get(style_id)

    if user_pub and wig_pub_id and cloud_name:
        result_url = apply_wig_overlay(user_pub, wig_pub_id, cloud_name, style_id, pic.get("face"))
    elif wig_pub_id and cloud_name:
        # No user photo public_id — just return the wig preview image
        result_url = f"https://res.cloudinary.com/{cloud_name}/image/upload/{wig_pub_id}"
    else:
        result_url = pic.get("file_path", "")

    # Make sure result_url is never None
    if not result_url:
        result_url = ""

    new_id  = abs(hash(f"{user_picture_id}_style_{style_id}")) % 1000000
    new_pic = {**pic, "id": new_id, "file_path": result_url}
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
    """Re-register a picture from Firestore after server restart"""
    pictures_store[picture_id] = {
        "id":           picture_id,
        "file_name":    "photo.jpg",
        "file_path":    url,
        "public_id":    public_id,
        "file_size":    "0",
        "height":       None,
        "width":        None,
        "date_created": ""
    }
    return {"status": "registered", "picture_id": picture_id}

@app.get("/test/wig_url")
def test_wig_url(user_public_id: str, style_id: int = 1,
                  face_x: int = None, face_y: int = None,
                  face_w: int = None, face_h: int = None):
    cloud_name = os.getenv("CLOUDINARY_CLOUD_NAME", "")
    wig_pub_id = WIG_PUBLIC_IDS.get(style_id, "")
    if not wig_pub_id or not cloud_name:
        return {"error": "Missing config", "cloud_name": cloud_name, "wig_pub_id": wig_pub_id}
    face = None
    if None not in (face_x, face_y, face_w, face_h):
        face = [face_x, face_y, face_w, face_h]
    url = apply_wig_overlay(user_public_id, wig_pub_id, cloud_name, style_id, face)
    return {"url": url, "user_public_id": user_public_id, "wig_pub_id": wig_pub_id, "face": face}