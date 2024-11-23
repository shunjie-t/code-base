package com.all.first.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/first")
public class JwtController {
	@Autowired
	JwtService jwtService;
	
	ObjectMapper mapper = new ObjectMapper();
	
	@PostMapping("/jwe/encrypt")
	public String encryptJweRequest(@RequestBody String body) {
		JwtModel request = null;
		try {
			request = mapper.readValue(body, JwtModel.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return jwtService.encryptJwe(request);
	}
	
	@PostMapping(path="/jwe/decrypt", consumes=MediaType.APPLICATION_JSON_VALUE)
	public String decryptJweRequest(@RequestBody JwtModel body) {
		return jwtService.decryptJwe(body);
	}
	
	@PostMapping("/jws/sign")
	public String signJwsRequest(@RequestBody String body) {
		JwtModel request = null;
		try {
			request = mapper.readValue(body, JwtModel.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return jwtService.signJws(request);
	}
	
	@PostMapping(path="/jws/verify", consumes=MediaType.APPLICATION_JSON_VALUE)
	public String verifyJwsRequest(@RequestBody JwtModel body) {
		return jwtService.verifyJws(body);
	}

	@PostMapping("/encrypted-jwt/encrypt")
	public String encryptJwtRequest(@RequestBody String body) {
		JwtModel request = null;
		try {
			request = mapper.readValue(body, JwtModel.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return jwtService.encryptJwt(request);
	}
	
	@PostMapping("/encrypted-jwt/decrypt")
	public String decryptJwtRequest(@RequestBody JwtModel body) {
		return jwtService.decryptJwt(body);
	}
	
	@PostMapping("/signed-jwt/sign")
	public String signJwtRequest(@RequestBody String body) {
		JwtModel request = null;
		try {
			request = mapper.readValue(body, JwtModel.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return jwtService.signJwt(request);
	}
	
	@PostMapping("/signed-jwt/verify")
	public String verifyJwtRequest(@RequestBody JwtModel body) {
		return jwtService.verifyJwt(body);
	}
}
