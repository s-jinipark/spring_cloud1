package com.example.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.vo.Greeting;

@RestController
@RequestMapping("/")
public class UserController {

	//[1]
	private Environment env;
	
	@Autowired
	public UserController(Environment env) {
		this.env = env;
	}
	//--> [1]
	
	//[2] 두번째 방법
	@Autowired
	private Greeting greeting;
	//--> [2]
	
	@GetMapping("/health_check")
	public String status() {
		return "It's Working in User Service";
	}
	
	@GetMapping("/welcome")
	public String welcome() {
		//return env.getProperty("greeting.message"); // -> [1]
		return greeting.getMessage(); // -> [2]
	}
	
}
