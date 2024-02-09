package com.example.firstservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/first-service")
@Slf4j
public class FirstServiceController {

	Environment env;
	
	@Autowired
	public FirstServiceController(Environment env) {
		this.env = env;
	}
	
	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome to the First service.";
	}
	
	@GetMapping("/message")
	public String message(@RequestHeader("first-request") String header) {
		log.info(header);
		return "Hello World in First Service.";
	}

	@GetMapping("/check")
	//public String check() {
	public String check(HttpServletRequest request) {  // mvc 에서는 선언만 해주면 사용 가능
		//return "Hi, there. This is a message from First Service.";
		
		log.info("Server port={}", request.getServerPort());
		return String.format("Hi, there. This is a message from First Service on PORT %s."
				, env.getProperty("local.server.port"));  // 2가지 방법
	}
}
