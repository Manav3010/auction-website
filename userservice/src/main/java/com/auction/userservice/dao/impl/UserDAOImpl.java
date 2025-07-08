package com.auction.userservice.dao.impl;

import com.auction.userservice.constants.UserConstants;
import com.auction.userservice.dao.UserDAO;
import com.auction.userservice.dto.UserDto;
import com.auction.userservice.model.User;
import com.auction.userservice.rowmapper.UserRowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository(UserConstants.USERDAOIMPL)
@PropertySource("classpath:postgres.properties")
public class UserDAOImpl implements UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${FIND_USER_BY_ID}")
    private String findUserById;

    @Value("${REGISTER_USER}")
    private String registerUser;

    @Value("${EXISTS_BY_EMAIL}")
    private String existsByEmail;

    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);



    @Override
    public UserDto findUserById(String userId) throws RuntimeException {
        try {
            System.out.println("HERE :::"+userId);
            return jdbcTemplate.queryForObject(
                    findUserById,
                    new Object[]{userId},
                    new UserRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            logger.fatal("Error in findUserById :"+e);
            throw e;
        }
    }

    @Override
    public UserDto registerUser(User user) {
        try {
            int rows = jdbcTemplate.update(
                    registerUser,
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getFullName(),
                    user.getAddress(),
                    user.getPhoneNumber(),
                    user.isActive(),
                    user.getRole().name()
            );

            if (rows > 0) {
                UserDto userDto = new UserDto();
                userDto.setId(user.getId());
                userDto.setUsername(user.getUsername());
                userDto.setEmail(user.getEmail());
                userDto.setPassword(user.getPassword());
                userDto.setFullName(user.getFullName());
                userDto.setAddress(user.getAddress());
                userDto.setPhoneNumber(user.getPhoneNumber());
                return userDto;
            } else {
                throw new RuntimeException("Failed to register user.");
            }
        }
        catch(Exception e){
            logger.fatal("Error in registerUser :"+e);
            throw e;
        }

    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            return Boolean.TRUE.equals(
                    jdbcTemplate.queryForObject(
                            existsByEmail,
                            new Object[]{email},
                            Boolean.class
                    )
            );
        } catch (Exception e) {
            logger.fatal("Error in existsByEmail :"+e);
            throw e;
        }
    }
}
