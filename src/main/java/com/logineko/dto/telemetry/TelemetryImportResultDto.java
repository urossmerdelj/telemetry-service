package com.logineko.dto.telemetry;

public record TelemetryImportResultDto(
    long totalRows, long successfulImports, long failedImports) {}
