package com.logineko.services;

import com.logineko.dto.pagination.PaginatedResponse;
import com.logineko.dto.pagination.PaginationQueryParam;
import com.logineko.dto.telemetry.TelemetryFilterDto;
import com.logineko.dto.telemetry.TelemetryImportResultDto;
import com.logineko.dto.telemetry.TelemetryParsingResultDto;
import com.logineko.entities.Vehicle;
import com.logineko.entities.VehicleTelemetry;
import com.logineko.mappers.VehicleMapper;
import com.logineko.repositories.VehicleTelemetryRepository;
import com.logineko.repositories.dto.PaginatedResult;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TelemetryService {
  @Inject VehicleService vehicleService;

  @Inject VehicleMapper vehicleMapper;

  @Inject TelemetryCsvImportService csvImportService;

  @Inject VehicleTelemetryRepository telemetryRepository;

  @Transactional
  public TelemetryImportResultDto importTelemetryCsv(InputStream csvStream, String serialNumber) {
    Instant start = Instant.now();
    Log.infof("Starting telemetry CSV import for vehicle %s", serialNumber);
    Vehicle vehicle = vehicleService.getVehicleBySerialNumberOrThrow(serialNumber);

    TelemetryParsingResultDto parsingResult = csvImportService.parse(csvStream, vehicle);

    int successfulCount = parsingResult.successful().size();
    int failedCount = parsingResult.failed().size();
    int totalCount = successfulCount + failedCount;

    if (totalCount > 0) {
      List<VehicleTelemetry> allTelemetry = new ArrayList<>();
      allTelemetry.addAll(parsingResult.successful());
      allTelemetry.addAll(parsingResult.failed());

      telemetryRepository.persist(allTelemetry);
    }

    Duration duration = Duration.between(start, Instant.now());
    Log.infof("Telemetry CSV import for vehicle %s took %d ms", serialNumber, duration.toMillis());

    return new TelemetryImportResultDto(
        vehicleMapper.toDto(vehicle),
        totalCount,
        successfulCount,
        failedCount,
        duration.toMillis());
  }

  public PaginatedResponse<VehicleTelemetry> searchTelemetry(
      List<TelemetryFilterDto> filters, PaginationQueryParam pagination) {
    PaginatedResult<VehicleTelemetry> result =
        telemetryRepository.searchTelemetry(filters, pagination);

    return new PaginatedResponse<>(result, pagination);
  }
}
