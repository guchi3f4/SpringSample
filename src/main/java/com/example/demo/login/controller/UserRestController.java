package com.example.demo.login.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.service.RestService;

@RestController
public class UserRestController {
	
	private final RestService restService;
	
	@Autowired
	public UserRestController(@Qualifier("RestServiceMybatisImpl") RestService restService) {
		this.restService = restService;
	}
	
	@GetMapping("/rest/get")
	public List<User> getUserMany() {
		return restService.selectMany();
	}
	
	@GetMapping("/rest/get/{id:.+}")
	public User getUserOne(@PathVariable("id") String userId) {
		return restService.selectOne(userId);
	}
	
	@PostMapping("/rest/insert")
	public String postUserOne(@RequestBody User user) {
		System.out.println(user);
		boolean result = restService.insert(user);
		String str = "";
		if(result == true) {
			str = "{\"result\":\"ok\"}";
		} else {
			str = "{\"result\":\"error\"}";
		}
		return str;
	}
	
	@PutMapping("/rest/update")
	public String putUserOne(@RequestBody User user) {
		System.out.println("通過");
		boolean result = restService.update(user);
		String str = "";
		if(result == true) {
			str = "{\"result\":\"ok\"}";
		} else {
			str = "{\"result\":\"error\"}";
		}
		return str;
	}
	
	@DeleteMapping("/rest/delete/{id:.+}")
	public String deleteUserOne(@PathVariable("id") String userId) {
		boolean result = restService.delete(userId);
		String str = "";
		if(result == true) {
			str = "{\"result\":\"ok\"}";
		} else {
			str = "{\"result\":\"error\"}";
		}
		return str;
	}
}
