package com.all.zeroth.hash;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import com.all.zeroth.common.Constants;

@Service
public class HashService {
	public String hmac(String data) {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(Constants.hmacSecretKey.getBytes(), Constants.hmacMethod);
			Mac mac = Mac.getInstance(Constants.hmacMethod);
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(data.getBytes());
			StringBuilder result = new StringBuilder();
			
			for(byte d : rawHmac) {
				result.append(String.format(Constants.hexadecimalFormat, d));
			}
			
			return result.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		
		return Constants.invalidRequest;
	}
	
	public String cmac(String data) {
		Security.addProvider(new BouncyCastleProvider());
		byte[] key = Constants.cmacSecretKey.getBytes();
		CMac cmac = new CMac(AESEngine.newInstance());
		CipherParameters keyParam = new KeyParameter(key);
		cmac.init(keyParam);
		cmac.update(data.getBytes(), 0, data.getBytes().length);
		byte[] mac = new byte[cmac.getMacSize()];
		cmac.doFinal(mac, 0);
		
		return DatatypeConverter.printHexBinary(mac);
	}
	
	public String sha512hashing(String data) {
		return hashing(data, Constants.SHA_512);
	}
	
	public String sha384hashing(String data) {
		return hashing(data, Constants.SHA_384);
	}
	
	public String sha256hashing(String data) {
		return hashing(data, Constants.SHA_256);
	}
	
	public String sha1hashing(String data) {
		return hashing(data, Constants.SHA_1);
	}
	
	public String md5hashing(String data) {
		return hashing(data, Constants.MD5);
	}
	
	private String hashing(String data, String algorithm) {
		/*Message digests are secure one-way hash functions that take arbitrary-sized data and output a fixed-length hash value.*/
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			byte[] hash = digest.digest(data.getBytes());
			StringBuilder result = new StringBuilder();
			
			for(byte d : hash) {
				result.append(String.format(Constants.hexadecimalFormat, d));
			}
			
			return result.toString().toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return Constants.invalidRequest;
	}
}
