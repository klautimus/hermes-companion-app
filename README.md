<div align="center">

# 📱 Hermes Companion

### A mobile interface for the [Hermes Agent](https://github.com/nousresearch/hermes-agent) AI system

Chat with your AI agent, manage kanban boards, and monitor tasks — all from your Android phone.

[![Release](https://img.shields.io/badge/release-v1.0.0-blue)](https://github.com/klautimus/hermes-companion/releases/latest)
[![License: MIT](https://img.shields.io/badge/license-MIT-green)](LICENSE)
[![Platform: Android 9+](https://img.shields.io/badge/platform-Android%209%2B-orange)](https://github.com/klautimus/hermes-companion/releases/latest)
[![Backend: Python](https://img.shields.io/badge/backend-Python%203.10%2B-yellow)](daemon/)

</div>

---

## ✨ Features

### 💬 Chat
- Browse and search session history with a swipeable drawer
- Create new chat sessions
- Send messages with **streaming responses** (Server-Sent Events)
- **Markdown rendering** — code blocks, syntax highlighting, bold/italic, links
- Attach and view images and files inline

### 📋 Kanban
- Full board visualization with 8 status columns (triage → done)
- Task cards showing title, assignee, priority, and age
- Create, edit, assign, block, unblock, complete, archive, and reclaim tasks
- Add comments and link parent-child task dependencies
- Bulk status updates and reassignment
- Board statistics dashboard

### 🔒 Security
- End-to-end encrypted credential storage (Android Keystore)
- Email-based **2FA** authentication
- QR code setup flow for secure first-run pairing

---

## 🚀 Quick Start

### One-line install (daemon)

```bash
curl -fsSL https://raw.githubusercontent.com/klautimus/hermes-companion/main/install.sh | bash
```

### Get the Android app

Download the latest APK from the **[Releases page](https://github.com/klautimus/hermes-companion/releases/latest)**.

---

## 📦 What's in this repo

```
hermes-companion/
│
├── install.sh              ← One-line installer (systemd or Docker)
├── docker-compose.yml      ← Docker deployment
│
├── daemon/                 ← Python backend server
│   ├── server.py           ← HTTP API (aiohttp)
│   ├── Dockerfile          ← Container build
│   ├── pyproject.toml      ← pip-installable package
│   └── tests/              ← 194 passing tests
│
├── android/                ← Kotlin frontend app
│   ├── app/                ← Jetpack Compose source
│   ├── gradlew             ← Build tool
│   └── settings.gradle
│
└── .github/workflows/      ← CI: auto-builds APK on release
```

---

## 🛠 Installation

### Prerequisites

- A machine running [Hermes Agent](https://github.com/nousresearch/hermes-agent) (the AI system this app connects to)
- An Android phone running Android 9.0+ (API 28+)

### Option 1: One-line install (recommended)

```bash
curl -fsSL https://raw.githubusercontent.com/klautimus/hermes-companion/main/install.sh | bash
```

This script will:
1. ✅ Install the daemon to `/opt/hermes-companion`
2. ✅ Create a Python virtual environment with all dependencies
3. ✅ Set up a systemd service with auto-restart
4. ✅ Start the server on port 8777

### Option 2: Docker

```bash
git clone https://github.com/klautimus/hermes-companion.git
cd hermes-companion
docker-compose up -d
```

### Option 3: Manual

```bash
git clone https://github.com/klautimus/hermes-companion.git
cd hermes-companion/daemon
pip install -e .
hermes-companion serve
```

### Connect your phone

1. Download the APK from [Releases](https://github.com/klautimus/hermes-companion/releases/latest)
2. Open the app and enter your server URL: `http://<your-machine-ip>:8777`
3. Enter your credentials (set during setup)
4. Start chatting! 🎉

---

## 🏗 Architecture

```
  ┌──────────────────────────────────────────┐
  │         Your Android Phone                │
  │                                          │
  │   ┌─────────┐  ┌─────────┐  ┌────────┐  │
  │   │  Chat   │  │ Kanban  │  │Settings│  │
  │   └────┬────┘  └────┬────┘  └───┬────┘  │
  │        └──────┬─────┘───────────┘       │
  │               ▼                         │
  │        HTTP (Basic Auth + 2FA)          │
  └───────────────┼─────────────────────────┘
                  │ WiFi / LAN / Tunnel
                  ▼
  ┌──────────────────────────────────────────┐
  │     Companion Daemon (Python)             │
  │     Port 8777                             │
  │                                           │
  │  • Auth + 2FA (scrypt + Gmail OTP)       │
  │  • Chat proxy → Hermes API               │
  │  • Kanban CLI wrapper                    │
  │  • File attachment storage               │
  └───────────────┬──────────────────────────┘
                  │ localhost
                  ▼
  ┌──────────────────────────────────────────┐
  │     Hermes Agent (port 8642)              │
  │     AI sessions, kanban DB, CLI           │
  └──────────────────────────────────────────┘
```

The daemon acts as a trusted intermediary — it holds credentials server-side so the phone never touches Hermes directly. The Android app talks to the daemon; the daemon talks to Hermes.

---

## 🧰 Tech Stack

| Component | Technology |
|-----------|-----------|
| **Android app** | Kotlin, Jetpack Compose, Material 3 |
| **Markdown** | Markwon (code highlighting, links) |
| **Networking** | OkHttp with SSE streaming |
| **Security** | EncryptedSharedPreferences, Android Keystore |
| **Daemon** | Python 3.10+, aiohttp |
| **Auth** | scrypt hashing, email-based 2FA via Gmail API |
| **Package** | pip-installable (`hermes-companion-server`) |
| **Deploy** | systemd service or Docker container |

---

## 🔧 Configuration

The daemon reads config from `~/.hermes/companion/config.yaml`:

```yaml
server:
  host: 127.0.0.1    # 0.0.0.0 for Docker
  port: 8777

hermes:
  api_url: http://127.0.0.1:8642
  cli_path: auto      # auto-detect hermes binary
```

Environment overrides: `COMPANION_HOST`, `COMPANION_PORT`, `HERMES_API`

---

## 👨‍💻 Building from Source

### Android APK

```bash
cd android
./gradlew assembleDebug
# APK: android/app/build/outputs/apk/debug/app-debug.apk
```

### Daemon

```bash
cd daemon
pip install -e ".[dev]"
pytest tests/ -v     # 194 tests
python server.py     # Run directly
```

---

## 📋 API Reference

The daemon exposes a REST API on port 8777. Key endpoints:

<details>
<summary><b>Click to expand full API table</b></summary>

| Category | Method | Endpoint | Description |
|----------|--------|----------|-------------|
| **Chat** | `GET` | `/api/sessions` | List sessions |
| | `POST` | `/api/sessions` | Create session |
| | `GET` | `/api/sessions/{id}/messages` | Get messages |
| | `POST` | `/v1/chat/completions/stream` | Stream chat response |
| **Kanban** | `GET` | `/api/kanban/boards` | List boards |
| | `GET` | `/api/kanban/tasks?board=X` | List tasks |
| | `PATCH` | `/api/kanban/tasks/{id}` | Update task |
| | `POST` | `/api/kanban/tasks/{id}/complete` | Complete task |
| | `POST` | `/api/kanban/tasks/{id}/block` | Block task |
| | `POST` | `/api/kanban/tasks/{id}/unblock` | Unblock task |
| | `POST` | `/api/kanban/tasks/{id}/assign` | Assign task |
| | `GET` | `/api/kanban/stats` | Board statistics |
| **Auth** | `POST` | `/api/auth/2fa/verify` | Verify 2FA code |
| **Health** | `GET` | `/healthz` | Health check |

</details>

---

## 🤝 Contributing

This is a personal project but suggestions and bug reports are welcome! Please [open an issue](https://github.com/klautimus/hermes-companion/issues).

---

## 📄 License

[MIT](LICENSE) — do whatever you want, just don't sue me.

---

<div align="center">

**[⬇ Download APK](https://github.com/klautimus/hermes-companion/releases/latest)** ·
**[📖 Full Docs](daemon/README.md)** ·
**[🐛 Report Bug](https://github.com/klautimus/hermes-companion/issues)**

Built by [Kevin Disher](https://github.com/klautimus) · Powered by [Hermes Agent](https://github.com/nousresearch/hermes-agent)

</div>
