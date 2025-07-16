package com.logineko.dto.vehicle;

import com.logineko.entities.enums.VehicleType;
import java.util.UUID;

public record VehicleDto(UUID id, String serialNumber, VehicleType vehicleType) {}
