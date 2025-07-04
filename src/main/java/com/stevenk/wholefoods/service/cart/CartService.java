package com.stevenk.wholefoods.service.cart;

import com.stevenk.wholefoods.model.Cart;
import com.stevenk.wholefoods.model.CartItem;
import com.stevenk.wholefoods.repository.CartItemRepository;
import com.stevenk.wholefoods.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{

    private final CartRepository cartRepo;
    private final CartItemRepository ciRepo;

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepo.findById(id).orElseThrow(()->new RuntimeException("Cart Not Found"));
        BigDecimal total = cart.getTotal();
        cart.setTotal(total);
        return cartRepo.save(cart);
    }

    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        ciRepo.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepo.deleteById(id);

    }

    @Override
    public BigDecimal getTotal(Long id) {
        Cart cart = getCart(id);
        return cart.getItems().stream()
                .map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Long initializeCart() {
        Cart cart = new Cart();
        Cart saved = cartRepo.save(cart);
        return saved.getId();
    }
}
