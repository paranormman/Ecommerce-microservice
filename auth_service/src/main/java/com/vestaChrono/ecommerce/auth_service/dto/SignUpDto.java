package com.vestaChrono.ecommerce.auth_service.dto;

import lombok.Data;

@Data
public class SignUpDto {

    private String email;
    private String password;
    private String name;
}
