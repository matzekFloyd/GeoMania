# Contributing

Pull requests and issues are welcome.

## Desktop (Java / Gradle)

From the repository root, run `.\gradlew.bat build` (or `./gradlew build`) so unit tests and the Shadow fat JAR build succeed before you open a pull request. Follow the style of existing code under `src/main/java/geomania/`.

## Web (p5.js / Vite)

Run `cd web && npm ci && npm run build`. Match patterns in `web/src/` and keep the browser bundle servable over HTTP (not `file://`).

## General

- Prefer **small, focused** changes; avoid unrelated drive-by refactors in the same PR.
- Do **not** commit `web/node_modules/`, `build/`, `.gradle/`, local IDE metadata you would not want shared, or any secrets.
