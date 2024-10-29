package com.all.zeroth.hash;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.all.zeroth.common.CommonRequestModel;
import com.all.zeroth.common.Constants;

@RestController
@RequestMapping("/zeroth")
public class HashController {
	@Autowired
	private HashService macService;
	
	@PostMapping(path="/hmac", consumes=MediaType.APPLICATION_JSON_VALUE)
	public String hmac(@RequestBody CommonRequestModel request) {
		if(StringUtils.isNotBlank(request.getRequestData())) {
			return macService.hmac(request.getRequestData());
		}
		
		return Constants.invalidRequest;
	}
	
	@PostMapping(path="/cmac", consumes=MediaType.APPLICATION_JSON_VALUE)
	public String cmac(@RequestBody CommonRequestModel request) {
		if(StringUtils.isNotBlank(request.getRequestData())) {
			return macService.cmac(request.getRequestData());
		}
		
		return Constants.invalidRequest;
	}
	
	@PostMapping(path="/hashing", consumes=MediaType.APPLICATION_JSON_VALUE)
	public String md5Hash(@RequestBody HashRequestModel request) {
		if(ObjectUtils.allNotNull(request)) {
			if(request.getAlgorithm().equals("MD5")) {
				return macService.md5hashing(request.getRequestData());
			}
			else if(request.getAlgorithm().equals("SHA1") || request.getAlgorithm().equals("SHA-1")) {
				return macService.sha1hashing(request.getRequestData());
			}
			else if(request.getAlgorithm().equals("SHA256") || request.getAlgorithm().equals("SHA-256")) {
				return macService.sha1hashing(request.getRequestData());
			}
			else if(request.getAlgorithm().equals("SHA384") || request.getAlgorithm().equals("SHA-384")) {
				return macService.sha1hashing(request.getRequestData());
			}
			else if(request.getAlgorithm().equals("SHA512") || request.getAlgorithm().equals("SHA-512")) {
				return macService.sha256hashing(request.getRequestData());
			}
		}
		
		return Constants.invalidRequest;
	}
}
