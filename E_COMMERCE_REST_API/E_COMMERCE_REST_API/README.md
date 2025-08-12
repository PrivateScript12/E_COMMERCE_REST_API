# E-commerce REST API

A comprehensive Spring Boot REST API for a mini e-commerce application with JWT authentication, user-specific shopping carts, and role-based access control.

## Features

### Core Features
- **Product Management**: CRUD operations for products with pagination, sorting, and filtering
- **Shopping Cart**: User-specific cart management with secure operations
- **Authentication**: JWT-based authentication with role-based access (USER, ADMIN)
- **API Documentation**: OpenAPI/Swagger documentation
- **CORS Support**: Cross-origin resource sharing enabled for frontend integration

### Technical Features
- **Java 21** and **Spring Boot 3.x**
- **Spring Security** with JWT authentication
- **Spring Data JPA** with H2 database (development) / PostgreSQL (production)
- **Bean Validation** for input validation
- **Unit Tests** for services and controllers
- **Docker Support** with docker-compose
- **OpenAPI/Swagger** documentation

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Authenticate and get JWT token

### Products (Public)
- `GET /api/products` - List all products (pagination, sorting, filtering)
- `GET /api/products/{id}` - Get single product
- `GET /api/products/category/{category}` - Get products by category
- `GET /api/products/search?name={name}` - Search products by name
- `GET /api/products/categories` - Get all categories

### Products (Admin Only)
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product
- `POST /api/products/reload` - Reload products from JSON file
- `GET /api/products/count` - Get total product count

### Cart (Authenticated Users)
- `GET /api/cart` - Get cart contents
- `POST /api/cart/add` - Add item to cart
- `PUT /api/cart/item/{id}?quantity={quantity}` - Update cart item quantity
- `DELETE /api/cart/item/{id}` - Remove item from cart
- `DELETE /api/cart/clear` - Clear cart
- `GET /api/cart/total` - Get cart total
- `GET /api/cart/count` - Get cart item count

## Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- Docker and Docker Compose (for containerized deployment)

## Quick Start

### Option 1: Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd E_COMMERCE_REST_API
   ```

2. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Access the application**
   - Application: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - H2 Console: http://localhost:8080/h2-console

### Option 2: Docker Deployment

1. **Build and run with Docker Compose**
   ```bash
   docker-compose up --build
   ```

2. **Access the application**
   - Application: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html

## Sample Data

The application comes with pre-loaded sample data:

### Sample Users
- **Admin**: username: `admin`, password: `admin123`
- **User**: username: `user`, password: `user123`

### Sample Products
The application comes with 20 professional power tools products including:
- Power Tools: Drills, Impact Drivers, Angle Grinders, Hammer Drills, etc.
- Measurement Tools: Laser Receivers, Laser Levels
- Accessories: Dust Extractors

**Product Data Management:**
- Products are loaded from `src/main/resources/products.json` on startup
- Admin can reload products using `POST /api/products/reload` endpoint
- Easy to update products by modifying the JSON file

## API Usage Examples

### 1. Register a new user
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "password": "password123"
  }'
```

### 2. Login and get JWT token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user",
    "password": "user123"
  }'
```

### 3. Get all products with pagination
```bash
curl -X GET "http://localhost:8080/api/products?page=0&size=10&sortBy=name&sortDir=ASC"
```

### 4. Add item to cart (requires JWT token)
```bash
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'
```

### 5. Get cart contents (requires JWT token)
```bash
curl -X GET http://localhost:8080/api/cart \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 6. Reload products from JSON (Admin only)
```bash
curl -X POST http://localhost:8080/api/products/reload \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

### 7. Get product count
```bash
curl -X GET http://localhost:8080/api/products/count
```

## Configuration

### Application Properties

The application uses `application.yml` for configuration:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb  # H2 for development
    username: sa
    password: password
  
jwt:
  secret: your-jwt-secret-key
  expiration: 86400000  # 24 hours

server:
  port: 8080
```

### Environment Variables

For production deployment, you can override these properties:

