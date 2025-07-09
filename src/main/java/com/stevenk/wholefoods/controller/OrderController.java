package com.stevenk.wholefoods.controller;

import com.stevenk.wholefoods.dto.OrderDTO;
import com.stevenk.wholefoods.exceptions.ResourceNotFoundException;
import com.stevenk.wholefoods.model.Order;
import com.stevenk.wholefoods.response.ApiResponse;
import com.stevenk.wholefoods.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<ApiResponse> makeOrder(@RequestParam Long userId) {
        try {
            Order order = orderService.placeOrder(userId);
            OrderDTO orderDTO = orderService.convertToDTO(order);
            return ResponseEntity.ok(new ApiResponse("Order placed successfully", orderDTO));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Error placing order: " + e.getMessage(), null));
        }
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long id) {
        try {
            OrderDTO order = orderService.getOrder(id);
            return ResponseEntity.ok(new ApiResponse("Found.", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error retrieving order: " + e.getMessage(), null));
        }
    }

    @GetMapping("/byUserId/{id}")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long id) {
        try {
            List<OrderDTO> orders = orderService.getUserOrders(id);
            return ResponseEntity.ok(new ApiResponse("Found.", orders));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error retrieving order: " + e.getMessage(), null));
        }
    }


}