# **Technical Documentation: Invoice Server Starter**

---

## **1. Architecture Overview**

### **1.1. System Architecture**
The `invoice-server-starter` is a **Spring Boot** application designed for managing invoices, persons, and user authentication. The architecture follows a **layered (MVC) pattern** with the following key components:

```
┌───────────────────────────────────────────────────────────────────────────────┐
│                                Client (Frontend)                            │
└───────────────────────────────────────────────────────────────────────────────┘
                                      ↓
┌───────────────────────────────────────────────────────────────────────────────┐
│                                API Layer (Controllers)                        │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐               │
│  │ InvoiceController│  │ PersonController│  │ AuthController │               │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘               │
└───────────────────────────────────────────────────────────────────────────────┘
                                      ↓
┌───────────────────────────────────────────────────────────────────────────────┐
│                                Service Layer                                │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐               │
│  │ InvoiceService │  │ PersonService   │  │ UserService    │               │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘               │
└───────────────────────────────────────────────────────────────────────────────┘
                                      ↓
┌───────────────────────────────────────────────────────────────────────────────┐
│                                Repository Layer                             │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐               │
│  │ InvoiceRepo    │  │ PersonRepo      │  │ UserRepo       │               │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘               │
└───────────────────────────────────────────────────────────────────────────────┘
                                      ↓
┌───────────────────────────────────────────────────────────────────────────────┐
│                                Database (PostgreSQL)                        │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐               │
│  │ invoices       │  │ persons        │  │ users          │               │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘               │
└───────────────────────────────────────────────────────────────────────────────┘
```

---

### **1.2. Key Technologies**
| **Component**       | **Technology**                     | **Purpose**                                                                 |
|---------------------|-----------------------------------|-----------------------------------------------------------------------------|
| **Framework**       | Spring Boot 3.3.5                  | Backend framework for rapid application development.                        |
| **Persistence**    | Spring Data JPA + Hibernate        | ORM for database interactions.                                                |
| **Validation**      | Spring Validation                 | Input validation for DTOs and entities.                                     |
| **Mapping**         | MapStruct                         | Efficient conversion between entities and DTOs.                              |
| **Security**        | Spring Security + JWT             | Authentication and authorization.                                            |
| **API Docs**        | SpringDoc OpenAPI                 | Automatic API documentation generation.                                       |
| **Build Tool**     | Maven                             | Dependency management and build automation.                                  |
| **Container**      | Docker                            | Containerization for deployment.                                             |
| **Logging**         | SLF4J + Logback                   | Structured logging for debugging and monitoring.                             |

---

## **2. Setup & Installation**

### **2.1. Prerequisites**
- **Java 17** (or higher)
- **Maven 3.8+** (or Gradle)
- **PostgreSQL** (or any other supported database)
- **Docker** (optional, for containerized deployment)

---

### **2.2. Cloning the Repository**
```bash
git clone <repository-url>
cd invoice-server-starter
```

---

### **2.3. Environment Configuration**
The application uses an **`.env` file** for sensitive configurations (e.g., database credentials, JWT secret). Example:

```env
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/invoice_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
PORT=8081

# JWT Configuration
JWT_SECRET=your_jwt_secret_key_here

# Google OAuth Configuration
GOOGLE_OAUTH_CLIENT_ID=your_google_client_id
```

> **Note:** The `.env` file is **ignored by Git** (as per `.gitignore`) and should not be committed to version control.

---

### **2.4. Building the Project**
#### **Using Maven:**
```bash
./mvnw clean install
```
This will:
1. Compile the code.
2. Run tests (if any).
3. Generate the executable JAR file in `target/`.

#### **Using Gradle:**
```bash
./gradlew build
```

---

### **2.5. Running the Application**
#### **Directly:**
```bash
java -jar target/invoice-server-starter-1.0-SNAPSHOT.jar
```
#### **Using Docker:**
```bash
docker build -t invoice-server .
docker run -p 8081:8080 --env-file .env invoice-server
```

---

