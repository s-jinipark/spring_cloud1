package com.example.userservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;

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

	// [5-2]
	@Override
	public UserDto getUserByUserId(String userId) {
		// TODO Auto-generated method stub
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) 
			throw new UsernameNotFoundException("User not found");
		
		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
		
		List<ResponseOrder> orders = new ArrayList<>();
		userDto.setOrders(orders);
		
		return userDto;
	}

	@Override
	public Iterable<UserEntity> getUserByAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();  // 기본 제공되는 메소드
	}
	//-> [5-2]
}
