package com.glamora_store.exception;

import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.enums.ErrorCode;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  // Handle ResponseStatusException with "code|message" format
  // Flexibly throw HTTP errors (400, 404, 403, 500...) in REST APIs without creating many custom
  // exception classes
  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiResponse<Void>> handleResponseStatusException(
    ResponseStatusException ex) {
    String reason = ex.getReason();
    int code = ErrorCode.UNCATEGORIZED_EXCEPTION.getCode();
    String message = reason;

    if (reason != null && reason.contains("|")) {
      String[] parts = reason.split("\\|", 2);
      try {
        code = Integer.parseInt(parts[0]);
        message = parts[1];
      } catch (NumberFormatException e) {
        log.warn("Invalid error code format: {}", reason);
      }
    }

    return ResponseEntity.status(ex.getStatusCode()).body(new ApiResponse<>(code, message));
  }

  /*
     The handler above catch exceptions manually thrown in the code.
     The remaining handlers catch exceptions automatically thrown by Spring for invalid requests.
  */

  @ExceptionHandler(
    MethodArgumentNotValidException.class) // @NotBlank, @NotEmpty, @Pattern, @Size,... in dto
  public ApiResponse<String> handleValidationErrors(MethodArgumentNotValidException ex) {
    List<String> messages = new ArrayList<>();

    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      String key = error.getDefaultMessage(); // expects enum name like "CCCD_INVALID", etc.

      try {
        String message = ErrorCode.valueOf(key).getMessage();
        messages.add(message);
      } catch (IllegalArgumentException e) {
        return new ApiResponse<>(
          ErrorCode.INVALID_MESSAGE_KEY.getCode(),
          ErrorCode.INVALID_MESSAGE_KEY.getMessage() + ": " + key);
      }
    }

    String combinedMessage = String.join("; ", messages) + ";";

    return new ApiResponse<>(
      ErrorCode.VALIDATION_FAILED.getCode(),
      ErrorCode.VALIDATION_FAILED.getMessage() + ": [" + combinedMessage + "]");
  }

  @ExceptionHandler(
    ConstraintViolationException.class) // @NotBlank, @NotEmpty, @Pattern, @Size,... in Entity
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResponse<String> handleConstraintViolation(ConstraintViolationException ex) {
    List<String> messages = new ArrayList<>();

    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      String key =
        violation
          .getMessage(); // expected to be enum name like "PHONE_INVALID", "EMAIL_INVALID", etc.

      try {
        String message = ErrorCode.valueOf(key).getMessage();
        messages.add(message);
      } catch (IllegalArgumentException e) {
        return new ApiResponse<>(
          ErrorCode.INVALID_MESSAGE_KEY.getCode(),
          ErrorCode.INVALID_MESSAGE_KEY.getMessage() + ": " + key);
      }
    }

    String combinedMessage = String.join("; ", messages) + ";";

    return new ApiResponse<>(
      ErrorCode.VALIDATION_FAILED.getCode(),
      ErrorCode.VALIDATION_FAILED.getMessage() + ": [" + combinedMessage + "]");
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<Void> handleUniqueConstraintViolation(DataIntegrityViolationException ex) {
    Throwable rootCause = ex.getRootCause();
    String message = rootCause != null ? rootCause.getMessage() : ex.getMessage();
    ErrorCode errorCode = ErrorCode.CONSTRAINT_VIOLATION;
    return new ApiResponse<>(errorCode.getCode(), errorCode.getMessage() + message);
  }

  // Handle type mismatch in @PathVariable or @RequestParam
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<Void> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    ErrorCode errorCode = ErrorCode.INVALID_PARAMETER_TYPE;
    return new ApiResponse<>(errorCode.getCode(), errorCode.getMessage() + ": " + ex.getMessage());
  }

  // Handle invalid or unreadable request body (e.g., malformed JSON)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<Void> jsonParseErrorHandler(HttpMessageNotReadableException ex) {
    ErrorCode errorCode = ErrorCode.MALFORMED_JSON;
    return new ApiResponse<>(errorCode.getCode(), errorCode.getMessage() + ex.getMessage());
  }

  // Handle wrong URL paths that look like static resources (e.g., /api/v1/patients/8/234)
  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiResponse<Void> handleNoResourceFound(NoResourceFoundException ex) {
    ErrorCode errorCode = ErrorCode.URL_NOT_FOUND;
    return new ApiResponse<>(errorCode.getCode(), errorCode.getMessage() + ": " + ex.getMessage());
  }

  @ExceptionHandler(JOSEException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResponse<Void> handleJoseException(JOSEException ex) {
    ErrorCode errorCode = ErrorCode.CANNOT_CREATE_TOKEN;
    return new ApiResponse<>(errorCode.getCode(), errorCode.getMessage() + ": " + ex.getMessage());
  }

  @ExceptionHandler(ParseException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<Void> handleParseException(ParseException ex) {
    ErrorCode errorCode = ErrorCode.INVALID_TOKEN_FORMAT;
    return new ApiResponse<>(errorCode.getCode(), errorCode.getMessage() + ": " + ex.getMessage());
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ApiResponse<Void> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
    ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
    return new ApiResponse<>(errorCode.getCode(), errorCode.getMessage());
  }

  // Handle all other uncaught exceptions (fallback)
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResponse<Void> genericExceptionHandler(Exception ex) {
    log.error("Uncategorized Exception: ", ex);
    ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
    return new ApiResponse<>(errorCode.getCode(), errorCode.getMessage());
  }


}
