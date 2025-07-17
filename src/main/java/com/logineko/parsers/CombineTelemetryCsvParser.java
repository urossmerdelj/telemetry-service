package com.logineko.parsers;

import com.logineko.annotations.VehicleTypeParser;
import com.logineko.dto.telemetry.csv.CombineCsvColumn;
import com.logineko.dto.telemetry.csv.CsvColumn;
import com.logineko.dto.telemetry.csv.CsvColumnBase;
import com.logineko.entities.CombineTelemetry;
import com.logineko.entities.Vehicle;
import com.logineko.entities.VehicleTelemetry;
import com.logineko.entities.enums.CropType;
import com.logineko.entities.enums.EnabledStatus;
import com.logineko.entities.enums.VehicleType;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

@ApplicationScoped
@VehicleTypeParser(VehicleType.COMBINE)
public class CombineTelemetryCsvParser extends TelemetryCsvParserBase {

  @Override
  public VehicleTelemetry parseRow(Map<String, String> row, Vehicle vehicle) {
    CombineTelemetry telemetry = new CombineTelemetry();

    parseCommonFields(telemetry, row, vehicle);
    telemetry.setGroundSpeed(parseDouble(row.get(CombineCsvColumn.GROUND_SPEED.getColumnName())));

    // Combine-specific fields
    telemetry.setDrumSpeed(parseInteger(row.get(CombineCsvColumn.DRUM_SPEED.getColumnName())));
    telemetry.setFanSpeed(parseInteger(row.get(CombineCsvColumn.FAN_SPEED.getColumnName())));
    telemetry.setRotorStrawWalkerSpeed(
        parseInteger(row.get(CombineCsvColumn.ROTOR_STRAW_WALKER_SPEED.getColumnName())));
    telemetry.setSeparationLosses(
        parseDouble(row.get(CombineCsvColumn.SEPARATION_LOSSES.getColumnName())));
    telemetry.setSieveLosses(parseDouble(row.get(CombineCsvColumn.SIEVE_LOSSES.getColumnName())));
    telemetry.setChopperStatus(
        parseEnumFromString(
            row.get(CombineCsvColumn.CHOPPER_STATUS.getColumnName()), EnabledStatus::fromString));
    telemetry.setDieselTankLevel(
        parseDouble(row.get(CombineCsvColumn.DIESEL_TANK_LEVEL.getColumnName())));
    telemetry.setNumberOfPartialWidths(
        parseInteger(row.get(CombineCsvColumn.NUMBER_OF_PARTIAL_WIDTHS.getColumnName())));
    telemetry.setFrontAttachmentStatus(
        parseEnumFromString(
            row.get(CombineCsvColumn.FRONT_ATTACHMENT_STATUS.getColumnName()),
            EnabledStatus::fromString));
    telemetry.setMaxNumberOfPartialWidths(
        parseInteger(row.get(CombineCsvColumn.MAX_NUMBER_OF_PARTIAL_WIDTHS.getColumnName())));
    telemetry.setFeedRakeSpeed(
        parseInteger(row.get(CombineCsvColumn.FEED_RAKE_SPEED.getColumnName())));
    telemetry.setWorkingPosition(
        parseEnumFromString(
            row.get(CombineCsvColumn.WORKING_POSITION.getColumnName()), EnabledStatus::fromString));
    telemetry.setGrainTankUnloading(
        parseEnumFromString(
            row.get(CombineCsvColumn.GRAIN_TANK_UNLOADING.getColumnName()),
            EnabledStatus::fromString));
    telemetry.setMainDriveStatus(
        parseEnumFromString(
            row.get(CombineCsvColumn.MAIN_DRIVE_STATUS.getColumnName()),
            EnabledStatus::fromString));
    telemetry.setConcavePosition(
        parseInteger(row.get(CombineCsvColumn.CONCAVE_POSITION.getColumnName())));
    telemetry.setUpperSievePosition(
        parseInteger(row.get(CombineCsvColumn.UPPER_SIEVE_POSITION.getColumnName())));
    telemetry.setLowerSievePosition(
        parseInteger(row.get(CombineCsvColumn.LOWER_SIEVE_POSITION.getColumnName())));
    telemetry.setGrainTankFillLevel70Reached(
        parseEnumFromString(
            row.get(CombineCsvColumn.GRAIN_TANK_FILL_LEVEL_70_REACHED.getColumnName()),
            EnabledStatus::fromString));
    telemetry.setGrainTankFillLevel100Reached(
        parseEnumFromString(
            row.get(CombineCsvColumn.GRAIN_TANK_FILL_LEVEL_100_REACHED.getColumnName()),
            EnabledStatus::fromString));
    telemetry.setGrainMoistureContent(
        parseDouble(row.get(CombineCsvColumn.GRAIN_MOISTURE_CONTENT.getColumnName())));
    telemetry.setThroughput(parseDouble(row.get(CombineCsvColumn.THROUGHPUT.getColumnName())));
    telemetry.setRadialSpreaderSpeed(
        parseInteger(row.get(CombineCsvColumn.RADIAL_SPREADER_SPEED.getColumnName())));
    telemetry.setGrainInReturns(
        parseDouble(row.get(CombineCsvColumn.GRAIN_IN_RETURNS.getColumnName())));
    telemetry.setChannelPosition(
        parseDouble(row.get(CombineCsvColumn.CHANNEL_POSITION.getColumnName())));
    telemetry.setYieldMeasurementStatus(
        parseEnumFromString(
            row.get(CombineCsvColumn.YIELD_MEASUREMENT_STATUS.getColumnName()),
            EnabledStatus::fromString));
    telemetry.setReturnsAugerMeasurement(
        parseDouble(row.get(CombineCsvColumn.RETURNS_AUGER_MEASUREMENT.getColumnName())));
    telemetry.setMoistureMeasurementStatus(
        parseEnumFromString(
            row.get(CombineCsvColumn.MOISTURE_MEASUREMENT_STATUS.getColumnName()),
            EnabledStatus::fromString));
    telemetry.setTypeOfCrop(
        parseEnumFromString(
            row.get(CombineCsvColumn.TYPE_OF_CROP.getColumnName()), CropType::fromString));
    telemetry.setSpecificCropWeight(
        parseInteger(row.get(CombineCsvColumn.SPECIFIC_CROP_WEIGHT.getColumnName())));
    telemetry.setAutoPilotStatus(
        parseEnumFromString(
            row.get(CombineCsvColumn.AUTO_PILOT_STATUS.getColumnName()),
            EnabledStatus::fromString));
    telemetry.setCruisePilotStatus(
        parseEnumFromInteger(
            row.get(CombineCsvColumn.CRUISE_PILOT_STATUS.getColumnName()),
            EnabledStatus::fromCode));
    telemetry.setRateOfWork(parseDouble(row.get(CombineCsvColumn.RATE_OF_WORK.getColumnName())));
    telemetry.setYieldValue(parseDouble(row.get(CombineCsvColumn.YIELD_VALUE.getColumnName())));
    telemetry.setQuantimeterCalibrationFactor(
        parseDouble(row.get(CombineCsvColumn.QUANTIMETER_CALIBRATION_FACTOR.getColumnName())));
    telemetry.setSeparationSensitivity(
        parseInteger(row.get(CombineCsvColumn.SEPARATION_SENSITIVITY.getColumnName())));
    telemetry.setSieveSensitivity(
        parseInteger(row.get(CombineCsvColumn.SIEVE_SENSITIVITY.getColumnName())));

    return telemetry;
  }

  @Override
  public CsvColumn[] getRequiredColumns() {
    return Stream.concat(
            Arrays.stream(CsvColumnBase.values()), Arrays.stream(CombineCsvColumn.values()))
        .toArray(CsvColumn[]::new);
  }
}
