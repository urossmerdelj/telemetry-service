package com.logineko.dto.telemetry;

import com.logineko.entities.VehicleTelemetry;
import java.util.List;

public record TelemetryParsingResult(
    List<VehicleTelemetry> successful, List<VehicleTelemetry> failed) {}
