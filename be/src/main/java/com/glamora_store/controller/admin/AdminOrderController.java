package com.glamora_store.controller.admin;

import com.glamora_store.dto.request.admin.order.UpdateOrderStatusRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.dto.response.common.order.OrderResponse;
import com.glamora_store.enums.OrderStatus;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
@Tag(name = "Orders - Admin", description = "Admin endpoints for managing all orders")
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get all orders or search orders", description = "Get paginated list of all orders or search orders by status, user ID, order code, user email, or user full name. When no search parameters are provided, returns all orders.")
    public ApiResponse<PageResponse<OrderResponse>> searchOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String orderCode,
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) String userFullName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return new ApiResponse<>(
                SuccessMessage.SEARCH_ORDER_SUCCESS.getMessage(),
                orderService.searchOrders(status, userId, orderCode, userEmail, userFullName, pageable));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID", description = "Get detailed information of a specific order by ID")
    public ApiResponse<OrderResponse> getOrderById(@PathVariable Long orderId) {
        return new ApiResponse<>(
                SuccessMessage.GET_ORDER_SUCCESS.getMessage(),
                orderService.getOrderById(orderId));
    }

    @GetMapping("/code/{orderCode}")
    @Operation(summary = "Get order by code", description = "Get detailed information of a specific order by order code")
    public ApiResponse<OrderResponse> getOrderByCode(@PathVariable String orderCode) {
        return new ApiResponse<>(
                SuccessMessage.GET_ORDER_SUCCESS.getMessage(),
                orderService.getOrderByCode(orderCode));
    }

    @PutMapping("/{orderId}/status")
    @Operation(summary = "Update order status", description = "Update the status of an order (with validation for valid transitions)")
    public ApiResponse<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        return new ApiResponse<>(
                SuccessMessage.UPDATE_ORDER_SUCCESS.getMessage(),
                orderService.updateOrderStatus(orderId, request));
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete order", description = "Delete an order permanently (only CANCELED orders can be deleted)")
    public ApiResponse<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return new ApiResponse<>(SuccessMessage.DELETE_ORDER_SUCCESS.getMessage());
    }
}
