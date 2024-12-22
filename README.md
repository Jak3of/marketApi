# Market API

API REST para un sistema de mercado desarrollado con Spring Boot.

## Características

- Autenticación basada en JWT
- Autorización basada en roles (ADMIN, STAFF, CUSTOMER)
- Gestión de productos y categorías
- Validación de datos
- Documentación de API

## Requisitos

- Java 17 o superior
- Maven 3.6 o superior
- MySQL/PostgreSQL

## Configuración

1. Clona el repositorio
2. Configura la base de datos en `application.properties`
3. Ejecuta `mvn clean install`
4. Inicia la aplicación con `mvn spring-boot:run`

## Configuración JWT

La aplicación usa JWT (JSON Web Tokens) para la autenticación. Las siguientes propiedades pueden ser configuradas:

```properties
# JWT Configuration
jwt.secret=your_secret_key_here    # Clave secreta para firmar los tokens
jwt.expiration=2592000             # Tiempo de expiración en segundos (30 días por defecto)
```

Estas propiedades pueden ser sobrescritas usando variables de entorno:
- JWT_SECRET: para la clave secreta
- JWT_EXPIRATION: para el tiempo de expiración

## Autenticación y Autorización

### Roles Disponibles

- **ROLE_ADMIN**: Acceso total al sistema
- **ROLE_STAFF**: Puede gestionar productos y categorías
- **ROLE_CUSTOMER**: Solo puede ver productos y hacer compras

### Endpoints de Autenticación

#### Registro de Usuario
```http
POST /auth/register
Content-Type: application/json

{
    "name": "Usuario Ejemplo",
    "email": "usuario@ejemplo.com",
    "password": "contraseña123",
    "roles": ["ROLE_CUSTOMER"]  // Opcional, por defecto ROLE_CUSTOMER
}
```

Respuesta:
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5..."
}
```

#### Inicio de Sesión
```http
POST /auth/login
Content-Type: application/json

{
    "email": "usuario@ejemplo.com",
    "password": "contraseña123"
}
```

Respuesta:
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5..."
}
```

### Uso del Token

Incluir el token en el header de las peticiones:
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...
```

### Endpoints Protegidos

#### Acceso Público
- `GET /api/public/**`: Acceso sin autenticación

#### Acceso ADMIN
- `POST /api/admin/**`: Solo administradores

#### Acceso STAFF y ADMIN
- `POST /api/categories/**`: Gestión de categorías
- `POST /api/products/**`: Gestión de productos

#### Acceso Autenticado
- Todos los demás endpoints requieren autenticación

## Usuario Admin por Defecto

Al iniciar la aplicación se crea un usuario administrador:
- Email: admin@market.com
- Contraseña: admin123

## Seguridad

- Contraseñas hasheadas con BCrypt
- Tokens JWT con expiración
- CSRF deshabilitado (usando tokens)
- Sesiones stateless

## Estructura del Proyecto

```
src/main/java/com/api/market/
├── bean/
│   ├── AuthCredentials.java
│   ├── RegisterRequest.java
│   └── TokenResponse.java
├── config/
│   └── DataInitializer.java
├── model/
│   ├── Role.java
│   └── User.java
├── repository/
│   ├── RoleRepository.java
│   └── UserRepository.java
├── resource/
│   └── AuthController.java
├── security/
│   ├── JWTAuthenticationFilter.java
│   ├── JWTAuthorizationFilter.java
│   ├── TokenUtils.java
│   ├── UserDetailsImpl.java
│   ├── UserDetailsServiceImpl.java
│   └── WebSecurityConfig.java
└── service/
    └── UserService.java
```

## Contribuir

1. Haz fork del proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request
