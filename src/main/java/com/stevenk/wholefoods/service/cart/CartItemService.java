package com.stevenk.wholefoods.service.cart;

import com.stevenk.wholefoods.dto.CartDTO;
import com.stevenk.wholefoods.exceptions.ProductNotFoundException;
import com.stevenk.wholefoods.model.Cart;
import com.stevenk.wholefoods.model.CartItem;
import com.stevenk.wholefoods.model.Product;
import com.stevenk.wholefoods.repository.CartItemRepository;
import com.stevenk.wholefoods.repository.CartRepository;
import com.stevenk.wholefoods.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{

    private final CartItemRepository ciRepo;
    private final IProductService productServ;
    private final ICartService cartServ;
    private final CartRepository cartRepo;

    @Override
    public void addItem(Long cartId, Long productId, int quantity) {
        Cart cart = cartServ.getCart(cartId);
        Product prod = productServ.getProductById(productId);
        
        if (cart.getItems() == null) {
            cart.setItems(new HashSet<>());
        }
        
        CartItem cartItem = cart.getItems().stream()
                .filter(citem -> citem.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());
        if (cartItem.getId() == null) {
            cartItem.setProduct(prod);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cartItem.setUnitPrice(BigDecimal.valueOf(prod.getPrice()));
        }
        else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        ciRepo.save(cartItem);
        cartRepo.save(cart);
    }

    @Override
    public void removeItem(Long cartId, Long productId) {
        Cart cart = cartServ.getCart(cartId);
        CartItem cartItem = getCartItem(cartId, productId);
        cart.removeItem(cartItem);
        cartRepo.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartServ.getCart(cartId);
        cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(BigDecimal.valueOf(item.getProduct().getPrice()));
                    item.setTotalPrice();
                    cartRepo.save(cart);
                });
        BigDecimal total = cart.getTotal();
        cart.setTotal(total);
        cartRepo.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartServ.getCart(cartId);
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(() -> new ProductNotFoundException("Product not found."));
    }
}