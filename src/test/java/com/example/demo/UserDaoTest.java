package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.login.domain.repository.UserDao;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class UserDaoTest {
	
	private final UserDao dao;
	
	@Autowired
	public UserDaoTest(@Qualifier("UserDaoJdbcImpl3") UserDao dao) {
		this.dao = dao;
	}
	
	@Test
	public void test1() {
		assertEquals(dao.count(), 2);
	}
	
	@Test
	@Sql("/testdata.sql")
	public void countTest2() {
		assertEquals(dao.count(), 3);
	}

}
