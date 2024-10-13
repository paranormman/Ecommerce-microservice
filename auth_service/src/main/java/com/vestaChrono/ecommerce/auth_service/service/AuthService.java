package com.vestaChrono.ecommerce.auth_service.service;

import com.vestaChrono.ecommerce.auth_service.dto.LoginDto;
import com.vestaChrono.ecommerce.auth_service.dto.LoginResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginDto loginDto);
    LoginResponseDto refreshToken(String refreshToken);
}
