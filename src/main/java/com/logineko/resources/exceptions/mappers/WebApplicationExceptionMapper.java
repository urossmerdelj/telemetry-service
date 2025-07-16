package com.logineko.resources.exceptions.mappers;

import com.logineko.dto.ErrorResponse;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

  @Override
  public Response toResponse(WebApplicationException exception) {
    int status = exception.getResponse().getStatus();
    String message = exception.getMessage();

    ErrorResponse errorResponse = new ErrorResponse(status, message);

    return Response.status(status).entity(errorResponse).type(MediaType.APPLICATION_JSON).build();
  }
}
