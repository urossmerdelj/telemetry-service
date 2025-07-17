package com.logineko.parsers;

import static com.logineko.utils.DateTimeUtils.EXTERNAL_SOURCE_DATE_FORMATTER;

import com.logineko.dto.telemetry.csv.CsvColumn;
import com.logineko.dto.telemetry.csv.CsvColumnBase;
import com.logineko.entities.Vehicle;
import com.logineko.entities.VehicleTelemetry;
import com.logineko.resources.exceptions.TelemetryCsvParsingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.function.Function;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometries;
import org.geolatte.geom.Point;
import org.geolatte.geom.crs.CoordinateReferenceSystems;

public abstract class TelemetryCsvParserBase implements TelemetryCsvParser {

  @Override
  public abstract VehicleTelemetry parseRow(Map<String, String> row, Vehicle vehicle);

  @Override
  public abstract CsvColumn[] getRequiredColumns();

  protected void parseCommonFields(
      VehicleTelemetry telemetry, Map<String, String> row, Vehicle vehicle) {
    telemetry.setVehicle(vehicle);
    telemetry.setDateTime(parseDateTime(row.get(CsvColumnBase.DATE_TIME.getColumnName())));
    telemetry.setLongitude(parseDouble(row.get(CsvColumnBase.GPS_LONGITUDE.getColumnName())));
    telemetry.setLatitude(parseDouble(row.get(CsvColumnBase.GPS_LATITUDE.getColumnName())));
    telemetry.setLocation(
        parseLocation(
            row.get(CsvColumnBase.GPS_LONGITUDE.getColumnName()),
            row.get(CsvColumnBase.GPS_LATITUDE.getColumnName())));
    telemetry.setTotalWorkingHours(
        parseDouble(row.get(CsvColumnBase.TOTAL_WORKING_HOURS.getColumnName())));
    telemetry.setEngineSpeed(parseInteger(row.get(CsvColumnBase.ENGINE_SPEED.getColumnName())));
    telemetry.setEngineLoad(parseInteger(row.get(CsvColumnBase.ENGINE_LOAD.getColumnName())));
  }

  protected boolean isNotApplicable(String value) {
    return value == null || value.isBlank() || "NA".equalsIgnoreCase(value.trim());
  }

  protected LocalDateTime parseDateTime(String value) {
    if (isNotApplicable(value)) {
      throw new TelemetryCsvParsingException("Date/Time", value);
    }

    try {
      return LocalDateTime.parse(value, EXTERNAL_SOURCE_DATE_FORMATTER);
    } catch (DateTimeParseException e) {
      throw new TelemetryCsvParsingException("Date/Time", value, e);
    }
  }

  protected Point<G2D> parseLocation(String longitudeStr, String latitudeStr) {
    if (isNotApplicable(longitudeStr) || isNotApplicable(latitudeStr)) {
      throw new TelemetryCsvParsingException(
          "GPS Location", String.format("lon=%s, lat=%s", longitudeStr, latitudeStr));
    }

    try {
      double longitude = Double.parseDouble(longitudeStr);
      double latitude = Double.parseDouble(latitudeStr);
      return Geometries.mkPoint(new G2D(longitude, latitude), CoordinateReferenceSystems.WGS84);
    } catch (NumberFormatException e) {
      throw new TelemetryCsvParsingException(
          "GPS Location", String.format("lon=%s, lat=%s", longitudeStr, latitudeStr), e);
    }
  }

  protected Double parseDouble(String value) {
    if (isNotApplicable(value)) {
      return null;
    }

    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException e) {
      throw new TelemetryCsvParsingException("Double", value, e);
    }
  }

  protected Integer parseInteger(String value) {
    if (isNotApplicable(value)) {
      return null;
    }

    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw new TelemetryCsvParsingException("Integer", value, e);
    }
  }

  protected <T extends Enum<T>> T parseEnumFromInteger(
      String value, Function<Integer, T> fromCode) {
    try {
      return fromCode.apply(parseInteger(value));
    } catch (IllegalArgumentException e) {
      throw new TelemetryCsvParsingException("Enum from Integer", value, e);
    }
  }

  protected <T extends Enum<T>> T parseEnumFromString(
      String value, Function<String, T> fromString) {
    try {
      return fromString.apply(value);
    } catch (Exception e) {
      throw new TelemetryCsvParsingException("Enum from String", value, e);
    }
  }
}
