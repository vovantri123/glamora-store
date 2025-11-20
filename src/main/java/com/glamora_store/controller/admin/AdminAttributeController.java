package com.glamora_store.controller.admin;

import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.AttributeResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.AttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/attributes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAttributeController {

    private final AttributeService attributeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AttributeResponse>>> getAllAttributes() {
        List<AttributeResponse> attributes = attributeService.getAllAttributes();
        return ResponseEntity.ok(new ApiResponse<>(SuccessMessage.GET_ALL_ATTRIBUTE_SUCCESS.getMessage(), attributes));
    }
}
