package com.logineko.parsers;

import com.logineko.annotations.VehicleTypeParser;
import com.logineko.entities.TractorTelemetry;
import com.logineko.entities.Vehicle;
import com.logineko.entities.VehicleTelemetry;
import com.logineko.entities.enums.ActivationStatus;
import com.logineko.entities.enums.VehicleType;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Map;

@ApplicationScoped
@VehicleTypeParser(VehicleType.TRACTOR)
public class TractorTelemetryCsvParser extends TelemetryCsvParserBase {

  @Override
  public VehicleTelemetry parseRow(Map<String, String> row, Vehicle vehicle) {
    TractorTelemetry telemetry = new TractorTelemetry();

    parseCommonFields(telemetry, row, vehicle);
    telemetry.setGroundSpeed(parseDouble(row.get("Ground speed gearbox [km/h]")));

    // tractor-specific fields
    telemetry.setFuelConsumption(parseDouble(row.get("Fuel consumption [l/h]")));
    telemetry.setGroundSpeedRadar(parseDouble(row.get("Ground speed radar [km/h]")));
    telemetry.setCoolantTemperature(parseDouble(row.get("Coolant temperature [°C]")));
    telemetry.setSpeedFrontPto(parseInteger(row.get("Speed front PTO [rpm]")));
    telemetry.setSpeedRearPto(parseInteger(row.get("Speed rear PTO [rpm]")));
    telemetry.setCurrentGearShift(parseInteger(row.get("current gear shift []")));
    telemetry.setAmbientTemperature(parseDouble(row.get("Ambient temperature [°C]")));
    telemetry.setParkingBrakeStatus(
        parseEnumFromInteger(row.get("Parking brake status []"), ActivationStatus::fromCode));
    telemetry.setTransverseDifferentialLockStatus(
        parseEnumFromInteger(
            row.get("Transverse differential lock status []"), ActivationStatus::fromCode));
    telemetry.setAllWheelDriveStatus(
        ActivationStatus.fromString(row.get("All-wheel drive status []")));
    telemetry.setCreeperStatus(ActivationStatus.fromString(row.get("Actual status of creeper []")));

    return telemetry;
  }
}
