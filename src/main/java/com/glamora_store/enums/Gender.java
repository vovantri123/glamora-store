package com.glamora_store.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.glamora_store.util.ExceptionUtil;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public enum Gender {
  MALE,
  FEMALE,
  OTHER;


  @JsonCreator // Dùng khi deserialize (JSON → Java)
  // Jackson dùng method này để chuyển chuỗi JSON thành enum Gender trước khi lưu DB
  public static Gender fromDisplayName(String value) {
    if (value == null)
      return null;

    return Arrays.stream(Gender.values())
        .filter(g -> g.name().equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(() -> ExceptionUtil.with(HttpStatus.BAD_REQUEST, ErrorMessage.GENDER_INVALID, value));
  }
}
