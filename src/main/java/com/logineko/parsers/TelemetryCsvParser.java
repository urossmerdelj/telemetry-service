package com.logineko.parsers;

import com.logineko.dto.telemetry.csv.CsvColumn;
import com.logineko.entities.Vehicle;
import com.logineko.entities.VehicleTelemetry;
import java.util.Map;

public interface TelemetryCsvParser {
  VehicleTelemetry parseRow(Map<String, String> row, Vehicle vehicle);

  CsvColumn[] getRequiredColumns();
}
