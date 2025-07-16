# Vehicle Telemetry Service

This project is a RESTful service for importing and querying vehicle telemetry data, built with Quarkus and Java. It
provides a robust, validated, and efficient API for managing data from different vehicle types, such as tractors and
combines.

## Core Features

* **Vehicle Management**: A standard RESTful API (`/vehicles`) for creating, retrieving, and deleting vehicle entities.
* **Telemetry Data Import**: A `POST /telemetry/import` endpoint for uploading CSV files containing vehicle telemetry
  data for existing vehicles.
* **Dynamic Telemetry Search**: A powerful `POST /telemetry/search` endpoint that allows for filtering data by any field
  on any vehicle type, using a flexible set of operators.

---

## Tech Stack

* **Language**: Java 21
* **Framework**: Quarkus
* **API**: JAX-RS (REST)
* **Persistence**: Hibernate ORM / Panache with the type-safe JPA Criteria API
* **Database**: PostgreSQL with PostGIS
* **Database Migrations**: Flyway
* **Testing**: JUnit 5, RestAssured, Testcontainers

---

## How to Run the Service

### Prerequisites

* Java 21+
* Maven 3.8+
* Docker

For the database, you have two options:

1. **Use the provided Docker Compose setup**: A `docker-compose.example.yml` file is included to easily start a
   PostgreSQL container with the PostGIS extension.
2. **Use your own database**: If you have your own PostgreSQL instance with PostGIS, you can use it by updating the
   database connection settings in `application.properties` or the `.env` file.

### Running in Development Mode

1. Clone the repository.
2. Run the application using the Quarkus Maven plugin:
   ```bash
   ./mvnw quarkus:dev
   ```
3. The service will be available at `http://localhost:8080`.

### Running Tests

Execute the following command to run the full suite of integration tests:

```bash
./mvnw clean install
```

---

## How to Use the API

The typical workflow for using the service is as follows:

### 1. Create a Vehicle

First, you must register a vehicle in the system by sending a `POST` request to `/vehicles`. This is a prerequisite for
importing its data.

**Example Request:**

```http
POST /vehicles
Content-Type: application/json

{
    "serialNumber": "A5304997",
    "vehicleType": "TRACTOR"
}
```

### 2. Import Telemetry Data

Once a vehicle exists, you can import its telemetry data by sending a `POST` request with a `multipart/form-data` body
to `/telemetry/import`. The filename must follow the format `LD_<serialNumber>_...csv`.

* **Form Field**: `file`
* **Content-Type**: `text/csv`

### 3. Search Telemetry Data

Send a `POST` request with a JSON body to `/telemetry/search` to query the imported data.
The fieldName can be any property from the telemetry entity classes, allowing for flexible filtering. You can find a
complete list of available fields in the following entity files

* `VehicleTelemetry`: for fields common to all vehicles.
* `TractorTelemetry`: for fields specific to tractors.
* `CombineTelemetry`: for fields specific to combines.

**Request Body Structure:**

```json
{
  "filters": [
    {
      "field": "fieldName",
      "operation": "EQUALS | GREATER_THAN | LESS_THAN | CONTAINS",
      "value": "filterValue"
    }
  ]
}
```

**Example Query:**

Find all telemetry records for vehicle `C7502627` where the `groundSpeed` is greater than 5.0.

```json
{
  "filters": [
    {
      "field": "serialNumber",
      "operation": "EQUALS",
      "value": "C7502627"
    },
    {
      "field": "groundSpeed",
      "operation": "GREATER_THAN",
      "value": 5.0
    }
  ]
}
```

### 4. Delete a Vehicle

Deleting a vehicle via `DELETE /vehicles/{id}` will also cascade and delete all of its associated telemetry data,
ensuring data integrity.

---

## Manual API Testing

Once the application is running in development mode, you can test the endpoints manually using the following methods:

### Swagger UI

Quarkus automatically generates an OpenAPI specification and a Swagger UI for exploring the API.

1. Start the application with `./mvnw quarkus:dev`.
2. Open your web browser and navigate to `http://localhost:8080/q/swagger-ui`.
3. You can use this interface to view all available endpoints and execute requests directly from your browser.

### Postman

A Postman collection is included in the `/postman` directory of this repository.

1. Open Postman.
2. Click `Import` and select the `Telemetry Service.postman_collection.json` file from the `/postman` directory.
3. The collection includes pre-configured requests for all endpoints, making it easy to test the entire API workflow.

---

## Potential Future Improvements

* **Authentication and RBAC**: Implement security using JWT or OAuth2. Role-Based Access Control could be added so that,
  for example, regular users can create vehicles and search telemetry, while only system administrators or specific
  services can perform data imports.
* **Asynchronous Import**: For very large CSV files, the import process could be handled asynchronously in a background
  thread to prevent long-running HTTP requests. The API would immediately return a `202 Accepted` with a job ID to check
  the status.
* **Enhanced Import Logic**: Improve the CSV import process to be more resilient by skipping invalid rows (and logging
  them for later review) instead of failing the entire import. Add logic to handle or ignore duplicate rows within a
  single file.
* **Global Exception Mapper**: Implement a Global Exception Mapper to catch unhandled exceptions and rather respond with
  `INTERNAL_SERVER_ERROR`, so errors details are not leaked to clients.
* **Unit Tests**: Only integration tests were implemented. The main unit tests that should be implemented are for:
    * CSV parsing
    * Search Filter validation
