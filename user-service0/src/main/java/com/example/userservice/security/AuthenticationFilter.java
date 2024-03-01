package com.example.userservice.security;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

		log.debug( ((User)authResult.getPrincipal()).getUsername() );
		
		// email 가지고 -> db 조회 -> id 로 jwt 생성
        String userName = ((User) authResult.getPrincipal()).getUsername();
        UserDto userDetails = userService.getUserDetailsByEmail(userName);

        byte[] secretKeyBytes = Base64.getEncoder().encode(environment.getProperty("token.secret").getBytes());
        
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        Instant now = Instant.now();

        String token = Jwts.builder()
                .subject(userDetails.getUserId())
                .expiration(Date.from(now.plusMillis(Long.parseLong(environment.getProperty("token.expiration_time")))))
                .issuedAt(Date.from(now))
                .signWith(secretKey)  // 암호화
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userDetails.getUserId());
	}
	
}
