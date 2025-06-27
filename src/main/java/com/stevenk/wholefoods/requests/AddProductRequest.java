package com.stevenk.wholefoods.requests;

import com.stevenk.wholefoods.model.Category;
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
