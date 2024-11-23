package com.all.first.rest.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.all.first.common.CommonResponseModel;

//@FeignClient(name="apiClient", url="http://localhost/second")
public interface ApiClient {
//	@GetMapping("/rest/get")
//	public CommonResponseModel getRest();
//	
//	@PostMapping("/rest/post")
//	public CommonResponseModel postRest();
//	
//	@PutMapping("/rest/put")
//	public CommonResponseModel putRest();
//	
//	@PatchMapping("/rest/patch")
//	public CommonResponseModel patchRest();
//	
//	@DeleteMapping("/rest/delete")
//	public CommonResponseModel deleteRest();
}
