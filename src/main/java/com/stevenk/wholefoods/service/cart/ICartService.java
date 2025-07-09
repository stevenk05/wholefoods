package com.stevenk.wholefoods.service.cart;

import com.stevenk.wholefoods.dto.CartDTO;
import com.stevenk.wholefoods.model.Cart;
import com.stevenk.wholefoods.model.User;

import java.math.BigDecimal;
import java.util.Optional;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotal(Long id);

    Cart initializeCart(User user);

    Cart getCartByUserId(Long userId);

}
