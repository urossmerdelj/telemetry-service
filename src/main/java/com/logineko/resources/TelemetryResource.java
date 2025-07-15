package com.logineko.resources;

import com.logineko.dto.telemetry.TelemetryImportResultDto;
import com.logineko.services.TelemetryService;
import com.logineko.validators.AllowedFileType;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@Path("/telemetries")
public class TelemetryResource {

  private static final Pattern FILENAME_PATTERN =
      Pattern.compile("^(LD)_(.+)_(\\d{8})_(\\d{8})(\\.csv)$");

  @Inject TelemetryService telemetryService;

  @POST
  @Path("/import")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public TelemetryImportResultDto importTelemetryCsv(
      @FormParam("fileUpload")
          @Valid
          @NotNull(message = "File upload is required.")
          @AllowedFileType("text/csv")
          FileUpload fileUpload) {
    if (fileUpload.fileName() == null || fileUpload.fileName().isEmpty()) {
      throw new BadRequestException("Filename cannot be null or empty.");
    }

    String serialNumber = parseSerialNumber(fileUpload.fileName());

    try (InputStream inputStream = Files.newInputStream(fileUpload.uploadedFile())) {
      return telemetryService.importTelemetryCsv(inputStream, serialNumber);
    } catch (IOException e) {
      Log.error("Failed to read the uploaded file.", e);
      throw new WebApplicationException("Failed to read the uploaded file.");
    }
  }

  private String parseSerialNumber(String filename) {
    Matcher matcher = FILENAME_PATTERN.matcher(filename);

    if (matcher.matches()) {
      return matcher.group(2);
    } else {
      throw new BadRequestException(
          "Invalid filename format, expected LD_SERIALNUMBER_YYYYMMDD_YYYYMMDD.csv");
    }
  }
}
