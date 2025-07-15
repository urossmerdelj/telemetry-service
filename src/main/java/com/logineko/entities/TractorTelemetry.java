package com.logineko.entities;

import com.logineko.entities.enums.ActivationStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("TRACTOR")
public class TractorTelemetry extends VehicleTelemetry {

  /** The rate of fuel consumption. Unit: liters per hour (l/h). */
  private Double fuelConsumption;

  /**
   * The ground speed as measured by radar, independent of wheel slip. Unit: kilometers per hour
   * (km/h).
   */
  private Double groundSpeedRadar;

  /** The temperature of the engine coolant. Unit: degrees Celsius (°C). */
  private Double coolantTemperature;

  /** The rotational speed of the front Power Take-Off (PTO). Unit: revolutions per minute (RPM). */
  private Integer speedFrontPto;

  /** The rotational speed of the rear Power Take-Off (PTO). Unit: revolutions per minute (RPM). */
  private Integer speedRearPto;

  /**
   * The currently engaged gear of the transmission. The values can represent specific states, e.g.,
   * 0 for Neutral.
   */
  private Integer currentGearShift;

  /** The ambient air temperature. Unit: degrees Celsius (°C). */
  private Double ambientTemperature;

  /**
   * The status of the parking brake. The integer value is a code defined by the data source. (in
   * sample data there is only 3 = Based on J1939 probably NA)
   */
  @Enumerated(EnumType.STRING)
  private ActivationStatus parkingBrakeStatus;

  /**
   * The status of the transverse differential lock. The integer value is a code defined by the data
   * source. (in sample data there is only 0 = Based on J1939 probably INACTIVE)
   */
  @Enumerated(EnumType.STRING)
  private ActivationStatus transverseDifferentialLockStatus;

  /** The status of the all-wheel-drive (AWD) system. */
  @Enumerated(EnumType.STRING)
  private ActivationStatus allWheelDriveStatus;

  /** The activation status of the creeper gear. */
  @Enumerated(EnumType.STRING)
  private ActivationStatus creeperStatus;
}
