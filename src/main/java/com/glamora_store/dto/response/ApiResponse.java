package com.glamora_store.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // DELETE, ERROR
    public ApiResponse(String message) {
        this.message = message;
    }

    // GET, PUT, POST,
    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
