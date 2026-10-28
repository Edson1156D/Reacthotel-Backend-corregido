# Guía Backend Java – Hotel (Reacthotel)

Oct 26, 2026 · @Someone

El front Reacthotel usa 29 endpoints: 1 de login y 4 operaciones (listar, crear, editar, eliminar) por cada uno de 7 módulos. Esta guía da el contrato, los datos y los prompts para que Copilot construya el backend sin salirse del proyecto.

## Cómo usar esta guía

Trabaja los prompts en orden (0 al 13), uno por chat de Copilot, y haz commit después de cada uno. Cada prompt repite la regla de no inventar porque Copilot pierde el contexto entre chats.

1. Crea el repo del backend y copia ahí el archivo `openapi.yaml` (se entrega aparte) como `docs/openapi.yaml`. Es el contrato que Copilot debe seguir.
2. Copia también esta guía como `docs/GUIA.md` (la puedes exportar a Markdown desde el documento).
3. En cada prompt, adjunta `docs/openapi.yaml` con `#file:docs/openapi.yaml` en Copilot Chat.
4. Al terminar cada fase, abre Swagger UI y prueba los endpoints de esa fase.

**Stack propuesto.** El front no fija tecnologías de backend, así que esta es la elección mínima para cubrir lo que pide. Cámbiala si tu curso o tu repo usa otra.

| Pieza | Propuesta |
| --- | --- |
| Lenguaje y build | Java 25, Maven |
| Framework | Spring Boot 3.x (Web, Data JPA, Validation, Security) |
| Base de datos | PostgreSQL |
| Autenticación | JWT (el front envía `Authorization: Bearer <token>`) |
| Swagger | springdoc-openapi, línea 2.x para Spring Boot 3 |
| Paquetes | `config`, `security`, `controller`, `service`, `repository`, `entity`, `dto`, `exception` |

**Un cambio obligatorio en el front.** En `src/api/axios.js` la `BASE_URL` apunta hoy a `https://hotel-s031.onrender.com`, y los comentarios del código mencionan FastAPI, es decir, un backend anterior. Cuando tu backend Java esté en marcha, cambia solo esa línea por su URL.

## Endpoints (Swagger)

Salen de `src/api/services.js` y `src/api/auth.js`; la columna "Lo llama" indica qué pantalla los usa. Todos exigen JWT salvo el login. El front solo mira si la respuesta es 2xx o error, así que uso 201 en crear, 204 en eliminar y 200 en el resto. El contrato completo con esquemas está en `openapi.yaml`.

| Método | Ruta | Lo llama | Body que envía el front | Respuesta |
| --- | --- | --- | --- | --- |
| POST | `/auth/login` | Login | `{email, password}` | `{access_token, token_type}` |
| GET | `/usuarios/` | Usuarios, Mantenimientos | — | lista de Usuario |
| POST | `/usuarios/` | Usuarios | `{nombre, email, password, rol, activo}` | Usuario |
| PUT | `/usuarios/{id}` | Usuarios | `{nombre, email, rol, activo}` (sin password) | Usuario |
| DELETE | `/usuarios/{id}` | Usuarios | — | 204 |
| GET | `/clientes/` | Clientes, Reservas, Facturas | — | lista de Cliente |
| POST | `/clientes/` | Clientes | `{nombre, apellido, dni, email, telefono, direccion}` | Cliente |
| PUT | `/clientes/{id}` | Clientes | los mismos 6 campos | Cliente |
| DELETE | `/clientes/{id}` | Clientes | — | 204 |
| GET | `/tipos-habitacion/` | Tipos, Habitaciones | — | lista de TipoHabitacion |
| POST | `/tipos-habitacion/` | Tipos | `{nombre, descripcion, precio_noche, capacidad, imagen_url}` | TipoHabitacion |
| PUT | `/tipos-habitacion/{id}` | Tipos | los mismos 5 campos | TipoHabitacion |
| DELETE | `/tipos-habitacion/{id}` | Tipos | — | 204 |
| GET | `/habitaciones/` | Habitaciones, Reservas, Facturas, Mantenimientos | — | lista de Habitacion (con objeto `tipo`) |
| POST | `/habitaciones/` | Habitaciones | `{numero, piso, tipo_id, descripcion, estado}` | Habitacion |
| PUT | `/habitaciones/{id}` | Habitaciones, Reservas, Facturas | los 5 campos, o solo `{estado}` | Habitacion |
| DELETE | `/habitaciones/{id}` | Habitaciones | — | 204 |
| GET | `/reservas/` | Reservas, Facturas | — | lista de Reserva |
| POST | `/reservas/` | Reservas | `{cliente_id, habitacion_id, fecha_entrada, fecha_salida, observaciones}` | Reserva |
| PUT | `/reservas/{id}` | Reservas, Facturas | `{habitacion_id, fecha_entrada, fecha_salida, observaciones, estado}`, o solo `{estado}` | Reserva |
| DELETE | `/reservas/{id}` | Reservas | — | 204 |
| GET | `/facturas/` | Facturas | — | lista de Factura |
| POST | `/facturas/` | Facturas | `{reserva_id, notas}` | Factura |
| PUT | `/facturas/{id}` | Facturas | `{estado}` | Factura |
| DELETE | `/facturas/{id}` | Facturas | — | 204 |
| GET | `/mantenimientos/` | Mantenimientos | — | lista de Mantenimiento |
| POST | `/mantenimientos/` | Mantenimientos | `{habitacion_id, usuario_id, descripcion}` | Mantenimiento |
| PUT | `/mantenimientos/{id}` | Mantenimientos | `{descripcion, usuario_id}`, o `{resuelto, fecha_fin}` | Mantenimiento |
| DELETE | `/mantenimientos/{id}` | Mantenimientos | — | 204 |

