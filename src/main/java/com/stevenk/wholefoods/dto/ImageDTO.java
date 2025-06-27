package com.stevenk.wholefoods.dto;

import lombok.Data;

@Data
public class ImageDTO {
    private Long id;
    private String imageName;
    private String downloadURL;
}
