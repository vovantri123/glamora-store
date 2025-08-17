package com.glamora_store.enums;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  UNCATEGORIZED_EXCEPTION(1000, "Uncategorized error"), // để thông báo Exception này chưa phân loại (kiểu xử lý cái exception chung chung)
  INVALID_KEY(1001, "Invalid message key"), // Dùng khi message chứa ErrorCode bị sai chính tả (trong @Size)
  INVALID_PARAMETER_TYPE(1001, "Invalid value for parameter"),
  MALFORMED_JSON(1001, "The JSON in your request is invalid. Please check the syntax."),
  URL_NOT_FOUND(1001, "Requested URL is not mapped"),
  INVALID_MESSAGE_KEY(1001, "Invalid message key"),
  CONSTRAINT_VIOLATION(1001, "Constraint violation in database"),
  VALIDATION_FAILED(1001, "Validation failed"),

  PATIENT_NOT_FOUND(1001, "Patient not found"),
  LIST_PATIENT_ID_NOT_FOUND(1001, "Some patient IDs were not found"),
  CANNOT_UPDATE_DELETED_PATIENT(1001, "Cannot update a deleted patient"),
  GENDER_INVALID(1001, "Gender must be one of: MALE, FEMALE, OTHER (or male, female, other). Unknown"),
  USER_NOT_EXISTED(1001, "User not existed"),
  CANNOT_CREATE_TOKEN(1001, "Cannot create token"),
  INVALID_TOKEN_FORMAT(1001, "Invalid token format"),
  ROLE_NOT_FOUND(1001, "Role not found"),
  ROLES_NOT_FOUND(1001, "Roles not found"),
  UNAUTHENTICATED(1001, "Unauthenticated"),
  ACCESS_DENIED(1001, "You do not have permission"),
  PERMISSION_NOT_FOUND(1001, "Permission not found"),
  PERMISSIONS_NOT_FOUND(1001, "Permissions not found"),
  LIST_ROLE_NAMES_EMPTY(1001, "List roleNames cannot be empty"),


  // Cẩn thận xóa nhầm - Start (Chủ yếu lấy message detail, còn code với default message thì lấy của
  // VALIDATION_FAILED
  FULL_NAME_REQUIRED(1001, "Full name is required"),
  DOB_INVALID(1001, "Your age must be at least 18"),
  PHONE_NUMBER_INVALID(1001, "Invalid phone number (must be in format 0xxxxxxxxx or +84xxxxxxxxx)"),
  CCCD_INVALID(1001, "CCCD must include exactly 12 digits"),
  DUPLICATE_CCCD(1001, "CCCD already exists in the system"),
  BHYT_INVALID(1001, "BHYT must be 10 digits (new) or 15 characters (old format)"),
  BHYT_DUPLICATE(1001, "BHYT already exists in the system"),
  PATIENT_ID_LIST_CANNOT_EMPTY(1001, "Patient ID list cannot be empty"),
  PASSWORD_REQUIRED(1001, "Password is required"),
  PASSWORD_INVALID(1001, "Password must be at least 8 characters"),
  EMAIL_REQUIRED(1001, "Email is required"),
  EMAIL_INVALID(1001, "Invalid email address"),
  USER_NOT_FOUND(1001, "User not found"),
  // Cẩn thận xóa nhầm - End
  ;

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
