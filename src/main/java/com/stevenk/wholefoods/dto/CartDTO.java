package com.stevenk.wholefoods.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
public class CartDTO {
    private Long id;
    private Set<CartItemDTO> items = new HashSet<>();
    private BigDecimal total;
    private Long userId;
}
