package com.example.demo.login.domain.repository.service;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.UserDao;

@Transactional
@Service
public class UserService {
	
	private final UserDao dao;
	
	@Autowired
	public UserService(@Qualifier("UserDaoJdbcImpl3") UserDao dao) {
		this.dao = dao;
	}
	
	public int count() {
		return dao.count();
	}
	
	public boolean insert(User user) {
		int rowNumber = dao.insertOne(user);
		if(rowNumber > 0) {
			return true;
		}
		return false;
	}
	
	public List<User> selectMany(){
		return dao.selectMany();
	}
	
	public User selectOne(String userId) {
		return dao.selectOne(userId);
	}
	
	public boolean updateOne(User user) {
		int rowNumber = dao.updateOne(user);
		if(rowNumber > 0) {
			return true;
		}
		return false;
	}
	
	public boolean deletOne(String userId) {
		int rowNumber = dao.deleteOne(userId);
		if(rowNumber > 0) {
			return true;
		}
		return false;
	}
	
	public void userCsvOut() throws DataAccessException {
		dao.userCsvOut();
	}
	
	public byte[] getFile(String fileName) throws IOException {
		FileSystem fs = FileSystems.getDefault();
		
		Path p = fs.getPath(fileName);
		
		byte[] bytes = Files.readAllBytes(p);
		
		return bytes;
	}
}
