package com.logineko.services;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import com.logineko.dto.pagination.PaginatedResponse;
import com.logineko.dto.pagination.PaginationQueryParam;
import com.logineko.dto.vehicle.CreateVehicleRequestDto;
import com.logineko.dto.vehicle.VehicleDto;
import com.logineko.entities.Vehicle;
import com.logineko.mappers.VehicleMapper;
import com.logineko.repositories.VehicleRepository;
import com.logineko.repositories.dto.PaginatedResult;
import com.logineko.resources.exceptions.ConflictException;

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
        .orElseThrow(NotFoundException::new);
  }

  public PaginatedResponse<VehicleDto> getVehicles(PaginationQueryParam params) {
    PaginatedResult<Vehicle> paginatedResult = vehicleRepository.findAll(params);
    return vehicleMapper.toPaginatedResponse(params, paginatedResult);
  }
}
