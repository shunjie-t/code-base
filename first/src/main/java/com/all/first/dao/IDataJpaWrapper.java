package com.all.first.dao;

import java.math.BigDecimal;
import java.util.List;

import com.all.first.dao.view.DataView;

public interface IDataJpaWrapper {
	List<DataView> findAll();
	List<DataView> findAllByCreBy(String user);
	List<DataView> saveAllPostAndVerify(List<DataView> dataView) throws Exception;
	List<DataView> saveAllPutAndVerify(List<DataView> dataView) throws Exception;
	List<DataView> saveAllPatchAndVerify(List<DataView> dataView) throws Exception;
	String verifyAndDelete(List<BigDecimal> id, String user);
}
