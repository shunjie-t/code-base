package com.all.second.rest.target;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.all.second.common.Constants;
import com.all.second.dao.IDataJpaWrapper;
import com.all.second.dao.view.DataView;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/second")
public class RestTargetController implements InitializingBean {
	@Autowired
	private IDataJpaWrapper dataJpaWrapper;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		mapper.findAndRegisterModules();
	}
	
	@GetMapping(value = "/target")
	public List<DataView> getTarget() {
		return dataJpaWrapper.findAll();
	}
	
	@GetMapping(value = "/target/{creBy}")
	public ResponseEntity<List<DataView>> getTarget(@PathVariable String creBy) {
		List<DataView> response = dataJpaWrapper.findAllByCreBy(creBy);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(path = "/target")
	public ResponseEntity<String> postTarget(@RequestBody List<DataView> body, @RequestHeader(Constants.X_API_KEY) String header) {
		return savePostRequest(body, header, null);
	}
	
	@PostMapping(path = "/target/{user}")
	public ResponseEntity<String> postTarget(@RequestBody List<DataView> body, @RequestHeader(Constants.X_API_KEY) String header, @PathVariable String user) {
		return savePostRequest(body, header, user);
	}
	
	@Transactional(rollbackFor = (Exception.class))
	private ResponseEntity<String> savePostRequest(List<DataView> body, String header, String user) {
		if(!header.equals(Constants.X_API_KEY_VALUE)) {
			return new ResponseEntity<>("Invalid api key", HttpStatus.BAD_REQUEST);
		}
		
		for(DataView view : body) {
			if(view.getDataId() != null) {
				return new ResponseEntity<>("Data id must be blank", HttpStatus.BAD_REQUEST);
			}
			else if(StringUtils.isBlank(view.getDataType())) {
				return new ResponseEntity<>("Data type must not be blank", HttpStatus.BAD_REQUEST);
			}
			else if(StringUtils.isBlank(view.getData())) {
				return new ResponseEntity<>("Data must not be blank", HttpStatus.BAD_REQUEST);
			}
			else if(StringUtils.isBlank(view.getCreBy())) {
				if(user == null) {					
					return new ResponseEntity<>("Created by must not be blank", HttpStatus.BAD_REQUEST);
				}
				view.setCreBy(user);
			}
			else if(view.getCreOn() == null) {
				return new ResponseEntity<>("Created on must not be blank", HttpStatus.BAD_REQUEST);
			}
			else if(StringUtils.isBlank(view.getUpdBy())) {
				if(user == null) {					
					return new ResponseEntity<>("Updated by must not be blank", HttpStatus.BAD_REQUEST);
				}
				view.setUpdBy(user);
			}
			else if(view.getUpdOn() == null) {
				return new ResponseEntity<>("Updated on must not be blank", HttpStatus.BAD_REQUEST);
			}
		}
		
		try {
			List<DataView> result = dataJpaWrapper.saveAllPostAndVerify(body);
			String responseString = mapper.writeValueAsString(result);
			return new ResponseEntity<>(responseString, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Exception. Created failed, data reverted", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(path = "/target")
	public ResponseEntity<String> putTarget(@RequestBody List<DataView> body, @RequestHeader(Constants.X_API_KEY) String header) {
		return savePutRequest(body, header, null);
	}
	
	@PutMapping(path = "/target/{user}")
	public ResponseEntity<String> putTarget(@RequestBody List<DataView> body, @RequestHeader(Constants.X_API_KEY) String header, @PathVariable String user) {
		return savePutRequest(body, header, user);
	}
	
	@Transactional(rollbackFor = (Exception.class))
	private ResponseEntity<String> savePutRequest(List<DataView> body, String header, String user) {
		if(!header.equals(Constants.X_API_KEY_VALUE)) {
			return new ResponseEntity<>("Invalid api key", HttpStatus.BAD_REQUEST);
		}
		
		for(DataView view : body) {
			if(view.getDataId() == null) {
				if(StringUtils.isBlank(view.getCreBy())) {
					if(user == null) {						
						return new ResponseEntity<>("Blank data id must not have blank created by", HttpStatus.BAD_REQUEST);
					}
					view.setCreBy(user);
				}
				else if(view.getCreOn() == null) {
					return new ResponseEntity<>("Blank data id must not have blank created on", HttpStatus.BAD_REQUEST);
				}
			}
			else if(view.getDataId() != null) {
				if(StringUtils.isNotBlank(view.getCreBy())) {					
					return new ResponseEntity<>("Not blank data id must have blank created by", HttpStatus.BAD_REQUEST);
				}
				else if(view.getCreOn() != null) {
					return new ResponseEntity<>("Not blank data id must have blank created on", HttpStatus.BAD_REQUEST);
				}
			}
			
			if(StringUtils.isBlank(view.getDataType())) {
				return new ResponseEntity<>("Data type must not be blank", HttpStatus.BAD_REQUEST);
			}
			else if(StringUtils.isBlank(view.getData())) {
				return new ResponseEntity<>("Data must not be blank", HttpStatus.BAD_REQUEST);
			}
			else if(StringUtils.isBlank(view.getUpdBy())) {
				if(user == null) {					
					return new ResponseEntity<>("Updated by must not be blank", HttpStatus.BAD_REQUEST);
				}
				view.setUpdBy(user);
			}
			else if(view.getUpdOn() == null) {
				return new ResponseEntity<>("Updated on must not be blank", HttpStatus.BAD_REQUEST);
			}
		}
		
		try {
			List<DataView> result = dataJpaWrapper.saveAllPutAndVerify(body);
			String responseString = mapper.writeValueAsString(result);
			return new ResponseEntity<>(responseString, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Exception. Update failed, data reverted", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PatchMapping(path = "/target")
	public ResponseEntity<String> patchTarget(@RequestBody List<DataView> body, @RequestHeader(Constants.X_API_KEY) String header) {
		return savePatchRequest(body, header, null);
	}
	
	@PatchMapping(path = "/target/{user}")
	public ResponseEntity<String> patchTarget(@RequestBody List<DataView> body, @RequestHeader(Constants.X_API_KEY) String header, @PathVariable String user) {
		return savePatchRequest(body, header, user);
	}
	
	@Transactional(rollbackFor = (Exception.class))
	private ResponseEntity<String> savePatchRequest(List<DataView> body, String header, String user) {
		if(!header.equals(Constants.X_API_KEY_VALUE)) {
			return new ResponseEntity<>("Invalid api key", HttpStatus.BAD_REQUEST);
		}
		
		for(DataView view : body) {
			if(view.getDataId() == null) {
				return new ResponseEntity<>("Data id must not be blank", HttpStatus.BAD_REQUEST);
			}
			else if(StringUtils.isNotBlank(view.getCreBy())) {
				return new ResponseEntity<>("Created by must be blank", HttpStatus.BAD_REQUEST);
			}
			else if(view.getCreOn() != null) {
				return new ResponseEntity<>("Created on must be blank", HttpStatus.BAD_REQUEST);
			}
			else if(StringUtils.isBlank(view.getUpdBy())) {
				if(user == null) {					
					return new ResponseEntity<>("Updated by must not be blank", HttpStatus.BAD_REQUEST);
				}
				view.setUpdBy(user);
			}
			else if(view.getUpdOn() == null) {
				return new ResponseEntity<>("Updated on must not be blank", HttpStatus.BAD_REQUEST);
			}
		}
		
		try {
			List<DataView> result = dataJpaWrapper.saveAllPatchAndVerify(body);
			String responseString = mapper.writeValueAsString(result);
			return new ResponseEntity<>(responseString, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Exception. Patch failed, data reverted", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(path = "/target")
	public ResponseEntity<String> deleteTarget(@RequestBody List<BigDecimal> body, @RequestHeader(Constants.X_API_KEY) String header) {
		return verifyAndDelete(body, header, null);
	}
	
	@DeleteMapping(path = "/target/{user}")
	public ResponseEntity<String> deleteTarget(@RequestBody List<BigDecimal> body, @RequestHeader(Constants.X_API_KEY) String header, @PathVariable String user) {
		return verifyAndDelete(body, header, user);
	}
	
	private ResponseEntity<String> verifyAndDelete(List<BigDecimal> body, String header, String user) {
		if(!header.equals(Constants.X_API_KEY_VALUE)) {
			return new ResponseEntity<>("Invalid api key", HttpStatus.BAD_REQUEST);
		}
		
		String responseString = dataJpaWrapper.verifyAndDelete(body, user);
		return new ResponseEntity<>(responseString, HttpStatus.OK);
	}
}
