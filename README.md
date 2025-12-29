```markdown
# 📄 Invoice Server Starter

![Java CI](https://github.com/yourusername/invoice-server-starter/workflows/Java%20CI/badge.svg)
![Maven](https://img.shields.io/badge/maven-%23C71A36.svg?style=for-the-badge&logo=apache-maven&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/postgresql-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

## 🚀 Overview

**Invoice Server Starter** is a **Spring Boot** based backend application designed to manage invoices, persons, and user authentication with a focus on professionalism and extensibility. This project provides a solid foundation for building invoice management systems with support for:

- **CRUD operations** for invoices and persons
- **User authentication** with JWT and Google OAuth2
- **Data validation** and error handling
- **RESTful API** with Swagger/OpenAPI documentation
- **Soft delete** functionality for data integrity
- **Comprehensive statistics** and reporting capabilities

Perfect for developers looking to build invoice management systems, accounting applications, or any business solution requiring robust backend services.

---

## ✨ Features

✅ **Comprehensive Invoice Management**
- Create, read, update, and delete invoices
- Filter invoices by buyer, seller, price range, and date
- Generate invoice statistics and reports

✅ **Person Management**
- Manage vendors, customers, and other business entities
- Search and filter by name, identification number, etc.
- View sales and purchases history

✅ **Advanced Authentication**
- Email/password authentication with JWT
- Google OAuth2 integration
- Role-based access control (User/Admin)

✅ **Data Integrity & Security**
- Soft delete functionality for all entities
- Input validation and error handling
- Secure password storage with BCrypt

✅ **Modern Architecture**
- Clean separation of concerns (Controller-Service-Repository)
- MapStruct for efficient DTO mapping
- Spring Data JPA for database operations

✅ **Developer-Friendly**
- Comprehensive API documentation with Swagger UI
- Docker support for easy deployment
- Well-structured code with proper documentation
- Comprehensive test coverage (ready for expansion)

---

## 🛠️ Tech Stack

**Core Technologies:**
- **Java 17** (LTS)
- **Spring Boot 3.3.5**
- **Spring Data JPA**
- **Spring Security**
- **MapStruct** (for DTO mapping)
- **PostgreSQL** (relational database)

**Authentication:**
- **JWT (JSON Web Tokens)**
- **Google OAuth2**

**Build & Packaging:**
- **Maven** (build tool)
- **Lombok** (to reduce boilerplate code)

**Development Tools:**
- **Docker** (containerization)
- **Swagger/OpenAPI** (API documentation)
- **Postman** (API testing)

**Testing:**
- **JUnit 5** (unit tests)
- **Mockito** (mocking framework)

---

## 📦 Installation

### Prerequisites

Before you begin, ensure you have the following installed on your system:

- **[Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)** (LTS version)
- **[Maven 3.8+](https://maven.apache.org/install.html)**
- **[PostgreSQL 12+](https://www.postgresql.org/download/)**
- **[Docker](https://docs.docker.com/get-docker/)** (optional, for containerized deployment)
- **[Git](https://git-scm.com/downloads)** (for version control)

---

### Quick Start

#### 1. Clone the repository

```bash
git clone https://github.com/yourusername/invoice-server-starter.git
cd invoice-server-starter
```

#### 2. Set up environment variables

Create a `.env` file in the project root with the following content:

```properties
# Database configuration
DB_URL=jdbc:postgresql://localhost:5432/invoice_db
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password

# JWT configuration
JWT_SECRET=your_jwt_secret_key_here

# Google OAuth configuration
GOOGLE_CLIENT_ID=your_google_client_id

# Server configuration
PORT=8081
```

> **Note:** For development, you can use placeholder values, but for production, ensure you use secure, unique values.

#### 3. Build the project

```bash
./mvnw clean install
```

#### 4. Run the application

```bash
# Run directly with Maven
./mvnw spring-boot:run

