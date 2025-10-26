package com.glamora_store.enums;

import lombok.Getter;

@Getter
public enum ErrorMessage {
  // Message chung chung
  UNCATEGORIZED_EXCEPTION("Uncategorized error. Please check log for more detail"),
  INVALID_KEY("Invalid message key"), // Dùng khi message chứa ErrorCode bị sai chính tả (trong @Size)
  INVALID_PARAMETER_TYPE("Invalid value for parameter"),
  MALFORMED_JSON("The JSON in your request is invalid. Please check the syntax."),
  URL_NOT_FOUND("Requested URL is not mapped"),
  INVALID_MESSAGE_KEY("Invalid message key"),
  CONSTRAINT_VIOLATION("Constraint violation in database"),
  VALIDATION_FAILED("Validation failed"),
  UTILITY_CLASS_SHOULD_NOT_BE_INSTANTIATED("Utility class should not be instantiated"),

  // Message cụ thể
  GENDER_INVALID("Gender must be one of: MALE, FEMALE, OTHER (or male, female, other). Unknown"),
  USER_NOT_EXISTED("User not existed"),
  CANNOT_CREATE_TOKEN("Cannot create token"),
  INVALID_TOKEN_FORMAT("Invalid token format"),
  ROLE_NOT_FOUND("Role not found"),
  ROLES_NOT_FOUND("Roles not found"),
  UNAUTHENTICATED("Unauthenticated"),
  ACCESS_DENIED("You do not have permission"),
  PERMISSION_NOT_FOUND("Permission not found"),
  PERMISSIONS_NOT_FOUND("Permissions not found"),
  USER_EXISTED("User already existed"),
  SEND_EMAIL_FAIL("Failed to send OTP email"),
  OLD_PASSWORD_INCORRECT("Old password is incorrect"),
  ADDRESS_NOT_FOUND("Address not found"),
  DEFAULT_ADDRESS_NOT_FOUND("Default address not found"),
  USER_DELETED("User has been deleted"),

  CATEGORY_NOT_FOUND("Category not found"),
  PRODUCT_NOT_FOUND("Product not found"),

  // Category validation messages
  CATEGORY_NAME_REQUIRED("Category name is required"),
  CATEGORY_NAME_TOO_LONG("Category name must not exceed 255 characters"),
  CATEGORY_DESCRIPTION_TOO_LONG("Category description must not exceed 1000 characters"),
  CATEGORY_IMAGE_URL_INVALID("Category image URL is invalid"),
  CATEGORY_NAME_ALREADY_EXISTS("Category name already exists"),
  CATEGORY_CANNOT_BE_OWN_PARENT("Category cannot be its own parent"),
  CATEGORY_CANNOT_DELETE_WITH_PRODUCTS(
      "Cannot delete category with products. Please reassign or delete products first."),

  // Product validation messages
  PRODUCT_NAME_REQUIRED("Product name is required"),
  PRODUCT_NAME_TOO_LONG("Product name must not exceed 255 characters"),
  PRODUCT_DESCRIPTION_TOO_LONG("Product description must not exceed 5000 characters"),
  PRODUCT_CATEGORY_ID_REQUIRED("Category ID is required"),
  PRODUCT_NAME_ALREADY_EXISTS("Product name already exists"),

  CART_NOT_FOUND("Cart not found"),
  CART_ITEM_NOT_FOUND("Cart item not found"),
  PRODUCT_VARIANT_NOT_FOUND("Product variant not found"),
  INSUFFICIENT_STOCK("Insufficient stock available"),

  VOUCHER_NOT_FOUND("Voucher not found"),
  VOUCHER_CODE_ALREADY_EXISTS("Voucher code already exists"),
  VOUCHER_EXPIRED("Voucher has expired"),
  VOUCHER_NOT_STARTED("Voucher has not started yet"),
  VOUCHER_INACTIVE("Voucher is not active"),
  VOUCHER_USAGE_LIMIT_EXCEEDED("Voucher usage limit exceeded"),
  VOUCHER_USER_LIMIT_EXCEEDED("You have reached the usage limit for this voucher"),
  VOUCHER_ALREADY_COLLECTED("You have already collected this voucher"),
  VOUCHER_MIN_ORDER_NOT_MET("Order amount does not meet minimum requirement for this voucher"),
  USER_VOUCHER_NOT_FOUND("User voucher not found"),
  END_DATE_INVALID("End date must be after start date"),

