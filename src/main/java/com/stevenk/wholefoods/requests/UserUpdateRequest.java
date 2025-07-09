package com.stevenk.wholefoods.requests;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String firstname;
    private String lastname;
}
