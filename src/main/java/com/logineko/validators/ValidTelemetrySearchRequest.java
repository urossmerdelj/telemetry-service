package com.logineko.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidTelemetrySearchRequestValidator.class)
@Documented
public @interface ValidTelemetrySearchRequest {

  String message() default "The search request contains invalid filter criteria.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
