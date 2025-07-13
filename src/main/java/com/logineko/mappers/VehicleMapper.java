package com.logineko.mappers;

import com.logineko.dto.vehicle.CreateVehicleRequestDto;
import com.logineko.dto.vehicle.VehicleDto;
import com.logineko.entities.Vehicle;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface VehicleMapper extends BaseMapper<Vehicle, VehicleDto> {

  @Mapping(target = "id", ignore = true)
  Vehicle toEntity(CreateVehicleRequestDto dto);
}
