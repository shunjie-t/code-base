package com.all.fourth.dao;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.all.fourth.dao.view.DataView;

public interface DataJpaDao extends JpaRepository<DataView, BigDecimal> {

}
