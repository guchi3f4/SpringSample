package com.example.demo.login.domain.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.login.domain.model.User;

public class UserRowMapper implements RowMapper<User> {
	
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		User user = new User();
		
		user.setUserId((String)rs.getString("user_id"));
		user.setPassword((String)rs.getString("password"));
		user.setUserName((String)rs.getString("user_name"));
		user.setBirthday((Date)rs.getDate("birthday"));
		user.setAge((int)rs.getInt("age"));
		user.setMarriage((boolean)rs.getBoolean("marriage"));
		user.setRole((String)rs.getString("role"));
		
		return user;
	}
}
