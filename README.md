# MusicBridge Android

Companion Android app for [MusicBridge](https://github.com/faraway-world/Musicbridge-server-side).  
This app listens to music player notifications on your phone (e.g. YouTube Music, Spotify, etc.) and forwards the **song title** and **artist** to the Flask server running on your PC. The server then updates your **Discord Rich Presence** in real time.

## Features
- Written in **Kotlin**.  
- Uses Android’s **Notification Listener Service** to detect media playback.  
- Sends track info via HTTP POST to your PC.  
- Lightweight, runs in the background while you play music.  

## How It Works
1. When a song starts on your phone, the app reads the notification.  
2. Extracts **title** and **artist**.  
3. Sends them as JSON to your PC’s Flask server (default `http://<PC-IP>:5000/update`).  
4. Your PC updates Discord RPC with the new info.  

## Permissions
The app requires:
- **Notification access** (to read song metadata).  
- **Internet permission** (to send data to your PC).  

## Setup
1. Clone or download this repo:
   ```bash
   git clone https://github.com/faraway-world/MusicBridge.git
