# Mobile App Web Service (Spring Boot)

This project is a **RESTful web service** built using **Spring Boot** for managing users and their addresses.  
It includes **JWT-based authentication & authorization**, **HATEOAS links**, and **partial updates using JSON Patch**.

---

## **Features**
- **User Management APIs** (CRUD operations).
- **JWT Authentication & Authorization**.
- **Spring Security integration**.
- **HATEOAS support** for hypermedia-driven responses.
- **Partial updates** using `JSON Patch`.
- **MySQL Database Integration** via Spring Data JPA.
- **XML & JSON response formats** (Content Negotiation).
- **ModelMapper** for DTO conversions.

---

## **Tech Stack**
- **Java 8**
- **Spring Boot 2.4.5**
- **Spring Security + JWT**
- **Spring Data JPA**
- **HATEOAS**
- **MySQL**
- **Maven**

---

## **API Endpoints**

### **Authentication**
- `POST /users/login` – Authenticate user and get JWT token.
- `POST /users` – Register a new user.

### **User Management**
- `GET /users` – Get all users.
- `GET /users/limit?page=0&limit=25` – Get paginated users.
- `GET /users/{id}` – Get a user by ID.
- `PUT /users/{id}` – Update a user.
- `PATCH /users/{id}` – Partially update a user (JSON Patch).
- `DELETE /users/{id}` – Delete a user.

### **Addresses**
- `GET /users/{id}/addresses` – Get all addresses for a user.
- `GET /users/{userId}/addresses/{addressId}` – Get a specific address.

---

## **Security**
- Authentication is implemented using **JWT tokens**.
- Login endpoint returns a **JWT token** in the `Authorization` header.
- All endpoints (except signup/login) require `Bearer <token>`.

---

## **Project Structure**

src/main/java/com/appsdeveloperblog/app/ws/mobileappws/

│

├── security/                  # JWT, Security Configurations

│   ├── WebSecurity.java

│   ├── AuthenticationFilter.java

│   └── AuthorizationFilter.java

│

├── ui/controller/             # REST Controllers

│   └── UserApi.java

│

├── ui/model/                  # Request & Response Models

│   ├── request/UserDetailsRequestModel.java

│   └── response/UserRest.java

│

├── service/                   # Service Layer

│

├── shared/                    # DTOs, Utility Classes

│

└── exception/                 # Custom Exceptions


---

## **Setup & Run**

### **Prerequisites**
- **Java 8**
- **Maven**
- **MySQL** running locally.

### **Steps**
1. **Clone repository:**
   ```bash
   git clone https://github.com/your-username/mobile-app-ws.git
   cd mobile-app-ws
   ```

2. **Configure MySQL:**
   Update `application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/mobile_app_db
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Build & Run:**

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Access API:**
   Visit: `http://localhost:8080/users`

---

## **Future Enhancements**

* Add unit and integration tests.
* Implement role-based access control (RBAC).
* Add Swagger/OpenAPI documentation.
