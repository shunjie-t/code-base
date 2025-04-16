package com.all.second.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.all.second.dao.view.DataView;

@Component
public class DataJpaWrapperImpl implements IDataJpaWrapper {
	@Autowired
	private DataJpaDao dataJpaDao;

	@Override
	public List<DataView> findAll() {
		List<DataView> result = dataJpaDao.findAll();
		return result;
	}

	@Override
	public List<DataView> findAllByCreBy(String creBy) {
		return dataJpaDao.findAllByCreBy(creBy);
	}

	@Override
	public List<DataView> saveAllPostAndVerify(List<DataView> dataView) throws Exception {
		List<DataView> resultList = dataJpaDao.saveAll(dataView);
		if(dataViewIsEqual(dataView, resultList)) {
			throw new Exception();
		}
		return resultList;
	}
	
	private boolean dataViewIsEqual(List<DataView> dv1, List<DataView> dv2) {
		for(int i = 0; i < dv1.size(); i++) {
			if(!dv1.get(i).getDataId().equals(dv2.get(i).getDataId()) || 
			!dv1.get(i).getDataType().equals(dv2.get(i).getDataType()) || 
			!dv1.get(i).getData().equals(dv2.get(i).getData()) ||
			!dv1.get(i).getCreBy().equals(dv2.get(i).getCreBy()) ||
			!dv1.get(i).getCreOn().equals(dv2.get(i).getCreOn()) ||
			!dv1.get(i).getUpdBy().equals(dv2.get(i).getUpdBy()) ||
			!dv1.get(i).getUpdOn().equals(dv2.get(i).getUpdOn()) ||
			!dv1.get(i).isEnabled() == dv2.get(i).isEnabled()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<DataView> saveAllPutAndVerify(List<DataView> dataView) throws Exception {
		List<BigDecimal> ids = dataView.stream().filter(data -> data.getDataId() != null).map(data -> data.getDataId()).toList();
		List<DataView> withoutId = dataView.stream().filter(data -> data.getDataId() == null).map(data -> data).toList();
		dataView.removeAll(withoutId);
		List<DataView> existing = dataJpaDao.findAllById(ids);
		for(int i = 0; i < dataView.size(); i++) {
			if(dataView.get(i).getDataId() != null && !dataJpaDao.existsById(dataView.get(i).getDataId())) {
				throw new Exception();
			}
			else {
				dataView.get(i).setCreBy(existing.get(i).getCreBy());
				dataView.get(i).setCreOn(existing.get(i).getCreOn());
			}
		}
		dataView.addAll(withoutId);
		
		List<DataView> resultList = dataJpaDao.saveAll(dataView);
		for(int i = 0; i < resultList.size(); i++) {
			DataView result = resultList.get(i);
			DataView data = dataView.get(i);
			if(
					result.getDataId().intValue() != data.getDataId().intValue() || 
					!result.getDataType().toString().equals(data.getDataType().toString()) || 
					!result.getData().toString().equals(data.getData().toString()) || 
					!result.getCreBy().toString().equals(data.getCreBy().toString()) || 
					!result.getCreOn().toString().equals(data.getCreOn().toString())
				) {				
				throw new Exception();
			}
		}
		return resultList;
	}
	
	@Override
	public List<DataView> saveAllPatchAndVerify(List<DataView> dataView) throws Exception {
		List<BigDecimal> ids = dataView.stream().filter(data -> data.getDataId() != null).map(data -> data.getDataId()).toList();
		List<DataView> existing = dataJpaDao.findAllById(ids);
		for(int i = 0; i < dataView.size(); i++) {
			if(dataView.get(i).getDataType() == null) {
				dataView.get(i).setDataType(existing.get(i).getDataType());
			}
			if(dataView.get(i).getData() == null) {
				dataView.get(i).setData(existing.get(i).getData());
			}
			if(dataView.get(i).getCreBy() == null) {
				dataView.get(i).setCreBy(existing.get(i).getCreBy());
			}
			if(dataView.get(i).getCreOn() == null) {
				dataView.get(i).setCreOn(existing.get(i).getCreOn());
			}
		}
		
		List<DataView> resultList = dataJpaDao.saveAll(dataView);
		for(int i = 0; i < resultList.size(); i++) {
			DataView result = resultList.get(i);
			DataView data = dataView.get(i);
			if(
					result.getDataId().intValue() != data.getDataId().intValue() || 
					(data.getDataType() != null && !result.getDataType().toString().equals(data.getDataType().toString())) || 
					(data.getData() != null && !result.getData().toString().equals(data.getData().toString())) || 
					(data.getCreBy() != null && !result.getCreBy().toString().equals(data.getCreBy().toString())) || 
					(data.getCreOn() != null && !result.getCreOn().toString().equals(data.getCreOn().toString()))
				) {				
				throw new Exception();
			}
		}
		return resultList;
	}
	
	@Override
	public String verifyAndDelete(List<BigDecimal> id, String user) {
		List<DataView> records = dataJpaDao.findAllById(id);
		if(records.isEmpty()) {
			return "No exsiting records to delete";
		}
		
		if(!StringUtils.isBlank(user)) {			
			for(DataView view : records) {
				if(!view.getCreBy().equals(user)) {
					id.remove(view.getDataId());
				}
			}
		}
		
		if(!id.isEmpty()) {			
			dataJpaDao.deleteAllById(id);
			return "Delete request successful";
		}
		return "No exsiting records to delete";
	}
}
