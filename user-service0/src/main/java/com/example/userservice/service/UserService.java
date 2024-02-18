package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;

public interface UserService {
	UserDto createUser(UserDto userDto);
	
	// [5-2]
	UserDto getUserByUserId(String userId);
	
	Iterable<UserEntity> getUserByAll();
	//-> [5-2]
}
