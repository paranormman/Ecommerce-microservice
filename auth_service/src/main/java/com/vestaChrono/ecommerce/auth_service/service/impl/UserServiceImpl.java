package com.vestaChrono.ecommerce.auth_service.service.impl;

import com.vestaChrono.ecommerce.auth_service.dto.SignUpDto;
import com.vestaChrono.ecommerce.auth_service.dto.UserDto;
import com.vestaChrono.ecommerce.auth_service.entity.User;
import com.vestaChrono.ecommerce.auth_service.exception.ResourceNotFoundException;
import com.vestaChrono.ecommerce.auth_service.repository.UserRepository;
import com.vestaChrono.ecommerce.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with email "+userId+" is not found"));
    }

    @Override
    public UserDto signUp(SignUpDto signUpDto) {

        Optional<User> user = userRepository.findByEmail(signUpDto.getEmail());
        if (user.isPresent()){
            throw new BadCredentialsException("User with Email already exists "+ signUpDto.getEmail());
        }

        User toBeCreatedUser = modelMapper.map(signUpDto, User.class);
        toBeCreatedUser.setPassword(passwordEncoder.encode(toBeCreatedUser.getPassword()));

        User savedUser = userRepository.save(toBeCreatedUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: "+email));
    }

    @Override
    public User save(User newUser) {
        return userRepository.save(newUser);
    }

}
