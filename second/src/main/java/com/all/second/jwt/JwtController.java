package com.all.second.jwt;

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
@RequestMapping("/second")
public class JwtController {
	@Autowired
	JwtService jwtService;
	
	ObjectMapper mapper = new ObjectMapper();
	
	@RequestMapping(value="/jwe/encrypt", consumes=MediaType.APPLICATION_JSON_VALUE)
	public String encryptJweRequest(@RequestBody JwtModel param) {
		return jwtService.encryptJwe(param);
	}
	
	@RequestMapping("/jwe/decrypt")
	public String decryptJweRequest(@RequestParam String param) {
		JwtModel request = null;
		try {
			request = mapper.readValue(param, JwtModel.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return jwtService.decryptJwe(request);
	}
	
	@PostMapping(value="/jws/sign", consumes=MediaType.APPLICATION_JSON_VALUE)
	public String signJwsRequest(@RequestBody JwtModel param) {
		return jwtService.signJws(param);
	}
	
	@PostMapping("/jws/verify")
	public String verifyJwsRequest(@RequestParam String param) {
		JwtModel request = null;
		try {
			request = mapper.readValue(param, JwtModel.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return jwtService.verifyJws(request);
	}
	
	@PostMapping("/encrypted-jwt/encrypt")
	public String encryptJwtRequest(@RequestBody JwtModel param) {
		return jwtService.encryptJwt(param);
	}
	
	@PostMapping("/encrypted-jwt/decrypt")
	public String decryptJwtRequest(@RequestParam String param) {
		JwtModel request = null;
		try {
			request = mapper.readValue(param, JwtModel.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return jwtService.decryptJwt(request);
	}
	
	@PostMapping("/signed-jwt/sign")
	public String signJwtRequest(@RequestBody JwtModel param) {
		return jwtService.signJwt(param);
	}
	
	@PostMapping("/signed-jwt/verify")
	public String verifyJwtRequest(@RequestParam String param) {
		JwtModel request = null;
		try {
			request = mapper.readValue(param, JwtModel.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return jwtService.verifyJwt(request);
	}
}
