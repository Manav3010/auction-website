# Auction Website

## Overview
This is a microservices-based auction platform built with Spring Boot. It allows users to register, log in, and participate in online auctions. The system is designed for scalability, security, and maintainability, using modern cloud-native patterns.

## Architecture
The project consists of several microservices:
- **User Service**: Manages user registration, authentication, and profile data.
- **Auction Service**: Handles auction creation, bidding, and auction lifecycle.
- **Scheduler Service**: Manages scheduled tasks, such as auto-closing auctions and event notifications.
- **API Gateway**: Routes requests to appropriate services and provides a single entry point.
- **Eureka Server**: Service registry for microservices discovery.
- **Config Server**: Centralized configuration management for all services.

## Technologies Used
- Java 17+
- Spring Boot
- Spring Cloud (Eureka, Config Server, Gateway)
- RabbitMQ (for event-driven communication)
- PostgreSQL (database)
- Log4j2 (logging)
- Maven (build tool)
- Docker (recommended for deployment)

## Features
- User registration and login
- Role-based access control
- Auction creation and bidding
- Scheduled auction expiry
- Event-driven architecture (RabbitMQ)
- Centralized configuration
- Service discovery
- API Gateway routing

## Getting Started
### Prerequisites
- Java 17+
- Maven
- PostgreSQL
- RabbitMQ
- Docker (optional)

### Setup Instructions
1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/auction-website.git
   cd auction-website
   ```
2. **Configure the database**
   - Create the database using `dbscript.sql`.
   - Update `postgres.properties` in each service with your DB credentials.
3. **Start RabbitMQ**
   - Ensure RabbitMQ is running locally or update connection settings.
4. **Start Eureka Server**
   - Navigate to `eureka/` and run:
     ```bash
     mvn spring-boot:run
     ```
5. **Start Config Server**
   - Navigate to `configserver/` and run:
     ```bash
     mvn spring-boot:run
     ```
6. **Start Other Services**
   - Start each service (`userservice`, `auctionservice`, `schedularservice`, `gateway`) similarly.
7. **Access the API Gateway**
   - The gateway will be available at `http://localhost:<gateway-port>`.

## Development
- Each microservice has its own `pom.xml` and can be developed independently.
- Configuration is managed centrally via the config server.
- Logging is configured via Log4j2.
- Use the provided `HELP.md` files in each service for service-specific details.

## Contributing
Contributions are welcome! Please open issues or submit pull requests for improvements, bug fixes, or new features.

## Contact
For questions or support, open an issue or contact the maintainer at [your-email@example.com].

---

**Note:** See `TODO list.txt` for planned features and improvements, including Keycloak integration, Kafka events, and more advanced auction features.
