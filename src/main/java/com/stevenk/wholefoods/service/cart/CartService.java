package com.stevenk.wholefoods.service.cart;

import com.stevenk.wholefoods.dto.CartDTO;
import com.stevenk.wholefoods.exceptions.AlreadyExistsException;
import com.stevenk.wholefoods.exceptions.ResourceNotFoundException;
import com.stevenk.wholefoods.model.Cart;
import com.stevenk.wholefoods.model.CartItem;
import com.stevenk.wholefoods.model.User;
import com.stevenk.wholefoods.repository.CartItemRepository;
import com.stevenk.wholefoods.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{

    private final CartRepository cartRepo;
    private final CartItemRepository ciRepo;
    private final ModelMapper mapper;
    private final AtomicLong cartId = new AtomicLong(0);

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Cart Not Found"));
        BigDecimal total = cart.getTotal();
        cart.setTotal(total);
        return cartRepo.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        ciRepo.deleteAllByCartId(id);
        cart.getItems().clear();
        cart.setTotal(BigDecimal.ZERO);
        cartRepo.deleteById(id);

    }

    @Override
    public BigDecimal getTotal(Long id) {
        Cart cart = getCart(id);
        return cart.getItems().stream()
                .map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Cart initializeCart(User user) {
        if (cartRepo.existsByUserId(user.getId())) {
            throw new AlreadyExistsException("Cart already exists for user with ID: " + user.getId());
        }

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new HashSet<>());
        cart.setTotal(BigDecimal.ZERO);
        return cartRepo.save(cart);
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepo.findByUserId(userId);
    }

    public CartDTO convertToCartDTO(Cart cart) {
        return mapper.map(cart, CartDTO.class);
    }
}