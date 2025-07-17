package com.logineko.dto.vehicle;

import jakarta.validation.constraints.NotBlank;

public record CreateVehicleRequestDto(@NotBlank() String serialNumber) {}
