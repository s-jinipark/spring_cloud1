package com.example.apigatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config>{

	public LoggingFilter() {
		super(Config.class);
	}
	
	@Data
	public static class Config {
		private String baseMessge;
		private boolean preLogger;
		private boolean postLogger;
	}

	@Override
	public GatewayFilter apply(Config config) { // 구현시킬 함수는 apply

		// Custom Pre Filter
//		return (exchange, chain) -> {
//			ServerHttpRequest request = exchange.getRequest();
//			ServerHttpResponse response = exchange.getResponse();
//			
//			// config 를 활용 -> 참조하여 값을 찍어 줌.. => config 에 넣어주는 값은 ? yml 파일에 정의
//			log.info("Global filter baseMessage: {}", config.getBaseMessge() );
//			
//			if (config.isPreLogger()) {
//				log.info("Global filter Start: request id -> {}", request.getId() );
//			}
//			
//			//Custom Post Filter
//			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//				if (config.isPostLogger()) {
//					log.info("Global filter End: response id -> {}", response.getStatusCode() );
//				}
//			}));
//		};
		GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();
			
			log.info("Logging filter baseMessage: {}", config.getBaseMessge() );
			
			if (config.isPreLogger()) {
				log.info("Logging PRE Filter: request id -> {}", request.getId() );
			}
			
			//Custom Post Filter
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				if (config.isPostLogger()) {
					log.info("Logging Post Filter: response id -> {}", response.getStatusCode() );
				}
			}));
		}, Ordered.LOWEST_PRECEDENCE);  // 순서를 조정  Ordered.HIGHEST_PRECEDENCE);
		
		return filter;
	}
}
