package com.logineko.annotations;

import com.logineko.entities.enums.VehicleType;
import jakarta.enterprise.util.AnnotationLiteral;

public class VehicleTypeParserLiteral extends AnnotationLiteral<VehicleTypeParser>
    implements VehicleTypeParser {

  private final VehicleType vehicleType;

  public VehicleTypeParserLiteral(VehicleType vehicleType) {
    this.vehicleType = vehicleType;
  }

  @Override
  public VehicleType value() {
    return vehicleType;
  }
}
