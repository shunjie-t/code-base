package com.all.first.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.all.first.dao.view.DataView;

@Repository
public interface DataJpaDao extends JpaRepository<DataView, BigDecimal> {
	List<DataView> findAllByCreBy(String creBy);
}
