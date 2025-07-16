package com.logineko.services;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.logineko.dto.telemetry.TelemetryParsingResultDto;
import com.logineko.entities.CombineTelemetry;
import com.logineko.entities.TractorTelemetry;
import com.logineko.entities.Vehicle;
import com.logineko.entities.VehicleTelemetry;
import com.logineko.parsers.TelemetryCsvParser;
import com.logineko.parsers.TelemetryCsvParserFactory;
import com.logineko.resources.exceptions.TelemetryCsvParsingException;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class TelemetryCsvImportService {

  @Inject TelemetryCsvParserFactory parserFactory;

  public TelemetryParsingResultDto parse(InputStream csvStream, Vehicle vehicle) {
    TelemetryCsvParser recordParser = parserFactory.getParser(vehicle.getVehicleType());
    List<VehicleTelemetry> successfulRecords = new ArrayList<>();
    List<VehicleTelemetry> failedRecords = new ArrayList<>();

    CsvMapper mapper = new CsvMapper();
    mapper.enable(CsvParser.Feature.TRIM_SPACES);

    CsvSchema schema =
        CsvSchema.emptySchema().withHeader().withColumnSeparator(';').withQuoteChar('"');

    try (MappingIterator<Map<String, String>> iterator =
        mapper.readerFor(Map.class).with(schema).readValues(csvStream)) {
      while (iterator.hasNext()) {
        int rowNumber = iterator.getCurrentLocation().getLineNr();
        Map<String, String> row = iterator.next();

        validateVehicleSerialNumber(rowNumber, row, vehicle);

        try {
          VehicleTelemetry telemetry = recordParser.parseRow(row, vehicle);
          validateRequiredFields(telemetry);

          successfulRecords.add(telemetry);
        } catch (TelemetryCsvParsingException e) {
          Log.warnf(
              e,
              "Skipping malformed telemetry row %s for vehicle %s",
              rowNumber,
              vehicle.getSerialNumber());
          failedRecords.add(createFailedRecord(row, vehicle));
        }
      }

      return new TelemetryParsingResultDto(successfulRecords, failedRecords);
    } catch (IOException e) {
      Log.error("Failed to read CSV file", e);
      throw new InternalServerErrorException("Failed to read CSV file", e);
    }
  }

  private void validateVehicleSerialNumber(
      int rowNumber, Map<String, String> row, Vehicle vehicle) {
    String serialNumber = row.get("Serial number");
    if (serialNumber == null || !serialNumber.trim().equalsIgnoreCase(vehicle.getSerialNumber())) {
      throw new BadRequestException(
          String.format(
              "Vehicle serial number mismatch at row %d: found '%s' but expected '%s'.",
              rowNumber, serialNumber, vehicle.getSerialNumber()));
    }
  }

  private void validateRequiredFields(VehicleTelemetry telemetry) {
    List<String> missing = new ArrayList<>();
    if (telemetry.getDateTime() == null) missing.add("Date/Time");
    if (telemetry.getLongitude() == null) missing.add("GPS longitude [°]");
    if (telemetry.getLatitude() == null) missing.add("GPS latitude [°]");

    if (!missing.isEmpty()) {
      throw new BadRequestException("Missing required field(s): " + String.join(", ", missing));
    }
  }

  private VehicleTelemetry createFailedRecord(Map<String, String> row, Vehicle vehicle) {
    VehicleTelemetry failedTelemetry =
        switch (vehicle.getVehicleType()) {
          case TRACTOR -> new TractorTelemetry();
          case COMBINE -> new CombineTelemetry();
        };

    failedTelemetry.setVehicle(vehicle);
    failedTelemetry.setImportFailed(true);
    failedTelemetry.setRawCsvData(row);
    return failedTelemetry;
  }
}
