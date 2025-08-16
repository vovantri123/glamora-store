package com.glamora_store.enums;

public enum SuccessMessage {
  CREATE_USER_SUCCESS("Create user successfully"),
  UPDATE_USER_SUCCESS("Update user successfully"),
  GET_USER_SUCCESS("Get user successfully"),
  SEARCH_USER_SUCCESS("Search user successfully"),
  DELETE_USER_SUCCESS("Patient has been deleted"),
  DELETE_LIST_USER_SUCCESS("Soft delete list user successfully"),
  ACTIVATE_USER_SUCCESS("Activate user successfully"),
  LOGIN_SUCCESS("Login successfully"),
  TOKEN_VALIDATION_SUCCESS("Token validation success"),
  TOKEN_VALIDATION_FAILURE("Token validation failure"),
  THIS_IS_MY_INFO("This is my info"),
  CREATE_PERMISSION_SUCCESS("Create permission successfully"),
  DELETE_PERMISSION_SUCCESS("Delete permission successfully"),
  GET_ALL_PERMISSION_SUCCESS("Get all permission successfully"),
  CREATE_ROLE_SUCCESS("Create role successfully"),
  DELETE_ROLE_SUCCESS("Delete role successfully"),
  GET_ALL_ROLE_SUCCESS("Get all role successfully"),
  UPDATE_ROLE_OF_USER_SUCCESS("Update role of user successfully"),
  ;

  private final String message;

  SuccessMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
