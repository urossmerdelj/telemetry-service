package com.logineko.resources.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

@Provider
public class PSQLExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

  @Override
  public Response toResponse(ConstraintViolationException exception) {
    Throwable rootCause = getRootCause(exception);

    Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

    if (rootCause instanceof PSQLException psqlException) {
      String sqlState = psqlException.getSQLState();
      if (isConflict(sqlState)) {
        status = Response.Status.CONFLICT;
      }
    }

    return Response.status(status).build();
  }

  private boolean isConflict(String sqlState) {
    return PSQLState.UNIQUE_VIOLATION.getState().equals(sqlState)
        || PSQLState.FOREIGN_KEY_VIOLATION.getState().equals(sqlState);
  }

  private Throwable getRootCause(Throwable throwable) {
    Throwable cause = throwable.getCause();
    return (cause == null || cause == throwable) ? throwable : getRootCause(cause);
  }
}
