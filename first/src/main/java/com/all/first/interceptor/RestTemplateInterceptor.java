package com.all.first.interceptor;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
	private String token = "Bearer auth-token";
	private String apiKey = "2D59A30C18F";
	
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		
		request.getHeaders().add("Authorization", token);
		request.getHeaders().set("x-api-key", apiKey);
		
		return execution.execute(request, body);
	}
}
