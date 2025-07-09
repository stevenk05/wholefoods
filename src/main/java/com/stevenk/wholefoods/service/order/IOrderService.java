package com.stevenk.wholefoods.service.order;

import com.stevenk.wholefoods.dto.OrderDTO;
import com.stevenk.wholefoods.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder (Long userId);
    OrderDTO getOrder(Long orderId);

    List<OrderDTO> getUserOrders(Long userId);

    OrderDTO convertToDTO(Order order);
}