### **2.6. Database Setup**
The application uses **PostgreSQL** by default. Ensure the database is created and configured in the `.env` file.

#### **Schema Initialization**
The application uses `spring.jpa.hibernate.ddl-auto=update`, which will automatically update the schema based on the entities. However, for production, consider using `validate` or `none` and manually manage migrations.

---

## **3. API Documentation**

### **3.1. Base URL**
```
http://localhost:8081/api
```

### **3.2. Endpoints**

#### **Authentication**
| **Endpoint**               | **Method** | **Description**                                                                 | **Request Body**                     | **Response**                     |
|----------------------------|------------|---------------------------------------------------------------------------------|--------------------------------------|----------------------------------|
| `/auth/register`           | POST       | Register a new user with email and password.                                   | `UserAuthDTO`                        | `ResponseEntity<User>`            |
| `/auth/login`              | POST       | Authenticate a user and return a JWT token.                                    | `LoginDTO`                           | `LoginResponseDTO`               |
| `/auth/google/login`       | POST       | Authenticate using Google OAuth.                                               | `GoogleLoginDTO`                     | `LoginResponseDTO`               |

#### **Invoices**
| **Endpoint**               | **Method** | **Description**                                                                 | **Query Parameters**                | **Response**                     |
|----------------------------|------------|---------------------------------------------------------------------------------|-------------------------------------|----------------------------------|
| `/invoices`               | GET        | Get a list of invoices (with optional filters).                                | `buyerName`, `sellerName`, `minPrice`, `maxPrice`, `limit` | `List<InvoiceDTO>` |
| `/invoices/{id}`          | GET        | Get a specific invoice by ID.                                                  | -                                   | `InvoiceDTO`                     |
| `/invoices`               | POST       | Create a new invoice.                                                          | `InvoiceDTO`                        | `InvoiceDTO`                     |
| `/invoices/{id}`          | PUT        | Update an existing invoice.                                                     | `InvoiceDTO`                        | `InvoiceDTO`                     |
| `/invoices/{id}`          | DELETE     | Delete an invoice (soft delete).                                               | -                                   | `ResponseEntity<Void>`            |
| `/invoices/statistics`    | GET        | Get invoice statistics (sums, counts).                                         | -                                   | `InvoiceStatisticsDTO`           |

#### **Persons**
| **Endpoint**               | **Method** | **Description**                                                                 | **Query Parameters**                | **Response**                     |
|----------------------------|------------|---------------------------------------------------------------------------------|-------------------------------------|----------------------------------|
| `/persons`                | GET        | Get a list of all persons.                                                      | -                                   | `List<PersonDTO>`                |
| `/persons/{id}`           | GET        | Get a specific person by ID.                                                    | -                                   | `PersonDTO`                      |
| `/persons`                | POST       | Create a new person.                                                            | `PersonDTO`                         | `PersonDTO`                      |
| `/persons/{id}`           | PUT        | Update an existing person.                                                      | `PersonDTO`                         | `PersonDTO`                      |
| `/persons/{id}`           | DELETE     | Delete a person (soft delete).                                                   | -                                   | `ResponseEntity<Void>`            |
| `/persons/{id}/sales`     | GET        | Get sales invoices for a person (by ICO).                                       | -                                   | `List<InvoiceReadDTO>`            |
| `/persons/{id}/purchases` | GET        | Get purchase invoices for a person (by ICO).                                    | -                                   | `List<InvoiceReadDTO>`            |

---

### **3.3. Request/Response Examples**

#### **Register a User**
**Request:**
```json
{
  "email": "user@example.com",
  "password": "securePassword123",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "1234567890"
}
```
**Response:**
```json
{
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "1234567890",
  "oauthUser": false,
  "profilePicture": null
}
```