No hay GET por id, registro de usuarios ni paginación: el front no los usa, por eso no están.

## Modelo de datos

Sale de los formularios y de lo que cada pantalla lee de las respuestas. Un asterisco (\*) marca los campos que el formulario exige. Los ids son numéricos porque el front hace `parseInt` sobre ellos.

- **Usuario**: `id`, `nombre`*, `email`* (único, es el login), `password`\* (solo al crear; se guarda con BCrypt y nunca se devuelve), `rol`\* (`ADMIN`, `RECEPCION` o `MANTENIMIENTO`; el formulario parte en `RECEPCION`), `activo` (booleano, parte en `true`).
- **Cliente**: `id`, `nombre`*, `apellido`*, `dni`\*, `email`, `telefono`, `direccion` (los tres últimos pueden venir `null`).
- **TipoHabitacion**: `id`, `nombre`*, `descripcion`, `precio_noche`* (decimal), `capacidad`\* (entero, mínimo 1), `imagen_url` (opcional; si falta, el front pinta una imagen por defecto).
- **Habitacion**: `id`, `numero`\* (texto, por ejemplo "101"), `piso` (entero, puede ser `null`), `descripcion`, `estado` (`LIBRE`, `OCUPADA`, `RESERVADA` o `MANTENIMIENTO`; parte en `LIBRE`), y el tipo: se escribe como `tipo_id` y se responde como objeto `tipo` completo, porque el front lee `hab.tipo.id` y `hab.tipo.nombre`.
- **Reserva**: `id`, `cliente_id`*, `habitacion_id`*, `fecha_entrada`\* y `fecha_salida`\* (fecha `YYYY-MM-DD`), `estado` (`PENDIENTE`, `CONFIRMADA`, `CANCELADA` o `COMPLETADA`; parte en `PENDIENTE`), `observaciones`. Se responde plana, solo con ids, porque el front lee `r.cliente_id` y `r.habitacion_id`.
- **Factura**: `id`, `reserva_id`\*, `total` (lo calcula el backend; el front nunca lo envía), `estado` (`PENDIENTE`, `PAGADA` o `ANULADA`; parte en `PENDIENTE`), `notas` (puede ser `null`).
- **Mantenimiento**: `id`, `habitacion_id`*, `usuario_id` (técnico, puede ser `null`), `descripcion`*, `resuelto` (booleano, parte en `false`), `fecha_fin` (fecha y hora ISO-8601, `null` hasta resolverse).

**Relaciones:** Reserva → Cliente y Reserva → Habitacion (muchas a una); Habitacion → TipoHabitacion; Factura → Reserva (una reserva puede tener varias facturas solo si las anteriores están `ANULADA`); Mantenimiento → Habitacion y Mantenimiento → Usuario (opcional).

## Reglas del front que el backend debe respetar

Son detalles del código del front que, si el backend no los cumple, rompen pantallas aunque el endpoint exista.

