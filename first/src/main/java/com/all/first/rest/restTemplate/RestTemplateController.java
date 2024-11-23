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
import org.springframework.web.bind.annotation.RequestParam;
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
	public CommonResponseModel<?> getData(@PathVariable String version, @RequestParam String user) {
		return restTemplateService.getData(version, user);
	}
	
	@PostMapping("/rest-template/{version}")
	public CommonResponseModel<?> postData(@RequestBody CommonRequestModel request, @PathVariable String version, @RequestParam String user) {
		if(StringUtils.isNotBlank(request.getRequestData())) {
			return restTemplateService.postData(request, version, user);
		}
		
		return new CommonResponseModel<String>(Constants.invalidRequest);
	}
	
	@PutMapping("/rest-template/{version}")
	public CommonResponseModel<?> putData(@RequestBody CommonRequestModel request, @PathVariable String version, @RequestParam String user) {
		if(StringUtils.isNotBlank(request.getRequestData())) {
			return restTemplateService.putData(request, version, user);
		}
		
		return new CommonResponseModel<String>(Constants.invalidRequest);
	}
	
	@PatchMapping("/rest-template/{version}")
	public CommonResponseModel<?> patchData(@RequestBody CommonRequestModel request, @PathVariable String version, @RequestParam String user) {
		if(StringUtils.isNotBlank(request.getRequestData())) {
			return restTemplateService.patchData(request, version, user);
		}
		
		return new CommonResponseModel<String>(Constants.invalidRequest);
	}
	
	@DeleteMapping("/rest-template")
	public CommonResponseModel<?> deleteData(@RequestBody CommonRequestModel request, @RequestParam String user) {
		if(StringUtils.isNotBlank(request.getRequestData())) {
			return restTemplateService.deleteData(request, user);
		}
		
		return new CommonResponseModel<String>(Constants.invalidRequest);
	}
}
