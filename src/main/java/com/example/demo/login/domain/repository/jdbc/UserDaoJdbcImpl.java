package com.example.demo.login.domain.repository.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.UserDao;

@Repository("UserDaoJdbcImpl")
public class UserDaoJdbcImpl implements UserDao {
	
	protected final JdbcTemplate jdbcTemplate;
	
	protected final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserDaoJdbcImpl(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
		this.jdbcTemplate = jdbcTemplate;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public int count() throws DataAccessException {
		return jdbcTemplate.queryForObject(
			"SELECT COUNT(*) FROM m_user",Integer.class
		);
	}

	@Override
	public int insertOne(User user) throws DataAccessException {
		String password = passwordEncoder.encode(user.getPassword());
		return jdbcTemplate.update(
			"INSERT INTO m_user(user_id, password, user_name, birthday, age, marriage, role) "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?)",
			user.getUserId(), password, user.getUserName(),
			user.getBirthday(), user.getAge(), user.isMarriage(), user.getRole()
		);
	}

	@Override
	public User selectOne(String userId) throws DataAccessException {
		Map<String, Object> result =
			jdbcTemplate.queryForMap("SELECT * FROM m_user WHERE user_id = ?",userId);
		
		User user = new User();
		user.setUserId((String)result.get("user_id"));
		user.setPassword((String)result.get("password"));
		user.setUserName((String)result.get("user_name"));
		user.setBirthday((Date)result.get("birthday"));
		user.setAge((int)result.get("age"));
		user.setMarriage((boolean)result.get("marriage"));
		user.setRole((String)result.get("role"));
		
		return user;
	}

	@Override
	public List<User> selectMany() throws DataAccessException {
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList("SELECT * FROM m_user");
		List<User> userList = new ArrayList<User>();
		for(var result : resultList) {
			User user = new User();
			user.setUserId((String)result.get("user_id"));
			user.setPassword((String)result.get("password"));
			user.setUserName((String)result.get("user_name"));
			user.setBirthday((Date)result.get("birthday"));
			user.setAge((int)result.get("age"));
			user.setMarriage((boolean)result.get("marriage"));
			user.setRole((String)result.get("role"));
			userList.add(user);
		}
		return userList;
	}

	@Override
	public int updateOne(User user) throws DataAccessException {
		String password = passwordEncoder.encode(user.getPassword());
		int rowNumber = jdbcTemplate.update(
			"UPDATE m_user SET password = ?, user_name = ?, birthday = ?, age = ?, marriage = ?, role = ? "
			+ "WHERE user_id = ?",
			password, user.getUserName(), user.getBirthday(),
			user.getAge(), user.isMarriage(), user.getRole(), user.getUserId()
		);
		
//		if(rowNumber > 0) {
//			throw new DataAccessException("テストトランザクション") {};
//		}
		return rowNumber;
	}

	@Override
	public int deleteOne(String userId) throws DataAccessException {
		return jdbcTemplate.update("DELETE FROM m_user WHERE user_id = ?",userId);
	}

	@Override
	public void userCsvOut() throws DataAccessException {
		String sql = "SELECT * FROM m_user";
		UserRowCallbackHandler handler = new UserRowCallbackHandler();
		jdbcTemplate.query(sql, handler);
	}

}
