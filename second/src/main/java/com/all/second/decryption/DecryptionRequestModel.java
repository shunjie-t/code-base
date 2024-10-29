package com.all.second.decryption;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DecryptionRequestModel {
	private String cipherText;
	private String secretKey;
	private String iv;
	private String tag;
	private String salt;
}
