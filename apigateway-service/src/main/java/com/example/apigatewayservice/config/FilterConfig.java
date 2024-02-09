package com.example.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

	@Bean
	public RouteLocator getewayRoutes(RouteLocatorBuilder builder) {
		return builder.routes() // yaml 파일 대신 여기(java class) 에서
				.route(r -> r.path("/first-service/**") // 요청이 들어오면 아래 uri 로 이동
						.filters(f -> f.addRequestHeader("first-request", "first-request-header")
										.addResponseHeader("first-response", "first-response-header"))
						.uri("http://localhost:8081"))
				.route(r -> r.path("/second-service/**") // 요청이 들어오면 아래 uri 로 이동
						.filters(f -> f.addRequestHeader("second-request", "second-request-header")
										.addResponseHeader("second-response", "second-response-header"))
						.uri("http://localhost:8082"))
				.build();
	}
}
