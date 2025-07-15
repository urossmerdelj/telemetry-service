package com.logineko.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.logineko.entities.Vehicle;
import com.logineko.entities.enums.VehicleType;
import com.logineko.repositories.VehicleRepository;
import com.logineko.repositories.VehicleTelemetryRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class TelemetryResourceTest {

  private static final String VEHICLE_SERIAL_NUMBER = "A5304997";
  private static final String CSV_FILENAME = "LD_A5304997_20230331_20230401.csv";

  @Inject VehicleRepository vehicleRepository;

  @Inject VehicleTelemetryRepository telemetryRepository;

  @BeforeEach
  @Transactional
  void setUp() {
    telemetryRepository.deleteAll();
    vehicleRepository.deleteAll();

    Vehicle vehicle = new Vehicle();
    vehicle.setSerialNumber(VEHICLE_SERIAL_NUMBER);
    vehicle.setVehicleType(VehicleType.TRACTOR);
    vehicleRepository.persist(vehicle);
  }

  @AfterEach
  @Transactional
  void tearDown() {
    telemetryRepository.deleteAll();
    vehicleRepository.deleteAll();
  }

  private File getTestFile() throws URISyntaxException {
    URL resource = getClass().getClassLoader().getResource(CSV_FILENAME);
    if (resource == null) {
      throw new IllegalArgumentException("Test file not found!");
    }
    return new File(resource.toURI());
  }

  private byte[] getTestFileBytes() throws URISyntaxException, IOException {
    return Files.readAllBytes(getTestFile().toPath());
  }

  @Test
  @SneakyThrows
  void testImportTelemetryCsv_success() {
    given()
        .multiPart("fileUpload", getTestFile(), "text/csv")
        .when()
        .post("/telemetries/import")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("totalRows", equalTo(10))
        .body("successfulImports", equalTo(10))
        .body("failedImports", equalTo(0));

    assertEquals(10, telemetryRepository.count());
  }

  @Test
  @SneakyThrows
  void testImportTelemetryCsv_invalidFilename() {
    String invalidFilename = "invalid_filename.csv";

    given()
        .multiPart("fileUpload", invalidFilename, getTestFileBytes(), "text/csv")
        .when()
        .post("/telemetries/import")
        .then()
        .statusCode(400);
  }

  @Test
  @SneakyThrows
  void testImportTelemetryCsv_conflictWhenTelemetryExists() {
    given()
        .multiPart("fileUpload", getTestFile(), "text/csv")
        .when()
        .post("/telemetries/import")
        .then()
        .statusCode(200);

    given()
        .multiPart("fileUpload", getTestFile(), "text/csv")
        .when()
        .post("/telemetries/import")
        .then()
        .statusCode(409);
  }

  @Test
  @SneakyThrows
  void testImportTelemetryCsv_vehicleNotFound() {
    String filenameForUpload = "LD_UNKNOWN-VEHICLE_20221114_20221115.csv";

    given()
        .multiPart("fileUpload", filenameForUpload, getTestFileBytes(), "text/csv")
        .when()
        .post("/telemetries/import")
        .then()
        .statusCode(404);
  }
}
