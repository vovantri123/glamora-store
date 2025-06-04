package com.glamora_store.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"), // để thông báo Exception này chưa phân loại (kiểu xử lý cái exception chung chung)
    USER_EXISTS(1001, "Username already exists"),
    NAME_REQUIRED(1001, "Name is required"),
    EMAIL_REQUIRED(1001, "Email is required"),
    EMAIL_INVALID(1001, "Invalid email address"),
    PASSWORD_REQUIRED(1001, "Password is required"),
    PASSWORD_INVALID(1001, "Password must be at least 3 characters"),

    INVALID_KEY(1000, "Invalid message key") // Dùng khi message chứa ErrorCode bị sai chính tả (trong @Size)
    ;

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
