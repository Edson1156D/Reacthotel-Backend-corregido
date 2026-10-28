# Reacthotel

Sistema de gestión hotelera con un frontend en React y un backend REST en
Spring Boot. Permite administrar usuarios, clientes, tipos de habitación,
habitaciones, reservas, facturas y mantenimientos.

## Tecnologías

- **Frontend:** React 19, Vite, Tailwind CSS y Axios.
- **Backend:** Java 21, Spring Boot 3.5, Spring Security y JWT.
- **Persistencia local:** H2 en memoria.
- **Documentación de API:** OpenAPI 3 y Swagger UI.
- **Gestor de dependencias del frontend:** pnpm.
- **Build del backend:** Maven.

## Requisitos

Instala las siguientes herramientas antes de ejecutar el proyecto:

- Java Development Kit (JDK) 21 o superior.
- Maven 3.9 o superior.
- Node.js 20 o superior.
- pnpm 9 o superior.

Comprueba las versiones:

```powershell
java -version
mvn -version
node --version
pnpm --version
```

## Estructura del proyecto

```text
.
├── docs/
│   ├── GUIA.md
│   └── openapi.yaml
├── hotel-backend/       # API Spring Boot
├── src/                 # Aplicación React
├── package.json
└── pnpm-lock.yaml
```

Los comandos de esta guía deben ejecutarse desde la carpeta que contiene este
README (`Reacthotel-main\Reacthotel-main`).

## Configuración del backend

El perfil `dev` usa una base de datos H2 en memoria, por lo que los datos se
reinician cada vez que se detiene la aplicación. El backend escucha por defecto
en el puerto **8083**.

Antes de iniciar el backend, define las variables de entorno. En PowerShell:

```powershell
$env:JWT_SECRET = "cambia-esta-clave-en-produccion"
$env:JWT_EXPIRATION_MINUTES = "60"
$env:ADMIN_EMAIL = "admin@hotel.com"
$env:ADMIN_PASSWORD = "Admin123!"
$env:CORS_ALLOWED_ORIGINS = "http://localhost:5173,http://localhost:5174"
```

`ADMIN_EMAIL` y `ADMIN_PASSWORD` se utilizan para crear el primer usuario
administrador cuando la base de datos está vacía. No uses estas credenciales
de ejemplo en producción y no las guardes en el repositorio.

## Ejecutar el backend

Desde la raíz del proyecto:

```powershell
cd hotel-backend
mvn spring-boot:run
```

También puedes compilar y ejecutar el `.jar`:

```powershell
cd hotel-backend
mvn clean package
java -jar target\hotel-backend-0.0.1-SNAPSHOT.jar
```

La API estará disponible en:

- API base: <http://localhost:8083>
- OpenAPI generado: <http://localhost:8083/v3/api-docs>
- Swagger UI: <http://localhost:8083/swagger-ui.html>

## Ejecutar el frontend

Abre otra terminal en la raíz del proyecto:

```powershell
pnpm install
pnpm dev
```

Vite mostrará la dirección local, normalmente
<http://localhost:5173>. El frontend está configurado para consumir el backend
en `http://localhost:8083`.

Para generar una compilación de producción:

```powershell
pnpm build
pnpm preview
```

Para ejecutar el linter:

```powershell
pnpm lint
```

## Inicio de sesión

1. Inicia el backend.
2. Inicia el frontend.
3. Abre <http://localhost:5173>.
4. Usa el correo y la contraseña definidos en `ADMIN_EMAIL` y
   `ADMIN_PASSWORD`.

El login devuelve un token JWT. El frontend lo guarda en `localStorage` y lo
envía automáticamente como `Authorization: Bearer <token>` en las peticiones
protegidas.

## Swagger y contrato OpenAPI

La documentación interactiva está disponible en
<http://localhost:8083/swagger-ui.html> cuando el backend está ejecutándose.
Desde Swagger UI puedes:

1. Ejecutar `POST /auth/login` con el usuario administrador.
2. Copiar el valor `access_token` de la respuesta.
3. Pulsar **Authorize**.
4. Introducir `Bearer <access_token>`.
5. Probar los endpoints protegidos.

La especificación estática usa como servidor local
`http://localhost:8083`. La especificación generada por el backend se puede
consultar en <http://localhost:8083/v3/api-docs>.

## Endpoints principales

| Módulo | Ruta base | Operaciones |
| --- | --- | --- |
| Autenticación | `/auth/login` | Iniciar sesión |
| Usuarios | `/usuarios` | Listar, crear, actualizar y eliminar |
| Clientes | `/clientes` | Listar, crear, actualizar y eliminar |
| Tipos de habitación | `/tipos-habitacion` | Listar, crear, actualizar y eliminar |
| Habitaciones | `/habitaciones` | Listar, crear, actualizar y eliminar |
| Reservas | `/reservas` | Listar, crear, actualizar y eliminar |
| Facturas | `/facturas` | Listar, crear, actualizar y eliminar |
| Mantenimientos | `/mantenimientos` | Listar, crear, actualizar y eliminar |

Todas las rutas, excepto `/auth/login` y las rutas de documentación, requieren
un token JWT válido.

## Solución de problemas

- **Error de conexión en el frontend:** comprueba que el backend esté
  ejecutándose en el puerto 8083.
- **Error `401 Unauthorized`:** inicia sesión de nuevo; el token pudo haber
  expirado o ser inválido.
- **No aparece el administrador:** verifica que `ADMIN_EMAIL` y
  `ADMIN_PASSWORD` estén definidos antes de iniciar el backend.
- **El puerto está ocupado:** cambia `server.port` en
  `hotel-backend/src/main/resources/application.properties` y actualiza la
  URL base del frontend y la URL `servers` de `docs/openapi.yaml`.

## Licencia

Este proyecto es de uso académico.
