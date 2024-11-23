package com.all.first.rest.webClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.all.first.common.CommonResponseModel;

import reactor.core.publisher.Mono;

//@Service
public class WebClientService {
//	@Autowired
//	private WebClient webClient;
//	
//	public WebClientService(WebClient.Builder webClientBuilder) {
//		webClient = webClientBuilder.baseUrl("https://api.example.com").build();
//	}
//	
//	public Mono<String> getRest() {
//		webClient = WebClient.builder().baseUrl("https://localhost:8081").build();
//		return webClient.get().uri("/second/rest/get").retrieve().bodyToMono(String.class);
////		return webClient.get().uri("/data").retrieve().bodyToMono(String.class);
//	}
//	
//	public CommonResponseModel postRest() {
//		return new CommonResponseModel();
//	}
//	
//	public CommonResponseModel putRest() {
//		return new CommonResponseModel();
//	}
//	
//	public CommonResponseModel patchRest() {
//		return new CommonResponseModel();
//	}
//	
//	public CommonResponseModel deleteRest() {
//		return new CommonResponseModel();
//	}
}
