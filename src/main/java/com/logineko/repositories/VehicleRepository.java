package com.logineko.repositories;

import java.util.Optional;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;

import com.logineko.dto.pagination.PaginationQueryParam;
import com.logineko.entities.Vehicle;
import com.logineko.repositories.dto.PaginatedResult;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class VehicleRepository implements PanacheRepositoryBase<Vehicle, UUID> {

  public Optional<Vehicle> findBySerialNumber(String serialNumber) {
    return find("serialNumber", serialNumber).firstResultOptional();
  }

  public PaginatedResult<Vehicle> findAll(PaginationQueryParam params) {
    PanacheQuery<Vehicle> query = findAll().page(params.getPageIndex(), params.getPerPage());
    return new PaginatedResult<>(query.list(), query.count());
  }
}
