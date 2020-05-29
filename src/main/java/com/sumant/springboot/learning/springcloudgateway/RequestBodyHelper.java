package com.sumant.springboot.learning.springcloudgateway;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.util.JsonUtils;
import net.minidev.json.JSONObject;
import reactor.core.publisher.Flux;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class RequestBodyHelper {

	public static MultiValueMap<String, String> resolveQueryParamsRequest(ServerHttpRequest serverHttpRequest) {
		Flux<DataBuffer> body = serverHttpRequest.getBody();
		AtomicReference<String> bodyRef = new AtomicReference<>();
		body.subscribe(buffer -> {
			CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
			DataBufferUtils.release(buffer);
			bodyRef.set(charBuffer.toString());
		});
		return convertJsonToQueryParamMap(bodyRef.get());
	}

	private static MultiValueMap<String, String> convertJsonToQueryParamMap( String json ) {

		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = null;
		try {
			jsonNode = mapper.readTree(json);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		Iterator<Entry<String, JsonNode>> fields = jsonNode.fields();

		while ( fields.hasNext() ){
			Map.Entry<String, JsonNode> entry = fields.next();
			multiValueMap.add(entry.getKey(), entry.getValue().asText());
		}

		return multiValueMap;
	}



}
