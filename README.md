# Reacthotel

Sistema de gestión hotelera con frontend React y backend REST Spring Boot.

> El código de la aplicación está en
> [`Reacthotel-main/Reacthotel-main`](Reacthotel-main/Reacthotel-main/).

## Requisitos

- JDK 21 o superior
- Maven 3.9 o superior
- Node.js 20 o superior
- pnpm 9 o superior

## Ejecutar el proyecto

Abre una terminal para el backend:

```powershell
cd Reacthotel-main\Reacthotel-main
$env:JWT_SECRET = "cambia-esta-clave-en-produccion"
$env:JWT_EXPIRATION_MINUTES = "60"
$env:ADMIN_EMAIL = "admin@hotel.com"
$env:ADMIN_PASSWORD = "Admin123!"
$env:CORS_ALLOWED_ORIGINS = "http://localhost:5173,http://localhost:5174"
cd hotel-backend
mvn spring-boot:run
```

Abre otra terminal para el frontend:

```powershell
cd Reacthotel-main\Reacthotel-main
pnpm install
pnpm dev
```

La aplicación web estará disponible normalmente en
<http://localhost:5173> y el backend en <http://localhost:8083>.

El usuario administrador inicial se crea con los valores de `ADMIN_EMAIL` y
`ADMIN_PASSWORD`. Cambia estas credenciales en entornos reales y no las
guardes en el repositorio.

## Swagger / OpenAPI

Con el backend iniciado:

- **Swagger UI:** <http://localhost:8083/swagger-ui.html>
- **OpenAPI generado:** <http://localhost:8083/v3/api-docs>
- **Contrato OpenAPI versionado:** [docs/openapi.yaml](Reacthotel-main/Reacthotel-main/docs/openapi.yaml)
- **Guía completa de ejecución:** [README de la aplicación](Reacthotel-main/Reacthotel-main/README.md)

Para probar endpoints protegidos desde Swagger:

1. Ejecuta `POST /auth/login`.
2. Copia el `access_token` de la respuesta.
3. Pulsa **Authorize**.
4. Introduce `Bearer <access_token>`.

## Comandos útiles

```powershell
cd Reacthotel-main\Reacthotel-main
pnpm build
pnpm lint
```

Para construir el backend:

```powershell
cd Reacthotel-main\Reacthotel-main\hotel-backend
mvn clean package
```

La base de datos local utiliza H2 en memoria; sus datos se reinician al detener
el backend.
