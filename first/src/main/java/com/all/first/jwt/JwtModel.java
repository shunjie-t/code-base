package com.all.first.jwt;

import com.all.first.common.CommonRequestModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtModel extends CommonRequestModel {
	private String encryptionAlgo;
}
