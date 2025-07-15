package com.logineko.services;

import com.logineko.dto.pagination.PaginatedResponse;
import com.logineko.dto.vehicle.CreateVehicleRequestDto;
import com.logineko.dto.vehicle.VehicleDto;
import com.logineko.dto.vehicle.VehicleQueryParamDto;
import com.logineko.entities.Vehicle;
import com.logineko.mappers.VehicleMapper;
import com.logineko.repositories.VehicleRepository;
import com.logineko.repositories.dto.PaginatedResult;
import com.logineko.resources.exceptions.ConflictException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.UUID;

@ApplicationScoped
public class VehicleService {

  @Inject VehicleRepository vehicleRepository;

  @Inject VehicleMapper vehicleMapper;

  @Transactional
  public VehicleDto createVehicle(CreateVehicleRequestDto body) {
    vehicleRepository
        .findBySerialNumber(body.serialNumber())
        .ifPresent(
            v -> {
              throw new ConflictException("Vehicle with serial number already exists.");
            });

    Vehicle vehicle = vehicleMapper.toEntity(body);
    vehicleRepository.persist(vehicle);
    return vehicleMapper.toDto(vehicle);
  }

  public VehicleDto getVehicleByIdOrThrow(UUID id) {
    return vehicleRepository
        .findByIdOptional(id)
        .map(vehicleMapper::toDto)
        .orElseThrow(() -> new NotFoundException("Vehicle by id not found."));
  }

  public Vehicle getVehicleBySerialNumberOrThrow(String serialNumber) {
    return vehicleRepository
        .findBySerialNumber(serialNumber)
        .orElseThrow(() -> new NotFoundException("Vehicle by serialNumber not found."));
  }

  public PaginatedResponse<VehicleDto> getVehicles(VehicleQueryParamDto params) {
    PaginatedResult<Vehicle> paginatedResult = vehicleRepository.findAll(params);
    return vehicleMapper.toPaginatedResponse(params, paginatedResult);
  }
}
