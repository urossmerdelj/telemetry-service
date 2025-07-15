package com.logineko.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import com.logineko.dto.vehicle.CreateVehicleRequestDto;
import com.logineko.entities.Vehicle;
import com.logineko.entities.enums.VehicleType;
import com.logineko.repositories.VehicleRepository;
import com.logineko.repositories.VehicleTelemetryRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class VehicleResourceTest {

  @Inject VehicleRepository vehicleRepository;

  @Inject VehicleTelemetryRepository telemetryRepository;

  @BeforeEach
  @Transactional
  void setUp() {
    telemetryRepository.deleteAll();
    vehicleRepository.deleteAll();
  }

  @AfterEach
  @Transactional
  void tearDown() {
    telemetryRepository.deleteAll();
    vehicleRepository.deleteAll();
  }

  @Transactional
  void setUpVehicle(Vehicle vehicle) {
    vehicleRepository.persist(vehicle);
  }

  @Test
  void testCreateVehicle_success() {
    CreateVehicleRequestDto request = new CreateVehicleRequestDto("SN-123", VehicleType.TRACTOR);

    given()
        .contentType(ContentType.JSON)
        .body(request)
        .when()
        .post("/vehicles")
        .then()
        .statusCode(200)
        .body("id", notNullValue())
        .body("serialNumber", equalTo("SN-123"))
        .body("vehicleType", equalTo("TRACTOR"));
  }

  @Test
  void testCreateVehicle_conflict() {
    Vehicle vehicle = new Vehicle();
    vehicle.setSerialNumber("SN-CONFLICT");
    vehicle.setVehicleType(VehicleType.COMBINE);
    setUpVehicle(vehicle);

    CreateVehicleRequestDto request =
        new CreateVehicleRequestDto("SN-CONFLICT", VehicleType.TRACTOR);

    given()
        .contentType(ContentType.JSON)
        .body(request)
        .when()
        .post("/vehicles")
        .then()
        .statusCode(409);
  }

  @Test
  void testCreateVehicle_badRequest() {
    CreateVehicleRequestDto request = new CreateVehicleRequestDto(null, VehicleType.TRACTOR);

    given()
        .contentType(ContentType.JSON)
        .body(request)
        .when()
        .post("/vehicles")
        .then()
        .statusCode(400);
  }

  @Test
  void testGetVehicleById_success() {
    Vehicle vehicle = new Vehicle();
    vehicle.setSerialNumber("SN-FETCH");
    vehicle.setVehicleType(VehicleType.TRACTOR);
    setUpVehicle(vehicle);

    given()
        .when()
        .get("/vehicles/" + vehicle.getId())
        .then()
        .statusCode(200)
        .body("id", equalTo(vehicle.getId().toString()))
        .body("serialNumber", equalTo("SN-FETCH"));
  }

  @Test
  void testGetVehicleById_notFound() {
    UUID randomId = UUID.randomUUID();
    given().when().get("/vehicles/" + randomId).then().statusCode(404);
  }

  @Test
  void testGetVehicles_success() {
    Vehicle v1 = new Vehicle();
    v1.setSerialNumber("V1");
    v1.setVehicleType(VehicleType.TRACTOR);
    setUpVehicle(v1);

    Vehicle v2 = new Vehicle();
    v2.setSerialNumber("V2");
    v2.setVehicleType(VehicleType.COMBINE);
    setUpVehicle(v2);

    given()
        .when()
        .get("/vehicles")
        .then()
        .statusCode(200)
        .body("meta.total", equalTo(2))
        .body("data", hasSize(2));
  }

  @Test
  void testGetVehicles_withPagination() {
    for (int i = 0; i < 5; i++) {
      Vehicle vehicle = new Vehicle();
      vehicle.setSerialNumber("SN-PAGED-" + i);
      vehicle.setVehicleType(VehicleType.TRACTOR);
      setUpVehicle(vehicle);
    }

    given()
        .queryParam("page", 1)
        .queryParam("size", 2)
        .when()
        .get("/vehicles")
        .then()
        .statusCode(200)
        .body("meta.page", equalTo(1))
        .body("meta.size", equalTo(2))
        .body("meta.total", equalTo(5))
        .body("data", hasSize(2));
  }
}
