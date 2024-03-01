package com.example.userservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;

//public interface UserService {
//	UserDto createUser(UserDto userDto);
//	
//	// [5-2]
//	UserDto getUserByUserId(String userId);
//	
//	Iterable<UserEntity> getUserByAll();
//	//-> [5-2]
//}


// [section 6]
public interface UserService extends UserDetailsService { // spring security 에서 상속
    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);
    Iterable<UserEntity> getUserByAll();

    UserDto getUserDetailsByEmail(String userName);
}