#### **Create an Invoice**
**Request:**
```json
{
  "invoiceNumber": 1001,
  "issued": "2023-10-01",
  "dueDate": "2023-11-01",
  "product": "Software License",
  "price": 999.99,
  "vat": 210.00,
  "note": "Annual subscription",
  "seller": {
    "id": 1,
    "name": "IT Network"
  },
  "buyer": {
    "id": 2,
    "name": "Acme Corp"
  },
  "hidden": false
}
```
**Response:**
```json
{
  "id": 1,
  "invoiceNumber": 1001,
  "issued": "2023-10-01",
  "dueDate": "2023-11-01",
  "product": "Software License",
  "price": 999.99,
  "vat": 210.00,
  "note": "Annual subscription",
  "seller": {
    "id": 1,
    "name": "IT Network"
  },
  "buyer": {
    "id": 2,
    "name": "Acme Corp"
  },
  "hidden": false
}
```

---

### **3.4. Error Handling**
The application uses **Spring's `@ControllerAdvice`** to handle common exceptions globally. Example:

| **Exception**               | **HTTP Status** | **Description**                                                                 |
|-----------------------------|-----------------|---------------------------------------------------------------------------------|
| `EntityNotFoundException`   | 404             | Resource not found (e.g., invoice or person).                                    |
| `IllegalArgumentException`  | 400             | Invalid input (e.g., negative price).                                           |
| `NotFoundException`         | 404             | Resource not found (e.g., Google OAuth token invalid).                           |

---

## **4. Database Schema**

### **4.1. Entities and Relationships**

#### **`users` Table**
| **Column**       | **Type**         | **Description**                                                                 |
|-------------------|------------------|---------------------------------------------------------------------------------|
| `id`             | `INTEGER`        | Primary key.                                                                   |
| `email`          | `VARCHAR(255)`   | Unique email address.                                                            |
| `password`       | `VARCHAR(255)`   | Hash of the user's password.                                                     |
| `enabled`        | `BOOLEAN`        | Whether the account is active.                                                  |
| `role`           | `VARCHAR(20)`    | User role (`ROLE_USER` or `ROLE_ADMIN`).                                         |
| `firstName`      | `VARCHAR(100)`   | User's first name.                                                               |
| `lastName`       | `VARCHAR(100)`   | User's last name.                                                                |
| `phone`          | `VARCHAR(20)`    | User's phone number.                                                              |
| `oauthUser`      | `BOOLEAN`        | Whether the user was registered via Google OAuth.                                |
| `googleId`       | `VARCHAR(255)`   | Google OAuth user ID.                                                           |
| `profilePicture` | `VARCHAR(255)`   | URL to the user's profile picture.                                               |

---

#### **`persons` Table**
| **Column**         | **Type**         | **Description**                                                                 |
|--------------------|------------------|---------------------------------------------------------------------------------|
| `id`               | `INTEGER`        | Primary key.                                                                   |
| `name`             | `VARCHAR(255)`   | Name of the person (company or individual).                                    |
| `identificationNumber` | `VARCHAR(20)` | IČO (Identification Number) for legal entities.                                |
| `taxNumber`        | `VARCHAR(20)`    | DIČ (Tax Number).                                                                |
| `accountNumber`    | `VARCHAR(50)`    | Bank account number.                                                           |
| `bankCode`         | `VARCHAR(10)`    | Bank code.                                                                     |
| `iban`             | `VARCHAR(50)`    | IBAN (International Bank Account Number).                                       |
| `telephone`        | `VARCHAR(20)`    | Contact phone number.                                                            |
| `mail`             | `VARCHAR(255)`   | Email address.                                                                   |
| `street`           | `VARCHAR(255)`   | Street address.                                                                  |
| `zip`              | `VARCHAR(10)`    | ZIP code.                                                                       |
| `city`             | `VARCHAR(100)`   | City.                                                                          |
| `country`          | `VARCHAR(20)`    | Country (e.g., `CZECHIA`, `SLOVAKIA`).                                           |
| `note`             | `TEXT`           | Additional notes.                                                                |
| `hidden`           | `BOOLEAN`        | Soft delete flag.                                                                |

---

