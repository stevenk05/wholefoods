package com.stevenk.wholefoods.requests;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
