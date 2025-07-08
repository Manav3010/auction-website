package com.auction.userservice.rowmapper;

import com.auction.userservice.dao.impl.UserDAOImpl;
import com.auction.userservice.dto.UserDto;
import com.auction.userservice.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<UserDto> {

    private static final Logger logger = LogManager.getLogger(UserRowMapper.class);

    @Override
    public UserDto mapRow(ResultSet rs, int rowNum) throws SQLException {

        UserDto userResponse = new UserDto();
        try {



            userResponse.setId(rs.getString("id"));
            userResponse.setUsername(rs.getString("username"));
            userResponse.setEmail(rs.getString("email"));
            userResponse.setFullName(rs.getString("full_name"));
            userResponse.setPassword(rs.getString("password"));
            userResponse.setAddress(rs.getString("address"));
            userResponse.setPhoneNumber(rs.getString("phone_number"));
        }
        catch(Exception e){
            logger.fatal("Error in mapRow :"+e);
            throw e;
        }
        return userResponse;
    }
}