  SHIPPING_METHOD_NOT_FOUND("Shipping method not found"),
  SHIPPING_METHOD_NAME_EXISTS("Shipping method name already exists"),
  SHIPPING_METHOD_CODE_EXISTS("Shipping method code already exists"),
  SHIPPING_METHOD_INACTIVE("Shipping method is not active"),

  ORDER_NOT_FOUND("Order not found"),
  ORDER_ACCESS_DENIED("You do not have permission to access this order"),
  ORDER_CANNOT_CANCEL("Order can only be canceled when in PENDING or PAID status"),
  ORDER_CANNOT_DELETE("Order can only be deleted when in CANCELED status"),
  ORDER_INVALID_STATUS_TRANSITION("Invalid order status transition"),
  ORDER_STATUS_CANNOT_CHANGE_COMPLETED("Cannot change status of completed orders"),
  ORDER_CANNOT_CONFIRM_RECEIVED("Can only confirm orders in SHIPPING status"),
  ORDER_CANNOT_CHANGE_SHIPPING_STATUS("Cannot change SHIPPING status. User must confirm order received"),

  // Message in dto - Start
  FULL_NAME_REQUIRED("Full name is required"),
  DOB_INVALID("Your age must be at least 6"),
  PHONE_NUMBER_INVALID("Invalid phone number (must be in format 0xxxxxxxxx or +84xxxxxxxxx)"),
  CCCD_INVALID("CCCD must include exactly 12 digits"),
  DUPLICATE_CCCD("CCCD already exists in the system"),
  PATIENT_ID_LIST_CANNOT_EMPTY("Patient ID list cannot be empty"),
  PASSWORD_REQUIRED("Password is required"),
  PASSWORD_INVALID("Password must be at least 8 characters"),
  EMAIL_REQUIRED("Email is required"),
  EMAIL_INVALID("Invalid email address"),
  USER_NOT_FOUND("User not found"),
  ROLE_NAME_REQUIRED("Role name is required"),
  PERMISSION_NAME_REQUIRED("Permission name is required"),
  LIST_ROLE_NAMES_EMPTY("List roleNames cannot be empty"),
  LIST_PERMISSION_NAMES_EMPTY("List permissionNames cannot be empty"),
  TOKEN_REQUIRED("Token is required"),

  RECEIVER_NAME_REQUIRED("Receiver name is required"),
  RECEIVER_PHONE_REQUIRED("Receiver phone is required"),
  PROVINCE_REQUIRED("Province is required"),
  DISTRICT_REQUIRED("District is required"),
  WARD_REQUIRED("Ward is required"),
  STREET_DETAIL_REQUIRED("Street detail is required"),

  VOUCHER_CODE_REQUIRED("Voucher code is required"),
  VOUCHER_CODE_TOO_LONG("Voucher code must not exceed 50 characters"),
  VOUCHER_NAME_REQUIRED("Voucher name is required"),
  VOUCHER_NAME_TOO_LONG("Voucher name must not exceed 255 characters"),
  VOUCHER_DISCOUNT_TYPE_REQUIRED("Discount type is required"),
  VOUCHER_DISCOUNT_VALUE_REQUIRED("Discount value is required"),
  VOUCHER_DISCOUNT_VALUE_INVALID("Discount value must be greater than 0"),
  VOUCHER_MIN_ORDER_VALUE_INVALID("Minimum order value must be greater than or equal to 0"),
  VOUCHER_MAX_DISCOUNT_INVALID("Maximum discount amount must be greater than or equal to 0"),
  VOUCHER_START_DATE_REQUIRED("Start date is required"),
  VOUCHER_START_DATE_FUTURE("Start date must be in the future"),
  VOUCHER_END_DATE_REQUIRED("End date is required"),
  VOUCHER_END_DATE_FUTURE("End date must be in the future"),
  VOUCHER_USAGE_LIMIT_INVALID("Usage limit must be greater than 0"),
  VOUCHER_USAGE_PER_USER_INVALID("Usage per user must be greater than 0"),
  VARIANT_ID_REQUIRED("Variant ID is required"),
  QUANTITY_REQUIRED("Quantity is required"),
  QUANTITY_INVALID("Quantity must be greater than 0"),

