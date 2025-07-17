package com.logineko.dto.telemetry.csv;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CsvColumnBase implements CsvColumn {
  SERIAL_NUMBER("Serial number"),
  DATE_TIME("Date/Time"),
  GPS_LONGITUDE("GPS longitude [°]"),
  GPS_LATITUDE("GPS latitude [°]"),
  TOTAL_WORKING_HOURS("Total working hours counter [h]"),
  ENGINE_SPEED("Engine speed [rpm]"),
  ENGINE_LOAD("Engine load [%]");

  private final String columnName;
}
