package com.stevenk.wholefoods.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private String status;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private List<OrderItemDTO> items;
}
