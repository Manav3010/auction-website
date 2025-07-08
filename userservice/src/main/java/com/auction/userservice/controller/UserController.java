package com.auction.userservice.controller;

import com.auction.userservice.constants.UserConstants;
import com.auction.userservice.dto.RegisterRequest;
import com.auction.userservice.dto.UserDto;
import com.auction.userservice.service.UserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    @Qualifier(UserConstants.USERSERVICEIMPL)
    private UserService userService;

    private static final Logger logger = LogManager.getLogger(UserController.class);


    //Endpoints
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable String userId){
        try{
            return ResponseEntity.ok(userService.getUserProfile(userId));
        }
        catch (Exception e){
            logger.fatal("Error in getUserProfile :"+e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid  @RequestBody RegisterRequest request){
        try {
            return ResponseEntity.ok(userService.registerUser(request));
        } catch (Exception e) {
            logger.fatal("Error in getUserProfile :" + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


}
