package com.glamora_store.enums;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    UNCATEGORIZED_EXCEPTION(
            "Uncategorized error"), // để thông báo Exception này chưa phân loại (kiểu xử lý cái exception chung chung)
    INVALID_KEY("Invalid message key"), // Dùng khi message chứa ErrorCode bị sai chính tả (trong @Size)
    INVALID_PARAMETER_TYPE("Invalid value for parameter"),
    MALFORMED_JSON("The JSON in your request is invalid. Please check the syntax."),
    URL_NOT_FOUND("Requested URL is not mapped"),
    INVALID_MESSAGE_KEY("Invalid message key"),
    CONSTRAINT_VIOLATION("Constraint violation in database"),
    VALIDATION_FAILED("Validation failed"),

    PATIENT_NOT_FOUND("Patient not found"),
    LIST_PATIENT_ID_NOT_FOUND("Some patient IDs were not found"),
    CANNOT_UPDATE_DELETED_PATIENT("Cannot update a deleted patient"),
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
    LIST_ROLE_NAMES_EMPTY("List roleNames cannot be empty"),
    USER_EXISTED("User already existed"),
    SEND_EMAIL_FAIL("Failed to send OTP email"),
    OLD_PASSWORD_INCORRECT("Old password is incorrect"),

    // Cẩn thận xóa nhầm - Start (Chủ yếu lấy message detail, còn code với default message thì lấy của
    // VALIDATION_FAILED
    FULL_NAME_REQUIRED("Full name is required"),
    DOB_INVALID("Your age must be at least 18"),
    PHONE_NUMBER_INVALID("Invalid phone number (must be in format 0xxxxxxxxx or +84xxxxxxxxx)"),
    CCCD_INVALID("CCCD must include exactly 12 digits"),
    DUPLICATE_CCCD("CCCD already exists in the system"),
    BHYT_INVALID("BHYT must be 10 digits (new) or 15 characters (old format)"),
    BHYT_DUPLICATE("BHYT already exists in the system"),
    PATIENT_ID_LIST_CANNOT_EMPTY("Patient ID list cannot be empty"),
    PASSWORD_REQUIRED("Password is required"),
    PASSWORD_INVALID("Password must be at least 8 characters"),
    EMAIL_REQUIRED("Email is required"),
    EMAIL_INVALID("Invalid email address"),
    USER_NOT_FOUND("User not found"),
// Cẩn thận xóa nhầm - End
;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
