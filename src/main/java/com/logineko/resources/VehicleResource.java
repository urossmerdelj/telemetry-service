package com.logineko.resources;

import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;

import com.logineko.dto.pagination.PaginatedResponse;
import com.logineko.dto.pagination.PaginationQueryParam;
import com.logineko.dto.vehicle.CreateVehicleRequestDto;
import com.logineko.dto.vehicle.VehicleDto;
import com.logineko.services.VehicleService;

@Path("/vehicles")
public class VehicleResource {

  @Inject VehicleService vehicleService;

  @POST
  public VehicleDto createVehicle(@Valid CreateVehicleRequestDto body) {
    return vehicleService.createVehicle(body);
  }

  @GET
  @Path("/{id}")
  public VehicleDto getVehicleById(@PathParam("id") UUID id) {
    return vehicleService.getVehicleByIdOrThrow(id);
  }

  @GET
  public PaginatedResponse<VehicleDto> getVehicles(@BeanParam @Valid PaginationQueryParam params) {
    return vehicleService.getVehicles(params);
  }
}
