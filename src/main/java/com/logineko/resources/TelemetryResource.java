package com.logineko.resources;

import com.logineko.dto.pagination.PaginatedResponse;
import com.logineko.dto.pagination.PaginationQueryParam;
import com.logineko.dto.telemetry.TelemetryImportDto;
import com.logineko.dto.telemetry.TelemetryImportResultDto;
import com.logineko.dto.telemetry.TelemetrySearchRequestDto;
import com.logineko.entities.VehicleTelemetry;
import com.logineko.services.TelemetryService;
import com.logineko.validators.ValidTelemetrySearchRequest;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("/telemetry")
public class TelemetryResource {

  private static final Pattern FILENAME_PATTERN = Pattern.compile("^LD_([^_]+)_");

  @Inject TelemetryService telemetryService;

  @POST
  @Path("/import")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public TelemetryImportResultDto importTelemetryCsv(@Valid TelemetryImportDto body) {
    if (body.file.fileName() == null || body.file.fileName().isEmpty()) {
      throw new BadRequestException("Filename cannot be null or empty.");
    }

    String serialNumber = parseSerialNumber(body.file.fileName());

    try (InputStream inputStream = Files.newInputStream(body.file.uploadedFile())) {
      return telemetryService.importTelemetryCsv(inputStream, serialNumber);
    } catch (IOException e) {
      Log.error("Failed to read the uploaded file.", e);
      throw new WebApplicationException("Failed to read the uploaded file.");
    }
  }

  @POST
  @Path("/search")
  public PaginatedResponse<VehicleTelemetry> searchTelemetry(
      @Valid @ValidTelemetrySearchRequest TelemetrySearchRequestDto searchRequestDto,
      @BeanParam @Valid PaginationQueryParam params) {

    return telemetryService.searchTelemetry(searchRequestDto.filters(), params);
  }

  private String parseSerialNumber(String filename) {
    Matcher matcher = FILENAME_PATTERN.matcher(filename);

    if (matcher.find()) {
      return matcher.group(1);
    } else {
      throw new BadRequestException("Invalid filename format, expected LD_<serialNumber>_*.csv");
    }
  }
}
