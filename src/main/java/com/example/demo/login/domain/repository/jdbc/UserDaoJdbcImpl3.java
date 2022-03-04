package com.example.demo.login.domain.repository.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.example.demo.login.domain.model.User;

@Repository("UserDaoJdbcImpl3")
public class UserDaoJdbcImpl3 extends UserDaoJdbcImpl {
	
	@Autowired
	public UserDaoJdbcImpl3(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
		super(jdbcTemplate, passwordEncoder);
	}

	@Override
	public User selectOne(String userId) throws DataAccessException {
		String sql = "SELECT * FROM m_user WHERE user_id = ?";
		var rowMapper = new BeanPropertyRowMapper<User>(User.class);
		return jdbcTemplate.queryForObject(sql, rowMapper, userId);
	}

	@Override
	public List<User> selectMany() throws DataAccessException {
		String sql = "SELECT * FROM m_user";
		var rowMapper = new BeanPropertyRowMapper<User>(User.class);
		return jdbcTemplate.query(sql, rowMapper);
	}

}
