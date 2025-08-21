package com.glamora_store.util;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.glamora_store.enums.ErrorMessage;

public class ExceptionUtil {

    private ExceptionUtil() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static ResponseStatusException badRequest(ErrorMessage errorMessage) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage.getMessage());
    }

    public static ResponseStatusException notFound(ErrorMessage errorMessage) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage.getMessage());
    }

    public static ResponseStatusException forbidden(ErrorMessage errorMessage) {
        return new ResponseStatusException(HttpStatus.FORBIDDEN, errorMessage.getMessage());
    }

    public static ResponseStatusException with(HttpStatus status, ErrorMessage errorMessage, List<String> details) {
        return new ResponseStatusException(
                status, errorMessage.getMessage() + ": [" + String.join(", ", details) + "]");
    }

    public static ResponseStatusException with(HttpStatus status, ErrorMessage errorMessage, String details) {
        return new ResponseStatusException(status, errorMessage.getMessage() + ": [" + details + "]");
    }

    public static ResponseStatusException with(HttpStatus status, ErrorMessage errorMessage) {
        return new ResponseStatusException(status, errorMessage.getMessage());
    }
}
