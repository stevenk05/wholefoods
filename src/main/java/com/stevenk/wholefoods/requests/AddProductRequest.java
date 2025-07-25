package com.stevenk.wholefoods.requests;

import lombok.Data;

@Data
public class AddProductRequest {
    private Long id;
    private String name;
    private String brand;
    private int inventory;
    private double price;
    private String description;
    private String category;
}
