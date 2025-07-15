package com.logineko.entities.enums;

import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum ActivationStatus {
  INACTIVE(0),
  ACTIVE(1),
  NA(2),
  UNKNOWN(3);

  private final int code;

  ActivationStatus(int code) {
    this.code = code;
  }

  public static ActivationStatus fromCode(Integer code) {
    if (code == null) {
      return NA;
    }

    return Stream.of(ActivationStatus.values())
        .filter(status -> status.getCode() == code)
        .findFirst()
        .orElse(UNKNOWN);
  }

  public static ActivationStatus fromString(String value) {
    if (value == null || value.isBlank()) {
      return NA;
    }

    final String trimmedValue = value.trim();

    return Stream.of(values())
        .filter(status -> status.name().equalsIgnoreCase(trimmedValue))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
