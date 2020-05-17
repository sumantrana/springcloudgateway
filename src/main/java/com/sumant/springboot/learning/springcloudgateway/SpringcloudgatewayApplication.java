package com.sumant.springboot.learning.springcloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringcloudgatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringcloudgatewayApplication.class, args);
	}

//	@Bean
//	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
//		return builder.routes()
//				.route(p -> p
//						.path("/myDefaultBook")
//						.filters(f -> f.rewritePath("/myDefaultBook", "/defaultBook"))
//						.uri("http://localhost:8080"))
//				.build();
//	}

}