# Or build and run the JAR
java -jar target/invoice-server-starter-1.0-SNAPSHOT.jar
```

#### 5. Access the application

- **API Documentation:** [http://localhost:8081/api-docs](http://localhost:8081/api-docs)
- **Swagger UI:** [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

---

### Docker Setup (Alternative)

#### 1. Build the Docker image

```bash
docker build -t invoice-server .
```

#### 2. Run the container

```bash
docker run -d -p 8081:8080 --env-file .env invoice-server
```

---

## 🎯 Usage

### Basic API Endpoints

#### 1. Authentication

**Register a new user:**
```bash
POST /api/auth/register
{
    "email": "user@example.com",
    "password": "securePassword123!",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "1234567890"
}
```

**Login with email/password:**
```bash
POST /api/auth/login
{
    "email": "user@example.com",
    "password": "securePassword123!"
}
```

**Login with Google OAuth:**
```bash
POST /api/auth/google
{
    "idToken": "your_google_id_token"
}
```

#### 2. Invoice Management

**Get all invoices:**
```bash
GET /api/invoices?buyerName=Customer&sellerName=Vendor&minPrice=100&maxPrice=1000
```

**Create a new invoice:**
```bash
POST /api/invoices
{
    "invoiceNumber": 12345,
    "issued": "2023-10-01",
    "dueDate": "2023-11-01",
    "product": "Software License",
    "price": 999.99,
    "vat": 21,
    "seller": {
        "id": 1,
        "name": "Vendor Inc."
    },
    "buyer": {
        "id": 2,
        "name": "Customer Ltd."
    }
}
```

#### 3. Person Management

**Get all persons:**
```bash
GET /api/persons
```

**Search persons by name:**
```bash
GET /api/persons/search?name=Customer
```

**Get sales by ICO:**
```bash
GET /api/persons/1/sales
```

---

### Example: Java Client Code

Here's a simple Java example using `RestTemplate` to interact with the API:

```java
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;

public class InvoiceClient {

    private static final String BASE_URL = "http://localhost:8081/api";
    private static final String AUTH_TOKEN = "your_jwt_token_here";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // Create headers with authorization
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(AUTH_TOKEN);

        // Example: Create an invoice
        String invoiceUrl = BASE_URL + "/invoices";
        HttpEntity<InvoiceDTO> request = new HttpEntity<>(createInvoiceDTO(), headers);

        ResponseEntity<InvoiceDTO> response = restTemplate.exchange(
                invoiceUrl,
                HttpMethod.POST,
                request,
                InvoiceDTO.class
        );

        System.out.println("Created Invoice: " + response.getBody());
    }

    private static InvoiceDTO createInvoiceDTO() {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setInvoiceNumber(12345);
        dto.setIssued("2023-10-01");
        dto.setDueDate("2023-11-01");
        dto.setProduct("Software License");
        dto.setPrice(999.99);
        dto.setVat(21.0);

        PersonReadDTO seller = new PersonReadDTO();
        seller.setId(1L);
        seller.setName("Vendor Inc.");

        PersonReadDTO buyer = new PersonReadDTO();
        buyer.setId(2L);
        buyer.setName("Customer Ltd.");

        dto.setBuyer(buyer);
        dto.setSeller(seller);
        return dto;
    }
}
```

---

## 📁 Project Structure

```
invoice-server-starter/
├── .env.example                     # Example environment variables
├── .gitignore                       # Git ignore rules
├── Dockerfile                       # Docker configuration
├── pom.xml                          # Maven build configuration
├── src/
│   ├── main/
│   │   ├── java/cz/itnetwork/       # Main application code
│   │   │   ├── config/              # Configuration classes
│   │   │   ├── controller/         # REST controllers
│   │   │   ├── dto/                # Data Transfer Objects
│   │   │   ├── entity/             # JPA entities
│   │   │   ├── repository/         # Spring Data repositories
│   │   │   ├── security/           # Security-related classes
│   │   │   ├── service/            # Business logic services
│   │   │   └── ApplicationMain.java # Main application class
│   │   └── resources/
│   │       ├── application.properties # Main configuration
│   │       └── static/              # Static resources
│   └── test/                        # Test classes
├── TRD.md                           # Technical documentation
└── README.md                        # This file
```

---

## 🔧 Configuration

### Environment Variables

| Variable               | Description                                                                 | Default Value |
|------------------------|-----------------------------------------------------------------------------|---------------|
| `DB_URL`               | PostgreSQL database connection URL                                           | -             |
| `DB_USERNAME`          | Database username                                                           | -             |
| `DB_PASSWORD`          | Database password                                                           | -             |
| `JWT_SECRET`           | Secret key for JWT token signing                                           | -             |
| `GOOGLE_CLIENT_ID`     | Google OAuth client ID                                                      | -             |
| `PORT`                 | Application server port                                                     | 8081          |

### Database Configuration

The application uses **PostgreSQL** for data storage. Ensure your database is configured with the following table structure:

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255),
    enabled BOOLEAN NOT NULL DEFAULT true,
    role VARCHAR(20) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone VARCHAR(20),
    oauth_user BOOLEAN NOT NULL DEFAULT false,
    google_id VARCHAR(255),
    profile_picture VARCHAR(255)
);

CREATE TABLE persons (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    identification_number VARCHAR(20) NOT NULL,
    tax_number VARCHAR(20),
    account_number VARCHAR(50),
    bank_code VARCHAR(10),
    iban VARCHAR(50),
    telephone VARCHAR(20),
    mail VARCHAR(255),
    street VARCHAR(255),
    zip VARCHAR(20),
    city VARCHAR(100),
    country VARCHAR(50) NOT NULL,
    note TEXT,
    hidden BOOLEAN DEFAULT false
);

CREATE TABLE invoices (
    id SERIAL PRIMARY KEY,
    invoice_number INTEGER NOT NULL,
    issued DATE NOT NULL,
    due_date DATE NOT NULL,
    product VARCHAR(255),
    price DECIMAL(10, 2) NOT NULL,
    vat DECIMAL(10, 2),
    note TEXT,
    seller_id INTEGER NOT NULL,
    buyer_id INTEGER NOT NULL,
    hidden BOOLEAN DEFAULT false,
    FOREIGN KEY (seller_id) REFERENCES persons(id),
    FOREIGN KEY (buyer_id) REFERENCES persons(id)
);
```

