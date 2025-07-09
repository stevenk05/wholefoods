package com.stevenk.wholefoods.service.order;

import com.stevenk.wholefoods.dto.OrderDTO;
import com.stevenk.wholefoods.enumeration.OrderStatus;
import com.stevenk.wholefoods.exceptions.ResourceNotFoundException;
import com.stevenk.wholefoods.model.*;
import com.stevenk.wholefoods.repository.OrderRepository;
import com.stevenk.wholefoods.repository.ProductRepository;
import com.stevenk.wholefoods.service.cart.CartService;
import com.stevenk.wholefoods.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

    private final OrderRepository orderRepo;
    private final ProductRepository prodRepo;
    private final CartService cartServ;
    private final ModelMapper mapper;
    private final UserService userService;

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartServ.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItems));
        order.setTotalPrice(getTotal(orderItems));

        User user = userService.getUserById(userId);
        if (user.getOrders() == null) {
            user.setOrders(new ArrayList<>());
        }
        user.getOrders().add(order);


        Order saved = orderRepo.save(order);
        cartServ.clearCart(cart.getId());

        return saved;
    }

    private Order createOrder(Cart cart){
        Order order = new Order();

        order.setUser(cart.getUser());

        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getItems().stream().map( item -> {
                    Product product = item.getProduct();
                    product.setInventory(product.getInventory() - item.getQuantity());
                    prodRepo.save(product);
                    return new OrderItem(
                            item.getQuantity(), item.getUnitPrice(), order, product
                    );
                }
        ).toList();
    }

    private BigDecimal getTotal(List<OrderItem> itemList){
        return itemList.stream().map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @Override
    public OrderDTO getOrder(Long orderId) {
        return orderRepo.findById(orderId)
                .map(this :: convertToDTO)
                .orElseThrow(()-> new ResourceNotFoundException("Order Not Found"));
    }

    @Override
    public List<OrderDTO> getUserOrders(Long userId) {
        List<Order> orders = orderRepo.findByUserId(userId);
        return orders.stream().map(this::convertToDTO).toList();
    }

    public OrderDTO convertToDTO(Order order) {
        return mapper.map(order, OrderDTO.class);
    }
}
