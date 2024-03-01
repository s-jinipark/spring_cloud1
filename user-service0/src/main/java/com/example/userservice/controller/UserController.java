package com.example.userservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;

@RestController
//@RequestMapping("/")   // [5-2]
//@RequestMapping("/user-service/") // [section 6]
@RequestMapping("/")   // [section 6] 에서 다시 뺌 .. apigateway 에서 rewrite ...
public class UserController {

	//[1]
//	private Environment env;
	
//	@Autowired
//	public UserController(Environment env) {
//		this.env = env;
//	}
	//--> [1]
	
	//[1] + [3]
	private Environment env;
	private UserService userService;
	
	@Autowired
	public UserController(Environment env, UserService userService) {
		this.env = env;
		this.userService = userService;
	}
	//--> [1] + [3]
	
	//[2] 두번째 방법
	@Autowired
	private Greeting greeting;
	//--> [2]
	
	@GetMapping("/health_check")
	public String status() {
		//return "It's Working in User Service";
		// [5-1]
		return String.format("It's Working in User Service on PORT %s",
				env.getProperty("local.server.port"));
	}
	
	@GetMapping("/welcome")
	public String welcome() {
		//return env.getProperty("greeting.message"); // -> [1]
		return greeting.getMessage(); // -> [2]
	}
	
	// [3] => jpa
//	@PostMapping("/users")
//	public String createUser(@RequestBody RequestUser user) {
//		ModelMapper mapper = new ModelMapper();
//		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//		
//		UserDto userDto = mapper.map(user, UserDto.class);
//		userService.createUser(userDto);
//		
//		return "Create user method is called";
//	}
	// [3-2] => jpa + return 변경
//	@PostMapping("/users")
//	public ResponseEntity createUser(@RequestBody RequestUser user) {
//		ModelMapper mapper = new ModelMapper();
//		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//		
//		UserDto userDto = mapper.map(user, UserDto.class);
//		userService.createUser(userDto);
//		 
//		return new ResponseEntity(HttpStatus.CREATED);
//	}
	// [3-3] 
	@PostMapping("/users")
	public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto userDto = mapper.map(user, UserDto.class);
		userService.createUser(userDto);
		
		ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

		return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
	}
	
	// [5-2] 
	@GetMapping("/users")
	public ResponseEntity<List<ResponseUser>> getUsers() {
		
		Iterable<UserEntity> userList = userService.getUserByAll();
		
		List<ResponseUser> result = new ArrayList<>();
		userList.forEach(v -> {
			result.add(new ModelMapper().map(v, ResponseUser.class));
		});

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
		// @PathVariable -> 이름이 일치하지 않아도 원하는 변수명으로 지정
		
		UserDto userDto = userService.getUserByUserId(userId);
		
		ResponseUser returnValue = new ModelMapper().map(userDto, ResponseUser.class);

		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}
}
