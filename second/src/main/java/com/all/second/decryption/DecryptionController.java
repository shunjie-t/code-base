package com.all.second.decryption;

import java.util.Base64;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.all.second.common.CommonRequestModel;
import com.all.second.common.Constants;

import flexjson.JSONDeserializer;

@RestController
@RequestMapping("/second")
public class DecryptionController {
	@Autowired
	private DecryptionService decryptionService;
	
	private static final JSONDeserializer<DecryptionRequestModel> deserializer = new JSONDeserializer<>();
	
	@PostMapping(value="/decryption/{version}", consumes=MediaType.APPLICATION_JSON_VALUE)
	public String decryptInfo(@RequestBody CommonRequestModel request, @PathVariable String version) {
		if(ObjectUtils.allNotNull(request)) {
			byte[] decodedRequest = Base64.getDecoder().decode(request.getRequestData());
			DecryptionRequestModel decryptionRequest = deserializer.deserialize(new String(decodedRequest), DecryptionRequestModel.class);
			
			return decryptionService.decryptInfo(decryptionRequest, version);
		}
		
		return Constants.invalidRequest;
	}
}
