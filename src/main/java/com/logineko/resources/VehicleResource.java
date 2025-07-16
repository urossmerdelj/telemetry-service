package com.logineko.resources;

import com.logineko.dto.pagination.PaginatedResponse;
import com.logineko.dto.vehicle.CreateVehicleRequestDto;
import com.logineko.dto.vehicle.VehicleDto;
import com.logineko.dto.vehicle.VehicleQueryParamDto;
import com.logineko.services.VehicleService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import java.util.UUID;

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
  public PaginatedResponse<VehicleDto> getVehicles(@BeanParam @Valid VehicleQueryParamDto params) {
    return vehicleService.getVehicles(params);
  }

  @DELETE
  @Path("/{id}")
  public void deleteVehicle(@PathParam("id") UUID id) {
    vehicleService.deleteVehicleById(id);
  }
}
