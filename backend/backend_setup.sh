#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────────────────────
#  StyleMe Backend – One-Shot Setup Script
#
#  Run this once to set up the full backend environment.
#  Prerequisites: Docker, Docker Compose, git
#
#  Usage:
#    chmod +x backend_setup.sh
#    ./backend_setup.sh
# ─────────────────────────────────────────────────────────────────────────────

set -e

REPO_URL="https://github.com/HairdressingProject/styleme.git"
BACKEND_DIR="$(dirname "$0")"

echo ""
echo "╔══════════════════════════════════════════════╗"
echo "║       StyleMe Backend – Setup Script         ║"
echo "╚══════════════════════════════════════════════╝"
echo ""

# ── 1. Check prerequisites ────────────────────────────────────────────────────
echo "▶ Checking prerequisites..."
command -v docker  >/dev/null 2>&1 || { echo "❌  Docker not found. Install Docker first."; exit 1; }
command -v git     >/dev/null 2>&1 || { echo "❌  git not found.";  exit 1; }
echo "✅  Docker and git found."

# ── 2. Create .env from template if missing ───────────────────────────────────
ENV_FILE="$BACKEND_DIR/.env"
TEMPLATE="$BACKEND_DIR/.env.template"
if [ ! -f "$ENV_FILE" ]; then
    echo ""
    echo "▶ Creating .env from template..."
    cp "$TEMPLATE" "$ENV_FILE"
    echo "⚠️  .env created. Edit it before continuing (especially JWT_SECRET and passwords):"
    echo "      $ENV_FILE"
    echo ""
    read -p "   Press ENTER after you've edited .env to continue, or Ctrl+C to abort..."
fi

# ── 3. Clone API source code from original repo ───────────────────────────────
echo ""
echo "▶ Fetching API source from original repo..."

CLONE_DIR="/tmp/styleme_source_$$"
git clone --depth 1 "$REPO_URL" "$CLONE_DIR"

echo "   Copying UsersAPI source..."
rsync -a "$CLONE_DIR/Backend/UsersAPI/" "$BACKEND_DIR/users_api/" \
    --exclude=bin --exclude=obj --exclude='.vs'

echo "   Copying PicturesAPI source..."
rsync -a "$CLONE_DIR/Backend/PicturesAPI/" "$BACKEND_DIR/pictures_api/" \
    --exclude='__pycache__' --exclude='*.pyc' --exclude='.env'

rm -rf "$CLONE_DIR"
echo "✅  Source copied."

# ── 4. Build and start services ───────────────────────────────────────────────
echo ""
echo "▶ Building Docker images and starting services..."
echo "   (This takes a while on first run — ML deps are large)"
echo ""
cd "$BACKEND_DIR"
docker compose up -d --build

# ── 5. Wait for db to be healthy ──────────────────────────────────────────────
echo ""
echo "▶ Waiting for database to be ready..."
MAX_WAIT=60
COUNT=0
until docker compose exec db mariadb-admin ping -h localhost -u root -p"${DB_ROOT_PASSWORD:-rootpassword}" --silent 2>/dev/null; do
    COUNT=$((COUNT + 1))
    if [ "$COUNT" -ge "$MAX_WAIT" ]; then
        echo "❌  Database did not become ready within ${MAX_WAIT}s. Check logs:"
        echo "    docker compose logs db"
        exit 1
    fi
    printf "."
    sleep 2
done
echo ""
echo "✅  Database is ready."

# ── 6. Seed model pictures ────────────────────────────────────────────────────
echo ""
echo "▶ Seeding model pictures (ML face detection runs for each image)..."
echo "   This may take several minutes..."
docker compose exec pictures_api python init_models.py || \
    echo "⚠️  Model seeding failed or init_models.py not found. Run manually later."

# ── 7. Done ───────────────────────────────────────────────────────────────────
echo ""
echo "╔══════════════════════════════════════════════════════╗"
echo "║            Backend is running! 🎉                    ║"
echo "╠══════════════════════════════════════════════════════╣"
echo "║  Users API:       http://localhost:5000              ║"
echo "║  Pictures API:    http://localhost:8000              ║"
echo "║  Adminer (DB UI): http://localhost:8080              ║"
echo "╠══════════════════════════════════════════════════════╣"
echo "║  Android Emulator base URLs:                         ║"
echo "║    Users API:     http://10.0.2.2:5000/             ║"
echo "║    Pictures API:  http://10.0.2.2:8000/             ║"
echo "╠══════════════════════════════════════════════════════╣"
echo "║  Physical device: use your machine's LAN IP          ║"
echo "║    e.g.  http://192.168.1.x:5000/                   ║"
echo "╚══════════════════════════════════════════════════════╝"
echo ""
echo "Update BASE_URL_USERS and BASE_URL_PICTURES in:"
echo "  app/build.gradle → defaultConfig → buildConfigField"
echo ""
