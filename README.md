PokerDice (Full-stack: API + Web UI)

Backend: Kotlin + Spring Boot, PostgreSQL (via JDBI), token-based auth, invite codes, lobby + game domain logic, multiple REST endpoints.

Real-time updates: Server-Sent Events (SSE) endpoints for lobbies and games.

Frontend: React + TypeScript (Vite), routes for login/signup, lobbies, lobby details, game, profile, about; toast notifications; served behind Nginx reverse proxy.

Infra: Docker Compose orchestrates Postgres + backend + Nginx; Gradle tasks build container images.

Testing: JUnit-based tests for game/lobby/user services (in-memory repos used in tests).

VIDEO :
https://iplx-my.sharepoint.com/:v:/g/personal/51682_alunos_isel_ipl_pt/IQDGlxbtAOCIS4SlFo6k7JiOAaLHqpjXlwnhOJTIx8ZuTro?e=nJssM5
