package com.example.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
public class UserService0Application {

	public static void main(String[] args) {
		SpringApplication.run(UserService0Application.class, args);
	}


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {	// [4] Bean 으로 해주면 됨. 메소드명은 관계 없음
        return new BCryptPasswordEncoder();
    }
}
