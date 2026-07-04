# Parking Microservice

## Tecnologías

- Kotlin
- Spring Boot 4
- Java 21
- PostgreSQL
- Docker
- Spring Security
- JWT
- AWS Cognito

## Arquitectura

- Controller
- Service
- Repository
- Entity
- DTO
- Mapper
- Exception

## Base de datos

PostgreSQL ejecutándose mediante Docker Compose.

## Seguridad

OAuth2 Resource Server con JWT emitidos por AWS Cognito.

## Endpoints

GET /api/spaces/available

POST /api/tickets/entry

POST /api/tickets/exit