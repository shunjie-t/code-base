package com.all.first.encryption;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.all.first.common.CommonRequestModel;
import com.all.first.common.CommonResponseModel;
import com.all.first.common.Constants;

@RestController
@RequestMapping("/first")
public class EncryptionController {
	@Autowired
	private EncryptionService encryptionService;
	
	@PostMapping(path="/encryption/{version}", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CommonResponseModel<String> encryptInfo(@RequestBody CommonRequestModel request, @PathVariable String version) {
		if(StringUtils.isNotBlank(request.getRequestData())) {
			return encryptionService.encryptInfo(request.getRequestData(), version);
		}
		
		return new CommonResponseModel<>(Constants.invalidRequest);
	}
}
