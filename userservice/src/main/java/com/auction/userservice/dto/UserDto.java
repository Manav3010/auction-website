package com.auction.userservice.dto;

import com.auction.userservice.model.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class UserDto {

    private String id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String address;
    private String phoneNumber;


}
