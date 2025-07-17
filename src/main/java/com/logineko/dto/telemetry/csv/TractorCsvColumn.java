package com.logineko.dto.telemetry.csv;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TractorCsvColumn implements CsvColumn {
  GROUND_SPEED_GEARBOX("Ground speed gearbox [km/h]"),
  FUEL_CONSUMPTION("Fuel consumption [l/h]"),
  GROUND_SPEED_RADAR("Ground speed radar [km/h]"),
  COOLANT_TEMPERATURE("Coolant temperature [°C]"),
  SPEED_FRONT_PTO("Speed front PTO [rpm]"),
  SPEED_REAR_PTO("Speed rear PTO [rpm]"),
  CURRENT_GEAR_SHIFT("current gear shift []"),
  AMBIENT_TEMPERATURE("Ambient temperature [°C]"),
  PARKING_BRAKE_STATUS("Parking brake status []"),
  TRANSVERSE_DIFFERENTIAL_LOCK_STATUS("Transverse differential lock status []"),
  ALL_WHEEL_DRIVE_STATUS("All-wheel drive status []"),
  CREEPER_STATUS("Actual status of creeper []");

  private final String columnName;
}
