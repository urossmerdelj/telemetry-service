package com.logineko.validators;

import static com.logineko.utils.DateTimeUtils.EXTERNAL_SOURCE_DATE_FORMATTER;

import com.logineko.dto.telemetry.TelemetryFilterDto;
import com.logineko.dto.telemetry.TelemetryFilterOperation;
import com.logineko.dto.telemetry.TelemetrySearchRequestDto;
import com.logineko.services.TelemetryFieldRegistryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ValidTelemetrySearchRequestValidator
    implements ConstraintValidator<ValidTelemetrySearchRequest, TelemetrySearchRequestDto> {

  @Inject TelemetryFieldRegistryService fieldRegistry;

  @Override
  public boolean isValid(
      TelemetrySearchRequestDto searchRequest, ConstraintValidatorContext context) {
    if (searchRequest == null
        || searchRequest.filters() == null
        || searchRequest.filters().isEmpty()) {
      return true;
    }

    // we will build our own violations, so disable the default one.
    context.disableDefaultConstraintViolation();

    List<TelemetryFilterDto> filters = searchRequest.filters();
    boolean isValid = true;

    for (int i = 0; i < filters.size(); i++) {
      String filterPath = String.format("filters[%d]", i);

      if (!validateFilter(filters.get(i), context, filterPath)) {
        isValid = false;
      }
    }

    return isValid;
  }

  @SuppressWarnings("unchecked")
  private boolean validateFilter(
      TelemetryFilterDto filter, ConstraintValidatorContext context, String filterPath) {
    String field = filter.field();
    TelemetryFilterOperation operation = filter.operation();
    Object value = filter.value();
    Class<?> type = fieldRegistry.getFieldType(field);

    // check if the field actually exists in our registry/entities.
    if (!fieldRegistry.fieldExists(field)) {
      addViolationToContext(
          context,
          filterPath,
          String.format("Filter field '%s' is not a valid telemetry field.", field));
      return false;
    }

    List<String> violations = new ArrayList<>();

    // check for supported operation
    if (!fieldRegistry.getSupportedOperations(type).contains(operation)) {
      violations.add(
          String.format(
              "Operation '%s' is not supported for field '%s' of type %s.",
              operation, field, type.getSimpleName()));
    }

    // check value compatibility with operation
    if ((operation == TelemetryFilterOperation.GREATER_THAN
            || operation == TelemetryFilterOperation.LESS_THAN)
        && !(value instanceof Number)
        && !(type == LocalDateTime.class)) {
      violations.add(
          String.format(
              "Operation '%s' on field '%s' requires a numeric value, but got '%s'.",
              operation, field, value));
    }

    if (operation == TelemetryFilterOperation.CONTAINS && !(value instanceof String)) {
      violations.add(
          String.format(
              "Operation '%s' on field '%s' requires a string value, but got '%s'.",
              operation, field, value));
    }

    // check enum value type
    if (type.isEnum()) {
      if (!(value instanceof String stringValue)) {
        violations.add(String.format("Filter value for enum field '%s' must be a string.", field));
      } else {
        try {
          Enum.valueOf((Class<Enum>) type, stringValue.toUpperCase());
        } catch (IllegalArgumentException e) {
          violations.add(
              String.format("Invalid value '%s' for enum field '%s'.", stringValue, field));
        }
      }
    }

    if (type == LocalDateTime.class) {
      if (!(value instanceof String stringValue)) {
        violations.add(
            String.format("Filter value for date/time field '%s' must be a string.", field));
      } else {
        try {
          // attempt to parse using the agreed custom format.
          LocalDateTime.parse(stringValue, EXTERNAL_SOURCE_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
          violations.add(
              String.format(
                  "Invalid date/time format for field '%s'. Expected format: '%s' (e.g., 'Oct 7, 2022, 10:13:04 AM').",
                  EXTERNAL_SOURCE_DATE_FORMATTER, field));
        }
      }
    }

    violations.forEach(violation -> addViolationToContext(context, filterPath, violation));

    return violations.isEmpty();
  }

  private void addViolationToContext(
      ConstraintValidatorContext context, String filterPath, String message) {
    context
        .buildConstraintViolationWithTemplate(message)
        .addPropertyNode(filterPath)
        .addConstraintViolation();
  }
}
