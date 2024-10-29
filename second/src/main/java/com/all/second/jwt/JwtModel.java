package com.all.second.jwt;

import com.all.second.common.CommonRequestModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtModel extends CommonRequestModel {
	private String encryptionAlgo;
}
