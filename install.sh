#!/usr/bin/env bash
#
#  Hermes Companion — One-line installer
#
#  Usage:
#    curl -fsSL https://raw.githubusercontent.com/klautimus/hermes-companion/main/install.sh | bash
#
#  Or clone and run:
#    ./install.sh                # systemd (default)
#    ./install.sh --docker       # Docker mode
#    ./install.sh --uninstall    # Remove everything
#
set -euo pipefail

VERSION="1.0.0"
REPO_URL="https://github.com/klautimus/hermes-companion"
APK_URL="$REPO_URL/releases/latest"

# Defaults
INSTALL_DIR="/opt/hermes-companion"
VENV_DIR="$INSTALL_DIR/venv"
DATA_DIR="/var/lib/hermes-companion"
CONFIG_DIR="/etc/hermes-companion"
PORT="8777"
HERMES_API="http://localhost:8642"
MODE="systemd"
ACTION="install"

# Colors
B='\033[1m'
G='\033[32m'
Y='\033[33m'
R='\033[31m'
C='\033[36m'
N='\033[0m'

print_banner() {
    echo -e "${C}"
    echo '  ╔═══════════════════════════════════════════════╗'
    echo '  ║                                               ║'
    echo '  ║    Hermes Companion v'$VERSION'                  ║'
    echo '  ║    Mobile interface for Hermes Agent          ║'
    echo '  ║                                               ║'
    echo '  ╚═══════════════════════════════════════════════╝'
    echo -e "${N}"
}

print_info()  { echo -e "${C}ℹ${N}  $1"; }
print_ok()    { echo -e "${G}✓${N}  $1"; }
print_warn()  { echo -e "${Y}⚠${N}  $1"; }
print_err()   { echo -e "${R}✗${N}  $1"; }

# Parse args
while [[ $# -gt 0 ]]; do
    case "$1" in
        --docker)      MODE="docker"; shift ;;
        --uninstall)   ACTION="uninstall"; shift ;;
        --port)        PORT="$2"; shift 2 ;;
        --hermes-api)  HERMES_API="$2"; shift 2 ;;
        --help|-h)
            echo "Usage: ./install.sh [--docker] [--uninstall] [--port N] [--hermes-api URL]"
            echo ""
            echo "Options:"
            echo "  --docker       Install using Docker instead of systemd"
            echo "  --uninstall    Remove Hermes Companion"
            echo "  --port N       Server port (default: 8777)"
            echo "  --hermes-api   Hermes Agent API URL (default: http://localhost:8642)"
            exit 0
            ;;
        *) echo "Unknown option: $1"; exit 1 ;;
    esac
done

# ─── Uninstall ───────────────────────────────────────────────
do_uninstall() {
    print_banner
    print_info "Uninstalling Hermes Companion..."

    # Stop systemd service
    if systemctl is-active --quiet hermes-companion 2>/dev/null; then
        sudo systemctl stop hermes-companion
        print_ok "Stopped systemd service"
    fi
    sudo systemctl disable hermes-companion 2>/dev/null || true
    sudo rm -f /etc/systemd/system/hermes-companion.service
    sudo systemctl daemon-reload

    # Stop Docker container
    if docker ps -a --format '{{.Names}}' | grep -q hermes-companion 2>/dev/null; then
        docker rm -f hermes-companion 2>/dev/null || true
        print_ok "Removed Docker container"
    fi

    # Remove files
    sudo rm -rf "$INSTALL_DIR" "$DATA_DIR"
    print_ok "Removed install directory"

    print_ok "Uninstall complete!"
}

# ─── Docker install ──────────────────────────────────────────
install_docker() {
    print_info "Mode: ${B}Docker${N}"

    # Check Docker
    if ! command -v docker &>/dev/null; then
        print_err "Docker not found. Install Docker first: https://docs.docker.com/get-docker/"
        exit 1
    fi
    print_ok "Docker detected: $(docker --version)"

    # Check Hermes Agent CLI
    if command -v hermes &>/dev/null; then
        print_ok "Hermes CLI detected"
    else
        print_warn "Hermes CLI not found — kanban features will not work"
        print_warn "Install Hermes Agent: https://github.com/nousresearch/hermes-agent"
    fi

    # Build image
    print_info "Building Docker image..."
    docker build -t hermes-companion:$VERSION -f daemon/Dockerfile daemon/

    # Run container
    print_info "Starting container on port $PORT..."
    docker run -d \
        --name hermes-companion \
        --restart unless-stopped \
        -p "$PORT:8777" \
        -e "HERMES_API=$HERMES_API" \
        -v hermes-companion-data:/data \
        -v ~/.hermes:/home/companion/.hermes:ro \
        hermes-companion:$VERSION

    sleep 3
    if docker ps --format '{{.Names}}' | grep -q hermes-companion; then
        print_ok "Container running on port $PORT"
    else
        print_err "Container failed to start. Check: docker logs hermes-companion"
        exit 1
    fi
}

