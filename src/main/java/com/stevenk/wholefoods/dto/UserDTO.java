package com.stevenk.wholefoods.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private List<OrderDTO> orders;
    private CartDTO cart;
}
