package com.glamora_store.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.glamora_store.util.ExceptionUtil;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public enum Gender {
  MALE("male"),
  FEMALE("female"),
  OTHER("other");

  private final String displayName;

  Gender(String displayName) {
    this.displayName = displayName;
  }

  // When deserialize (From JSON sang Java object)
  // It converts a given string ("male", "MALE", "Male") to the corresponding Gender enum name.
  @JsonCreator
  public static Gender fromDisplayName(String value) {
    if (value == null) return null;

    return Arrays.stream(Gender.values())
        .filter(g -> g.name().equalsIgnoreCase(value) || g.displayName.equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(
            () -> ExceptionUtil.with(HttpStatus.BAD_REQUEST, ErrorCode.GENDER_INVALID, value));
  }

  //  When serialize (From Java object to JSON)
  // Instead of outputting the enum name (MALE), it returns the displayName ("male" or "nam",...).
  @JsonValue
  public String getDisplayName() {
    return displayName;
  }
}
