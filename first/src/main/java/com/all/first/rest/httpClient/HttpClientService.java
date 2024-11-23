package com.all.first.rest.httpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.protocol.HttpCoreContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.all.first.common.CommonRequestModel;
import com.all.first.common.CommonResponseModel;
import com.all.first.common.Constants;
import com.all.first.dao.view.DataView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HttpClientService implements InitializingBean {
	@Autowired
	private CloseableHttpClient httpClient;
	
	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public void afterPropertiesSet() throws Exception {
		mapper.findAndRegisterModules();
	}
	
	public CommonResponseModel<?> getData(String user) {
		if(StringUtils.isBlank(user)) {
			HttpGet httpGet = new HttpGet(Constants.second_target_url);
			HttpClientResponseHandler<String> responseHandler = response -> {
				int status = response.getCode();
				if (status >= 200 && status < 300) {
					return EntityUtils.toString(response.getEntity());
				} else {
					throw new HttpResponseException(status, "Unexpected response status: " + status);
				}
			};
			try {
				String responseString = httpClient.execute(httpGet, responseHandler);
				List<DataView> response = mapper.readValue(responseString, new TypeReference<List<DataView>>() {});
				return new CommonResponseModel<List<DataView>>(response);
			} catch (IOException e) {
				e.printStackTrace();
				return new CommonResponseModel<String>("IOException");
			}
		}
		else {
			HttpGet httpGet = new HttpGet(Constants.second_target_url + "/" + user);
			HttpHost httpHost = new HttpHost("http", "localhost", 8082);
			HttpCoreContext httpCoreContext = new HttpCoreContext();
			HttpEntity entity = null;
			
			try(ClassicHttpResponse httpResponse = httpClient.executeOpen(httpHost, httpGet, httpCoreContext)) {
				entity = httpResponse.getEntity();
				try(BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()))) {
					StringBuffer responseData = new StringBuffer();
					String line = null;
					while((line = reader.readLine()) != null) {
						responseData.append(line);
					}
					List<DataView> result = mapper.readValue(responseData.toString(), new TypeReference<List<DataView>>() {});
					return new CommonResponseModel<List<DataView>>(result);
				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
					return new CommonResponseModel<String>("UnsupportedOperationException");
				} catch (IOException e) {
					e.printStackTrace();
					return new CommonResponseModel<String>("IOException");
				}
			} catch (IOException e) {
				e.printStackTrace();
				return new CommonResponseModel<String>("IOException");
			}
		}
	}
	
	public CommonResponseModel<?> postData(CommonRequestModel request) {
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
		
		for(DataView view : viewList) {
			if(StringUtils.isBlank(view.getDataType())) {
				return new CommonResponseModel<String>("Data type must not be blank");
			}
			else if(StringUtils.isBlank(view.getData())) {
				return new CommonResponseModel<String>("Data must not be blank");
			}
			view.setDataId(null);
			view.setCreBy(Constants.first);
			view.setUpdBy(Constants.first);
			LocalDateTime date = LocalDateTime.now();
			view.setCreOn(date);
			view.setUpdOn(date);
		}
		
		String payload = null;
		try {
			payload = mapper.writeValueAsString(viewList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new CommonResponseModel<String>("JsonProcessingException");
		}
		
		HttpPost httpPost = new HttpPost(Constants.second_target_url);
		httpPost.setHeader(Constants.X_API_KEY, Constants.X_API_KEY_VALUE);
		StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
		httpPost.setEntity(entity);
		
		HttpClientResponseHandler<ResponseEntity<String>> responseHandler = response -> {
			int status = response.getCode();
			if (status >= 200 && status < 300) {
				return new ResponseEntity<>(EntityUtils.toString(response.getEntity()), HttpStatus.OK);
			} else {
				throw new HttpResponseException(status, "Unexpected response status: " + status);
			}
		};
		try {
			ResponseEntity<String> responseEntity = httpClient.execute(httpPost, responseHandler);
			List<DataView> response = mapper.readValue(responseEntity.getBody(), new TypeReference<List<DataView>>() {});
			return new CommonResponseModel<List<DataView>>(response);
		} catch (IOException e) {
			e.printStackTrace();
			return new CommonResponseModel<String>("IOException");
		}		
	}
	
	public CommonResponseModel<?> putData(CommonRequestModel request) {
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
				view.setCreBy(Constants.first);
				view.setCreOn(date);
			}
			view.setUpdBy(Constants.first);
			view.setUpdOn(date);
		}
		
		String payload = null;
		try {
			payload = mapper.writeValueAsString(viewList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new CommonResponseModel<String>("JsonProcessingException");
		}
		
		HttpPut httpPut = new HttpPut(Constants.second_target_url);
        httpPut.setHeader(Constants.X_API_KEY, Constants.X_API_KEY_VALUE);
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
        httpPut.setEntity(entity);
        
		HttpClientResponseHandler<ResponseEntity<String>> responseHandler = response -> {
			int status = response.getCode();
			if (status >= 200 && status < 300) {
				return new ResponseEntity<>(EntityUtils.toString(response.getEntity()), HttpStatus.OK);
			} else {
				throw new HttpResponseException(status, "Unexpected response status: " + status);
			}
		};
		try {
			ResponseEntity<String> responseEntity = httpClient.execute(httpPut, responseHandler);
			List<DataView> response = mapper.readValue(responseEntity.getBody(), new TypeReference<List<DataView>>() {});
			return new CommonResponseModel<List<DataView>>(response);
		} catch (IOException e) {
			e.printStackTrace();
			return new CommonResponseModel<String>("IOException");
		}        
    }
	
	public CommonResponseModel<?> patchData(CommonRequestModel request) {
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
		
		for(DataView view : viewList) {
			if(view.getDataId() == null) {
				return new CommonResponseModel<String>("Data id must not be blank");
			}
			view.setCreBy(null);
			view.setCreOn(null);
			view.setUpdBy(Constants.first);
			view.setUpdOn(LocalDateTime.now());
		}
		
		String payload = null;
		try {
			payload = mapper.writeValueAsString(viewList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new CommonResponseModel<String>("JsonProcessingException");
		}
		
		HttpPatch httpPatch = new HttpPatch(Constants.second_target_url);
		httpPatch.setHeader(Constants.X_API_KEY, Constants.X_API_KEY_VALUE);
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
        httpPatch.setEntity(entity);
        
		HttpClientResponseHandler<ResponseEntity<String>> responseHandler = response -> {
			int status = response.getCode();
			if (status >= 200 && status < 300) {
				return new ResponseEntity<>(EntityUtils.toString(response.getEntity()), HttpStatus.OK);
			} else {
				throw new HttpResponseException(status, "Unexpected response status: " + status);
			}
		};
		try {
			ResponseEntity<String> responseEntity = httpClient.execute(httpPatch, responseHandler);
			List<DataView> response = mapper.readValue(responseEntity.getBody(), new TypeReference<List<DataView>>() {});
			return new CommonResponseModel<List<DataView>>(response);
		} catch (IOException e) {
			e.printStackTrace();
			return new CommonResponseModel<String>("IOException");
		}        
    }
	
	public CommonResponseModel<String> deleteData(CommonRequestModel request) {
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
		
		String payload = null;
		try {
			payload = mapper.writeValueAsString(idList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new CommonResponseModel<String>("JsonProcessingException");
		}
		
		HttpDelete httpDelete = new HttpDelete(Constants.second_target_url);
        httpDelete.setHeader(Constants.X_API_KEY, Constants.X_API_KEY_VALUE);
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
        httpDelete.setEntity(entity);
        
		HttpClientResponseHandler<ResponseEntity<String>> responseHandler = response -> {
			int status = response.getCode();
			if (status >= 200 && status < 300) {
				return new ResponseEntity<>(EntityUtils.toString(response.getEntity()), HttpStatus.OK);
			} else {
				throw new HttpResponseException(status, "Unexpected response status: " + status);
			}
		};
		try {
			ResponseEntity<String> responseEntity = httpClient.execute(httpDelete, responseHandler);
			return new CommonResponseModel<String>(responseEntity.getBody());
		} catch (IOException e) {
			e.printStackTrace();
			return new CommonResponseModel<String>("IOException");
		}
    }
}