#### **`invoices` Table**
| **Column**         | **Type**         | **Description**                                                                 |
|--------------------|------------------|---------------------------------------------------------------------------------|
| `id`               | `INTEGER`        | Primary key.                                                                   |
| `invoiceNumber`    | `INTEGER`        | Invoice number.                                                                 |
| `issued`           | `DATE`           | Date when the invoice was issued.                                               |
| `dueDate`          | `DATE`           | Due date for payment.                                                           |
| `product`          | `VARCHAR(255)`   | Description of the product/service.                                             |
| `price`            | `DECIMAL(10,2)`  | Price of the product/service (excl. VAT).                                      |
| `vat`              | `DECIMAL(10,2)`  | VAT amount.                                                                     |
| `note`             | `TEXT`           | Additional notes.                                                                |
| `seller_id`        | `INTEGER`        | Foreign key to `persons` (seller).                                             |
| `buyer_id`         | `INTEGER`        | Foreign key to `persons` (buyer).                                               |
| `hidden`           | `BOOLEAN`        | Soft delete flag.                                                                |

---

### **4.2. Relationships**
- **One-to-Many**: A `Person` can have many `Invoice`s (as seller or buyer).
- **Many-to-Many**: Implicitly handled via `seller_id` and `buyer_id` foreign keys.

---

## **5. Configuration**

### **5.1. Application Properties (`application.properties`)**
The application uses `application.properties` for non-sensitive configurations. Key settings:

```properties
# Server Configuration
server.port=${PORT:8081}

# Database Configuration
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.hikari.maximum-pool-size=5

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.open-in-view=false

# Security Configuration
jwt.secret=${JWT_SECRET}
google.oauth.client-id=${GOOGLE_OAUTH_CLIENT_ID}

# API Documentation
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

---

### **5.2. Environment Variables**
| **Variable**               | **Description**                                                                 |
|----------------------------|---------------------------------------------------------------------------------|
| `PORT`                    | Port for the application (default: `8081`).                                      |
| `DB_URL`                  | Database connection URL.                                                          |
| `DB_USERNAME`              | Database username.                                                                |
| `DB_PASSWORD`              | Database password.                                                                |
| `JWT_SECRET`               | Secret key for JWT token generation.                                              |
| `GOOGLE_OAUTH_CLIENT_ID`   | Client ID for Google OAuth authentication.                                       |

---

## **6. Development Guidelines**

### **6.1. Coding Standards**
1. **Naming Conventions**:
    - Use **camelCase** for variables, methods, and classes.
    - Use **UPPER_SNAKE_CASE** for constants and enums.
    - Use **PascalCase** for DTOs and entities.

2. **Validation**:
    - Use `@NotNull`, `@Email`, `@Positive`, etc., from `jakarta.validation`.
    - Validate inputs in **DTOs** and **entities**.

3. **Logging**:
    - Use `SLF4J` for logging.
    - Log at appropriate levels (`INFO`, `DEBUG`, `ERROR`).

4. **Error Handling**:
    - Use `@ControllerAdvice` for global exception handling.
    - Return meaningful error messages.

5. **Testing**:
    - Write unit tests for services and controllers.
    - Use `@SpringBootTest` for integration tests.

---

### **6.2. Best Practices**
- **Dependency Injection**: Use `@Autowired` or constructor injection.
- **Immutable DTOs**: Use `@Getter` and `@Setter` judiciously; prefer immutable objects where possible.
- **Soft Deletes**: Use the `hidden` flag for logical deletion.
- **Security**: Always validate inputs and use HTTPS in production.
- **Documentation**: Use `@ApiModel`, `@ApiResponse`, and `@ApiParam` for Swagger/OpenAPI docs.

---

## **7. Deployment Instructions**

### **7.1. Building the Executable JAR**
```bash
./mvnw clean package
```
This generates a JAR file in `target/`.

---

### **7.2. Running in Production**
#### **Option 1: Direct Execution**
```bash
java -jar target/invoice-server-starter-1.0-SNAPSHOT.jar --spring.profiles.active=prod
```

#### **Option 2: Docker Deployment**
1. Build the Docker image:
   ```bash
   docker build -t invoice-server .
   ```
2. Run the container:
   ```bash
   docker run -p 8081:8080 --env-file .env invoice-server
   ```

---

### **7.3. Scaling**
- Use **Kubernetes** or **Docker Swarm** for orchestration.
- Configure **load balancing** for high availability.
- Use **database replication** for read scalability.

---

## **8. Monitoring and Logging**

### **8.1. Logging**
- The application uses **SLF4J + Logback** for logging.
- Logs are written to `logs/` directory (as per `.gitignore`).
- Request timing is logged via `RequestTimingInterceptor`.

### **8.2. Metrics**
- Integrate with **Prometheus** and **Grafana** for monitoring.
- Use **Spring Boot Actuator** for health checks and metrics.

---

## **9. Troubleshooting**

### **9.1. Common Issues**
| **Issue**                          | **Solution**                                                                 |
|------------------------------------|-----------------------------------------------------------------------------|
| Database connection fails          | Verify `.env` file and database credentials.                                |
| JWT token validation fails         | Ensure `JWT_SECRET` is correctly set in `.env`.                              |
| 404 Not Found errors               | Check if the endpoint exists and is correctly mapped.                        |
| Soft delete not working            | Verify `hidden` flag is set correctly in the database.                      |
| Slow API responses                 | Optimize queries, add indexes, or increase database connection pool size.    |

---

### **9.2. Debugging Tips**
- Enable **SQL logging** in `application.properties`:
  ```properties
  spring.jpa.show-sql=true
  spring.jpa.properties.hibernate.format_sql=true
  ```
- Use **PostgreSQL client** (e.g., pgAdmin) to inspect database tables.
- Check logs in `logs/` directory for errors.

---

## **10. License**
This project is part of a **premium educational series** by [IT Network](http://www.itnetwork.cz/licence). The source code is **not open-source** and is intended for **personal use only**. Redistribution or commercial use is prohibited without explicit permission.

---

## **11. Appendix**

### **11.1. Dependencies (`pom.xml`)**
```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- Database -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Mapping -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>1.5.3.Final</version>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>

    <!-- API Docs -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.0.2</version>
    </dependency>

    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

