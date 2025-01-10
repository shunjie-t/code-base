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
	public List<DataView> findByConditions(List<String> dataType, boolean enabled, List<String> createdBy, List<String> UpdatedBy) {
		return dataJpaDao.findByConditions(dataType, enabled, createdBy, UpdatedBy);
	}

	@Override
	public List<DataView> saveAllPostAndVerify(List<DataView> dataView) throws Exception {
		List<DataView> resultList = dataJpaDao.saveAll(dataView);
		if(!resultList.equals(dataView)) {
			throw new Exception();
		}
		return resultList;
	}

}
