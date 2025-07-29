package com.glamora_store.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
  private int code = 1000; // Tự quy định, mặc định 1000 là thành công
  private String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T data;

  // DELETE, ERROR
  public ApiResponse(int code, String message) {
    this.code = code;
    this.message = message;
  }

  // GET, PUT, POST,
  public ApiResponse(int code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }
}
