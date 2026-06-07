# ─────────────────────────────────────────────────────────────────────────────
#  Pictures API Dockerfile (FastAPI + ML)
#
#  Source code:
#    https://github.com/HairdressingProject/styleme/tree/master/Backend/PicturesAPI
#
#  Requires Python >= 3.8.5.
#  ML dependencies (PyTorch, OpenCV, face-alignment) are heavy — first build
#  will take 10-20 minutes.
#
#  After first startup, seed model pictures:
#    docker exec styleme_pictures_api python init_models.py
# ─────────────────────────────────────────────────────────────────────────────

FROM python:3.10-slim AS base

# System libs required for OpenCV and face landmark detection
RUN apt-get update && apt-get install -y --no-install-recommends \
    libglib2.0-0 libsm6 libxrender1 libxext6 libgl1 \
    curl git cmake build-essential \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy and install Python dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy application source
COPY . .

# Create directories for pictures and model data
RUN mkdir -p /app/pictures /app/models

# Health check
HEALTHCHECK --interval=20s --timeout=10s --retries=5 \
  CMD curl -f http://localhost:8000/health || exit 1

EXPOSE 8000

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000", "--workers", "2"]
