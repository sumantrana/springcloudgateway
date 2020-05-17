package com.sumant.springboot.learning.springcloudgateway;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.mockserver.model.HttpRequest.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpResponse.response;


@ExtendWith({SpringExtension.class})
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GatewayPathForwardingTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@LocalServerPort
	private int port;

	private ClientAndServer clientAndServer;


	@BeforeAll
	public void init(){
		clientAndServer = ClientAndServer.startClientAndServer(8080);
	}

	@AfterAll
	public void destroy(){
		clientAndServer.stop();
	}


	@Test
	public void pathForwardToDefaultBook_ReturnsDefaultBook(){

		String returnValue = "{\"id\": 0,\"name\":\"DefaultBook\",\"value\":25,\"authorList\":null}";
		clientAndServer.when(request().withMethod("GET").withPath("/defaultBook")).respond( response().withStatusCode(200).withBody(returnValue));


		String gatewayUrl = "http://localhost:" + port + "/myDefaultBook";
		ResponseEntity<String> outputEntity = testRestTemplate.getForEntity(gatewayUrl, String.class);


		assertThat( outputEntity.getStatusCode() ).isEqualTo( HttpStatus.OK );
		assertThat( outputEntity.getBody() ).contains( "DefaultBook" );

	}

}
