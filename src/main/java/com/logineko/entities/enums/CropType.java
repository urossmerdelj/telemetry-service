package com.logineko.entities.enums;

import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CropType {
  WHEAT("Wheat"),
  BARLEY("Barley"),
  CANOLA("Canola"),
  CORN("Corn"),
  MAIZE("Maize"),
  SOYBEANS("Soybeans"),
  SUNFLOWERS("Sunflowers"),
  OATS("Oats"),
  RYE("Rye"),
  OTHER("Other"),
  NA("NA"),
  UNKNOWN("Unknown");

  private final String name;

  public static CropType fromString(String value) {
    if (value == null || value.isBlank() || "NA".equalsIgnoreCase(value.trim())) {
      return NA;
    }

    String trimmedValue = value.trim();
    return Stream.of(values())
        .filter(type -> !type.equals(UNKNOWN) && type.name.equalsIgnoreCase(trimmedValue))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
