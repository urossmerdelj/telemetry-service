package com.logineko.repositories;

import com.logineko.dto.vehicle.VehicleQueryParamDto;
import com.logineko.entities.Vehicle;
import com.logineko.repositories.dto.PaginatedResult;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class VehicleRepository implements PanacheRepositoryBase<Vehicle, UUID> {

  public Optional<Vehicle> findBySerialNumber(String serialNumber) {
    return find("serialNumber", serialNumber).firstResultOptional();
  }

  public PaginatedResult<Vehicle> findAll(VehicleQueryParamDto params) {
    PanacheQuery<Vehicle> query;

    if (params.getSerialNumber() != null && !params.getSerialNumber().isBlank()) {
      query =
          find(
              "serialNumber = :serialNumber",
              Parameters.with("serialNumber", params.getSerialNumber()));
    } else {
      query = findAll();
    }

    List<Vehicle> list = query.page(params.getPageIndex(), params.getSize()).list();

    return new PaginatedResult<>(list, query.count());
  }
}
