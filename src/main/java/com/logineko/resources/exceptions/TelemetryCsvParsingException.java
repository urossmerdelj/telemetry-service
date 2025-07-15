package com.logineko.resources.exceptions;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;

public class TelemetryCsvParsingException extends ClientErrorException {

  public TelemetryCsvParsingException(String fieldName, String value, Throwable cause) {
    super(
        String.format("Failed to parse field %s, with value %s", fieldName, value),
        Response.Status.BAD_REQUEST,
        cause);
  }

  public TelemetryCsvParsingException(String fieldName, String value) {
    super(
        String.format("Failed to parse field %s, with value %s", fieldName, value),
        Response.Status.BAD_REQUEST);
  }
}
