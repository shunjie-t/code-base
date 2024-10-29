package com.all.second.encryption;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EncryptionResponseModel {
	private String cipherText;
	private String SecretKey;
	private String iv;
	private String tag;
	private String salt;
}
