from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.middleware.cors import CORSMiddleware
import cloudinary
import cloudinary.uploader
import os, random, uuid

app = FastAPI()
app.add_middleware(CORSMiddleware, allow_origins=["*"], allow_methods=["*"], allow_headers=["*"])

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
    {"id": 1,  "name": "straight", "label": "Straight"},
    {"id": 2,  "name": "wavy",     "label": "Wavy"},
    {"id": 3,  "name": "curly",    "label": "Curly"},
    {"id": 4,  "name": "coily",    "label": "Coily / Natural"},
    {"id": 5,  "name": "bob",      "label": "Bob"},
    {"id": 6,  "name": "pixie",    "label": "Pixie Cut"},
    {"id": 7,  "name": "lob",      "label": "Long Bob"},
    {"id": 8,  "name": "layered",  "label": "Layered"},
    {"id": 9,  "name": "bangs",    "label": "Bangs / Fringe"},
    {"id": 10, "name": "updo",     "label": "Updo / Bun"},
]

MODEL_PICTURES = [
    {"id": 1,  "file_name": "Straight Short",  "file_path": None, "hair_style_id": 1,  "face_shape_id": None, "hair_length_id": 1},
    {"id": 2,  "file_name": "Wavy Medium",      "file_path": None, "hair_style_id": 2,  "face_shape_id": None, "hair_length_id": 2},
    {"id": 3,  "file_name": "Curly Long",        "file_path": None, "hair_style_id": 3,  "face_shape_id": None, "hair_length_id": 3},
    {"id": 4,  "file_name": "Coily Natural",     "file_path": None, "hair_style_id": 4,  "face_shape_id": None, "hair_length_id": 2},
    {"id": 5,  "file_name": "Bob Short",         "file_path": None, "hair_style_id": 5,  "face_shape_id": None, "hair_length_id": 1},
    {"id": 6,  "file_name": "Pixie Cut",         "file_path": None, "hair_style_id": 6,  "face_shape_id": None, "hair_length_id": 1},
    {"id": 7,  "file_name": "Long Bob",          "file_path": None, "hair_style_id": 7,  "face_shape_id": None, "hair_length_id": 2},
    {"id": 8,  "file_name": "Layered Long",      "file_path": None, "hair_style_id": 8,  "face_shape_id": None, "hair_length_id": 3},
    {"id": 9,  "file_name": "Bangs Fringe",      "file_path": None, "hair_style_id": 9,  "face_shape_id": None, "hair_length_id": 2},
    {"id": 10, "file_name": "Updo Bun",          "file_path": None, "hair_style_id": 10, "face_shape_id": None, "hair_length_id": 2},
]

# In-memory picture storage
pictures_store = {}

def make_placeholder_pic(picture_id: int):
    """Create a placeholder when picture not found in memory (server restarted)"""
    return {
        "id":           picture_id,
        "file_name":    "photo.jpg",
        "file_path":    None,
        "public_id":    None,
        "file_size":    "0",
        "height":       None,
        "width":        None,
        "date_created": ""
    }

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
    return MODEL_PICTURES

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
    public_id = pic.get("public_id")
    cloud_name = os.getenv("CLOUDINARY_CLOUD_NAME", "")
    hex_color = f"{r:02X}{g:02X}{b:02X}"

    if public_id and cloud_name:
        transformed_url = (
            f"https://res.cloudinary.com/{cloud_name}/image/upload/"
            f"e_colorize:60,co_rgb:{hex_color}/{public_id}"
        )
    else:
        transformed_url = pic.get("file_path")

    new_id = abs(hash(f"{picture_id}_{colour}_{hex_color}")) % 1000000
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
    # Always succeeds - uses placeholder if picture not in memory
    pic = pictures_store.get(user_picture_id, make_placeholder_pic(user_picture_id))

    new_id = abs(hash(f"{user_picture_id}_{model_picture_id}")) % 1000000
    new_pic = {**pic, "id": new_id}
    pictures_store[new_id] = new_pic

    model = next((m for m in MODEL_PICTURES if m["id"] == model_picture_id), None)

    return {
        "current_picture":  new_pic,
        "original_picture": pic,
        "history_entry": {
            "id":                  abs(hash(str(uuid.uuid4()))) % 1000000,
            "picture_id":          new_id,
            "original_picture_id": user_picture_id,
            "hair_style_id":       model["hair_style_id"] if model else model_picture_id
        }
    }

@app.delete("/pictures/discard_changes/{original_picture_id}")
def discard_changes(original_picture_id: int):
    pic = pictures_store.get(original_picture_id, make_placeholder_pic(original_picture_id))
    return {"current_picture": pic, "history": None}

@app.get("/history/latest")
def get_latest_history():
    return {"history_entry": None, "current_picture": None, "original_picture": None}