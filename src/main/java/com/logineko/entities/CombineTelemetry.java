package com.logineko.entities;

import jakarta.persistence.*;

import com.logineko.entities.enums.CropType;
import com.logineko.entities.enums.EnabledStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("COMBINE")
public class CombineTelemetry extends VehicleTelemetry {

  /** Rotational speed of the threshing drum. Unit: revolutions per minute (RPM). */
  private Integer drumSpeed;

  /** Rotational speed of the cleaning fan. Unit: revolutions per minute (RPM). */
  private Integer fanSpeed;

  /** Rotational speed of the rotor or straw walker. Unit: revolutions per minute (RPM). */
  private Integer rotorStrawWalkerSpeed;

  /** Grain losses from the separation system. Unit: percent (%). */
  private Double separationLosses;

  /** Grain losses from the sieves. Unit: percent (%). */
  private Double sieveLosses;

  /** The operational status of the chopper. */
  @Enumerated(EnumType.STRING)
  private EnabledStatus chopperStatus;

  /** The level of the diesel tank. Unit: percent (%). */
  private Double dieselTankLevel;

  /** The number of currently active partial widths of the header. */
  private Integer numberOfPartialWidths;

  /** The operational status of the front attachment (e.g., header). */
  @Enumerated(EnumType.STRING)
  private EnabledStatus frontAttachmentStatus;

  /** The maximum number of available partial widths on the header. */
  private Integer maxNumberOfPartialWidths;

  /** Rotational speed of the feed rake. Unit: revolutions per minute (RPM). */
  private Integer feedRakeSpeed;

  /** The status of the working position (e.g., header up/down). */
  @Enumerated(EnumType.STRING)
  private EnabledStatus workingPosition;

  /** The status of the grain tank unloading auger. */
  @Enumerated(EnumType.STRING)
  private EnabledStatus grainTankUnloading;

  /** The status of the main vehicle drive. */
  @Enumerated(EnumType.STRING)
  private EnabledStatus mainDriveStatus;

  /** The position of the concave. Unit: percent (%) or a coded value. */
  private Integer concavePosition;

  /** The position of the upper sieve. Unit: percent (%) or a coded value. */
  private Integer upperSievePosition;

  /** The position of the lower sieve. Unit: percent (%) or a coded value. */
  private Integer lowerSievePosition;

  @Column(name = "grain_tank_70")
  private Boolean grainTankFillLevel70Reached;

  @Column(name = "grain_tank_100")
  private Boolean grainTankFillLevel100Reached;

  /** The moisture content of the harvested grain. Unit: percent (%). */
  private Double grainMoistureContent;

  /** The rate of material flow through the machine. Unit: tons per hour (t/h). */
  private Double throughput;

  /** Rotational speed of the radial spreader. Unit: revolutions per minute (RPM). */
  private Integer radialSpreaderSpeed;

  /** The amount of grain in the returns system. Unit: percent (%). */
  private Double grainInReturns;

  /** The position of the feeder channel. Unit: percent (%). */
  private Double channelPosition;

  /** The status of the yield measurement system. */
  @Enumerated(EnumType.STRING)
  private EnabledStatus yieldMeasurementStatus;

  /** The measurement from the returns auger sensor. Unit: percent (%). */
  private Double returnsAugerMeasurement;

  /** The status of the moisture measurement system. */
  @Enumerated(EnumType.STRING)
  private EnabledStatus moistureMeasurementStatus;

  /** The type of crop being harvested. */
  @Enumerated(EnumType.STRING)
  private CropType typeOfCrop;

  /** The specific weight of the crop. Unit: kilograms per hectoliter (kg/hl). */
  private Integer specificCropWeight;

  /** The status of the auto-pilot system. */
  @Enumerated(EnumType.STRING)
  private EnabledStatus autoPilotStatus;

  /**
   * The status of the cruise pilot (throughput control) system. (in sample data there is only 0 =
   * Based on J1939 probably Off)
   */
  @Enumerated(EnumType.STRING)
  private EnabledStatus cruisePilotStatus;

  /** The rate of work being performed. Unit: hectares per hour (ha/h). */
  private Double rateOfWork;

  /** The measured yield of the crop. Unit: tons per hectare (t/ha). */
  private Double yieldValue;

  /** A dimensionless calibration factor for the yield meter (quantimeter). */
  private Double quantimeterCalibrationFactor;

  /** A setting value representing the sensitivity of the separation system. */
  private Integer separationSensitivity;

  /** A setting value representing the sensitivity of the sieve cleaning system. */
  private Integer sieveSensitivity;
}
