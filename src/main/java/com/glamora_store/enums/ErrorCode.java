package com.glamora_store.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ErrorCode {
  UNCATEGORIZED_EXCEPTION(
      9999,
      "Uncategorized error"), // để thông báo Exception này chưa phân loại (kiểu xử lý cái exception
  // chung chung)
  USER_EXISTS(1001, "Username already exists"),
  NAME_REQUIRED(1001, "Name is required"),

  INVALID_KEY(
      1000, "Invalid message key") // Dùng khi message chứa ErrorCode bị sai chính tả (trong @Size)
  ,

  //

  //    UNCATEGORIZED_EXCEPTION(10000, "Uncategorized error"),
  VALIDATION_FAILED(10001, "Validation failed"),
  PATIENT_NOT_FOUND(10002, "Patient not found"),
  LIST_PATIENT_ID_NOT_FOUND(10003, "Some patient IDs were not found"),
  CANNOT_UPDATE_DELETED_PATIENT(10004, "Cannot update a deleted patient"),
  GENDER_INVALID(
      10007, "Gender must be one of: MALE, FEMALE, OTHER (or male, female, other). Unknown"),

  // Cẩn thận xóa nhầm - Start (Chủ yếu lấy message detail, còn code với default message thì lấy của
  // VALIDATION_FAILED
  FULL_NAME_REQUIRED(10006, "Full name is required"),
  DOB_INVALID(10008, "Date of birth must be in the past"),
  PHONE_NUMBER_INVALID(1009, "Invalid phone number (must be in format 0xxxxxxxxx or +84xxxxxxxxx)"),
  //    EMAIL_INVALID(10010, "Invalid email format"),
  CCCD_INVALID(10011, "CCCD must include exactly 12 digits"),
  DUPLICATE_CCCD(10012, "CCCD already exists in the system"),
  BHYT_INVALID(10013, "BHYT must be 10 digits (new) or 15 characters (old format)"),
  BHYT_DUPLICATE(10014, "BHYT already exists in the system"),
  PATIENT_ID_LIST_CANNOT_EMPTY(10015, "Patient ID list cannot be empty"),
  PASSWORD_REQUIRED(10000, "Password is required"),
  PASSWORD_INVALID(1001, "Password must be at least 8 characters"),
  EMAIL_REQUIRED(1000, "Email is required"),
  EMAIL_INVALID(1001, "Invalid email address"),
  USER_NOT_FOUND(1002, "User not found"),
  // Cẩn thận xóa nhầm - End

  INVALID_PARAMETER_TYPE(10016, "Invalid value for parameter"),

  MALFORMED_JSON(10017, "The JSON in your request is invalid. Please check the syntax."),
  URL_NOT_FOUND(10018, "Requested URL is not mapped"),
  INVALID_MESSAGE_KEY(10019, "Invalid message key"),
  CONSTRAINT_VIOLATION(10020, "Constraint violation in database");

  private int code;
  private String message;

  ErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
