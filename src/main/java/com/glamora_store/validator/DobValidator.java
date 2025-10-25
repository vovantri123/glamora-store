package com.glamora_store.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {
  private int minAge;

  @Override
  public void initialize(DobConstraint constraintAnnotation) {
    this.minAge = constraintAnnotation.min(); // lấy tham số từ annotation
  }

  @Override
  public boolean isValid(LocalDate dob, ConstraintValidatorContext context) {
    if (dob == null) {
      return true; // để @NotNull xử lý riêng nếu muốn bắt buộc
    }
    return Period.between(dob, LocalDate.now()).getYears() >= minAge;
  }
}
