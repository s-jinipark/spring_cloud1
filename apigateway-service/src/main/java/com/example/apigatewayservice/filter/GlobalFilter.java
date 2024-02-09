package com.example.apigatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config>{

	public GlobalFilter() {
		super(Config.class);
	}
	
	@Data
	public static class Config {
		private String baseMessge;
		private boolean preLogger;
		private boolean postLogger;
	}

	@Override
	public GatewayFilter apply(Config config) {
		// TODO Auto-generated method stub
		//return null;
		// Custom Pre Filter
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();
			
			// config 를 활용 -> 참조하여 값을 찍어 줌.. => config 에 넣어주는 값은 ? yml 파일에 정의
			log.info("Global filter baseMessage: {}", config.getBaseMessge() );
			
			if (config.isPreLogger()) {
				log.info("Global filter Start: request id -> {}", request.getId() );
			}
			
			//Custom Post Filter
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				if (config.isPostLogger()) {
					log.info("Global filter End: response id -> {}", response.getStatusCode() );
				}
			}));
		};
	}
}
