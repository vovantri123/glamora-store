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
  SET_DEFAULT_ADDRESS_SUCCESS("Set default address successfully"),
  GET_ALL_CATEGORY_SUCCESS("Get all categories successfully"),
  GET_CATEGORY_SUCCESS("Get category successfully"),
  CREATE_CATEGORY_SUCCESS("Create category successfully"),
  UPDATE_CATEGORY_SUCCESS("Update category successfully"),
  DELETE_CATEGORY_SUCCESS("Delete category successfully"),
  ACTIVATE_CATEGORY_SUCCESS("Activate category successfully"),
  GET_ALL_PRODUCT_SUCCESS("Get all products successfully"),
  GET_PRODUCT_SUCCESS("Get product successfully"),
  SEARCH_PRODUCT_SUCCESS("Search products successfully"),
  CREATE_PRODUCT_SUCCESS("Create product successfully"),
  UPDATE_PRODUCT_SUCCESS("Update product successfully"),
  DELETE_PRODUCT_SUCCESS("Delete product successfully"),
  ACTIVATE_PRODUCT_SUCCESS("Activate product successfully"),
  ADD_TO_CART_SUCCESS("Add to cart successfully"),
  UPDATE_CART_ITEM_SUCCESS("Update cart item successfully"),
  REMOVE_CART_ITEM_SUCCESS("Remove cart item successfully"),
  CLEAR_CART_SUCCESS("Clear cart successfully"),
  GET_CART_SUCCESS("Get cart successfully"),
  CREATE_VOUCHER_SUCCESS("Create voucher successfully"),
  UPDATE_VOUCHER_SUCCESS("Update voucher successfully"),
  DELETE_VOUCHER_SUCCESS("Delete voucher successfully"),
  GET_VOUCHER_SUCCESS("Get voucher successfully"),
  GET_ALL_VOUCHER_SUCCESS("Get all vouchers successfully"),
  COLLECT_VOUCHER_SUCCESS("Collect voucher successfully"),
  REVOKE_VOUCHER_SUCCESS("Revoke voucher successfully"),
  CALCULATE_VOUCHER_DISCOUNT_SUCCESS("Calculate voucher discount successfully"),
  // Shipping method messages
  CREATE_SHIPPING_METHOD_SUCCESS("Create shipping method successfully"),
  UPDATE_SHIPPING_METHOD_SUCCESS("Update shipping method successfully"),
  DELETE_SHIPPING_METHOD_SUCCESS("Delete shipping method successfully"),
  GET_SHIPPING_METHOD_SUCCESS("Get shipping method successfully"),
  GET_ALL_SHIPPING_METHOD_SUCCESS("Get all shipping methods successfully"),
  // Order messages
  CREATE_ORDER_SUCCESS("Create order successfully"),
  UPDATE_ORDER_SUCCESS("Update order successfully"),
  DELETE_ORDER_SUCCESS("Delete order successfully"),
  CANCEL_ORDER_SUCCESS("Cancel order successfully"),
  CONFIRM_ORDER_RECEIVED_SUCCESS("Order confirmed as received successfully"),
  GET_ORDER_SUCCESS("Get order successfully"),
  SEARCH_ORDER_SUCCESS("Search orders successfully"),
  // Product Review messages
  CREATE_REVIEW_SUCCESS("Create review successfully"),
  UPDATE_REVIEW_SUCCESS("Update review successfully"),
  DELETE_REVIEW_SUCCESS("Delete review successfully"),
  GET_REVIEW_SUCCESS("Get review successfully"),
  GET_ALL_REVIEW_SUCCESS("Get all reviews successfully"),
  // Generic messages
  OPERATION_SUCCESSFUL("Operation successful"),
  CREATED_SUCCESSFULLY("Created successfully"),
  UPDATED_SUCCESSFULLY("Updated successfully"),
  DELETED_SUCCESSFULLY("Deleted successfully"),
  // Payment messages
  CREATE_PAYMENT_SUCCESS("Create payment successfully"),
  PAYMENT_SUCCESS("Payment completed successfully"),
  PAYMENT_REFUND_SUCCESS("Payment refunded successfully"),
  GET_PAYMENT_SUCCESS("Get payment successfully"),
  GET_ALL_PAYMENT_METHOD_SUCCESS("Get all payment methods successfully"),
  GET_PAYMENT_METHOD_SUCCESS("Get payment method successfully"),
  PAYMENT_URL_GENERATED("Payment URL generated successfully"),
  PAYMENT_VERIFIED_SUCCESS("Payment verified successfully");

  private final String message;

  SuccessMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
