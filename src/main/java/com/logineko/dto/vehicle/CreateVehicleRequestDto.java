package com.logineko.dto.vehicle;

import com.logineko.entities.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateVehicleRequestDto(
    @NotBlank() String serialNumber, @NotNull() VehicleType vehicleType) {}
