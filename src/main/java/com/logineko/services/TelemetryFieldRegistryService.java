package com.logineko.services;

import com.logineko.dto.telemetry.TelemetryFilterOperation;
import com.logineko.entities.CombineTelemetry;
import com.logineko.entities.TractorTelemetry;
import com.logineko.entities.Vehicle;
import com.logineko.entities.VehicleTelemetry;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@ApplicationScoped
public class TelemetryFieldRegistryService {

  private final Map<String, Class<?>> fieldTypes = new HashMap<>();
  private final Map<String, Class<?>> fieldDeclaringClasses = new HashMap<>();

  void onStart(@Observes StartupEvent ev) {
    Stream.of(VehicleTelemetry.class, TractorTelemetry.class, CombineTelemetry.class)
        .flatMap(clazz -> Arrays.stream(clazz.getDeclaredFields()))
        .filter(field -> !field.getName().equals("location"))
        .forEach(
            field -> {
              fieldTypes.put(field.getName(), field.getType());
              fieldDeclaringClasses.put(field.getName(), field.getDeclaringClass());
            });

    // manually register the 'serialNumber' field from the related Vehicle entity.
    fieldTypes.put("serialNumber", String.class);
    fieldDeclaringClasses.put("serialNumber", Vehicle.class);
  }

  public boolean fieldExists(String fieldName) {
    return fieldTypes.containsKey(fieldName);
  }

  public Class<?> getFieldType(String fieldName) {
    return fieldTypes.get(fieldName);
  }

  public Class<?> getDeclaringClass(String fieldName) {
    return fieldDeclaringClasses.get(fieldName);
  }

  public Set<TelemetryFilterOperation> getSupportedOperations(Class<?> type) {
    if (Number.class.isAssignableFrom(type) || LocalDateTime.class.isAssignableFrom(type)) {
      return EnumSet.of(
          TelemetryFilterOperation.EQUALS,
          TelemetryFilterOperation.GREATER_THAN,
          TelemetryFilterOperation.LESS_THAN);
    } else if (String.class.isAssignableFrom(type)) {
      return EnumSet.of(TelemetryFilterOperation.EQUALS, TelemetryFilterOperation.CONTAINS);
    } else if (type.isEnum() || Boolean.class.isAssignableFrom(type) || type == boolean.class) {
      return EnumSet.of(TelemetryFilterOperation.EQUALS);
    }

    return EnumSet.of(TelemetryFilterOperation.EQUALS);
  }
}
