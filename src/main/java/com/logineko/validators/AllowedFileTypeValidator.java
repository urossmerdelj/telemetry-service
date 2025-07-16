package com.logineko.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public class AllowedFileTypeValidator implements ConstraintValidator<AllowedFileType, FileUpload> {

  private final Set<String> allowedTypes = new HashSet<>();

  @Override
  public void initialize(AllowedFileType constraintAnnotation) {
    allowedTypes.addAll(Arrays.asList(constraintAnnotation.value()));
  }

  @Override
  public boolean isValid(FileUpload fileUpload, ConstraintValidatorContext context) {
    if (fileUpload == null) {
      return true;
    }

    String contentType = fileUpload.contentType();
    if (contentType == null || !allowedTypes.contains(contentType.toLowerCase())) {
      context.disableDefaultConstraintViolation();
      String message =
          String.format(
              "Invalid file type ('%s'). Allowed types are: %s",
              contentType, String.join(", ", allowedTypes));
      context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
      return false;
    }

    return true;
  }
}
