package com.logineko.parsers;

import com.logineko.annotations.VehicleTypeParser;
import com.logineko.entities.CombineTelemetry;
import com.logineko.entities.Vehicle;
import com.logineko.entities.VehicleTelemetry;
import com.logineko.entities.enums.CropType;
import com.logineko.entities.enums.EnabledStatus;
import com.logineko.entities.enums.VehicleType;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Map;

@ApplicationScoped
@VehicleTypeParser(VehicleType.COMBINE)
public class CombineTelemetryCsvParser extends TelemetryCsvParserBase {

  @Override
  public VehicleTelemetry parseRow(Map<String, String> row, Vehicle vehicle) {
    CombineTelemetry telemetry = new CombineTelemetry();

    parseCommonFields(telemetry, row, vehicle);
    telemetry.setGroundSpeed(parseDouble(row.get("Ground speed [km/h]")));

    // Combine-specific fields
    telemetry.setDrumSpeed(parseInteger(row.get("Drum speed [rpm]")));
    telemetry.setFanSpeed(parseInteger(row.get("Fan speed [rpm]")));
    telemetry.setRotorStrawWalkerSpeed(parseInteger(row.get("Rotor / straw walker speed [rpm]")));
    telemetry.setSeparationLosses(parseDouble(row.get("Separation losses [%]")));
    telemetry.setSieveLosses(parseDouble(row.get("Sieve losses [%]")));
    telemetry.setChopperStatus(
        parseEnumFromString(row.get("Chopper []"), EnabledStatus::fromString));
    telemetry.setDieselTankLevel(parseDouble(row.get("Diesel tank level [%]")));
    telemetry.setNumberOfPartialWidths(parseInteger(row.get("No. of partial widths []")));
    telemetry.setFrontAttachmentStatus(
        parseEnumFromString(row.get("Front attachment On/Off []"), EnabledStatus::fromString));
    telemetry.setMaxNumberOfPartialWidths(parseInteger(row.get("max. no. of partial widths []")));
    telemetry.setFeedRakeSpeed(parseInteger(row.get("Feed rake speed [rpm]")));
    telemetry.setWorkingPosition(
        parseEnumFromString(row.get("Working position [I/O]"), EnabledStatus::fromString));
    telemetry.setGrainTankUnloading(
        parseEnumFromString(row.get("Grain tank unloading [I/O]"), EnabledStatus::fromString));
    telemetry.setMainDriveStatus(
        parseEnumFromString(row.get("Main drive status [I/O]"), EnabledStatus::fromString));
    telemetry.setConcavePosition(parseInteger(row.get("Concave position [mm]")));
    telemetry.setUpperSievePosition(parseInteger(row.get("Upper sieve position [mm]")));
    telemetry.setLowerSievePosition(parseInteger(row.get("Lower sieve position [mm]")));
    telemetry.setGrainTankFillLevel70Reached(
        parseEnumFromString(row.get("Grain tank 70 [I/O]"), EnabledStatus::fromString));
    telemetry.setGrainTankFillLevel100Reached(
        parseEnumFromString(row.get("Grain tank 100 [I/O]"), EnabledStatus::fromString));
    telemetry.setGrainMoistureContent(parseDouble(row.get("Grain moisture content [%]")));
    telemetry.setThroughput(parseDouble(row.get("Throughput [t/h]")));
    telemetry.setRadialSpreaderSpeed(parseInteger(row.get("Radial spreader speed [rpm]")));
    telemetry.setGrainInReturns(parseDouble(row.get("Grain in returns [%]")));
    telemetry.setChannelPosition(parseDouble(row.get("Channel position [%]")));
    telemetry.setYieldMeasurementStatus(
        parseEnumFromString(row.get("Yield measurement [I/O]"), EnabledStatus::fromString));
    telemetry.setReturnsAugerMeasurement(parseDouble(row.get("Returns auger measurement [%]")));
    telemetry.setMoistureMeasurementStatus(
        parseEnumFromString(row.get("Moisture measurement []"), EnabledStatus::fromString));
    telemetry.setTypeOfCrop(parseEnumFromString(row.get("Type of crop []"), CropType::fromString));
    telemetry.setSpecificCropWeight(parseInteger(row.get("Specific crop weight [g/l]")));
    telemetry.setAutoPilotStatus(
        parseEnumFromString(row.get("Auto Pilot status []"), EnabledStatus::fromString));
    telemetry.setCruisePilotStatus(
        parseEnumFromInteger(row.get("Cruise Pilot status []"), EnabledStatus::fromCode));
    telemetry.setRateOfWork(parseDouble(row.get("Rate of work [ha/h]")));
    telemetry.setYieldValue(parseDouble(row.get("Yield [t/ha]")));
    telemetry.setQuantimeterCalibrationFactor(
        parseDouble(row.get("Quantimeter calibration factor []")));
    telemetry.setSeparationSensitivity(parseInteger(row.get("Separation sensitivity [%]")));
    telemetry.setSieveSensitivity(parseInteger(row.get("Sieve sensitivity [%]")));

    return telemetry;
  }
}
