package com.auction.userservice.dao;

import com.auction.userservice.dto.UserDto;
import com.auction.userservice.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public interface UserDAO {
    UserDto findUserById(String userId) throws RuntimeException ;

    UserDto registerUser(User user);

    boolean existsByEmail(@NotBlank(message="Email is Required") @Email(message="Invalid email format") String email);
}
