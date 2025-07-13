package com.logineko.dto.vehicle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.logineko.entities.enums.VehicleType;

public record CreateVehicleRequestDto(
    @NotBlank() String serialNumber, @NotNull() VehicleType vehicleType) {}
