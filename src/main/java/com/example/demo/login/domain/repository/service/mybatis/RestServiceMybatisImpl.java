package com.example.demo.login.domain.repository.service.mybatis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.mybatis.UserMapper2;
import com.example.demo.login.domain.repository.service.RestService;

@Transactional
@Service("RestServiceMybatisImpl")
public class RestServiceMybatisImpl implements RestService{
	
	private UserMapper2 userMapper;

	@Autowired
	public RestServiceMybatisImpl(UserMapper2 userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public boolean insert(User user) {
		return userMapper.insert(user);
	}

	@Override
	public User selectOne(String userId) {
		return userMapper.selectOne(userId);
	}

	@Override
	public List<User> selectMany() {
		return userMapper.selectMany();
	}

	@Override
	public boolean update(User user) {
		return userMapper.updateOne(user);
	}

	@Override
	public boolean delete(String userId) {
		return userMapper.deleteOne(userId);
	}
}