# ─── systemd install ─────────────────────────────────────────
install_systemd() {
    print_info "Mode: ${B}systemd${N}"

    # Check Python
    if ! command -v python3 &>/dev/null; then
        print_err "Python 3 not found. Install Python 3.10+ first."
        exit 1
    fi

    PY_VER=$(python3 -c 'import sys; print(f"{sys.version_info.major}.{sys.version_info.minor}")')
    print_ok "Python $PY_VER detected"

    if [[ "$PY_VER" < "3.10" ]]; then
        print_err "Python 3.10+ required, found $PY_VER"
        exit 1
    fi

    # Check Hermes Agent CLI
    HERMES_PATH=$(command -v hermes 2>/dev/null || echo "")
    if [ -n "$HERMES_PATH" ]; then
        print_ok "Hermes CLI detected at $HERMES_PATH"
    else
        print_warn "Hermes CLI not found — kanban features will not work"
        print_warn "Install Hermes Agent: https://github.com/nousresearch/hermes-agent"
        HERMES_PATH="/usr/local/bin/hermes"
    fi

    # Create directories
    print_info "Creating directories..."
    sudo mkdir -p "$INSTALL_DIR" "$DATA_DIR"

    # Copy daemon files
    print_info "Installing daemon..."
    sudo cp -r daemon/ "$INSTALL_DIR/daemon"
    sudo cp daemon/Dockerfile "$INSTALL_DIR/" 2>/dev/null || true

    # Create virtual environment
    print_info "Setting up Python environment..."
    sudo python3 -m venv "$VENV_DIR"
    sudo "$VENV_DIR/bin/pip" install --upgrade pip -q
    sudo "$VENV_DIR/bin/pip" install -e "$INSTALL_DIR/daemon" -q

    # Set up auth.json
    AUTH_FILE="$DATA_DIR/auth.json"
    if [ ! -f "$AUTH_FILE" ]; then
        print_info "Creating default auth file..."
        sudo mkdir -p "$DATA_DIR"
        echo '{"users": {}}' | sudo tee "$AUTH_FILE" > /dev/null
    fi

    # Create systemd service
    print_info "Installing systemd service..."
    local HERMES_BIN_DIR
    HERMES_BIN_DIR=$(dirname "$HERMES_PATH")

    sudo tee /etc/systemd/system/hermes-companion.service > /dev/null <<UNIT
[Unit]
Description=Hermes Companion Daemon
After=network.target

[Service]
Type=simple
WorkingDirectory=$INSTALL_DIR/daemon
ExecStart=$VENV_DIR/bin/python3 server.py
Restart=on-failure
RestartSec=5
Environment=HERMES_API=$HERMES_API
Environment=PYTHONUNBUFFERED=1
Environment=COMPANION_HOST=127.0.0.1
Environment=COMPANION_PORT=$PORT
Environment=PATH=$HERMES_BIN_DIR:$VENV_DIR/bin:/usr/local/bin:/usr/bin:/bin

[Install]
WantedBy=multi-user.target
UNIT

    # Enable and start
    print_info "Starting service..."
    sudo systemctl daemon-reload
    sudo systemctl enable hermes-companion
    sudo systemctl start hermes-companion

    sleep 3
    if systemctl is-active --quiet hermes-companion; then
        print_ok "Service is running on port $PORT"
    else
        print_err "Service failed to start"
        print_info "Check logs: journalctl -u hermes-companion -n 30"
        exit 1
    fi

    # Verify health
    if curl -sf "http://localhost:$PORT/healthz" > /dev/null 2>&1; then
        print_ok "Health check passed"
    else
        print_warn "Health check failed — service may still be starting up"
    fi
}

# ─── Main ────────────────────────────────────────────────────
print_banner

if [ "$ACTION" = "uninstall" ]; then
    do_uninstall
    exit 0
fi

print_info "Repository: $REPO_URL"
print_info "APK: $APK_URL"
echo ""

if [ "$MODE" = "docker" ]; then
    install_docker
else
    install_systemd
fi

echo ""
echo -e "${G}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${N}"
echo -e "${G}  ✓  Hermes Companion installed!${N}"
echo -e "${G}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${N}"
echo ""
echo -e "${B}Next steps:${N}"
echo ""
echo -e "  ${C}1.${N}  ${B}Get the Android app${N}"
echo -e "      Download: ${C}$APK_URL${N}"
echo -e "      Or build:  ${C}cd android && ./gradlew assembleDebug${N}"
echo ""
echo -e "  ${C}2.${N}  ${B}Set up credentials${N}"
echo -e "      Run:       ${C}$VENV_DIR/bin/hermes-companion setup${N}"
echo -e "      Or edit:   ${C}$DATA_DIR/auth.json${N}"
echo ""
echo -e "  ${C}3.${N}  ${B}Connect from your phone${N}"
echo -e "      Server URL: ${C}http://<this-machine-ip>:$PORT${N}"
echo ""
echo -e "  ${C}4.${N}  ${B}Optional: Expose externally${N}"
echo -e "      Tunnel:     ${C}cloudflared tunnel --url http://localhost:$PORT${N}"
echo ""
echo -e "${B}Useful commands:${N}"
echo -e "  ${C}systemctl status hermes-companion${N}    Check status"
echo -e "  ${C}journalctl -u hermes-companion -f${N}    View logs"
echo -e "  ${C}systemctl restart hermes-companion${N}   Restart"
echo ""
echo -e "${B}Documentation:${N}  $REPO_URL#readme"
echo -e "${B}Issues:${N}        $REPO_URL/issues"
echo ""
