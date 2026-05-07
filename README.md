# GeoMania

GeoMania is a small arcade game created during university and now prepared for a first public release.

You control a player in an 800x800 arena, shoot in four directions, and clear enemy waves across multiple levels.

## Features

- Retro-style Processing-based gameplay
- 3 enemy shape types (square, triangle, circle)
- Enemy splitting mechanic with increasing score
- Multiple levels with start and win screens
- Built-in score and timer display

## Tech Stack

- Java 8
- [Processing core library](https://processing.org/download/) (`core.jar`)
- NetBeans project structure (`nbproject/`)

## Gameplay

- Move with arrow keys
- Shooting is tied to movement direction (holding an arrow key both moves and shoots)
- Clear all enemies to advance to the next level
- Defeating larger enemies splits them into smaller ones until they disappear

## Controls

- `Arrow keys`: move and shoot in pressed direction
- `P`: pause/unpause
- `S`: restart current level
- `R`: full game restart (back to start screen)
- `Space`: debug/cheat skip to next level

## Project Structure

- `src/geomania/GeoMania.java`: game entry point (`main`)
- `src/geomania/`: game entities and logic
- `web/`: browser implementation using p5.js
- `geometry.jpg`: background image loaded at runtime
- `nbproject/`: NetBeans project metadata

## Requirements

1. Java JDK 8 (or newer; Java 8 is the original target)
2. Processing `core.jar` available locally

> Note: This repository does not include `core.jar`, so you need to provide it yourself.

## Run in NetBeans (recommended)

1. Open the project folder in NetBeans.
2. Download Processing and locate `core.jar`.
3. Add `core.jar` to project libraries/classpath.
4. Ensure the main class is `geomania.GeoMania`.
5. Run the project.

## Run from command line

Example (PowerShell):

```powershell
# from repository root
New-Item -ItemType Directory -Force build | Out-Null
javac -cp ".;C:\path\to\core.jar" -d build src\geomania\*.java
java -cp ".;build;C:\path\to\core.jar" geomania.GeoMania
```

Replace `C:\path\to\core.jar` with your actual Processing `core.jar` location.

## Browser Version (p5.js)

The repository now includes a browser-playable implementation in `web/`.

Run locally:

```powershell
cd web
npm install
npm run dev
```

Build static files:

```powershell
cd web
npm run build
```

The static output is written to `web/dist/` and can be hosted on any static host.

**Controls (browser):** Arrow keys or `W` `A` `S` `D` to move and shoot; on-screen D-pad on touch devices; `P` pause; `T` restart current level; `R` full restart to title; `Space` skip level.

When using a downloaded build bundle, do not open `index.html` directly via `file://` (browser module/CORS restrictions). Serve the extracted folder via HTTP instead, for example:

```bash
# from the extracted dist folder
python -m http.server 8080
# or
npx serve .
```

## Releases / CI

GitHub Actions workflows:

- `web-ci.yml`: builds `web/` on relevant pushes/PRs and uploads artifact `geomania-web-dist`.
- `web-release.yml`: runs on `v*` tags, builds `web/dist`, and attaches `geomania-web-<tag>.zip` to the GitHub Release.
- Release bundle usage: extract the ZIP and serve it via HTTP (for example `python -m http.server 8080` or `npx serve .`) instead of opening `index.html` with `file://`.

Create a release build:

```bash
git tag v1.0.1
git push origin v1.0.1
```

## Screenshots

![GeoMania gameplay background](geometry.jpg)

## Notes

- The game expects `geometry.jpg` in the project root at runtime.
- Original source comments may contain a mix of English and German.

## Future Improvements

- Migrate to a build tool (`Maven` or `Gradle`) so Processing dependencies can be resolved automatically instead of manually providing `core.jar`.

## Release Status

`v1.0.0` ships the original Java/Processing sources and a browser build under `web/` (run `npm run build` to produce `web/dist/` for static hosting).

## License

This project is licensed under the MIT License. See `LICENSE` for details.
