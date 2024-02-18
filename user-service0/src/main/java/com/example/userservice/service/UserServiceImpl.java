package com.example.userservice.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;

@Service
public class UserServiceImpl implements UserService {

//	@Autowired
//	UserRepository userRepository;
	//[4]
	UserRepository userRepository;
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	//--> [4]
	
//	@Override
//	public UserDto createUser(UserDto userDto) {
//		// TODO Auto-generated method stub
//		userDto.setUserId(UUID.randomUUID().toString());
//		
//		// 하나의 객체를 -> 다른 객체로 (일일이 get, set 하기 어려우므로..)
//		ModelMapper mapper = new ModelMapper();
//		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//		// 전략 -> 딱 맞아 떨어지게..
//		UserEntity userEntity = mapper.map(userDto, UserEntity.class);
//		userEntity.setEncryptedPwd("encrypted_password");
//		
//		userRepository.save(userEntity);
//
//		// [3-3]
//		//return null;
//		UserDto returnUserDto = mapper.map(userEntity, UserDto.class);
//		return returnUserDto;
//	}

	@Override
	public UserDto createUser(UserDto userDto) {
		// TODO Auto-generated method stub
		userDto.setUserId(UUID.randomUUID().toString());
		
		// 하나의 객체를 -> 다른 객체로 (일일이 get, set 하기 어려우므로..)
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		// 전략 -> 딱 맞아 떨어지게..
		UserEntity userEntity = mapper.map(userDto, UserEntity.class);
		//userEntity.setEncryptedPwd("encrypted_password");
		// [4]
		userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
		
		userRepository.save(userEntity);

		UserDto returnUserDto = mapper.map(userEntity, UserDto.class);
		return returnUserDto;
	}
}