- `SPRING_DATASOURCE_URL` - Database connection URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password
- `JWT_SECRET` - JWT secret key
- `JWT_EXPIRATION` - JWT expiration time

## Security

### JWT Authentication
- JWT tokens are required for cart operations
- Tokens expire after 24 hours by default
- User-specific cart isolation

### Role-Based Access
- **USER**: Can manage their own cart
- **ADMIN**: Can manage products and access all features

### CORS Configuration
- Configured to allow all origins for development
- Can be customized for production

## Testing

### Run Unit Tests
```bash
./mvnw test
```

### Run Integration Tests
```bash
./mvnw verify
```

## Docker Commands

### Build Docker Image
```bash
docker build -t ecommerce-api .
```

### Run with Docker
```bash
docker run -p 8080:8080 ecommerce-api
```

### Run with Docker Compose
```bash
# Start all services
docker-compose up

# Start in background
docker-compose up -d

# Stop services
docker-compose down

# View logs
docker-compose logs -f app
```

## Database

### Development (H2)
- In-memory database
- Auto-created on startup
- Access via H2 Console: http://localhost:8080/h2-console

### Production (PostgreSQL)
- Configured in docker-compose.yml
- Persistent data storage
- Can be customized for your environment

## API Documentation

### Swagger UI
Access the interactive API documentation at: http://localhost:8080/swagger-ui.html

### OpenAPI Specification
Download the OpenAPI specification at: http://localhost:8080/api-docs

## Project Structure

```
src/
├── main/
│   ├── java/com/ecommerce/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA entities
│   │   ├── repository/     # Data access layer
│   │   ├── security/       # Security configuration
│   │   ├── service/        # Business logic
│   │   └── EcommerceApplication.java
│   └── resources/
│       ├── application.yml
│       └── products.json   # Product data file
└── test/
    └── java/com/ecommerce/
        └── service/        # Unit tests
```

## Product Data Management

### JSON File Structure
Products are stored in `src/main/resources/products.json` with the following structure:
```json
[
  {
    "id": "1",
    "name": "Product Name",
    "price": 129.99,
    "shortDescription": "Brief description",
    "fullDescription": "Detailed description",
    "images": ["image1.jpg", "image2.jpg"],
    "technicalSpecifications": {
      "Spec1": "Value1",
      "Spec2": "Value2"
    },
    "stockQuantity": 50,
    "category": "Category Name"
  }
]
```

### Updating Products
1. **Edit the JSON file**: Modify `src/main/resources/products.json`
2. **Reload via API**: Use `POST /api/products/reload` (Admin only)
3. **Use management script**: Run `./scripts/update-products.sh reload`
4. **Restart application**: Products are automatically loaded on startup

### Management Script
A convenient script is provided for product management:
```bash
# Make script executable (first time only)
chmod +x scripts/update-products.sh

# Reload products from JSON
./scripts/update-products.sh reload

# Check product count
./scripts/update-products.sh count

# Create backup of current products
./scripts/update-products.sh backup

# Validate JSON file
./scripts/update-products.sh validate
```

### Benefits
- **No code changes needed** to update products
- **Version control friendly** - track product changes in Git
- **Easy to manage** - simple JSON format
- **Fallback support** - hardcoded products if JSON fails
- **Admin control** - reload products without restarting

## Documentation

### Setup Instructions

#### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher
- Docker and Docker Compose (for containerized deployment)
- Git

#### Development Environment Setup
1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd E_COMMERCE_REST_API
   ```

2. **Verify Java version**
   ```bash
   java -version  # Should show Java 21
   ```

3. **Verify Maven installation**
   ```bash
   ./mvnw -version
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Verify the application is running**
   - Application: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - H2 Console: http://localhost:8080/h2-console

#### Docker Setup
1. **Build and run with Docker Compose**
   ```bash
   docker-compose up --build
   ```

2. **Verify Docker deployment**
   ```bash
   docker ps  # Should show app and postgres containers
   curl http://localhost:8080/api/products
   ```

#### Testing Setup
1. **Run unit tests**
   ```bash
   ./mvnw test
   ```

