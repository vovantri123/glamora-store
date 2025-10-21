package com.glamora_store.enums;

public enum SuccessMessage {
  CREATE_USER_SUCCESS("Create user successfully"),
  UPDATE_USER_SUCCESS("Update user successfully"),
  GET_USER_SUCCESS("Get user successfully"),
  SEARCH_USER_SUCCESS("Search user successfully"),
  NO_DATA_FOUND("No data found"),
  DELETE_USER_SUCCESS("User has been deleted"),
  DELETE_LIST_USER_SUCCESS("Soft delete list user successfully"),
  ACTIVATE_USER_SUCCESS("Activate user successfully"),
  LOGIN_SUCCESS("Login successfully"),
  TOKEN_VALIDATION_SUCCESS("Token validation success"),
  TOKEN_VALIDATION_FAILURE("Token validation failure"),
  THIS_IS_YOUR_INFO("This is your infomation"),
  CREATE_PERMISSION_SUCCESS("Create permission successfully"),
  DELETE_PERMISSION_SUCCESS("Delete permission successfully"),
  GET_ALL_PERMISSION_SUCCESS("Get all permission successfully"),
  CREATE_ROLE_SUCCESS("Create role successfully"),
  DELETE_ROLE_SUCCESS("Delete role successfully"),
  GET_ALL_ROLE_SUCCESS("Get all role successfully"),
  UPDATE_ROLE_OF_USER_SUCCESS("Update role of user successfully"),
  OTP_VERIFIED_SUCCESS("OTP verified successfully, account is activated"),
  OTP_INVALID_OR_EXPIRED("OTP is invalid or has expired"),
  OTP_SENT("OTP has been sent to your email"),
  UPDATE_PASSWORD_SUCCESS("Update password successfully"),
  PASSWORD_RESET_SUCCESS("Password has been reset successfully"),
  CREATE_ADDRESS_SUCCESS("Create address successfully"),
  UPDATE_ADDRESS_SUCCESS("Update address successfully"),
  DELETE_ADDRESS_SUCCESS("Delete address successfully"),
  GET_ADDRESS_SUCCESS("Get address successfully"),
  GET_ALL_ADDRESS_SUCCESS("Get all addresses successfully"),
  SET_DEFAULT_ADDRESS_SUCCESS("Set default address successfully");

  private final String message;

  SuccessMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
