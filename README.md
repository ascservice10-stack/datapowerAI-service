# RBAC App (Spring Boot 3, Java 17)

This is a scaffolded Spring Boot project implementing:

- User registration + JWT login
- OAuth2 SSO (Keycloak) configuration placeholders
- Entities: User, Role, Workspace, Project, DataApp, Agent, AppLog, Report
- Repositories, Services, Controllers for basic operations
- Swagger/OpenAPI via springdoc

## How to run
1. Update `src/main/resources/application.properties` with your Postgres DB settings and Keycloak client info.
2. Ensure the JWT secret `app.jwt.secret` is set to a secure random value (min 32 bytes).
3. Build and run:
   ```bash
   mvn clean package
   mvn spring-boot:run
   ```
4. API docs: `http://localhost:8080/swagger-ui/index.html`

## Notes
- This is a scaffold to bootstrap development. Add DTOs, validation messages, exception handlers, and tests as needed.
- For production, externalize secrets and use HTTPS, CORS policies, rate-limiting, and proper Keycloak client configuration.
