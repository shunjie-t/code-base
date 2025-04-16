package com.all.fourth.dao.view;

import java.math.BigDecimal;
import java.util.List;

public interface IDataJpaWrapper {
	List<DataView> findAllById(List<BigDecimal> id);
	List<DataView> findByConditions(List<String> dataType, Boolean enabled, List<String> createdBy, List<String> updatedBy);
	List<DataView> saveAllPostAndVerify(List<DataView> dataView) throws Exception;
	boolean deleteAllById(List<BigDecimal> id);
	boolean deleteByConditions(List<String> dataType, Boolean enabled, List<String> createdBy, List<String> updatedBy);
}
