# FX Deals Data Warehouse

Data warehouse for Bloomberg to analyze FX deals.

## Quick Start

```bash
# Build
mvn clean package -DskipTests

# Run with Docker
docker-compose up

# Test API
curl http://localhost:8081/api/deals
```

## Requirements

- Java 17
- Maven 3.6+
- Docker & Docker Compose

##  Technologies

- Spring Boot 3.5.8
- MySQL 8.0
- MapStruct
- Lombok
- Docker

## API Endpoints

### POST /api/deals
Create a new FX deal

```json
{
  "dealUniqueId": "DEAL001",
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "dealTimestamp": "2024-01-15T10:30:00",
  "dealAmount": 1000.50
}
```

### GET /api/deals
Get all FX deals

## 🧪 Run Tests

```bash
mvn test
```
## 👤 Author

Developed by Souhail Imarraine
