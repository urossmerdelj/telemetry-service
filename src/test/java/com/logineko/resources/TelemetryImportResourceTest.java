package com.logineko.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
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
class TelemetryImportResourceTest {

  @Inject VehicleRepository vehicleRepository;
  @Inject VehicleTelemetryRepository telemetryRepository;

  private static final String TRACTOR_SERIAL = "A5304997";
  private static final String TRACTOR_CSV_FILENAME = "LD_A5304997_20230331_20230401.csv";
  private static final String CSV_FILENAME_INVALID_SERIAL = "LD_A5304997_invalid_serial.csv";
  private static final String CSV_FILENAME_MALFORMED = "LD_A5304997_malformed.csv";

  private File getTestFile(String filename) throws URISyntaxException {
    URL resource = getClass().getClassLoader().getResource(filename);
    if (resource == null) {
      throw new IllegalArgumentException("Test file not found: " + filename);
    }
    return new File(resource.toURI());
  }

  private byte[] getTestFileBytes() throws URISyntaxException, IOException {
    return Files.readAllBytes(getTestFile(TRACTOR_CSV_FILENAME).toPath());
  }

  @BeforeEach
  @Transactional
  void setUp() {
    telemetryRepository.deleteAll();
    vehicleRepository.deleteAll();

    Vehicle tractor = new Vehicle();
    tractor.setSerialNumber(TRACTOR_SERIAL);
    tractor.setVehicleType(VehicleType.TRACTOR);
    vehicleRepository.persist(tractor);
  }

  @AfterEach
  @Transactional
  void tearDown() {
    telemetryRepository.deleteAll();
    vehicleRepository.deleteAll();
  }

  @Test
  @SneakyThrows
  void testImportTelemetryCsv_success() {
    given()
        .multiPart("file", getTestFile(TRACTOR_CSV_FILENAME), "text/csv")
        .when()
        .post("/telemetry/import")
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
        .multiPart("file", invalidFilename, getTestFileBytes(), "text/csv")
        .when()
        .post("/telemetry/import")
        .then()
        .statusCode(400);
  }

  @Test
  @SneakyThrows
  void testImportTelemetryCsv_vehicleNotFound() {
    String filenameForUpload = "LD_UNKNOWN-VEHICLE_20221114_20221115.csv";

    given()
        .multiPart("file", filenameForUpload, getTestFileBytes(), "text/csv")
        .when()
        .post("/telemetry/import")
        .then()
        .statusCode(404);
  }

  @Test
  @SneakyThrows
  void testImportTelemetryCsv_serialNumberMismatch() {
    given()
        .multiPart("file", getTestFile(CSV_FILENAME_INVALID_SERIAL), "text/csv")
        .when()
        .post("/telemetry/import")
        .then()
        .statusCode(400);
  }

  @Test
  @SneakyThrows
  void testImportTelemetryCsv_malformedHeader() {
    given()
        .multiPart("file", getTestFile(CSV_FILENAME_MALFORMED), "text/csv")
        .when()
        .post("/telemetry/import")
        .then()
        .statusCode(400)
        .body(containsString("CSV file is missing required columns:"));
  }

  @Test
  @SneakyThrows
  void testImportTelemetryCsv_conflictWhenTelemetryExists() {
    File file = getTestFile(TRACTOR_CSV_FILENAME);

    given()
        .multiPart("file", file, "text/csv")
        .when()
        .post("/telemetry/import")
        .then()
        .statusCode(200);

    given()
        .multiPart("file", file, "text/csv")
        .when()
        .post("/telemetry/import")
        .then()
        .statusCode(409);
  }
}