1. **Barra final en la colección.** El front llama `GET /usuarios/` y `POST /usuarios/` con barra, pero `PUT /usuarios/{id}` sin ella. Spring Boot 3 no trata `/usuarios` y `/usuarios/` como la misma ruta, así que hay que mapear ambas o el front recibe 404. Aplica a los 7 módulos.
2. **JSON en snake\_case.** Los campos viajan como `precio_noche`, `tipo_id`, `fecha_entrada`, `access_token` y así en todo el contrato. Los nombres Java pueden ser camelCase si Jackson los convierte.
3. **Errores como `{"detail": "mensaje"}`.** Usuarios y Clientes leen `error.response.data.detail` y lo muestran tal cual. Un cliente con reservas, o un tipo de habitación con habitaciones, no se puede eliminar: responder 409 con un `detail` claro.
4. **Login.** Credenciales inválidas deben dar 401; el front muestra "Credenciales incorrectas". Guarda `access_token` en `localStorage` y lo envía como `Bearer`. El front no maneja tokens vencidos ni refresh.
5. **PUT parcial.** Reservas, Facturas y Habitaciones envían `PUT` con solo `{estado}`. Un campo ausente no se modifica. Un campo opcional enviado como `null` sí se borra: Mantenimientos manda `usuario_id: null` para quitar al técnico y Habitaciones manda `piso: null`.
6. **Fechas.** `fecha_entrada` y `fecha_salida` son texto `YYYY-MM-DD`, porque el front las compara como cadenas con la fecha de hoy. `fecha_fin` de Mantenimiento llega como ISO-8601 con zona, por ejemplo `2026-10-26T14:30:00.000Z`.
7. **Validaciones de reserva.** El front ya valida que la salida sea posterior a la entrada y que no se solape con otra reserva de la misma habitación (ignorando las `CANCELADA` y `COMPLETADA` y a la propia al editar; solapa si `inicio < otraSalida` y `fin > otraEntrada`). El backend debe repetirlo y responder 400 o 409 con `detail`.
8. **Validaciones de factura.** Solo se factura una reserva `CONFIRMADA` que no tenga ya una factura que no esté `ANULADA`. El `total` lo calcula el backend (ver puntos a confirmar).
9. **Automatismos: los hace el front.** El front encadena varios `PUT`: autoconfirma reservas de hoy, pone la habitación `OCUPADA`, y al pagar una factura pasa la reserva a `COMPLETADA` y libera la habitación. El backend no debe repetirlos, solo aceptar esos `PUT`; si no, el cambio se aplicaría dos veces.
10. **CORS.** Permitir el origen del front desplegado en Vercel y `http://localhost:5173` (el puerto por defecto de Vite), con los métodos `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS` y el header `Authorization`. La URL de Vercel va en una variable de entorno.
11. **Primer usuario.** Todo exige login y el front no tiene pantalla de registro, así que el backend debe crear un `ADMIN` inicial al arrancar, con email y contraseña tomados de variables de entorno.
12. **Roles.** El menú es el mismo para los tres roles y ninguna ruta se restringe por rol, por lo que el backend solo exige estar autenticado. La pantalla de Mantenimientos filtra en el front a los usuarios con rol `MANTENIMIENTO` como técnicos.

## Prompts 0 a 4: base del proyecto

### Prompt 0 — Contexto maestro

Pégalo primero en Copilot Chat. Guárdalo además como `.github/copilot-instructions.md` para que Copilot lo lea en cada chat; los prompts siguientes lo citan como "las reglas del Prompt 0".

```text
Eres mi asistente para construir el backend en Java de un sistema de gestión de hotel. El front (React + Vite) ya existe y el backend debe ofrecerle exactamente lo que ese front consume.

REGLAS OBLIGATORIAS EN TODO EL PROYECTO
1. No inventes ni agregues nada que no esté en #file:docs/openapi.yaml o en #file:docs/GUIA.md: ni endpoints, ni campos, ni entidades, ni roles, ni estados, ni reglas de negocio. Si algo falta o es ambiguo, pregúntame antes de escribir código.
2. Los únicos endpoints son los 29 de openapi.yaml. No crees GET por id, registro de usuarios, paginación, búsquedas ni filtros.
3. Stack: Java 25, Maven, Spring Boot 3.x, Spring Web, Spring Data JPA, Validation, Spring Security con JWT, PostgreSQL y springdoc-openapi.
4. JSON en snake_case. Ids de tipo Long. Cada colección responde con y sin barra final (por ejemplo /clientes y /clientes/).
5. Todos los errores responden {"detail": "mensaje en español"}, siempre con detail como texto.
6. Contraseñas con BCrypt. Nunca devuelvas password en una respuesta.
7. No reimplementes en el backend los cambios de estado en cadena (reserva, habitación, factura): los hace el front con varios PUT. El backend solo guarda lo que recibe y valida lo indicado.
8. Los PUT son parciales: un campo ausente no se modifica; un campo opcional enviado como null se borra.
9. Paquetes: config, security, controller, service, repository, entity, dto, exception. Nunca devuelvas entidades JPA: usa DTOs. Comentarios y mensajes en español.
10. Al terminar cada tarea, lista los archivos creados y qué endpoints quedan listos para probar en Swagger.

Responde solo "entendido" y resume estas reglas en 5 líneas.
```

