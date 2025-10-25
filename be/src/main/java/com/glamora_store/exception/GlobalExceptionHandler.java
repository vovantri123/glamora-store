package com.glamora_store.exception;

import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.enums.ErrorMessage;
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
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  // Handle ResponseStatusException, flexibly throw HTTP errors (400, 404, 403, 500...) in REST APIs without
  // creating many custom exception classes

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiResponse<Void>> handleResponseStatusException(ResponseStatusException ex) {
    String reason = ex.getReason();

    // Nếu không có reason thì fallback về uncategorized
    String message = (reason != null) ? reason : ErrorMessage.UNCATEGORIZED_EXCEPTION.getMessage();

    return ResponseEntity.status(ex.getStatusCode()).body(new ApiResponse<>(message));
  }

    /*
       The handler above catch exceptions manually thrown in the code.
       The remaining handlers catch exceptions automatically thrown by Spring for invalid requests.
    */

  @ExceptionHandler(MethodArgumentNotValidException.class)
  // @NotBlank, @NotEmpty, @Pattern, @Size,... in dto
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<String> handleValidationErrors(MethodArgumentNotValidException ex) {
    List<String> messages = new ArrayList<>();

    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      String key = error.getDefaultMessage(); // expects enum name like "CCCD_INVALID", etc.

      try {
        String message = ErrorMessage.valueOf(key).getMessage();
        messages.add(message);
      } catch (IllegalArgumentException e) {
        return new ApiResponse<>(ErrorMessage.INVALID_MESSAGE_KEY.getMessage() + ": " + key);
      }
    }

    String combinedMessage = String.join("; ", messages) + ";";

    return new ApiResponse<>(ErrorMessage.VALIDATION_FAILED.getMessage() + ": [" + combinedMessage + "]");
  }

  @ExceptionHandler(ConstraintViolationException.class)
  // @NotBlank, @NotEmpty, @Pattern, @Size,... in Entity
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResponse<String> handleConstraintViolation(ConstraintViolationException ex) {
    List<String> messages = new ArrayList<>();

    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      String key = violation.getMessage(); // expected to be enum name like "PHONE_INVALID", "EMAIL_INVALID", etc.

      try {
        String message = ErrorMessage.valueOf(key).getMessage();
        messages.add(message);
      } catch (IllegalArgumentException e) {
        return new ApiResponse<>(ErrorMessage.INVALID_MESSAGE_KEY.getMessage() + ": " + key);
      }
    }

    String combinedMessage = String.join("; ", messages) + ";";

    return new ApiResponse<>(ErrorMessage.VALIDATION_FAILED.getMessage() + ": [" + combinedMessage + "]");
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<Void> handleUniqueConstraintViolation(DataIntegrityViolationException ex) {
    Throwable rootCause = ex.getRootCause();
    String message = rootCause != null ? rootCause.getMessage() : ex.getMessage();
    return new ApiResponse<>(ErrorMessage.CONSTRAINT_VIOLATION.getMessage() + message);
  }

  // Handle type mismatch in @PathVariable or @RequestParam
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<Void> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    return new ApiResponse<>(ErrorMessage.INVALID_PARAMETER_TYPE.getMessage() + ": " + ex.getMessage());
  }

  // Handle invalid or unreadable request body (e.g., malformed JSON)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<Void> jsonParseErrorHandler(HttpMessageNotReadableException ex) {
    return new ApiResponse<>(ErrorMessage.MALFORMED_JSON.getMessage() + ex.getMessage());
  }

  // Handle wrong URL paths that look like static resources (e.g., /patients/8/234)
  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiResponse<Void> handleNoResourceFound(NoResourceFoundException ex) {
    return new ApiResponse<>(ErrorMessage.URL_NOT_FOUND.getMessage() + ": " + ex.getMessage());
  }

  @ExceptionHandler(JOSEException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResponse<Void> handleJoseException(JOSEException ex) {
    return new ApiResponse<>(ErrorMessage.CANNOT_CREATE_TOKEN.getMessage() + ": " + ex.getMessage());
  }

  @ExceptionHandler(ParseException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<Void> handleParseException(ParseException ex) {
    return new ApiResponse<>(ErrorMessage.INVALID_TOKEN_FORMAT.getMessage() + ": " + ex.getMessage());
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ApiResponse<Void> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
    return new ApiResponse<>(ErrorMessage.ACCESS_DENIED.getMessage());
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
    return new ApiResponse<>(ex.getMessage());
  }

  // Handle unchecked exceptions (runtime errors that do not require `throws` declaration).
  // Ex: Catch checked and throw unckeck in OtpEmailServiceImpl
  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResponse<Void> handleRuntimeException(RuntimeException ex) {
    return new ApiResponse<>(ex.getMessage());
  }

  // Handle all other uncaught exceptions (fallback) – including checked or unexpected errors
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResponse<Void> handleGenericException(Exception ex) {
    log.error("Uncategorized Exception: ", ex); // log original error
    return new ApiResponse<>(ErrorMessage.UNCATEGORIZED_EXCEPTION.getMessage());
  }
}
