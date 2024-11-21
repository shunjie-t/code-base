package com.all.second.dao;

import java.math.BigDecimal;
import java.util.List;

import com.all.second.dao.view.DataView;

public interface IDataJpaWrapper {
	List<DataView> findAll();
	List<DataView> findAllByCreBy(String user);
	List<DataView> saveAllPostAndVerify(List<DataView> dataView) throws Exception;
	List<DataView> saveAllPutAndVerify(List<DataView> dataView) throws Exception;
	List<DataView> saveAllPatchAndVerify(List<DataView> dataView) throws Exception;
	String verifyAndDelete(List<BigDecimal> id, String user);
}
