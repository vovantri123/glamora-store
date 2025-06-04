package com.glamora_store.exception;

import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> runtimeExceptionHandler(RuntimeException e) { // Chỗ này phải có thêm ResponseEntity ở ngoài bao lấy ApiResponse là để tùy chỉnh cái HttpStatusCode, còn trong controller không cần nên mới thấy chỉ có mỗi ApiResponse
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String enumKey = e.getFieldError().getDefaultMessage(); // Thay vì truyền message lỗi trong @Size thì ta truyền message là Key của enum (HAY), xem trong class UserCreationRequest
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try {
            errorCode = ErrorCode.valueOf(enumKey); // valueOf lấy giá trị của key trong enum
        } catch (IllegalArgumentException exception) {
            log.error(exception.getMessage());
        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(apiResponse);
    }
}
