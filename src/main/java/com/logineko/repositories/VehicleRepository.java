package com.logineko.repositories;

import com.logineko.entities.Vehicle;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class VehicleRepository implements PanacheRepositoryBase<Vehicle, UUID> {

    public Optional<Vehicle> findBySerialNumber(String serialNumber) {
        return find("serialNumber", serialNumber).firstResultOptional();
    }
}
