package com.sumant.springboot.learning.springcloudgateway;

import java.net.URI;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import com.sumant.springboot.learning.springcloudgateway.PostToGetFilter.Config;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class PostToGetFilter extends AbstractGatewayFilterFactory<Config> {

	final Logger logger = LoggerFactory.getLogger(PostToGetFilter.class);

	public PostToGetFilter() {
		super(Config.class);
	}


//	@Override
//	public GatewayFilter apply(Config config) {
//		return (exchange, chain) -> {
//			return chain.filter( exchange
//					.mutate()
//					.request( request -> request
//							.method(HttpMethod.GET)
//							.uri(UriComponentsBuilder.fromUri( exchange.getRequest().getURI() ).queryParam("vin", "abcde12345").build().toUri()))
//					//.uri(UriComponentsBuilder.fromUri( exchange.getRequest().getURI() ).queryParams( RequestBodyHelper.resolveQueryParamsRequest(exchange.getRequest())).build().toUri()))
//					.build());
//		};
//	}


	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {

			ServerHttpRequest request = exchange.getRequest();
			URI requestUri = request.getURI();

			URI ex =UriComponentsBuilder.fromUri( exchange.getRequest().getURI() ).queryParams( RequestBodyHelper.resolveQueryParamsRequest(exchange.getRequest())).build().toUri();

			ServerHttpRequest newRequest = request.mutate().method(HttpMethod.GET).uri(ex).build();

			ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

			return chain.filter(newExchange);

		};
	}

//
//	@Override
//	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//
//		ServerHttpRequest request = exchange.getRequest();
//		URI requestUri = request.getURI();
//
//		URI ex =UriComponentsBuilder.fromUri( exchange.getRequest().getURI() ).queryParams( RequestBodyHelper.resolveQueryParamsRequest(exchange.getRequest())).build().toUri();
//
//		ServerHttpRequest newRequest = request.mutate().method(HttpMethod.GET).uri(ex).build();
//
//		ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
//
//		return chain.filter(newExchange);
//
//	}


	public static class Config {

		public Config() {
		}

	}
}
