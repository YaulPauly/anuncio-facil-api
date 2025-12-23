# Anuncio Fácil API

Guía rápida para levantar el backend con Docker y probar el login.

## Requisitos
- Docker y Docker Compose 3.9+ instalados.

## Variables de entorno
El proyecto usa un archivo `.env` en la raíz (ya incluido) con credenciales de base de datos y JWT:

```
DB_HOST=mysql
DB_PORT=3306
DB_NAME=anunciodb
DB_USER=anuncio
DB_PASSWORD=anuncio123

MYSQL_DATABASE=anunciodb
MYSQL_USER=anuncio
MYSQL_PASSWORD=anuncio123
MYSQL_ROOT_PASSWORD=rootpassword

JWT_SECRET=c3VwZXItc2VjcmV0LWtleS13aXRoLTMyLWJ5dGVzISE=
JWT_EXPIRATION_MS=86400000
```

`JWT_SECRET` debe ser una cadena Base64 de 32 bytes para firmar los tokens.

## Levantar con Docker
Desde la raíz del proyecto:

```bash
docker compose up --build -d
```

- Se crean dos contenedores: `anuncio-facil-mysql` (MySQL 8) y `anuncio-facil-api` (Spring Boot).
- La base de datos se inicializa con `sql/init.sql` y persiste en el volumen `mysql_data`.
- La API queda disponible en `http://localhost:8080`.

Para ver logs:

```bash
docker compose logs -f api
```

Para detener (opcional `-v` elimina datos):

```bash
docker compose down        # conserva el volumen
docker compose down -v     # borra el volumen mysql_data
```

## Probar el servicio de login
La semilla crea dos usuarios con contraseña `password`:
- `admin@anuncio.com` (rol ADMIN)
- `user@anuncio.com` (rol USER)

Endpoint:
- Método: `POST`
- URL: `http://localhost:8080/auth/login`
- Body JSON:

```json
{
  "email": "admin@anuncio.com",
  "password": "password"
}
```

Ejemplo con `curl`:

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@anuncio.com","password":"password"}'
```

Ejemplo usuario normal:

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@anuncio.com","password":"password"}'
```

Subida de imagen (usa el token y luego guarda la URL en el anuncio):

```bash
curl -X POST http://localhost:8080/media \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@/ruta/a/tu-imagen.jpg"
```

Crear anuncio subiendo imagen en el mismo request (multipart):

```bash
curl -X POST http://localhost:8080/ads \
  -H "Authorization: Bearer $TOKEN" \
  -F 'ad={"title":"Anuncio","description":"desc","image":null,"city":"Lima","district":"Miraflores","categoryId":1,"detail":"detalle"};type=application/json' \
  -F "file=@/ruta/a/tu-imagen.jpg"
```

Solo el dueño o un usuario con rol ADMIN puede editar/modificar un anuncio.

Respuesta esperada:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "admin@anuncio.com",
  "firstName": "Admin",
  "lastName": "Anuncio",
  "role": "ADMIN"
}
```

Usa el token con `Authorization: Bearer <token>` para acceder a los endpoints protegidos.

Endpoints públicos adicionales:
- `GET /categories`

## Estructura del proyecto
Resumen de carpetas y responsabilidades en `STRUCTURE.md`.
