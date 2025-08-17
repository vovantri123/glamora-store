package com.glamora_store.util;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.glamora_store.enums.ErrorCode;

public class ExceptionUtil {

    private ExceptionUtil() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static ResponseStatusException badRequest(ErrorCode errorCode) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, format(errorCode));
    }

    public static ResponseStatusException notFound(ErrorCode errorCode) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, format(errorCode));
    }

    public static ResponseStatusException forbidden(ErrorCode errorCode) {
        return new ResponseStatusException(HttpStatus.FORBIDDEN, format(errorCode));
    }

    // with: creates an exception with the following details (details can be one or multiple error
    // messages)
    public static ResponseStatusException with(HttpStatus status, ErrorCode errorCode, List<String> details) {
        return new ResponseStatusException(status, format(errorCode) + ": [" + String.join(", ", details) + "]");
    }

    public static ResponseStatusException with(HttpStatus status, ErrorCode errorCode, String details) {
        return new ResponseStatusException(status, format(errorCode) + ": [" + details + "]");
    }

    public static ResponseStatusException with(HttpStatus status, ErrorCode errorCode) {
        return new ResponseStatusException(status, format(errorCode));
    }

    private static String format(ErrorCode errorCode) {
        return errorCode.getCode() + "|" + errorCode.getMessage();
    }
}
