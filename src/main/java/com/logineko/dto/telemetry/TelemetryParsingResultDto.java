package com.logineko.dto.telemetry;

import com.logineko.entities.VehicleTelemetry;
import java.util.List;

public record TelemetryParsingResultDto(
    List<VehicleTelemetry> successful, List<VehicleTelemetry> failed) {}