  SHIPPING_METHOD_NAME_REQUIRED("Shipping method name is required"),
  SHIPPING_METHOD_NAME_TOO_LONG("Shipping method name must not exceed 100 characters"),
  SHIPPING_METHOD_CODE_REQUIRED("Shipping method code is required"),
  SHIPPING_METHOD_CODE_TOO_LONG("Shipping method code must not exceed 50 characters"),
  SHIPPING_METHOD_BASE_FEE_REQUIRED("Base fee is required"),
  SHIPPING_METHOD_BASE_FEE_INVALID("Base fee must be greater than or equal to 0"),
  SHIPPING_METHOD_FEE_PER_KM_INVALID("Fee per km must be greater than or equal to 0"),
  SHIPPING_METHOD_ESTIMATED_DAYS_INVALID("Estimated days must be at least 1"),

  ORDER_ADDRESS_ID_REQUIRED("Shipping address ID is required"),
  ORDER_SHIPPING_METHOD_REQUIRED("Shipping method ID is required"),
  ORDER_NOTE_TOO_LONG("Note must not exceed 500 characters"),
  ORDER_ITEMS_REQUIRED("Order must have at least 1 item"),
  ORDER_CANCEL_REASON_REQUIRED("Cancel reason is required"),
  ORDER_CANCEL_REASON_INVALID("Cancel reason must be between 10 and 500 characters"),
  ORDER_STATUS_REQUIRED("Order status is required"),

  ORDER_ID_REQUIRED("Order ID is required"),

  REVIEW_NOT_FOUND("Review not found"),
  REVIEW_ALREADY_EXISTS("You have already reviewed this product"),
  REVIEW_ACCESS_DENIED("You do not have permission to access this review"),
  REVIEW_RATING_REQUIRED("Rating is required"),
  REVIEW_RATING_INVALID("Rating must be between 1 and 5"),
  REVIEW_COMMENT_TOO_LONG("Comment must not exceed 2000 characters"),
  REVIEW_CANNOT_REVIEW_OWN_PRODUCT("You cannot review your own product"),
  REVIEW_PRODUCT_ID_REQUIRED("Product ID is required"),
  REVIEW_VARIANT_ID_REQUIRED("Variant ID is required"),
  PAYMENT_NOT_FOUND("Payment not found"),
  PAYMENT_METHOD_NOT_FOUND("Payment method not found"),
  PAYMENT_METHOD_INACTIVE("Payment method is not active"),
  PAYMENT_ALREADY_COMPLETED("Payment has already been completed"),
  PAYMENT_FAILED("Payment failed"),
  PAYMENT_CANCELLED("Payment has been cancelled"),
  PAYMENT_EXPIRED("Payment URL has expired"),
  PAYMENT_TRANSACTION_INVALID("Invalid payment transaction"),
  PAYMENT_AMOUNT_MISMATCH("Payment amount does not match order total"),
  PAYMENT_METHOD_ID_REQUIRED("Payment method ID is required"),
  VNPAY_CONFIG_MISSING("VNPay configuration is missing"),
  VNPAY_PAYMENT_URL_ERROR("Error generating VNPay payment URL"),
  VNPAY_SIGNATURE_INVALID("Invalid VNPay signature"),
  VNPAY_RETURN_URL_INVALID("Invalid VNPay return URL"),

  // Message in dto - End
  ;

  private final String message;

  ErrorMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
