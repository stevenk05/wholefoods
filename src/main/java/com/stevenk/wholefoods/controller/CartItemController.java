package com.stevenk.wholefoods.controller;

import com.stevenk.wholefoods.exceptions.ProductNotFoundException;
import com.stevenk.wholefoods.exceptions.ResourceNotFoundException;
import com.stevenk.wholefoods.model.Cart;
import com.stevenk.wholefoods.model.User;
import com.stevenk.wholefoods.repository.CartRepository;
import com.stevenk.wholefoods.response.ApiResponse;
import com.stevenk.wholefoods.service.cart.ICartItemService;
import com.stevenk.wholefoods.service.cart.ICartService;
import com.stevenk.wholefoods.service.user.IUserService;
import com.stevenk.wholefoods.service.user.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

    private final ICartItemService ciService;
    private final ICartService cartService;
    private final CartRepository cartRepo;
    private final UserService userService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addCartItem(@RequestParam(required = false) Long cartId,
                                                   @RequestParam Long prodId,
                                                   @RequestParam Integer quantity) {
        try {
            User user = userService.getAuthenticatedUser();
            Cart cart = cartService.getCartByUserId(user.getId());
            if (cart == null) {
                cart = cartService.initializeCart(user);
            }

            Long finalCartId = cartId != null ? cartId : cart.getId();

            ciService.addItem(finalCartId | cart.getId(), prodId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item added to cart", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (JwtException e){
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Transactional
    @DeleteMapping("/item/remove/{cartId}/{itemId}")
    public ResponseEntity<ApiResponse> removeCartItem(@PathVariable Long itemId, @PathVariable Long cartId) {
        try {
            ciService.removeItem(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("Item removed from cart", null));
        } catch (ResourceNotFoundException | ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/item/update/{cartID}/{prodID}/{quantity}")
    public ResponseEntity<ApiResponse> updateQuantity(@PathVariable Integer quantity,
                                                      @PathVariable Long cartID,
                                                      @PathVariable Long prodID){
        try {
            ciService.updateItemQuantity(cartID, prodID, quantity);
            
            Cart cart = cartRepo.findById(cartID)
                    .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
            cart.updateTotal();
            cartRepo.save(cart);
            
            return ResponseEntity.ok(new ApiResponse("Item quantity updated", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


}