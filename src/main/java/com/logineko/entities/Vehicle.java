package com.logineko.entities;

import java.util.UUID;

import jakarta.persistence.*;

import com.logineko.entities.enums.VehicleType;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Vehicle extends PanacheEntityBase {

  @Id @GeneratedValue public UUID id;

  @Column(unique = true, nullable = false)
  private String serialNumber;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private VehicleType vehicleType;
}
