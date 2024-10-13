package com.vestaChrono.ecommerce.auth_service.service.impl;

import com.vestaChrono.ecommerce.auth_service.dto.LoginDto;
import com.vestaChrono.ecommerce.auth_service.dto.LoginResponseDto;
import com.vestaChrono.ecommerce.auth_service.entity.User;
import com.vestaChrono.ecommerce.auth_service.service.AuthService;
import com.vestaChrono.ecommerce.auth_service.service.JwtService;
import com.vestaChrono.ecommerce.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    public LoginResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponseDto(user.getId(), accessToken, refreshToken);
    }

    @Override
    public LoginResponseDto refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);

        User user = userService.getUserById(userId);
//        Generate fresh AT and RT pair and store new session
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponseDto(user.getId(), newAccessToken, newRefreshToken);
    }

}
