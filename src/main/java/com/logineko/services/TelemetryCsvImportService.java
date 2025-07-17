package com.logineko.services;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.logineko.dto.telemetry.TelemetryParsingResultDto;
import com.logineko.dto.telemetry.csv.CsvColumn;
import com.logineko.dto.telemetry.csv.CsvColumnBase;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class TelemetryCsvImportService {

  @Inject TelemetryCsvParserFactory parserFactory;

  public TelemetryParsingResultDto parse(InputStream csvStream, Vehicle vehicle) {
    TelemetryCsvParser rowParser = parserFactory.getParser(vehicle.getVehicleType());
    List<VehicleTelemetry> successfulRows = new ArrayList<>();
    List<VehicleTelemetry> failedRows = new ArrayList<>();

    CsvMapper mapper = new CsvMapper();
    mapper.enable(CsvParser.Feature.TRIM_SPACES);

    CsvSchema schema =
        CsvSchema.emptySchema().withHeader().withColumnSeparator(';').withQuoteChar('"');

    try (MappingIterator<Map<String, String>> iterator =
        mapper.readerFor(Map.class).with(schema).readValues(csvStream)) {

      boolean firstRow = true;
      while (iterator.hasNext()) {
        Map<String, String> row = iterator.next();

        if (firstRow) {
          validateCsvHeaders(row.keySet(), rowParser.getRequiredColumns());
          firstRow = false;
        }

        processRow(
            iterator.getCurrentLocation().getLineNr(),
            row,
            vehicle,
            rowParser,
            successfulRows,
            failedRows);
      }

      return new TelemetryParsingResultDto(successfulRows, failedRows);
    } catch (IOException e) {
      Log.error("Failed to read CSV file", e);
      throw new InternalServerErrorException("Failed to read CSV file", e);
    }
  }

  private void processRow(
      int rowNumber,
      Map<String, String> row,
      Vehicle vehicle,
      TelemetryCsvParser rowParser,
      List<VehicleTelemetry> successfulRecords,
      List<VehicleTelemetry> failedRecords) {
    validateVehicleSerialNumber(rowNumber, row, vehicle);

    try {
      VehicleTelemetry telemetry = rowParser.parseRow(row, vehicle);
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

  private void validateCsvHeaders(Set<String> actualHeaders, CsvColumn[] requiredColumns) {
    Set<String> requiredHeaderNames =
        Arrays.stream(requiredColumns).map(CsvColumn::getColumnName).collect(Collectors.toSet());

    if (!actualHeaders.containsAll(requiredHeaderNames)) {
      Set<String> missingHeaders =
          requiredHeaderNames.stream()
              .filter(h -> !actualHeaders.contains(h))
              .collect(Collectors.toSet());
      throw new BadRequestException(
          String.format(
              "CSV file is missing required columns: %s", String.join(", ", missingHeaders)));
    }
  }

  private void validateVehicleSerialNumber(
      int rowNumber, Map<String, String> row, Vehicle vehicle) {
    String serialNumber = row.get(CsvColumnBase.SERIAL_NUMBER.getColumnName());
    if (serialNumber == null || !serialNumber.trim().equalsIgnoreCase(vehicle.getSerialNumber())) {
      throw new BadRequestException(
          String.format(
              "Vehicle serial number mismatch at row %d: found '%s' but expected '%s'.",
              rowNumber, serialNumber, vehicle.getSerialNumber()));
    }
  }

  private void validateRequiredFields(VehicleTelemetry telemetry) {
    List<String> missing = new ArrayList<>();
    if (telemetry.getDateTime() == null) missing.add(CsvColumnBase.DATE_TIME.getColumnName());
    if (telemetry.getLongitude() == null) missing.add(CsvColumnBase.GPS_LONGITUDE.getColumnName());
    if (telemetry.getLatitude() == null) missing.add(CsvColumnBase.GPS_LATITUDE.getColumnName());

    if (!missing.isEmpty()) {
      throw new BadRequestException(
          String.format("Missing required field(s): %s", String.join(", ", missing)));
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
