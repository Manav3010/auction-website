package com.auction.userservice.service;

import com.auction.userservice.constants.UserConstants;
import com.auction.userservice.dto.RegisterRequest;
import com.auction.userservice.dto.UserDto;
import org.springframework.stereotype.Service;


public interface UserService {
    UserDto getUserProfile(String userId);

    UserDto registerUser(RegisterRequest request);
}
