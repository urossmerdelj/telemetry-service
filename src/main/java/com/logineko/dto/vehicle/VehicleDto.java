package com.logineko.dto.vehicle;

import java.util.UUID;

import com.logineko.entities.enums.VehicleType;

public record VehicleDto(UUID id, String serialNumber, VehicleType vehicleType) {}
