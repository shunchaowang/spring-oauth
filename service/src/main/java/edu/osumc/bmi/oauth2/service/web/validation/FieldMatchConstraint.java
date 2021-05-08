package edu.osumc.bmi.oauth2.service.web.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldMatchValidator.class)
@Documented
public @interface FieldMatchConstraint {

  String message() default "Fields don't match!";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String field();

  String fieldMatch();

  @Target({ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @interface List {
    FieldMatchConstraint[] value();
  }
}
