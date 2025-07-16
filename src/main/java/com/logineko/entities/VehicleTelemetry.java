package com.logineko.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_type")
@Table(
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_vehicle_telemetry_vehicle_datetime",
          columnNames = {"vehicle_id", "date_time"})
    })
public abstract class VehicleTelemetry extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "vehicle_id", nullable = false)
  private Vehicle vehicle;

  @Column(nullable = false)
  private LocalDateTime dateTime;

  @Column(nullable = false)
  private Double longitude;

  @Column(nullable = false)
  private Double latitude;

  /** The geographic location of the vehicle, stored as a Point with SRID 4326 (WGS 84). */
  @Column(nullable = false, columnDefinition = "geometry(Point,4326)")
  @JsonIgnore
  private Point<G2D> location;

  /** The cumulative total working hours of the vehicle's engine. Unit: hours (h). */
  private Double totalWorkingHours;

  /** The rotational speed of the engine. Unit: revolutions per minute (RPM). */
  private Integer engineSpeed;

  /** The current load on the engine. Unit: percent (%). */
  private Integer engineLoad;

  /** The ground speed as measured by the vehicle's wheels. Unit: kilometers per hour (km/h). */
  private Double groundSpeed;

  /**
   * A flag indicating that this record had one or more fields that could not be parsed from the
   * original source data. This allows for easy querying of failed imports.
   */
  @Column(nullable = false)
  private boolean importFailed = false;

  /**
   * Stores the original, raw CSV row as a JSON object if 'importFailed' is true. This provides the
   * full context for debugging and later data migration without cluttering the main schema with
   * error-specific columns. Stored as JSONB in the database for efficient querying.
   */
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private Map<String, String> rawCsvData;
}
