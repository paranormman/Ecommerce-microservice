package com.vestaChrono.ecommerce.auth_service.service;

import com.vestaChrono.ecommerce.auth_service.entity.User;

public interface JwtService {

    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    Long getUserIdFromToken(String token);
}
