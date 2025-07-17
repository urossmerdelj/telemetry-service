package com.logineko.dto.telemetry.csv;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CombineCsvColumn implements CsvColumn {
  GROUND_SPEED("Ground speed [km/h]"),
  DRUM_SPEED("Drum speed [rpm]"),
  FAN_SPEED("Fan speed [rpm]"),
  ROTOR_STRAW_WALKER_SPEED("Rotor / straw walker speed [rpm]"),
  SEPARATION_LOSSES("Separation losses [%]"),
  SIEVE_LOSSES("Sieve losses [%]"),
  CHOPPER_STATUS("Chopper []"),
  DIESEL_TANK_LEVEL("Diesel tank level [%]"),
  NUMBER_OF_PARTIAL_WIDTHS("No. of partial widths []"),
  FRONT_ATTACHMENT_STATUS("Front attachment On/Off []"),
  MAX_NUMBER_OF_PARTIAL_WIDTHS("max. no. of partial widths []"),
  FEED_RAKE_SPEED("Feed rake speed [rpm]"),
  WORKING_POSITION("Working position [I/O]"),
  GRAIN_TANK_UNLOADING("Grain tank unloading [I/O]"),
  MAIN_DRIVE_STATUS("Main drive status [I/O]"),
  CONCAVE_POSITION("Concave position [mm]"),
  UPPER_SIEVE_POSITION("Upper sieve position [mm]"),
  LOWER_SIEVE_POSITION("Lower sieve position [mm]"),
  GRAIN_TANK_FILL_LEVEL_70_REACHED("Grain tank 70 [I/O]"),
  GRAIN_TANK_FILL_LEVEL_100_REACHED("Grain tank 100 [I/O]"),
  GRAIN_MOISTURE_CONTENT("Grain moisture content [%]"),
  THROUGHPUT("Throughput [t/h]"),
  RADIAL_SPREADER_SPEED("Radial spreader speed [rpm]"),
  GRAIN_IN_RETURNS("Grain in returns [%]"),
  CHANNEL_POSITION("Channel position [%]"),
  YIELD_MEASUREMENT_STATUS("Yield measurement [I/O]"),
  RETURNS_AUGER_MEASUREMENT("Returns auger measurement [%]"),
  MOISTURE_MEASUREMENT_STATUS("Moisture measurement []"),
  TYPE_OF_CROP("Type of crop []"),
  SPECIFIC_CROP_WEIGHT("Specific crop weight [g/l]"),
  AUTO_PILOT_STATUS("Auto Pilot status []"),
  CRUISE_PILOT_STATUS("Cruise Pilot status []"),
  RATE_OF_WORK("Rate of work [ha/h]"),
  YIELD_VALUE("Yield [t/ha]"),
  QUANTIMETER_CALIBRATION_FACTOR("Quantimeter calibration factor []"),
  SEPARATION_SENSITIVITY("Separation sensitivity [%]"),
  SIEVE_SENSITIVITY("Sieve sensitivity [%]");

  private final String columnName;
}
