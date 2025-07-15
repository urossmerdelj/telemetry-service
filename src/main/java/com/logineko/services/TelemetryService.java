package com.logineko.services;

import com.logineko.dto.telemetry.TelemetryImportResultDto;
import com.logineko.dto.telemetry.TelemetryParsingResult;
import com.logineko.entities.Vehicle;
import com.logineko.entities.VehicleTelemetry;
import com.logineko.repositories.VehicleTelemetryRepository;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TelemetryService {
  @Inject VehicleService vehicleService;

  @Inject TelemetryCsvImportService csvImportService;

  @Inject VehicleTelemetryRepository telemetryRepository;

  @Transactional
  public TelemetryImportResultDto importTelemetryCsv(InputStream csvStream, String serialNumber) {
    Log.infof("Importing Telemetry CSV for vehicle %s", serialNumber);
    Vehicle vehicle = vehicleService.getVehicleBySerialNumberOrThrow(serialNumber);

    Log.infof("Parsing Telemetry CSV");
    TelemetryParsingResult parsingResult = csvImportService.parse(csvStream, vehicle);

    long successfulCount = parsingResult.successful().size();
    long failedCount = parsingResult.failed().size();
    long totalCount = successfulCount + failedCount;

    if (totalCount > 0) {
      List<VehicleTelemetry> allTelemetries = new ArrayList<>();
      allTelemetries.addAll(parsingResult.successful());
      allTelemetries.addAll(parsingResult.failed());

      Log.infof("Persisting Telemetry CSV");
      telemetryRepository.persist(allTelemetries);
    }

    return new TelemetryImportResultDto(totalCount, successfulCount, failedCount);
  }
}
