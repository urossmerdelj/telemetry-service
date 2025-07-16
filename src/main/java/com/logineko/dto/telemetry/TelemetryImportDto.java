package com.logineko.dto.telemetry;

import com.logineko.validators.AllowedFileType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public class TelemetryImportDto {

  @RestForm("file")
  @Valid
  @NotNull(message = "File upload is required.")
  @AllowedFileType("text/csv")
  public FileUpload file;
}
