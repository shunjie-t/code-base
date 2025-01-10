package com.all.fourth.dao.view;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IDataJpaWrapper {
	List<DataView> findAllById(List<BigDecimal> id);
	List<DataView> findByConditions(List<String> dataType, boolean enabled, List<String> createdBy, List<String> UpdatedBy);
	List<DataView> saveAllPostAndVerify(List<DataView> dataView) throws Exception;
}
