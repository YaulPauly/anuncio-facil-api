# Servicios y ejemplos de uso

Auth (`/auth/**`) no requiere token. `GET /ads` y `GET /ads/{id}` son públicos; el resto usa header `Authorization: Bearer <token>`.

Base URL (docker por defecto): `http://localhost:8080`

## Auth

- **POST /auth/login**
  - Body:
    ```json
    { "email": "admin@anuncio.com", "password": "password" }
    ```
  - Ejemplo:
    ```bash
    curl -X POST http://localhost:8080/auth/login \
      -H "Content-Type: application/json" \
      -d '{"email":"admin@anuncio.com","password":"password"}'
    ```
  - Ejemplo usuario normal:
    ```bash
    curl -X POST http://localhost:8080/auth/login \
      -H "Content-Type: application/json" \
      -d '{"email":"user@anuncio.com","password":"password"}'
    ```
- **POST /auth/register** (registro de usuario normal)
  ```bash
  curl -X POST http://localhost:8080/auth/register \
    -H "Content-Type: application/json" \
    -d '{"email":"nuevo@anuncio.com","password":"clave123","firstName":"Nuevo","lastName":"Usuario"}'
  ```

## Media (subida de imágenes a R2)
- **POST /media** (multipart/form-data, autenticado)
  ```bash
  curl -X POST http://localhost:8080/media \
    -H "Authorization: Bearer $TOKEN" \
    -F "file=@/ruta/a/tu-imagen.jpg"
  ```
  Respuesta:
  ```json
  {"url":"https://<endpoint>/<bucket>/uploads/....jpg"}
  ```

## Categorías

- **POST /categories** crear
  ```bash
  curl -X POST http://localhost:8080/categories \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN" \
    -d '{"name":"Vehiculos","description":"Autos y motos"}'
  ```
- **PUT /categories/{id}** actualizar
  ```bash
  curl -X PUT http://localhost:8080/categories/1 \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN" \
    -d '{"name":"Tecnologia","description":"Gadgets y más"}'
  ```
- **DELETE /categories/{id}** eliminar
  ```bash
  curl -X DELETE http://localhost:8080/categories/2 \
    -H "Authorization: Bearer $TOKEN"
  ```
- **GET /categories** listar
  ```bash
  curl http://localhost:8080/categories
  ```
- **GET /categories/{id}** obtener
  ```bash
  curl http://localhost:8080/categories/1
  ```

## Anuncios
Solo el dueño o un usuario con rol ADMIN puede editar/modificar un anuncio.

- **POST /ads** crear (multipart: `ad` como JSON + `file` opcional para imagen)
  ```bash
  curl -X POST http://localhost:8080/ads \
    -H "Authorization: Bearer $TOKEN" \
    -F 'ad={"title":"Laptop en buen estado","description":"16GB RAM","image":null,"city":"Lima","district":"Miraflores","categoryId":1,"detail":"Incluye cargador"};type=application/json' \
    -F "file=@/ruta/a/tu-imagen.jpg"
  ```
- **PUT /ads/{id}** actualizar (multipart)
  ```bash
  curl -X PUT http://localhost:8080/ads/1 \
    -H "Authorization: Bearer $TOKEN" \
    -F 'ad={"title":"Laptop actualizada","description":"32GB RAM","image":null,"city":"Lima","district":"Miraflores","categoryId":1,"detail":"Nueva descripción"};type=application/json' \
    -F "file=@/ruta/a/tu-imagen.jpg"
  ```
- **PATCH /ads/{id}/status?status=INACTIVO|ACTIVO** cambiar estado
  ```bash
  curl -X PATCH "http://localhost:8080/ads/1/status?status=INACTIVO" \
    -H "Authorization: Bearer $TOKEN"
  ```
- **GET /ads/{id}** detalle
  ```bash
  curl http://localhost:8080/ads/1
  ```
- **GET /ads** listar con paginación (filtros opcionales `categoryId`, `city`, `district`, `status`, `page`, `size`)
  ```bash
  curl "http://localhost:8080/ads?categoryId=1&city=Lima&status=ACTIVO&page=0&size=10"
  ```
- **GET /ads/mine** mis anuncios
  ```bash
  curl http://localhost:8080/ads/mine \
    -H "Authorization: Bearer $TOKEN"
  ```
  - Nota: si el token pertenece a un usuario con rol ADMIN devolverá todos los anuncios; si es un usuario normal, solo sus propios anuncios.

## Comentarios (por anuncio)

Ruta base: `/ads/{adId}/comments`

- **POST /** crear comentario
  ```bash
  curl -X POST http://localhost:8080/ads/1/comments \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN" \
    -d '{"content":"¿Sigue disponible?"}'
  ```
- **PUT /{commentId}** actualizar comentario
  ```bash
  curl -X PUT http://localhost:8080/ads/1/comments/1 \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN" \
    -d '{"content":"Confirmo interés"}'
  ```
- **DELETE /{commentId}** eliminar comentario
  ```bash
  curl -X DELETE http://localhost:8080/ads/1/comments/1 \
    -H "Authorization: Bearer $TOKEN"
  ```
- **GET /** listar comentarios de un anuncio con paginación (`page`, `size`)
  ```bash
  curl "http://localhost:8080/ads/1/comments?page=0&size=10"
  ```

## Administración (solo ADMIN)

- **PATCH /users/{id}/status** bloquear/desbloquear usuario (`status` = `ACTIVE` | `BLOCKED`)
  ```bash
  curl -X PATCH http://localhost:8080/users/5/status \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN_ADMIN" \
    -d '{"status":"BLOCKED"}'
  ```
- **GET /users** listar todos los usuarios (solo ADMIN)
  ```bash
  curl http://localhost:8080/users \
    -H "Authorization: Bearer $TOKEN_ADMIN"
  ```
