package com.stevenk.wholefoods.service.cart;

import com.stevenk.wholefoods.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotal(Long id);

    Long initializeCart();
}
