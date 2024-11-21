package com.all.first.rest.restTemplate;

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

import com.all.first.common.CommonRequestModel;
import com.all.first.common.CommonResponseModel;
import com.all.first.common.Constants;

@RestController
@RequestMapping("/first")
public class RestTemplateController {
	@Autowired
	private RestTemplateService restTemplateService;
	
	@GetMapping("/rest-template/{version}")
	public CommonResponseModel<?> getAllData(@PathVariable String version) {
		return restTemplateService.getAllData(version);
	}
	
	@GetMapping("/rest-template/{version}/{user}")
	public CommonResponseModel<?> getUserData(@PathVariable String version, @PathVariable String user) {
		return restTemplateService.getUserData(version, user);
	}
	
	@PostMapping("/rest-template/{version}")
	public CommonResponseModel<?> postData(@RequestBody CommonRequestModel request, @PathVariable String version) {
		if(StringUtils.isNotBlank(request.getRequestData())) {
			return restTemplateService.postData(request, version);
		}
		
		return new CommonResponseModel<String>(Constants.invalidRequest);
	}
	
	@PutMapping("/rest-template/{version}")
	public CommonResponseModel<?> putData(@RequestBody CommonRequestModel request, @PathVariable String version) {
		if(StringUtils.isNotBlank(request.getRequestData())) {
			return restTemplateService.putData(request, version);
		}
		
		return new CommonResponseModel<String>(Constants.invalidRequest);
	}
	
	@PatchMapping("/rest-template/{version}")
	public CommonResponseModel<?> patchData(@RequestBody CommonRequestModel request, @PathVariable String version) {
		if(StringUtils.isNotBlank(request.getRequestData())) {
			return restTemplateService.patchData(request, version);
		}
		
		return new CommonResponseModel<String>(Constants.invalidRequest);
	}
	
	@DeleteMapping("/rest-template")
	public CommonResponseModel<?> deleteData(@RequestBody CommonRequestModel request) {
		if(StringUtils.isNotBlank(request.getRequestData())) {
			return restTemplateService.deleteData(request, null);
		}
		
		return new CommonResponseModel<String>(Constants.invalidRequest);
	}
	
	@DeleteMapping("/rest-template/{user}")
	public CommonResponseModel<?> deleteData(@RequestBody CommonRequestModel request, @PathVariable String user) {
		if(StringUtils.isNotBlank(request.getRequestData())) {
			return restTemplateService.deleteData(request, user);
		}
		
		return new CommonResponseModel<String>(Constants.invalidRequest);
	}
}
