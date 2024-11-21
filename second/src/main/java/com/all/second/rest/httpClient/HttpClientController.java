package com.all.second.rest.httpClient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.all.second.common.Constants;
import com.all.second.common.CommonRequestModel;
import com.all.second.common.CommonResponseModel;

@RestController
@RequestMapping("/second")
public class HttpClientController {
	@Autowired
	private HttpClientService httpClientService;
	
	@GetMapping("/http-client")
	public CommonResponseModel<?> getAllData() {
		return httpClientService.getData();
	}
	
	@GetMapping("/http-client/{user}")
	public CommonResponseModel<?> getUserData(@PathVariable String user) {
		return httpClientService.getUserData(user);
	}
	
	@PostMapping("/http-client")
	public CommonResponseModel<?> postRest(@RequestBody CommonRequestModel request) {
		if(StringUtils.isNotBlank(request.getRequestData())) {
			return httpClientService.postData(request);			
		}
		
		return new CommonResponseModel<String>(Constants.invalidRequest);
	}
	
	@PutMapping("/http-client")
	public CommonResponseModel<?> putRest(@RequestBody CommonRequestModel request) {
		if(StringUtils.isNotBlank(request.getRequestData())) {
			return httpClientService.putData(request);			
		}
		
		return new CommonResponseModel<String>(Constants.invalidRequest);
	}
	
	@PatchMapping("/http-client")
	public CommonResponseModel<?> patchRest(@RequestBody CommonRequestModel request) {
		if(StringUtils.isNotBlank(request.getRequestData())) {
			return httpClientService.patchData(request);			
		}
		
		return new CommonResponseModel<String>(Constants.invalidRequest);
	}
	
	@DeleteMapping("/http-client")
	public CommonResponseModel<?> deleteRest(@RequestBody CommonRequestModel request) {
		if(StringUtils.isNotBlank(request.getRequestData())) {
			return httpClientService.deleteData(request);			
		}
		
		return new CommonResponseModel<String>(Constants.invalidRequest);
	}
}
