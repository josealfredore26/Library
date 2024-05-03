# Library Project

This is a Spring Boot project for managing a library system.

## Description

This project provides a RESTful API for performing CRUD operations related to a library system. It allows managing users, books, and book loans.

## Technologies Used

- Java
- Spring Boot
- Maven
- H2 Database
- Spring Data JPA
- Spring Web

## Getting Started

To run this project locally, follow these steps:

1. Clone the repository:

   ```bash
   git clone https://github.com/josealfredore26/Library.git

2. Navigate to the project directory:
    ```bash
   cd Library

3. Build the project using Maven:
    ```bash
   mvn clean package

4. Run the application:
    ```bash
   java -jar target/Library-0.0.1-SNAPSHOT.jar

5. Access the API at http://localhost:8080


## API Documentation

The API documentation is available at:

- Swagger UI: [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)
- OpenAPI JSON: [http://localhost:8080/api-docs](http://localhost:8080/v3/api-docs)

## Configuration

The application is configured to use the following settings:

- Java version: 17
- Spring Boot version: 3.2.5
- Database: H2 (in-memory)
