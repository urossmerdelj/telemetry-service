package com.logineko.repositories;

import com.logineko.entities.VehicleTelemetry;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class VehicleTelemetryRepository implements PanacheRepositoryBase<VehicleTelemetry, UUID> {}
