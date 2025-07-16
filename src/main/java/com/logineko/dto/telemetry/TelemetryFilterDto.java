package com.logineko.dto.telemetry;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TelemetryFilterDto(
    @NotBlank() String field,
    @NotNull() TelemetryFilterOperation operation,
    @NotNull() Object value) {}
