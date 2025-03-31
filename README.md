# Market Microservice

## Objective
This microservice provides endpoints to manage and query purchases, including customer information, products, and personalized recommendations.

## Description
This is a demo project to analyze the skills of a senior developer. The microservice is built using Java 17 and Spring Boot 3.4.4.

## Technologies Used

### Language
- **Java 17**

### Frameworks and Libraries
- **Spring Boot 3.4.4**
    - `spring-boot-starter-web` - For developing web applications and REST APIs.
    - `spring-boot-starter-actuator` - For application monitoring and management.
    - `spring-boot-starter-logging` - For log management.
    - `spring-boot-starter-test` - For unit and integration testing support.
- **Spring Cloud 2024.0.1**
    - `spring-cloud-starter-openfeign` - For microservices communication via Feign Client.
- **ModelMapper 3.1.1** - For object conversion.
- **Lombok** - To reduce code verbosity by automatically generating getters, setters, and constructors.

### Testing
- **JUnit 5** - For running unit tests.
- **Mockito 5.10.0** - For mocking dependencies in tests.
- **mockito-junit-jupiter 5.10.0** - Integration of Mockito with JUnit 5.

### Build and Dependency Management
- **Maven** - Dependency manager and build automation tool.
- **maven-compiler-plugin** - For project compilation.
- **spring-boot-maven-plugin** - For packaging and running the Spring Boot application.