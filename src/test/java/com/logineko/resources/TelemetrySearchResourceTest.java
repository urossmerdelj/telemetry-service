package com.logineko.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;

import com.logineko.dto.telemetry.TelemetryFilterDto;
import com.logineko.dto.telemetry.TelemetryFilterOperation;
import com.logineko.dto.telemetry.TelemetrySearchRequestDto;
import com.logineko.entities.Vehicle;
import com.logineko.entities.enums.VehicleType;
import com.logineko.repositories.VehicleRepository;
import com.logineko.repositories.VehicleTelemetryRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class TelemetrySearchResourceTest {

  @Inject VehicleRepository vehicleRepository;
  @Inject VehicleTelemetryRepository telemetryRepository;

  private static final String TRACTOR_SERIAL = "A5304997";
  private static final String COMBINE_SERIAL = "C7502627";
  private static final String TRACTOR_CSV_FILENAME = "LD_A5304997_20230331_20230401.csv";
  private static final String COMBINE_CSV_FILENAME = "LD_C7502627_20221007_20221008.csv";

  private File getTestFile(String filename) throws URISyntaxException {
    URL resource = getClass().getClassLoader().getResource(filename);
    if (resource == null) {
      throw new IllegalArgumentException("Test file not found: " + filename);
    }
    return new File(resource.toURI());
  }

  @Transactional
  void setUpVehicle(Vehicle vehicle) {
    vehicleRepository.persist(vehicle);
  }

  @BeforeEach
  @SneakyThrows
  void setUp() {
    tearDown();

    Vehicle tractor = new Vehicle();
    tractor.setSerialNumber(TRACTOR_SERIAL);
    tractor.setVehicleType(VehicleType.TRACTOR);
    setUpVehicle(tractor);

    Vehicle combine = new Vehicle();
    combine.setSerialNumber(COMBINE_SERIAL);
    combine.setVehicleType(VehicleType.COMBINE);
    setUpVehicle(combine);

    given()
        .multiPart("file", getTestFile(TRACTOR_CSV_FILENAME), "text/csv")
        .post("/telemetry/import");
    given()
        .multiPart("file", getTestFile(COMBINE_CSV_FILENAME), "text/csv")
        .post("/telemetry/import");
  }

  @AfterEach
  @Transactional
  void tearDown() {
    telemetryRepository.deleteAll();
    vehicleRepository.deleteAll();
  }

  @Test
  void testSearch_success_findBySerialNumber() {
    TelemetrySearchRequestDto searchRequest =
        new TelemetrySearchRequestDto(
            List.of(
                new TelemetryFilterDto(
                    "serialNumber", TelemetryFilterOperation.EQUALS, COMBINE_SERIAL)));

    given()
        .contentType(ContentType.JSON)
        .body(searchRequest)
        .when()
        .post("/telemetry/search")
        .then()
        .statusCode(200)
        .body("meta.total", equalTo(9))
        .body("data.size()", equalTo(9));
  }

  @Test
  void testSearch_success_findByDateTimeAndEngineSpeed() {
    TelemetrySearchRequestDto searchRequest =
        new TelemetrySearchRequestDto(
            List.of(
                new TelemetryFilterDto(
                    "dateTime", TelemetryFilterOperation.GREATER_THAN, "Oct 7, 2022, 10:00:00 AM"),
                new TelemetryFilterDto("engineSpeed", TelemetryFilterOperation.GREATER_THAN, 700)));

    given()
        .contentType(ContentType.JSON)
        .body(searchRequest)
        .when()
        .post("/telemetry/search")
        .then()
        .statusCode(200)
        .body("meta.total", greaterThan(0));
  }

  @Test
  void testSearch_success_findByEnumValue() {
    TelemetrySearchRequestDto searchRequest =
        new TelemetrySearchRequestDto(
            List.of(
                new TelemetryFilterDto(
                    "parkingBrakeStatus", TelemetryFilterOperation.EQUALS, "UNKNOWN")));

    given()
        .contentType(ContentType.JSON)
        .body(searchRequest)
        .when()
        .post("/telemetry/search")
        .then()
        .statusCode(200)
        .body("meta.total", equalTo(10));
  }

  @Test
  void testSearch_success_withPagination() {
    TelemetrySearchRequestDto searchRequest =
        new TelemetrySearchRequestDto(
            List.of(
                new TelemetryFilterDto(
                    "serialNumber", TelemetryFilterOperation.EQUALS, TRACTOR_SERIAL)));

    given()
        .contentType(ContentType.JSON)
        .queryParam("page", 1)
        .queryParam("size", 3)
        .body(searchRequest)
        .when()
        .post("/telemetry/search")
        .then()
        .statusCode(200)
        .body("meta.total", equalTo(10))
        .body("meta.page", equalTo(1))
        .body("meta.size", equalTo(3))
        .body("data.size()", equalTo(3));
  }

  @Test
  void testSearch_validationError_invalidField() {
    TelemetrySearchRequestDto searchRequest =
        new TelemetrySearchRequestDto(
            List.of(
                new TelemetryFilterDto("engineColor", TelemetryFilterOperation.EQUALS, "blue")));

    given()
        .contentType(ContentType.JSON)
        .body(searchRequest)
        .when()
        .post("/telemetry/search")
        .then()
        .statusCode(400)
        .body("violations.size()", equalTo(1))
        .body(
            "violations[0].message",
            equalTo("Filter field 'engineColor' is not a valid telemetry field."));
  }

  @Test
  void testSearch_validationError_multipleViolations() {
    TelemetrySearchRequestDto searchRequest =
        new TelemetrySearchRequestDto(
            List.of(
                new TelemetryFilterDto("groundSpeed", TelemetryFilterOperation.CONTAINS, "fast"),
                new TelemetryFilterDto(
                    "parkingBrakeStatus", TelemetryFilterOperation.EQUALS, "INVALID_STATUS")));

    given()
        .contentType(ContentType.JSON)
        .body(searchRequest)
        .when()
        .post("/telemetry/search")
        .then()
        .statusCode(400)
        .body("violations.size()", equalTo(2))
        .body(
            "violations.message",
            hasItems(
                containsString("Operation 'CONTAINS' is not supported for field 'groundSpeed'"),
                containsString(
                    "Invalid value 'INVALID_STATUS' for enum field 'parkingBrakeStatus'")));
  }
}