2. **Run integration tests**
   ```bash
   ./mvnw verify
   ```

### Architecture Decisions

#### Technology Stack
- **Java 21**: Latest LTS version with modern features
- **Spring Boot 3.2.0**: Latest stable version with Spring Security 6.x
- **Spring Security**: Comprehensive security framework
- **Spring Data JPA**: Simplified data access layer
- **H2 Database**: In-memory database for development
- **PostgreSQL**: Production database for reliability
- **JWT (JJWT 0.12.3)**: Stateless authentication
- **Docker**: Containerization for consistent deployment

#### Design Patterns
1. **Layered Architecture**
   ```
   Controller → Service → Repository → Entity
   ```
   - **Benefits**: Separation of concerns, testability, maintainability
   - **Implementation**: Clear boundaries between layers

2. **Repository Pattern**
   ```java
   @Repository
   public interface ProductRepository extends JpaRepository<Product, Long> {
       // Custom query methods
   }
   ```
   - **Benefits**: Abstract data access, easy testing, consistent interface

3. **DTO Pattern**
   ```java
   public class ProductDto {
       // API-specific data transfer objects
   }
   ```
   - **Benefits**: Decouple API contracts from internal entities

4. **Filter Pattern**
   ```java
   public class JwtAuthenticationFilter extends OncePerRequestFilter {
       // JWT authentication filter
   }
   ```
   - **Benefits**: Centralized authentication, reusable across endpoints

#### Security Architecture
1. **JWT Authentication**
   - Stateless authentication for scalability
   - User-specific cart isolation
   - Role-based access control (USER, ADMIN)

2. **Spring Security Configuration**
   - Modern lambda-based DSL configuration
   - CORS support for frontend integration
   - Content Security Policy for H2 console

3. **Input Validation**
   - Bean Validation annotations
   - Custom validation for business rules
   - Proper error handling with HTTP status codes

#### Database Design
1. **Entity Relationships**
   ```
   User (1) ←→ (N) CartItem (N) ←→ (1) Product
   ```
   - User-specific cart isolation
   - Many-to-many relationship through CartItem

2. **Data Integrity**
   - Foreign key constraints
   - Validation annotations
   - Transactional operations

#### API Design
1. **RESTful Endpoints**
   - Consistent HTTP methods (GET, POST, PUT, DELETE)
   - Proper status codes (200, 201, 400, 401, 404)
   - Pagination and sorting support

2. **Documentation**
   - OpenAPI/Swagger integration
   - Comprehensive API documentation
   - Interactive testing interface

### Self-Assessment

#### Challenges Faced and Solutions

1. **Spring Security 6.x Migration**
   - **Challenge**: Deprecated methods and configuration changes
   - **Solution**: Updated to modern lambda-based DSL configuration
   - **Code**: 
     ```java
     // Old: .frameOptions().disable()
     // New: .contentSecurityPolicy(csp -> csp.policyDirectives("frame-ancestors 'self'"))
     ```

2. **JWT Implementation Updates**
   - **Challenge**: JJWT library API changes in version 0.12.x
   - **Solution**: Updated to modern builder pattern
   - **Code**:
     ```java
     // Old: .setClaims(claims).setSubject(subject)
     // New: .claims(claims).subject(subject)
     ```

3. **User-Specific Cart Isolation**
   - **Challenge**: Ensuring cart items belong to authenticated users
   - **Solution**: JWT user extraction + database-level constraints
   - **Code**:
     ```java
     private Long getCurrentUserId() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String username = authentication.getName();
         return userService.findByUsername(username).orElseThrow().getId();
     }
     ```

4. **Docker Multi-Stage Build**
   - **Challenge**: Optimizing Docker image size
   - **Solution**: Multi-stage build with separate build and runtime stages
   - **Result**: Reduced image size from ~500MB to ~200MB

5. **Product Data Management**
   - **Challenge**: Flexible product management without code changes
   - **Solution**: JSON-based product loading with admin reload capability
   - **Benefits**: Easy product updates, version control friendly

