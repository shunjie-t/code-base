package com.all.second.configuration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.all.second.interceptor.RestTemplateInterceptor;
import com.nimbusds.jose.util.StandardCharset;

@Configuration
public class RestTemplateConfig {
	private final long readTimeout = 5000;
	private final long connectTimeout = 6000;
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		restTemplateBuilder.setConnectTimeout(Duration.ofMillis(connectTimeout));
		restTemplateBuilder.setReadTimeout(Duration.ofMillis(readTimeout));
		
		RestTemplate restTemplate = restTemplateBuilder.additionalMessageConverters(new StringHttpMessageConverter(StandardCharset.UTF_8)).build();
		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();		
		interceptors.add(new RestTemplateInterceptor());
		restTemplate.setInterceptors(interceptors);
		
		return restTemplate;
	}
}