---

## 🤝 Contributing

We welcome contributions from the community! Here's how you can help:

### How to Contribute

1. **Fork the repository** and create your branch from `main`.
2. **Write tests** for your changes.
3. **Commit your changes** with clear, descriptive messages.
4. **Open a Pull Request** with a detailed description of your changes.

### Development Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/invoice-server-starter.git
   cd invoice-server-starter
   ```

2. Set up your environment:
   ```bash
   # Create .env file with your configuration
   cp .env.example .env
   ```

3. Build and run the project:
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

### Code Style Guidelines

- Follow **Java Code Conventions** (Oracle standard).
- Use **Lombok** annotations consistently.
- Write **clear, concise** comments.
- Ensure **proper error handling** in all methods.
- Follow **Spring Boot best practices** for configuration and dependency injection.

### Pull Request Process

1. Ensure your code passes all tests.
2. Update the documentation if necessary.
3. Submit a clear and descriptive pull request.

---

## 📝 License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

---

## 👥 Authors & Contributors

**Maintainers:**
- [Your Name](https://github.com/yourusername) - Initial work

**Contributors:**
- [Contributor Name](https://github.com/contributor) - [Contribution Description]
- [Another Contributor](https://github.com/another) - [Contribution Description]

---

## 🐛 Issues & Support

### Reporting Issues

If you encounter any problems or have feature requests, please open an issue on the [GitHub Issues](https://github.com/yourusername/invoice-server-starter/issues) page. Include:

- A clear description of the issue.
- Steps to reproduce the issue.
- Any relevant logs or error messages.
- Your environment details (Java version, Spring Boot version, etc.).

### Getting Help

- **Discussions:** [GitHub Discussions](https://github.com/yourusername/invoice-server-starter/discussions)
- **Community:** Join our [Slack channel](https://slack.com) or [forum](https://forum.example.com)
- **Email:** support@example.com

---

## 🗺️ Roadmap

### Planned Features

- **Multi-currency support** for invoices.
- **PDF invoice generation** with dynamic templates.
- **Advanced reporting** with export to Excel/CSV.
- **WebSocket integration** for real-time updates.
- **Audit logging** for all critical operations.

### Known Issues

- [Issue #1](https://github.com/yourusername/invoice-server-starter/issues/1): Docker image size optimization.
- [Issue #2](https://github.com/yourusername/invoice-server-starter/issues/2): Enhance Google OAuth error handling.

### Future Improvements

- **Microservices architecture** for scalability.
- **Integration with payment gateways** (Stripe, PayPal).
- **Advanced search and filtering** capabilities.
- **Mobile app support** with GraphQL API.

---

## 🎉 Get Started Today!

Ready to build your invoice management system? Clone the repository, set up your environment, and start contributing!

```bash
git clone https://github.com/yourusername/invoice-server-starter.git
cd invoice-server-starter
./mvnw spring-boot:run
```

Join our community and help shape the future of this project! 🚀
```

This README.md provides a comprehensive, engaging, and professional overview of your `invoice-server-starter` project. It includes all the necessary sections to attract contributors and users, with clear instructions for installation, usage, and contribution. The formatting is modern and visually appealing, making it easy to navigate and understand.