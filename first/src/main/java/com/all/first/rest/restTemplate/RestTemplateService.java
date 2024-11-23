package com.all.first.rest.restTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.all.first.common.CommonRequestModel;
import com.all.first.common.CommonResponseModel;
import com.all.first.common.Constants;
import com.all.first.dao.view.DataView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RestTemplateService implements InitializingBean {
	@Autowired
	private RestTemplate restTemplate;
	
	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public void afterPropertiesSet() throws Exception {
		mapper.findAndRegisterModules();
	}
	
	public CommonResponseModel<?> getData(String version, String user) {
		DataView[] responseArray = null;
		String lastVer = null;
		if(StringUtils.isBlank(user)) {
			lastVer = "v3";
			if(version.toLowerCase().equals("v1")) {			
				responseArray = restTemplate.getForObject(Constants.second_target_url, DataView[].class);
			}
			else if(version.toLowerCase().equals("v2")) {
				ResponseEntity<DataView[]> responseEntity = restTemplate.getForEntity(Constants.second_target_url, DataView[].class);
				responseArray = responseEntity.getBody();
			}
			else if(version.toLowerCase().equals(lastVer)) {
				HttpEntity<DataView[]> httpEntity = restTemplate.exchange(Constants.second_target_url, HttpMethod.GET, null, DataView[].class);
				responseArray = httpEntity.getBody();
			}
			else {
				return new CommonResponseModel<String>("Invalid version. Available versions are v1 to " + lastVer);
			}
		}
		else {
			lastVer = "v3";
			if(version.toLowerCase().equals("v1")) {
				responseArray = restTemplate.getForObject(Constants.second_target_url + "/{user}" , DataView[].class, user);			
			}
			else if(version.toLowerCase().equals("v2")) {
				ResponseEntity<DataView[]> responseEntity = restTemplate.getForEntity(Constants.second_target_url + "/{user}", DataView[].class, user);
				responseArray = responseEntity.getBody();
			}
			else if(version.toLowerCase().equals(lastVer)) {
				HttpEntity<DataView[]> httpEntity = restTemplate.exchange(Constants.second_target_url + "/{user}", HttpMethod.GET, null, DataView[].class, user);
				responseArray = httpEntity.getBody();
			}
			else {
				return new CommonResponseModel<String>("Invalid version. Available versions are v1 to " + lastVer);
			}
		}
		List<DataView> response = Arrays.asList(responseArray);
		return new CommonResponseModel<List<DataView>>(response);
	}
	
	public CommonResponseModel<?> postData(CommonRequestModel request, String version, String user) {
		List<DataView> viewList = null;
		try {
			viewList = mapper.readValue(request.getRequestData(), new TypeReference<List<DataView>>() {});
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return new CommonResponseModel<String>("JsonMappingException");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new CommonResponseModel<String>("JsonProcessingException");
		}
		
		if(StringUtils.isBlank(user)) {
			user = Constants.first;
		}
		
		for(DataView view : viewList) {
			if(StringUtils.isBlank(view.getDataType())) {
				return new CommonResponseModel<String>("Data type must not be blank");
			}
			else if(StringUtils.isBlank(view.getData())) {
				return new CommonResponseModel<String>("Data must not be blank");
			}
			view.setDataId(null);
			view.setCreBy(user);
			view.setUpdBy(user);
			LocalDateTime date = LocalDateTime.now();
			view.setCreOn(date);
			view.setUpdOn(date);
		}
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.add(Constants.X_API_KEY, Constants.X_API_KEY_VALUE);
		HttpEntity<List<DataView>> entity = new HttpEntity<>(viewList, header);
		String lastVer = "v4";
		DataView[] responseArray = null;
		if(version.toLowerCase().equals("v1")) {
			responseArray = restTemplate.postForObject(Constants.second_target_url, entity, DataView[].class);
		}
		else if(version.toLowerCase().equals("v2")) {
			ResponseEntity<DataView[]> responseEntity = restTemplate.postForEntity(Constants.second_target_url + "/{user}", entity, DataView[].class, user);
			responseArray = responseEntity.getBody();
		}
		else if(version.toLowerCase().equals("v3")) {
			restTemplate.postForLocation(Constants.second_target_url + "/{user}", entity, user);
			return new CommonResponseModel<String>(Constants.second_target_url + "/" + user);
		}
		else if(version.toLowerCase().equals(lastVer)) {			
			ResponseEntity<DataView[]> responseEntity = restTemplate.exchange(Constants.second_target_url + "/{user}", HttpMethod.POST, entity, DataView[].class, user);
			responseArray = responseEntity.getBody();
		}
		else {
			return new CommonResponseModel<String>("Invalid version. Available versions are v1 to " + lastVer);
		}
		List<DataView> response = Arrays.asList(responseArray);
		return new CommonResponseModel<List<DataView>>(response);
	}
	
	public CommonResponseModel<?> putData(CommonRequestModel request, String version, String user) {
		List<DataView> viewList = null;
		
		try {
			viewList = mapper.readValue(request.getRequestData(), new TypeReference<List<DataView>>() {});
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return new CommonResponseModel<String>("JsonMappingException");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new CommonResponseModel<String>("JsonProcessingException");
		}
		
		if(StringUtils.isBlank(user)) {
			user = Constants.first;
		}
		
		for(DataView view : viewList) {
			if(StringUtils.isBlank(view.getDataType())) {
				return new CommonResponseModel<String>("Data type must not be blank");
			}
			else if(StringUtils.isBlank(view.getData())) {
				return new CommonResponseModel<String>("Data must not be blank");
			}
			
			LocalDateTime date = LocalDateTime.now();
			if(view.getDataId() != null) {
				view.setCreBy(null);
				view.setCreOn(null);
			}
			else {
				view.setCreBy(user);
				view.setCreOn(date);
			}
			view.setUpdBy(user);
			view.setUpdOn(date);
		}
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.add(Constants.X_API_KEY, Constants.X_API_KEY_VALUE);
		HttpEntity<List<DataView>> entity = new HttpEntity<>(viewList, header);
		String lastVer = "v2";
		if(version.toLowerCase().equals("v1")) {
			restTemplate.put(Constants.second_target_url + "/{user}", entity, user);
			return new CommonResponseModel<String>("PUT request successful");
		}
		else if(version.toLowerCase().equals(lastVer)) {			
			ResponseEntity<DataView[]> responseEntity = restTemplate.exchange(Constants.second_target_url + "/{user}", HttpMethod.PUT, entity, DataView[].class, user);
			List<DataView> response = Arrays.asList(responseEntity.getBody());
			return new CommonResponseModel<List<DataView>>(response);
		}
		return new CommonResponseModel<String>("Invalid version. Available versions are V1 to " + lastVer);
	}
	
	public CommonResponseModel<?> patchData(CommonRequestModel request, String version, String user) {
		List<DataView> viewList = null;
		String lastVer = "v2";
		try {
			viewList = mapper.readValue(request.getRequestData(), new TypeReference<List<DataView>>() {});
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return new CommonResponseModel<String>("JsonMappingException");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new CommonResponseModel<String>("JsonProcessingException");
		}
		
		if(StringUtils.isBlank(user)) {
			user = Constants.first;
		}
		
		for(DataView view : viewList) {
			if(view.getDataId() == null) {
				return new CommonResponseModel<String>("Data id must not be blank");
			}
			view.setCreBy(null);
			view.setCreOn(null);
			view.setUpdBy(user);
			view.setUpdOn(LocalDateTime.now());
		}
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.add(Constants.X_API_KEY, Constants.X_API_KEY_VALUE);
		HttpEntity<List<DataView>> entity = new HttpEntity<>(viewList, header);
		
		DataView[] responseArray = null;
		if(version.toLowerCase().equals("v1")) {			
			responseArray = restTemplate.patchForObject(Constants.second_target_url, entity, DataView[].class);
		}
		else if(version.toLowerCase().equals(lastVer)) {			
			ResponseEntity<DataView[]> responseEntity = restTemplate.exchange(Constants.second_target_url + "/{user}", HttpMethod.PATCH, entity, DataView[].class, user);
			responseArray = responseEntity.getBody();
		}
		else {
			return new CommonResponseModel<String>("Invalid version. Available versions are v1 to " + lastVer);
		}
		List<DataView> response = Arrays.asList(responseArray);
		return new CommonResponseModel<List<DataView>>(response);
	}
	
	public CommonResponseModel<String> deleteData(CommonRequestModel request, String user) {
		List<BigDecimal> idList = null;
		try {
			idList = mapper.readValue(request.getRequestData(), new TypeReference<List<BigDecimal>>() {});
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return new CommonResponseModel<String>("JsonMappingException");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new CommonResponseModel<String>("JsonProcessingException");
		}
		
		if(StringUtils.isBlank(user)) {
			user = Constants.first;
		}
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.add(Constants.X_API_KEY, Constants.X_API_KEY_VALUE);
		HttpEntity<List<BigDecimal>> entity = new HttpEntity<>(idList, header);
		ResponseEntity<String> responseEntity = null;
		if(StringUtils.isBlank(user)) {
			responseEntity = restTemplate.exchange(Constants.second_target_url, HttpMethod.DELETE, entity, String.class);
		}
		else {
			responseEntity = restTemplate.exchange(Constants.second_target_url + "/{user}", HttpMethod.DELETE, entity, String.class, user);
		}
		return new CommonResponseModel<String>(responseEntity.getBody());
	}
}
