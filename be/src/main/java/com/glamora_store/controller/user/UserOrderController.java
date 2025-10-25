package com.glamora_store.controller.user;

import com.glamora_store.dto.request.user.order.CancelOrderRequest;
import com.glamora_store.dto.request.user.order.CreateOrderRequest;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/orders")
@RequiredArgsConstructor
@Tag(name = "Orders - User", description = "User endpoints for managing their orders")
public class UserOrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create new order", description = "Create a new order from cart or custom items")
    public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return new ApiResponse<>(
                SuccessMessage.CREATE_ORDER_SUCCESS.getMessage(),
                orderService.createOrder(request));
    }

    @GetMapping
    @Operation(summary = "Get my orders or search by status", description = "Get paginated list of current user's orders. When status is provided, filters orders by that status.")
    public ApiResponse<PageResponse<OrderResponse>> getMyOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return new ApiResponse<>(
                SuccessMessage.GET_ORDER_SUCCESS.getMessage(),
                orderService.searchMyOrders(status, pageable));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get my order by ID", description = "Get detailed information of a specific order by ID")
    public ApiResponse<OrderResponse> getMyOrderById(@PathVariable Long orderId) {
        return new ApiResponse<>(
                SuccessMessage.GET_ORDER_SUCCESS.getMessage(),
                orderService.getMyOrderById(orderId));
    }

    @GetMapping("/code/{orderCode}")
    @Operation(summary = "Get my order by code", description = "Get detailed information of a specific order by order code")
    public ApiResponse<OrderResponse> getMyOrderByCode(@PathVariable String orderCode) {
        return new ApiResponse<>(
                SuccessMessage.GET_ORDER_SUCCESS.getMessage(),
                orderService.getMyOrderByCode(orderCode));
    }

    @PutMapping("/{orderId}/cancel")
    @Operation(summary = "Cancel my order", description = "Cancel an order (only PENDING orders can be canceled)")
    public ApiResponse<OrderResponse> cancelMyOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody CancelOrderRequest request) {
        return new ApiResponse<>(
                SuccessMessage.CANCEL_ORDER_SUCCESS.getMessage(),
                orderService.cancelMyOrder(orderId, request));
    }

    @PutMapping("/{orderId}/confirm-received")
    @Operation(summary = "Confirm order received", description = "Confirm that the order has been received (only SHIPPING orders can be confirmed)")
    public ApiResponse<OrderResponse> confirmOrderReceived(@PathVariable Long orderId) {
        return new ApiResponse<>(
                SuccessMessage.CONFIRM_ORDER_RECEIVED_SUCCESS.getMessage(),
                orderService.confirmOrderReceived(orderId));
    }
}
