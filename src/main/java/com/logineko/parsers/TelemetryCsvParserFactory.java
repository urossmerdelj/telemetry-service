package com.logineko.parsers;

import com.logineko.annotations.VehicleTypeParserLiteral;
import com.logineko.entities.enums.VehicleType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class TelemetryCsvParserFactory {

  @Inject @Any Instance<TelemetryCsvParser> parsers;

  public TelemetryCsvParser getParser(VehicleType vehicleType) {
    return parsers.select(new VehicleTypeParserLiteral(vehicleType)).get();
  }
}
