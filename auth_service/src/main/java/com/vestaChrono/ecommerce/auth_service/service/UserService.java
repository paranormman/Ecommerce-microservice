package com.vestaChrono.ecommerce.auth_service.service;


import com.vestaChrono.ecommerce.auth_service.dto.SignUpDto;
import com.vestaChrono.ecommerce.auth_service.dto.UserDto;
import com.vestaChrono.ecommerce.auth_service.entity.User;

public interface UserService {

    User getUserById(Long userId);
    UserDto signUp(SignUpDto signUpDto);
    User getUserByEmail(String email);
    User save(User newUser);

}
