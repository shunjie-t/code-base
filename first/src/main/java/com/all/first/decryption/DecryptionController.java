package com.all.first.decryption;

import java.io.IOException;
import java.util.Base64;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.all.first.common.CommonRequestModel;
import com.all.first.common.Constants;

@RestController
@RequestMapping("/first")
public class DecryptionController {
	@Autowired
	private DecryptionService decryptionService;
	
	ObjectMapper mapper = new ObjectMapper();
	
	@PostMapping(path="/decryption/{version}", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public String decryptInfo(@RequestBody CommonRequestModel request, @PathVariable String version) {
		if(ObjectUtils.allNotNull(request.getRequestData())) {
			try {
				byte[] decodedRequest = Base64.getDecoder().decode(request.getRequestData());
				DecryptionRequestModel decryptionRequest = mapper.readValue(decodedRequest, DecryptionRequestModel.class);
				
				return decryptionService.decryptInfo(decryptionRequest, version);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return Constants.invalidRequest;
	}
}
