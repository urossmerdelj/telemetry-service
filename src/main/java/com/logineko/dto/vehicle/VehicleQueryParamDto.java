package com.logineko.dto.vehicle;

import com.logineko.dto.pagination.PaginationQueryParam;
import jakarta.ws.rs.QueryParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleQueryParamDto extends PaginationQueryParam {
  @QueryParam("serialNumber")
  private String serialNumber;
}
