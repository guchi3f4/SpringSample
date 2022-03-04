package com.example.demo.login.domain.model;

import java.util.Date;

import lombok.Data;

@Data
public class User {
	
	private String userId;
	private String password;
	private String userName;
	private int age;
	private Date birthday;
	private boolean marriage;
	private String role;
}
