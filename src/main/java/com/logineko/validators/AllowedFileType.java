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
@Constraint(validatedBy = AllowedFileTypeValidator.class)
@Documented
public @interface AllowedFileType {

  String[] value();

  String message() default "Invalid file type. Allowed types are: {value}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
