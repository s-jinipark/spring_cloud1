package com.example.userservice.jpa;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long>{

	// [5-2]
	UserEntity findByUserId(String userId);

	// [section 6]
	UserEntity findByEmail(String username);
}
