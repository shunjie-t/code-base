package com.all.second.dao.view;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class AuditFields {
	@Column(name="CREATED_BY")
	private String creBy;
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name="CREATED_ON")
	private LocalDateTime creOn;
	
	@Column(name="UPDATED_BY")
	private String updBy;
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name="UPDATED_ON")
	private LocalDateTime updOn;
}
