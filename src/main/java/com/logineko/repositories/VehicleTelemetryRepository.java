package com.logineko.repositories;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;

import com.logineko.entities.VehicleTelemetry;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class VehicleTelemetryRepository implements PanacheRepositoryBase<VehicleTelemetry, UUID> {}
