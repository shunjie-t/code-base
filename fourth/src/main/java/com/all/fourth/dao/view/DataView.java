package com.all.fourth.dao.view;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="DATA_TBL")
public class DataView extends AuditFields {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="DATA_ID")
	private BigDecimal dataId;
	
	@Column(name="DATA_TYPE")
	private String dataType;
	
	@Column(name="DATA")
	private String data;
	
	@Column(name="ENABLED")
	private boolean enabled = true;
}
