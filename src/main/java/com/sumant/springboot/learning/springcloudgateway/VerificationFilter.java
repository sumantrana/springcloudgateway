package com.sumant.springboot.learning.springcloudgateway;

import com.sumant.springboot.learning.springcloudgateway.VerificationFilter.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class VerificationFilter extends AbstractGatewayFilterFactory<Config> {

	final Logger logger = LoggerFactory.getLogger(VerificationFilter.class);

	public VerificationFilter() {
		super(Config.class);
	}


	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {

			//ServerHttpRequest request = exchange.getRequest();
			//ServerHttpRequest updatedRequest = exchange.getRequest().mutate().method(HttpMethod.GET).headers( httpHeaders -> httpHeaders.setAll(request.getHeaders().toSingleValueMap())).build();

			if ( isValidRequest(exchange.getRequest(), config) ){

				//execute the chain further
				logger.debug("Filter success. Data matches");

//				ServerWebExchange modifiedExchange = exchange.mutate()
//						.request( updatedRequest )
//						.build();


				return chain.filter(exchange);

			} else {

				//return 401 to the user
				logger.debug("Filter failure. Data mis-match");
				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				return exchange.getResponse().setComplete();
			}

		};
	}

	private boolean isValidRequest( ServerHttpRequest serverHttpRequest, Config config ){

		String nadId = serverHttpRequest.getHeaders().getFirst("NadId");
		String vin = serverHttpRequest.getHeaders().getFirst("Vin");

		if ( nadId != null && vin != null ) {
			return nadId.equals(vin);
		} else {
			return false;
		}

	}

	public static class Config {
		private String authenticationEndpoint;

		public Config() {
		}

		public String getAuthenticationEndpoint() {
			return authenticationEndpoint;
		}

		public void setAuthenticationEndpoint(String authenticationEndpoint) {
			this.authenticationEndpoint = authenticationEndpoint;
		}

	}
}
