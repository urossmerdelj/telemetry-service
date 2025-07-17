package com.logineko.parsers;

import com.logineko.annotations.VehicleTypeParser;
import com.logineko.dto.telemetry.csv.CsvColumn;
import com.logineko.dto.telemetry.csv.CsvColumnBase;
import com.logineko.dto.telemetry.csv.TractorCsvColumn;
import com.logineko.entities.TractorTelemetry;
import com.logineko.entities.Vehicle;
import com.logineko.entities.VehicleTelemetry;
import com.logineko.entities.enums.ActivationStatus;
import com.logineko.entities.enums.VehicleType;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

@ApplicationScoped
@VehicleTypeParser(VehicleType.TRACTOR)
public class TractorTelemetryCsvParser extends TelemetryCsvParserBase {

  @Override
  public VehicleTelemetry parseRow(Map<String, String> row, Vehicle vehicle) {
    TractorTelemetry telemetry = new TractorTelemetry();

    parseCommonFields(telemetry, row, vehicle);
    telemetry.setGroundSpeed(
        parseDouble(row.get(TractorCsvColumn.GROUND_SPEED_GEARBOX.getColumnName())));

    // tractor-specific fields
    telemetry.setFuelConsumption(
        parseDouble(row.get(TractorCsvColumn.FUEL_CONSUMPTION.getColumnName())));
    telemetry.setGroundSpeedRadar(
        parseDouble(row.get(TractorCsvColumn.GROUND_SPEED_RADAR.getColumnName())));
    telemetry.setCoolantTemperature(
        parseDouble(row.get(TractorCsvColumn.COOLANT_TEMPERATURE.getColumnName())));
    telemetry.setSpeedFrontPto(
        parseInteger(row.get(TractorCsvColumn.SPEED_FRONT_PTO.getColumnName())));
    telemetry.setSpeedRearPto(
        parseInteger(row.get(TractorCsvColumn.SPEED_REAR_PTO.getColumnName())));
    telemetry.setCurrentGearShift(
        parseInteger(row.get(TractorCsvColumn.CURRENT_GEAR_SHIFT.getColumnName())));
    telemetry.setAmbientTemperature(
        parseDouble(row.get(TractorCsvColumn.AMBIENT_TEMPERATURE.getColumnName())));
    telemetry.setParkingBrakeStatus(
        parseEnumFromInteger(
            row.get(TractorCsvColumn.PARKING_BRAKE_STATUS.getColumnName()),
            ActivationStatus::fromCode));
    telemetry.setTransverseDifferentialLockStatus(
        parseEnumFromInteger(
            row.get(TractorCsvColumn.TRANSVERSE_DIFFERENTIAL_LOCK_STATUS.getColumnName()),
            ActivationStatus::fromCode));
    telemetry.setAllWheelDriveStatus(
        ActivationStatus.fromString(
            row.get(TractorCsvColumn.ALL_WHEEL_DRIVE_STATUS.getColumnName())));
    telemetry.setCreeperStatus(
        ActivationStatus.fromString(row.get(TractorCsvColumn.CREEPER_STATUS.getColumnName())));

    return telemetry;
  }

  @Override
  public CsvColumn[] getRequiredColumns() {
    return Stream.concat(
            Arrays.stream(CsvColumnBase.values()), Arrays.stream(TractorCsvColumn.values()))
        .toArray(CsvColumn[]::new);
  }
}
