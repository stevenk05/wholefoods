package com.stevenk.wholefoods.service.cart;

import com.stevenk.wholefoods.model.CartItem;

public interface ICartItemService {
    void addItem(Long cartId, Long productId, int quantity);
    void removeItem(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItem(Long cartId, Long productId);
}
