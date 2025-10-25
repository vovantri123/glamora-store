package com.glamora_store.util;

import com.glamora_store.enums.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class ExceptionUtil {

  private ExceptionUtil() {
    throw new UnsupportedOperationException(ErrorMessage.UTILITY_CLASS_SHOULD_NOT_BE_INSTANTIATED.getMessage());
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
