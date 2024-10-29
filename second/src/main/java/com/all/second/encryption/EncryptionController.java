package com.all.second.encryption;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.all.second.common.CommonRequestModel;
import com.all.second.common.CommonResponseModel;
import com.all.second.common.Constants;

@RestController
@RequestMapping("/second")
public class EncryptionController {
	@Autowired
	private EncryptionService encryptionService;
	
	@PostMapping(path="/encryption/{version}", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CommonResponseModel encryptInfo(@RequestBody CommonRequestModel request, @PathVariable String version) {
		if(StringUtils.isNotBlank(request.getRequestData())) {
			return encryptionService.encryptInfo(request.getRequestData(), version);
		}
		
		return new CommonResponseModel(Constants.invalidRequest);
	}
}
