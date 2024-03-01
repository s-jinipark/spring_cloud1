package com.example.userservice.security;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private Environment environment;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                   UserService userService, Environment environment) {
                                                  
        super(authenticationManager);
        this.userService = userService;
        this.environment = environment;
    }
    
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, 
											HttpServletResponse response) throws AuthenticationException {
		// TODO Auto-generated method stub
        try {

            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
            //-> getInputStream 쓰는 이유는 POST 로 전달된 내용은 파라미터로 받을 수 없기 때문
            
            return getAuthenticationManager().authenticate(  // 인증 작업 요청
                    new UsernamePasswordAuthenticationToken(  // spring security 에서 사용할 수 있는 형태로...
                    		creds.getEmail(), creds.getPassword(), new ArrayList<>()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, 
											HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// TODO Auto-generated method stub


	}
	
}
