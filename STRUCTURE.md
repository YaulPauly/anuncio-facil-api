# Estructura del proyecto

Esta API sigue una organización por capas (inspirada en Clean/Hexagonal). Cada carpeta indica su rol y sus dependencias.

## Capas principales
- `domain/`: núcleo de negocio.
  - `model/`: entidades de dominio (`User`, `Role`, etc.).
  - `service/interfaces/`: puertos requeridos por el dominio (p. ej. `UserRepository`). Las implementaciones viven fuera del dominio.
- `application/`: casos de uso y DTOs.
  - `dto/request` y `dto/response`: contratos de entrada/salida (sin anotar entidades JPA).
  - `usecases/interfaces`: contratos de casos de uso (p. ej. `AuthUseCase`).
  - `usecases/impl`: orquestan reglas y colaboran con puertos del dominio (ej. `AuthUseCaseImpl`).
- `infrastructure/`: adapters y detalles técnicos.
  - `controller/`: endpoints REST que traducen HTTP ↔️ DTOs y delegan a casos de uso.
  - `persistence/jpa`: entidades de base (`entity`), repositorios Spring Data (`repository`) y adapters que implementan puertos (`adapter/UserRepositoryAdapter`).
  - `security/`: implementación de seguridad (JWT, `CustomUserDetailsService`).
  - `config/security`: configuración de Spring Security (filtros, rutas públicas, authentication provider).

## Recursos y configuración
- `src/main/resources/application.yaml`: configuración de Spring (datasource, JPA, JWT).
- `sql/init.sql`: seed de base de datos (roles y usuarios de ejemplo).
- `Dockerfile` y `docker-compose.yml`: levantan API y MySQL. Usa `.env` para credenciales y JWT.

## Flujo de login (ejemplo)
1. `POST /auth/login` llega a `AuthController`.
2. El controlador llama a `AuthUseCase.login(...)`.
3. El caso de uso autentica con `AuthenticationManager`, recupera el `User` por email (`UserRepository`), genera JWT (`JwtTokenProvider`) y responde un `AuthResponse`.
4. `JWTAuthorizationFilter` valida tokens en peticiones protegidas y puebla el `SecurityContext`.

## Próximos módulos: anuncios, categorías y comentarios
Guía para agregar el CRUD completo siguiendo la misma estructura.

### Modelo y reglas
- **Usuario**: puede tener varios anuncios; cada anuncio pertenece a un único usuario.
- **Anuncio**: campos `titulo`, `descripcion`, `imagen`, `ciudad`, `distrito`, `categoria`, `estado` (`ACTIVO`/`INACTIVO`), `createdAt`. Incluye un detalle asociado (texto ampliado o metadata específica).
- **Categoría**: entidad propia con su CRUD (nombre, descripción, etc.).
- **Comentarios**: cada comentario pertenece a un anuncio y a un usuario; sin replies anidados (todos al mismo nivel). Solo el autor puede editar/eliminar su comentario.

### Dominios/puertos a crear
- `Ad` y `Category` y `Comment` en `domain/model`.
- Puertos en `domain/service/interfaces`: `AdRepository`, `CategoryRepository`, `CommentRepository`.

### Casos de uso sugeridos
- Anuncios (`application/usecases`):
  - `CreateAd`, `UpdateAd`, `GetAd`, `ListAdsByUser`, `ListAds` (filtros: categoría, ciudad, estado), `ToggleAdStatus` (activo/inactivo).
- Categorías:
  - `CreateCategory`, `UpdateCategory`, `DeleteCategory`, `ListCategories`, `GetCategory`.
- Comentarios:
  - `AddCommentToAd`, `UpdateComment`, `DeleteComment`, `ListCommentsByAd`.

### Infraestructura
- **Controladores REST** en `infrastructure/controller` para cada agregado (AdsController, CategoriesController, CommentsController) que usen DTOs de `application/dto`.
- **Persistencia JPA**:
  - `AdEntity` con relación `@ManyToOne` a `UserEntity`, `@ManyToOne` a `CategoryEntity`, timestamps (`createdAt`), estado.
  - `AdDetailEntity` (1:1 con `AdEntity`) para contenido extendido.
  - `CommentEntity` con `@ManyToOne` a `AdEntity` y `UserEntity`, timestamps.
  - Repositorios Spring Data en `infrastructure/persistence/jpa/repository` y adapters en `.../adapter` que implementen los puertos.
- **Seguridad**: los endpoints de creación/edición/eliminación deben validar que el usuario autenticado es dueño del recurso (anuncio o comentario) o tiene rol admin según tu política.

### DTOs y validaciones
- Request/response en `application/dto/request|response`, no expongas entidades JPA.
- Usa validaciones de entrada (Bean Validation) y reglas de negocio en los casos de uso (e.g., solo el autor puede editar).

## Convenciones al extender
- Agrega nuevos casos de uso en `application/usecases` (interfaz + impl).
- Define puertos en `domain/service/interfaces` y sus adapters en `infrastructure/persistence/...` u otro submódulo técnico.
- No expongas entidades JPA desde controladores; usa DTOs.
- Mantén configuración de seguridad en `config/security`; lógica de seguridad (JWT, `UserDetailsService`) en `infrastructure/security`.
