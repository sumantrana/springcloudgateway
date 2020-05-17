package com.sumant.springboot.learning.springcloudgateway;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith({SpringExtension.class})
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GatewayPathForwardingTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@LocalServerPort
	private int port;

	@Autowired
	private WebTestClient webClient;

	WireMockServer wireMockServer;


	@BeforeAll
	public void init(){
		wireMockServer = new WireMockServer(options().port(8080));
		wireMockServer.start();
	}

	@AfterAll
	public void destroy(){
		wireMockServer.stop();
	}


	@Test
	public void pathForwardToDefaultBook_ReturnsDefaultBook(){

		String returnValue = "{\"id\": 0,\"name\":\"DefaultBook\",\"value\":25,\"authorList\":null}";
		stubFor( get ("/defaultBook").willReturn(aResponse().withStatus(200).withBody(returnValue)) );


		String gatewayUrl = "http://localhost:" + port + "/myDefaultBook";
		ResponseEntity<String> outputEntity = testRestTemplate.getForEntity(gatewayUrl, String.class);


		assertThat( outputEntity.getStatusCode() ).isEqualTo( HttpStatus.OK );
		assertThat( outputEntity.getBody() ).contains( "DefaultBook" );

	}

}
