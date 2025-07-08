package com.auction.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message="Email is Required")
    @Email(message="Invalid email format")
    private String email;

    @NotBlank(message="Password is Required")
    private String password;

    @NotBlank(message="Username is Required")
    private String username;

    @NotBlank(message="FullName is Required")
    private String fullName;

    @NotBlank(message="Address is Required")
    private String address;

    private String phoneNumber;

}
