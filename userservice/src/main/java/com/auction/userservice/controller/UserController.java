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

import java.util.Map;
import java.util.HashMap;

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
            logger.fatal("Error in register :" + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // NEW: Login endpoint for frontend integration
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest){
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            // TODO: Implement actual authentication logic
            // For now, this is a basic implementation
            UserDto user = userService.authenticateUser(username, password);

            if (user != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("token", "dummy-jwt-token-" + user.getId()); // Replace with actual JWT
                response.put("user", user);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } catch (Exception e) {
            logger.fatal("Error in login :" + e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // NEW: Update user profile endpoint
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUserProfile(@PathVariable String userId, @RequestBody UserDto userDto){
        try {
            UserDto updatedUser = userService.updateUserProfile(userId, userDto);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            logger.fatal("Error in updateUserProfile :" + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
