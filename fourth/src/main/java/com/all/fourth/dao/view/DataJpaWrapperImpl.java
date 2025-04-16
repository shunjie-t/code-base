package com.all.fourth.dao.view;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.all.fourth.dao.DataJpaDao;

@Component
public class DataJpaWrapperImpl implements IDataJpaWrapper {
	@Autowired
	private DataJpaDao dataJpaDao;
	
	@Override
	public List<DataView> findAllById(List<BigDecimal> id) {
		return dataJpaDao.findAllById(id);
	}
	
	@Override
	public List<DataView> findByConditions(List<String> dataType, Boolean enabled, List<String> createdBy, List<String> updatedBy) {
		return dataJpaDao.findByConditions(validateField(dataType), enabled, validateField(createdBy), validateField(updatedBy));
	}

	@Override
	public List<DataView> saveAllPostAndVerify(List<DataView> dataView) throws Exception {
		List<DataView> resultList = dataJpaDao.saveAll(dataView);
		if(dataViewIsEqual(dataView, resultList)) {
			throw new Exception();
		}
		return resultList;
	}
	
	@Override
	public boolean deleteAllById(List<BigDecimal> id) {
		dataJpaDao.deleteAllById(id);
		List<DataView> records = dataJpaDao.findAllById(id);
		return records.isEmpty();
	}
	
	@Override
	public boolean deleteByConditions(List<String> dataType, Boolean enabled, List<String> createdBy, List<String> updatedBy) {
		dataJpaDao.deleteByConditions(validateField(dataType), enabled, validateField(createdBy), validateField(updatedBy));
		List<DataView> records = dataJpaDao.findByConditions(validateField(dataType), enabled, validateField(createdBy), validateField(updatedBy));
		return records.isEmpty();
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
			dv1.get(i).isEnabled() != dv2.get(i).isEnabled()) {
				return true;
			}
		}
		return false;
	}
	
	private List<String> validateField(List<String> field) {
		return field.isEmpty() ? null : field;
	}
}
