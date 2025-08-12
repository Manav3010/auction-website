package com.auction.userservice.service.impl;

import com.auction.userservice.constants.UserConstants;
import com.auction.userservice.dao.UserDAO;
import com.auction.userservice.dto.RegisterRequest;
import com.auction.userservice.dto.UserDto;
import com.auction.userservice.model.User;
import com.auction.userservice.rowmapper.UserRowMapper;
import com.auction.userservice.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service(UserConstants.USERSERVICEIMPL)
public class UserServiceImpl implements UserService {

    @Autowired
    @Qualifier(UserConstants.USERDAOIMPL)
    private UserDAO userDao;

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);


    @Override
    public UserDto getUserProfile(String userId) {
        try {
            return userDao.findUserById(userId);
        } catch (RuntimeException e) {
            logger.fatal("Error in getUserProfile :"+e);
            throw e;
        }
    }

    @Override
    public UserDto registerUser(RegisterRequest request) {

        try {
            if (userDao.existsByEmail(request.getEmail())) {

                throw new RuntimeException();
            }
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setFullName(request.getFullName());
            user.setPassword(request.getPassword());
            user.setAddress(request.getAddress());
            user.setPhoneNumber(request.getPhoneNumber());

            return userDao.registerUser(user);
        } catch (Exception e) {
            logger.fatal("Error in registerUser :"+e);
            throw e;
        }

    }

    @Override
    public UserDto authenticateUser(String username, String password) {
        try {
            return userDao.authenticateUser(username, password);
        } catch (Exception e) {
            logger.fatal("Error in authenticateUser: " + e);
            throw e;
        }
    }

    @Override
    public UserDto updateUserProfile(String userId, UserDto userDto) {
        try {
            return userDao.updateUserProfile(userId, userDto);
        } catch (Exception e) {
            logger.fatal("Error in updateUserProfile: " + e);
            throw e;
        }
    }
}
