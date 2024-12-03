package com.all.third.dao;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.all.third.dao.view.DataView;

public interface DataJpaDao extends JpaRepository<DataView, BigDecimal> {

}
