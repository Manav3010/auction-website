package com.auction.userservice.service;

import com.auction.userservice.constants.UserConstants;
import com.auction.userservice.dto.RegisterRequest;
import com.auction.userservice.dto.UserDto;
import org.springframework.stereotype.Service;


public interface UserService {
    UserDto getUserProfile(String userId);

    UserDto registerUser(RegisterRequest request);

    // NEW: Authentication method for login
    UserDto authenticateUser(String username, String password);

    // NEW: Update user profile method
    UserDto updateUserProfile(String userId, UserDto userDto);
}