### Prompt 1 — Proyecto base y Swagger

Aplica las reglas del Prompt 0 (#file:.github/copilot-instructions.md).

Tarea: crear el proyecto Spring Boot base.
- Maven, Java 25, groupId com.hotel, artifactId hotel-backend.
- Dependencias: web, data-jpa, validation, security, h2 (scope runtime), springdoc-openapi-starter-webmvc-ui (línea 2.x para Spring Boot 3) y lombok. NO agregues PostgreSQL todavía.
- Base de datos H2 en memoria: spring.datasource.url=jdbc:h2:mem:hotel;DB_CLOSE_DELAY=-1, usuario sa, sin contraseña. Deja la consola H2 desactivada.
- application.properties con spring.profiles.active=dev por defecto. Las variables JWT_SECRET, JWT_EXPIRATION_MINUTES, ADMIN_EMAIL, ADMIN_PASSWORD y CORS_ALLOWED_ORIGINS se leen del entorno; en application-dev.properties dales valores por defecto de desarrollo (JWT_SECRET de al menos 32 caracteres, claramente falso; CORS_ALLOWED_ORIGINS=http://localhost:5173). Ningún secreto real en el código.
- spring.jpa.hibernate.ddl-auto=update.
- Jackson en snake_case (spring.jackson.property-naming-strategy=SNAKE_CASE) y fechas como texto ISO, no como timestamps numéricos.
- Swagger UI en /swagger-ui.html con el título "Hotel API" y el esquema de seguridad Bearer JWT configurado, para que aparezca el botón Authorize.
- Crea los paquetes vacíos de la regla 9.

No crees entidades ni controladores todavía.
Criterio de aceptación: la app arranca sin instalar ninguna base de datos y /swagger-ui.html abre sin errores.

### Prompt 2 — Entidades, enums y repositorios

Aplica las reglas del Prompt 0.

Tarea: crear SOLO entidades JPA, enums y repositorios, según la sección "Modelo de datos" de #file:docs/GUIA.md y los schemas de #file:docs/openapi.yaml.

Entidades: Usuario, Cliente, TipoHabitacion, Habitacion, Reserva, Factura, Mantenimiento.
Enums: Rol (ADMIN, RECEPCION, MANTENIMIENTO), EstadoHabitacion (LIBRE, OCUPADA, RESERVADA, MANTENIMIENTO), EstadoReserva (PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA), EstadoFactura (PENDIENTE, PAGADA, ANULADA).

Detalles:
- id Long autogenerado en todas.
- precio_noche y total como BigDecimal; fecha_entrada y fecha_salida como LocalDate; fecha_fin como Instant.
- Valores por defecto: Usuario.rol=RECEPCION y activo=true; Habitacion.estado=LIBRE; Reserva.estado=PENDIENTE; Factura.estado=PENDIENTE; Mantenimiento.resuelto=false.
- Relaciones: Habitacion N-1 TipoHabitacion; Reserva N-1 Cliente y N-1 Habitacion; Factura N-1 Reserva; Mantenimiento N-1 Habitacion y N-1 Usuario (opcional).
- Campos opcionales que aceptan null: Cliente.email/telefono/direccion, TipoHabitacion.descripcion/imagen_url, Habitacion.piso/descripcion, Reserva.observaciones, Factura.notas, Mantenimiento.usuario_id/fecha_fin.
- Solo Usuario.email es único. No agregues otras restricciones únicas.
- Un JpaRepository por entidad, sin métodos extra: los agregaremos en cada módulo cuando se necesiten.

No crees DTOs ni controladores todavía.
Criterio de aceptación: la app arranca y crea las 7 tablas en H2.

### Prompt 3 — Manejo global de errores

Va antes de los módulos para que todos devuelvan el mismo formato `detail` que lee el front.

```text
Aplica las reglas del Prompt 0.

Tarea: manejo global de errores.
- En el paquete exception crea: RecursoNoEncontradoException (404), ConflictoException (409) y SolicitudInvalidaException (400), todas con un mensaje en español.
- Crea un @RestControllerAdvice que responda SIEMPRE un JSON con una sola clave: {"detail": "<mensaje>"}, donde detail es un string.
  - Validación @Valid (MethodArgumentNotValidException): 400 con detail = "campo: mensaje; campo: mensaje".
  - JSON ilegible, enum o fecha inválidos (HttpMessageNotReadableException): 400.
  - Las tres excepciones propias: su código.
  - DataIntegrityViolationException: 409 con "No se puede completar la operación: el registro está relacionado con otros datos".
  - Cualquier otra excepción: 500 con "Error interno del servidor", sin stacktrace.
- No agregues otros campos al JSON de error.

No crees controladores.
Criterio de aceptación: la app compila y arranca.
```

### Prompt 4 — Seguridad JWT, login y primer administrador

```text
Aplica las reglas del Prompt 0.

Tarea: autenticación, según el tag Auth de #file:docs/openapi.yaml.
1. POST /auth/login (público). Body {email, password}. Éxito: 200 con {"access_token": "...", "token_type": "bearer"}. Credenciales inválidas: 401 con {"detail": "Credenciales incorrectas"}. Si el usuario tiene activo=false, también 401.
2. JWT HS256 firmado con JWT_SECRET, vigencia JWT_EXPIRATION_MINUTES, subject = email del usuario.
3. Filtro que lee Authorization: Bearer <token>. Todas las rutas exigen token excepto /auth/login, /swagger-ui.html, /swagger-ui/**, /v3/api-docs/** y las peticiones OPTIONS.
4. Sin token o con token inválido o vencido: 401 con {"detail": "..."} (usa un AuthenticationEntryPoint).
5. Sin restricciones por rol: cualquier usuario autenticado accede a todo.
6. CORS con CORS_ALLOWED_ORIGINS (lista separada por comas), métodos GET, POST, PUT, DELETE, OPTIONS y header Authorization.
7. Al arrancar, si no existe un usuario con ADMIN_EMAIL, créalo con rol ADMIN, activo=true, nombre "Administrador" y contraseña ADMIN_PASSWORD en BCrypt. No hardcodees credenciales.
8. Sesión sin estado (STATELESS), CSRF desactivado.

Criterio de aceptación: en Swagger, POST /auth/login con el admin devuelve el token y el botón Authorize lo acepta.
```

## Prompts 5 a 11: un módulo por prompt

Cada módulo sigue el mismo patrón: DTOs, service, controller y las validaciones que el front ya aplica. Adjunta siempre `#file:docs/openapi.yaml`. El orden importa porque Reservas depende de Clientes y Habitaciones, y Facturas depende de Reservas.

### Prompt 5 — Usuarios

```text
Aplica las reglas del Prompt 0.

Tarea: módulo Usuarios, según el tag Usuarios de #file:docs/openapi.yaml. Cada ruta de colección responde con y sin barra final.
- GET /usuarios/ -> lista de UsuarioResponse {id, nombre, email, rol, activo}.
- POST /usuarios/ -> body UsuarioCreate {nombre*, email*, password*, rol, activo}; rol por defecto RECEPCION y activo por defecto true. Responde 201 con UsuarioResponse. Email repetido: 409 {"detail": "El email ya está registrado"}.
- PUT /usuarios/{id} -> body UsuarioUpdate {nombre, email, rol, activo}, parcial y SIN password. Si no existe: 404 {"detail": "Usuario no encontrado"}. Si el email nuevo ya es de otro usuario: 409.
- DELETE /usuarios/{id} -> 204. Si el usuario tiene mantenimientos asignados: 409 con detail claro.

Valida que email tenga formato válido y que nombre y password no estén vacíos. La contraseña se guarda con BCrypt. UsuarioResponse nunca incluye password. No crees endpoint para cambiar la contraseña: el front no lo usa.
Anota el controller con @Tag(name = "Usuarios") y cada método con @Operation en español.

Criterio de aceptación: en Swagger puedo crear, listar, editar y eliminar un usuario, y el nuevo usuario puede hacer login.
```

### Prompt 6 — Clientes

```text
Aplica las reglas del Prompt 0.

Tarea: módulo Clientes, según el tag Clientes de #file:docs/openapi.yaml.
- GET /clientes/ -> lista de ClienteResponse {id, nombre, apellido, dni, email, telefono, direccion}.
- POST /clientes/ -> body ClienteRequest {nombre*, apellido*, dni*, email, telefono, direccion}. Responde 201.
- PUT /clientes/{id} -> mismo body, parcial. 404 {"detail": "Cliente no encontrado"} si no existe.
- DELETE /clientes/{id} -> 204. Si el cliente tiene reservas: 409 {"detail": "No se puede eliminar: el cliente tiene reservas asociadas"}. (Agrega a ReservaRepository solo el método existsByClienteId.)

Valida que nombre, apellido y dni no estén vacíos, y que email, si viene, tenga formato válido. email, telefono y direccion pueden ser null. No agregues unicidad a dni.
Anota con @Tag(name = "Clientes") y @Operation en español.

Criterio de aceptación: CRUD completo probado en Swagger.
```

### Prompt 7 — Tipos de habitación

```text
Aplica las reglas del Prompt 0.

Tarea: módulo Tipos de habitación, según el tag TiposHabitacion de #file:docs/openapi.yaml. La ruta es /tipos-habitacion/ (con y sin barra final).
- GET /tipos-habitacion/ -> lista de TipoHabitacionResponse {id, nombre, descripcion, precio_noche, capacidad, imagen_url}.
- POST /tipos-habitacion/ -> body {nombre*, descripcion, precio_noche*, capacidad*, imagen_url}. Responde 201.
- PUT /tipos-habitacion/{id} -> mismo body, parcial. 404 si no existe.
- DELETE /tipos-habitacion/{id} -> 204. Si hay habitaciones de ese tipo: 409 {"detail": "No se puede eliminar: existen habitaciones de este tipo"}. (Agrega a HabitacionRepository solo existsByTipoId.)

Valida que nombre no esté vacío, precio_noche sea obligatorio y no negativo, y capacidad sea un entero mayor o igual a 1. descripcion e imagen_url pueden ser null.
Anota con @Tag(name = "TiposHabitacion") y @Operation en español.

Criterio de aceptación: CRUD completo probado en Swagger.
```

### Prompt 8 — Habitaciones

```text
Aplica las reglas del Prompt 0.

Tarea: módulo Habitaciones, según el tag Habitaciones de #file:docs/openapi.yaml.
- GET /habitaciones/ -> lista de HabitacionResponse {id, numero, piso, descripcion, estado, tipo}, donde tipo es el objeto completo TipoHabitacionResponse (el front lee hab.tipo.id y hab.tipo.nombre).
- POST /habitaciones/ -> body HabitacionCreate {numero*, piso, tipo_id*, descripcion, estado}; estado por defecto LIBRE. Responde 201 con HabitacionResponse. Si tipo_id no existe: 404 {"detail": "Tipo de habitación no encontrado"}.
- PUT /habitaciones/{id} -> body HabitacionUpdate {numero, piso, tipo_id, descripcion, estado}, TODOS opcionales. El front también envía solo {estado}, y envía piso null para borrar el piso. 404 si la habitación no existe.
- DELETE /habitaciones/{id} -> 204. Si tiene reservas o mantenimientos: 409 con detail claro. (Agrega solo existsByHabitacionId a los repositorios de Reserva y Mantenimiento.)

No cambies el estado de la habitación por tu cuenta: solo el que reciba el PUT. No valides números repetidos: el front no lo define.
Anota con @Tag(name = "Habitaciones") y @Operation en español.

Criterio de aceptación: puedo crear una habitación con un tipo, cambiar solo su estado con PUT {"estado": "OCUPADA"} y borrar su piso con {"piso": null}.
```

### Prompt 9 — Reservas

```text
Aplica las reglas del Prompt 0.

Tarea: módulo Reservas, según el tag Reservas de #file:docs/openapi.yaml.
- GET /reservas/ -> lista de ReservaResponse {id, cliente_id, habitacion_id, fecha_entrada, fecha_salida, estado, observaciones}. Respuesta plana, solo ids, sin objetos anidados. Las fechas salen como texto YYYY-MM-DD.
- POST /reservas/ -> body ReservaCreate {cliente_id*, habitacion_id*, fecha_entrada*, fecha_salida*, observaciones}; estado inicial PENDIENTE. Responde 201. Si el cliente o la habitación no existen: 404 con detail.
- PUT /reservas/{id} -> body ReservaUpdate {habitacion_id, fecha_entrada, fecha_salida, observaciones, estado}, TODOS opcionales (el front también envía solo {estado}). cliente_id no se puede cambiar. 404 si no existe.
- DELETE /reservas/{id} -> 204. Si tiene facturas: 409 con detail claro. (Agrega solo existsByReservaId a FacturaRepository.)

Validaciones (las mismas que hoy hace el front; responde con detail en español):
1. fecha_salida debe ser posterior a fecha_entrada: 400.
2. No puede solaparse con otra reserva de la misma habitación. Solapa si existe otra reserva con id distinto, estado distinto de CANCELADA y COMPLETADA, fecha_entrada menor que la nueva salida y fecha_salida mayor que la nueva entrada. Error: 409 "La habitación ya está reservada en esas fechas (Ref #<id>)".
3. En el PUT, aplica 1 y 2 solo si el body trae habitacion_id, fecha_entrada o fecha_salida (combinando con los valores actuales). Si el body trae solo estado, NO revalides.

No cambies el estado de la habitación ni de ninguna otra entidad al guardar una reserva: lo hace el front.
Anota con @Tag(name = "Reservas") y @Operation en español.

Criterio de aceptación: dos reservas solapadas en la misma habitación dan 409; PUT {"estado": "CONFIRMADA"} funciona sin enviar fechas.
```

### Prompt 10 — Facturas

```text
Aplica las reglas del Prompt 0.

Tarea: módulo Facturas, según el tag Facturas de #file:docs/openapi.yaml.
- GET /facturas/ -> lista de FacturaResponse {id, reserva_id, total, estado, notas}.
- POST /facturas/ -> body FacturaCreate {reserva_id*, notas}. Responde 201 con estado PENDIENTE. Reglas:
  1. La reserva debe existir: 404 con detail.
  2. La reserva debe estar CONFIRMADA: si no, 400 "Solo se puede facturar una reserva CONFIRMADA".
  3. No debe existir otra factura de esa reserva cuyo estado sea distinto de ANULADA: si existe, 409 "La reserva ya tiene una factura activa".
  4. total = número de noches (días entre fecha_entrada y fecha_salida) x precio_noche del tipo de la habitación de la reserva. El cliente nunca envía el total.
- PUT /facturas/{id} -> body FacturaUpdate {estado}, con valores PENDIENTE, PAGADA o ANULADA. Solo cambia el estado de la factura; no toques la reserva ni la habitación (lo hace el front). 404 si no existe.
- DELETE /facturas/{id} -> 204, borrado físico.

Anota con @Tag(name = "Facturas") y @Operation en español.

Criterio de aceptación: facturar una reserva PENDIENTE da 400; facturar una CONFIRMADA de 3 noches con tipo de 100 de precio da total 300; una segunda factura de la misma reserva da 409 salvo que la primera esté ANULADA.
```

### Prompt 11 — Mantenimientos

```text
Aplica las reglas del Prompt 0.

Tarea: módulo Mantenimientos, según el tag Mantenimientos de #file:docs/openapi.yaml.
- GET /mantenimientos/ -> lista de MantenimientoResponse {id, habitacion_id, usuario_id, descripcion, resuelto, fecha_fin}. fecha_fin sale en ISO-8601 con zona, o null.
- POST /mantenimientos/ -> body MantenimientoCreate {habitacion_id*, usuario_id, descripcion*}. Responde 201 con resuelto=false y fecha_fin=null. Reglas:
  1. La habitación debe existir: 404.
  2. Si viene usuario_id, el usuario debe existir y tener rol MANTENIMIENTO: si no, 400 con detail.
  3. La habitación no puede tener otro mantenimiento con resuelto=false: 409 "La habitación ya tiene un mantenimiento pendiente".
- PUT /mantenimientos/{id} -> body MantenimientoUpdate {descripcion, usuario_id, resuelto, fecha_fin}, TODOS opcionales. El front envía {descripcion, usuario_id} al editar y {resuelto: true, fecha_fin: "<ISO>"} al marcar como lista. usuario_id enviado como null quita al técnico. habitacion_id no se puede cambiar. Aplica la regla 2 si usuario_id no es null. 404 si no existe.
- DELETE /mantenimientos/{id} -> 204.

No cambies el estado de la habitación al crear o resolver un mantenimiento: el front no lo hace.
Anota con @Tag(name = "Mantenimientos") y @Operation en español.

Criterio de aceptación: PUT {"resuelto": true, "fecha_fin": "2026-10-26T14:30:00.000Z"} funciona, y PUT {"usuario_id": null} deja la orden sin técnico.
```

## Prompts 12 y 13: verificación y despliegue

### Prompt 12 — Auditoría contra el contrato

Ejecútalo cuando los 7 módulos funcionen. Sirve para cazar endpoints de más o de menos antes de conectar el front.

```text
Aplica las reglas del Prompt 0.

Tarea: auditoría. No crees funciones nuevas; solo corrige diferencias contra #file:docs/openapi.yaml.
1. Lista todos los endpoints que exponen mis controladores y compáralos con los 29 de openapi.yaml. Reporta los que sobran o faltan.
2. Confirma que cada ruta de colección responde con y sin barra final.
3. Compara los nombres de campos JSON de cada DTO con los schemas de openapi.yaml (snake_case, mismos nombres, mismos opcionales).
4. Confirma que todo error devuelve {"detail": "<string>"}: validación, 401, 404, 409 y 500.
5. Confirma que los PUT son parciales: un campo ausente no cambia y un campo opcional enviado como null se borra.
6. Confirma que ninguna respuesta incluye password.
7. Compara /v3/api-docs con openapi.yaml en rutas, métodos y códigos de respuesta.
Entrégame una tabla: endpoint | estado (OK / diferencia) | corrección aplicada.
```

Después prueba el flujo completo desde el front, con `BASE_URL` apuntando a tu backend:

- [ ] Iniciar sesión con el administrador inicial.
- [ ] Crear un tipo de habitación y una habitación de ese tipo.
- [ ] Crear un cliente y una reserva para hoy; al abrir Reservas debe pasar a CONFIRMADA y la habitación a OCUPADA.
- [ ] Intentar una segunda reserva solapada: debe mostrar el error del backend.
- [ ] Emitir la factura de esa reserva y registrar el pago: reserva COMPLETADA y habitación LIBRE.
- [ ] Crear un usuario con rol MANTENIMIENTO, abrir una orden de mantenimiento asignada a él y marcarla como lista.
- [ ] Intentar eliminar un cliente con reservas: debe mostrar el error y no borrarlo.

### Prompt 13 — Despliegue (opcional)

Usa este prompt solo si vas a publicar el backend. El front anterior estaba en Render, pero el front no obliga a ninguna plataforma.

```text
Aplica las reglas del Prompt 0.

Tarea: preparar el despliegue sin tocar la lógica del negocio.
- Dockerfile multi-stage (build con Maven, ejecución con un JRE 17).
- server.port=${PORT:8080}.
- .dockerignore y .gitignore adecuados para Maven y para que no se suban secretos.
- README.md con la lista de variables de entorno (DB_URL, DB_USER, DB_PASSWORD, JWT_SECRET, JWT_EXPIRATION_MINUTES, ADMIN_EMAIL, ADMIN_PASSWORD, CORS_ALLOWED_ORIGINS), para qué sirve cada una y un ejemplo con valores falsos.
- No cambies controladores, servicios ni entidades.

Criterio de aceptación: docker build termina bien y el contenedor arranca con esas variables.
```

Al desplegar, incluye en `CORS_ALLOWED_ORIGINS` la URL de tu front en Vercel y luego actualiza `BASE_URL` en `src/api/axios.js`.

## Qué NO debe crear Copilot

Copilot tiende a "completar" un backend con extras. Nada de esto aparece en el front, así que si lo genera, pídele que lo quite:

- GET por id (`/clientes/{id}` y similares), registro de usuarios, cambio o recuperación de contraseña, logout y refresh token.
- Paginación, búsquedas, filtros u ordenamiento: el front trae las listas completas y filtra por su cuenta.
- Restricciones por rol (`@PreAuthorize` y similares).
- Cambios de estado en cadena: autoconfirmar reservas, ocupar o liberar habitaciones, completar reservas al pagar. Ya los hace el front.
- Campos o entidades que el front no usa: fecha de emisión o método de pago en facturas, impuestos, fecha de creación, huéspedes, servicios, reportes o dashboards.
- Subida de imágenes: `imagen_url` es solo un texto con una URL.

## Puntos a confirmar

El front no define estos detalles. La guía tomó la opción más razonable; si no es la tuya, cambia el prompt indicado antes de ejecutarlo.

| Tema | Qué asume la guía | Prompt |
| --- | --- | --- |
| Total de la factura | El front solo muestra `f.total` y nunca lo envía. La guía lo calcula como noches × precio\_noche del tipo de habitación. Si tu proyecto exige impuestos o descuentos, hay que decirlo. | 10 |
| Usuario con `activo = false` | No puede iniciar sesión (401), porque la pantalla lo llama "Suspendido". El front no lo exige. | 4 |
| DNI de clientes | No es único: el front no lo define. Solo `Usuario.email` es único. | 2, 6 |
| Roles | Cualquier usuario autenticado accede a todo, igual que el menú del front. Restringir por rol exigiría cambiar también el front. | 4 |
| Vigencia del token | La decide `JWT_EXPIRATION_MINUTES`. El front no maneja tokens vencidos: al vencer, las pantallas fallan hasta cerrar sesión y volver a entrar. | 1, 4 |
| `token_type` | El front solo lee `access_token`; la guía devuelve `"bearer"`. | 4 |
| Tecnologías | Java 25, Spring Boot 3.x, PostgreSQL y springdoc 2.x son propuestas. Si tu repo usa Spring Boot 4, verifica la versión compatible de springdoc antes del Prompt 1. | 0, 1 |
