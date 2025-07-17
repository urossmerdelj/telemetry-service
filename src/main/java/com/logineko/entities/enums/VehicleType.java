package com.logineko.entities.enums;

import jakarta.ws.rs.BadRequestException;
import java.util.Arrays;

public enum VehicleType {
  TRACTOR("A"),
  COMBINE("C");

  private final String serialNumberPrefix;

  VehicleType(String serialNumberPrefix) {
    this.serialNumberPrefix = serialNumberPrefix;
  }

  public static VehicleType fromSerialNumber(String serialNumber) {
    return Arrays.stream(values())
        .filter(type -> serialNumber.startsWith(type.serialNumberPrefix))
        .findFirst()
        .orElseThrow(
            () ->
                new BadRequestException(
                    "Invalid serial number in filename: must start with 'A' for tractors or 'C' for combines."));
  }
}
