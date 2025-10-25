package com.glamora_store.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD}) // annotation này áp dụng cho field
@Retention(RetentionPolicy.RUNTIME)
// tồn tại ở runtime để validator có thể đọc, còn lombok là COMPILE
@Constraint(validatedBy = {DobValidator.class}) // chỉ định class xử lý validate
public @interface DobConstraint {
  String message() default "Invalid date of birth"; // thông báo lỗi mặc định

  int min() default 18; // tham số tùy chỉnh (tuổi tối thiểu)

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
