package com.logineko.dto.telemetry;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record TelemetrySearchRequestDto(@Valid @NotEmpty List<TelemetryFilterDto> filters) {}