### **11.2. Key Classes**
| **Class**                          | **Purpose**                                                                 |
|-------------------------------------|-----------------------------------------------------------------------------|
| `ApplicationMain`                   | Main entry point for the Spring Boot application.                           |
| `WebConfig`                        | Configures HTTP interceptors (e.g., request timing).                        |
| `InvoiceController`                 | Handles REST endpoints for invoices.                                        |
| `PersonController`                  | Handles REST endpoints for persons.                                         |
| `AuthController`                   | Handles authentication (login, register, Google OAuth).                     |
| `InvoiceServiceImpl`                | Business logic for invoices (CRUD, filtering).                              |
| `PersonServiceImpl`                 | Business logic for persons (CRUD, statistics).                              |
| `UserServiceImpl`                   | Business logic for users (registration, authentication).                   |
| `JwtUtil`                          | Generates and validates JWT tokens.                                          |
| `GoogleTokenVerifier`               | Validates Google OAuth tokens.                                               |
| `RequestTimingInterceptor`          | Logs request processing time.                                                |
| `InvoiceRepository`                 | JPA repository for invoices (CRUD + custom queries).                         |
| `PersonRepository`                  | JPA repository for persons (CRUD + custom queries).                          |
| `UserRepository`                    | JPA repository for users.                                                    |

---

### **11.3. Example Request Flow**
1. **Client** sends a `POST /api/invoices` request with an `InvoiceDTO`.
2. **InvoiceController** validates the input and calls `InvoiceService.create()`.
3. **InvoiceServiceImpl** uses `InvoiceMapper` to convert `InvoiceDTO` to `Invoice` entity.
4. **InvoiceRepository** persists the entity to the database.
5. **InvoiceServiceImpl** returns the created `InvoiceDTO` to the controller.
6. **InvoiceController** returns the response to the client.

---

This documentation provides a **comprehensive overview** of the `invoice-server-starter` project, covering setup, architecture, API design, and deployment. For further details, refer to the source code and comments within the repository.