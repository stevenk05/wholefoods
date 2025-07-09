package com.stevenk.wholefoods.controller;

import com.stevenk.wholefoods.dto.CartDTO;
import com.stevenk.wholefoods.exceptions.ResourceNotFoundException;
import com.stevenk.wholefoods.model.Cart;
import com.stevenk.wholefoods.response.ApiResponse;
import com.stevenk.wholefoods.service.cart.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final CartService cartService;

    @GetMapping("/show/{id}")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long id) {
        try {
            CartDTO cart = cartService.convertToCartDTO(cartService.getCart(id));
            return ResponseEntity.ok(new ApiResponse("Found.", cart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Transactional
    @DeleteMapping("/clear/{id}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long id) {
        try {
            cartService.clearCart(id);
            return ResponseEntity.ok(new ApiResponse("Cart cleared.", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));        }
    }

    @GetMapping("/total/{id}")
    public ResponseEntity<ApiResponse> getTotal(@PathVariable Long id) {
        try {
            BigDecimal total = cartService.getTotal(id);
            return ResponseEntity.ok(new ApiResponse("Total calculated", total));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
