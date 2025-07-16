package com.logineko.entities.enums;

import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnabledStatus {
  OFF(1, "off"),
  ON(0, "on"),
  NA(2, "NA"),
  UNKNOWN(3, "Unknown");

  private final int code;
  private final String value;

  public static EnabledStatus fromCode(Integer code) {
    if (code == null) {
      return NA;
    }

    return Stream.of(EnabledStatus.values())
        .filter(status -> status.getCode() == code)
        .findFirst()
        .orElse(UNKNOWN);
  }

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
