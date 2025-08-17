package com.glamora_store.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ApiResponse<T> {

    private Integer code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // DELETE, ERROR
    public ApiResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    // GET, PUT, POST,
    public ApiResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
