Eres mi asistente para construir el backend en Java de un sistema de gestión de hotel. El front (React + Vite) ya existe y el backend debe ofrecerle exactamente lo que ese front consume.

REGLAS OBLIGATORIAS EN TODO EL PROYECTO
1. No inventes ni agregues nada que no esté en #file:../docs/openapi.yaml o en #file:../docs/GUIA.md ni endpoints, ni campos, ni entidades, ni roles, ni estados, ni reglas de negocio. Si algo falta o es ambiguo, pregúntame antes de escribir código.
2. Los únicos endpoints son los 29 de openapi.yaml. No crees GET por id, registro de usuarios, paginación, búsquedas ni filtros.
3. Stack: Java 25, Maven, Spring Boot 3.x, Spring Web, Spring Data JPA, Validation, Spring Security con JWT, H2 en memoria (por ahora; PostgreSQL llegará en un avance posterior) y springdoc-openapi.
4. JSON en snake_case. Ids de tipo Long. Cada colección responde con y sin barra final (por ejemplo /clientes y /clientes/).
5. Todos los errores responden {"detail": "mensaje en español"}, siempre con detail como texto.
6. Contraseñas con BCrypt. Nunca devuelvas password en una respuesta.
7. No reimplementes en el backend los cambios de estado en cadena (reserva, habitación, factura): los hace el front con varios PUT. El backend solo guarda lo que recibe y valida lo indicado.
8. Los PUT son parciales: un campo ausente no se modifica; un campo opcional enviado como null se borra.
9. Paquetes: config, security, controller, service, repository, entity, dto, exception. Nunca devuelvas entidades JPA: usa DTOs. Comentarios y mensajes en español.
10. Al terminar cada tarea, lista los archivos creados y qué endpoints quedan listos para probar en Swagger.

Responde solo "entendido" y resume estas reglas en 5 líneas.