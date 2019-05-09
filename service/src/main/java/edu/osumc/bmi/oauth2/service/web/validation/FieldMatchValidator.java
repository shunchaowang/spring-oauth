package edu.osumc.bmi.oauth2.service.web.validation;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatchConstraint, Object> {

  private String field;
  private String fieldMatch;

  /**
   * Implements the validation logic. The state of {@code value} must not be altered.
   *
   * <p>This method can be accessed concurrently, thread-safety must be ensured by the
   * implementation.
   *
   * @param value object to validate
   * @param context context in which the constraint is evaluated
   * @return {@code false} if {@code value} does not pass the constraint
   */
  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
    Object fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);
    if (fieldValue != null) {
      return fieldValue.equals(fieldMatchValue);
    } else {
      return fieldMatchValue == null;
    }
  }

  /**
   * Initializes the validator in preparation for {@link #isValid(Object,
   * ConstraintValidatorContext)} calls. The constraint annotation for a given constraint
   * declaration is passed.
   *
   * <p>This method is guaranteed to be called before any use of this instance for validation.
   *
   * <p>The default implementation is a no-op.
   *
   * @param constraintAnnotation annotation instance for a given constraint declaration
   */
  @Override
  public void initialize(FieldMatchConstraint constraintAnnotation) {
    this.field = constraintAnnotation.field();
    this.fieldMatch = constraintAnnotation.fieldMatch();
  }
}
