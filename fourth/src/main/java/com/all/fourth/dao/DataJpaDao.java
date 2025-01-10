package com.all.fourth.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.all.fourth.dao.view.DataView;

@Repository
public interface DataJpaDao extends JpaRepository<DataView, BigDecimal> {
	@Query("SELECT dv FROM DataView dv WHERE (:dataType IS NULL OR dv.dataType IN :dataType) AND (:enabled IS NULL OR dv.enabled = :enabled) AND (:creBy IS NULL OR dv.creBy IN :creBy) AND (:updBy IS NULL OR dv.updBy IN :updBy)")
	List<DataView> findByConditions(@Param("dataType") List<String> dataType, @Param("enabled") Boolean enabled, @Param("creBy") List<String> createdBy, @Param("updBy") List<String> UpdatedBy);
}
