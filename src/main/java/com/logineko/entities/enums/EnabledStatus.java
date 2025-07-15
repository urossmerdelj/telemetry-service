package com.logineko.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnabledStatus {
  ON("on"),
  OFF("off"),
  NA("NA"),
  UNKNOWN("Unknown");

  private final String value;

  public static EnabledStatus fromString(String value) {
    if (value == null || value.isBlank() || "NA".equalsIgnoreCase(value.trim())) {
      return NA;
    }

    String trimmedValue = value.trim();

    if (ON.value.equalsIgnoreCase(trimmedValue)) {
      return ON;
    }
    if (OFF.value.equalsIgnoreCase(trimmedValue)) {
      return OFF;
    }

    return UNKNOWN;
  }
}
