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

# Map style id to Cloudinary public_id of wig image
# UPDATE THESE with your actual Cloudinary public IDs after uploading
WIG_PUBLIC_IDS = {
    1: "styleme/wigs/hair1",
    2: "styleme/wigs/hair2",
    3: "styleme/wigs/hair3",
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

def apply_wig_overlay(user_public_id: str, wig_public_id: str, cloud_name: str) -> str:
    """
    Use Cloudinary's overlay transformation to place wig on top of face.
    Uses face detection (g_face) to position wig automatically.
    """
    # Encode the wig public_id for URL (replace / with :)
    wig_layer = wig_public_id.replace("/", ":")

    # Cloudinary transformation:
    # 1. Detect face and crop to face area with padding
    # 2. Overlay wig image centered on face, scaled to 130% of face width
    # 3. Position wig at top of face (gravity north_face)
    transformation = (
        f"w_600,h_700,c_fill,g_face,z_0.8/"          # crop to face
        f"l_{wig_layer},"                              # overlay wig layer
        f"w_1.3,h_1.3,fl_relative,"                   # scale wig to 130% of base
        f"g_north_face,y_-0.15,fl_layer_apply"        # position at top of face
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
            resource_type="image"
        )
        picture_id = abs(hash(result["public_id"])) % 1000000
        picture = {
            "id":           picture_id,
            "file_name":    result.get("original_filename", "photo") + ".jpg",
            "file_path":    result["secure_url"],
            "public_id":    result["public_id"],
            "file_size":    str(result.get("bytes", 0)),
            "height":       result.get("height"),
            "width":        result.get("width"),
            "date_created": str(result.get("created_at", ""))
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
        result_url = apply_wig_overlay(user_pub, wig_pub_id, cloud_name)
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