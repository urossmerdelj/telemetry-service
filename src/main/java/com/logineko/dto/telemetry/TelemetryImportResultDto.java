package com.logineko.dto.telemetry;

import com.logineko.dto.vehicle.VehicleDto;

public record TelemetryImportResultDto(
    VehicleDto vehicle, int totalRows, int successfulImports, int failedImports, long durationMs) {}
