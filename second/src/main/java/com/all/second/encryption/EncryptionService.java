package com.all.second.encryption;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.all.second.common.CommonResponseModel;
import com.all.second.common.Constants;
import com.all.second.common.KeystoreUtil;

@Service
public class EncryptionService {
	private static final String UTF_8 = "UTF-8";
	private static final String TEXT_ALGO = "AES/GCM/NoPadding";
	private static final String KEY_ALGO = "RSA/ECB/OAEPWithSHA-512AndMGF1Padding";
	
	private static final String AES = "AES";
	private static final int BIT_128 = 128;
	private static final int BIT_256 = 256;
	private static final String SECRET_KEY_ALGO = "PBKDF2WithHmacSHA1";
	private static final int AES_HASHING_ITERATION = 1000;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public CommonResponseModel encryptInfo(String plainText, String version) {
		CommonResponseModel response = new CommonResponseModel(Constants.invalidRequest);
		
		if(version.equals("v1") || version.equals("V1")) {				
			response.setResponseData(encryptInfoV1(plainText));
		}
		else if(version.equals("v2") || version.equals("V2")) {
			response.setResponseData(encryptInfoV2(plainText));
		}
		
		return response;
	}
	
	public String encryptInfoV1(String plainText) {
		KeyGenerator keyGenerator = null;
		
		try {
			keyGenerator = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return Constants.invalidRequest;
		}
		
		keyGenerator.init(BIT_256);
		SecretKey aesKey = keyGenerator.generateKey();
		
		byte[] iv = new byte[12];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(iv);
		
		try {
			Cipher payloadCipher = Cipher.getInstance(TEXT_ALGO);
			GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
			String tagStr = "";
			payloadCipher.init(Cipher.ENCRYPT_MODE, aesKey, gcmParameterSpec);
			payloadCipher.updateAAD(tagStr.getBytes(UTF_8));
			byte[] cipherText = payloadCipher.doFinal(plainText.getBytes(UTF_8));
			
			Cipher keyCipher = Cipher.getInstance(KEY_ALGO);
			OAEPParameterSpec oaepParameterSpec = new OAEPParameterSpec(Constants.SHA_512, "MGF1", MGF1ParameterSpec.SHA512, PSource.PSpecified.DEFAULT);
			keyCipher.init(Cipher.ENCRYPT_MODE, KeystoreUtil.getFirstRsaPublicKey(), oaepParameterSpec);
			byte[] encryptedAesKey = keyCipher.doFinal(aesKey.getEncoded());
			
			EncryptionResponseModel result = new EncryptionResponseModel();
			result.setCipherText(Base64.getEncoder().encodeToString(cipherText));
			result.setSecretKey(Base64.getEncoder().encodeToString(encryptedAesKey));
			result.setIv(Base64.getEncoder().encodeToString(iv));
			result.setTag(tagStr);
			
			byte[] byteArrayResult = mapper.writeValueAsBytes(result);
			String response = Base64.getEncoder().encodeToString(byteArrayResult);
			
			return response;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return Constants.invalidRequest;
	}
	
	public String encryptInfoV2(String plainText) {
		byte[] iv = new byte[16];
		byte[] salt = new byte[16];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(iv);
		secureRandom.nextBytes(salt);
		
		try {
			Cipher cipher = Cipher.getInstance(TEXT_ALGO);
			GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(BIT_128, iv);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SECRET_KEY_ALGO);
			KeySpec spec = new PBEKeySpec(Constants.password.toCharArray(), Hex.decodeHex(DatatypeConverter.printHexBinary(salt).toCharArray()), AES_HASHING_ITERATION, BIT_128);
			SecretKey key = new SecretKeySpec(keyFactory.generateSecret(spec).getEncoded(), AES);
			cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
			byte[] cipherText = cipher.doFinal(plainText.getBytes());
			
			EncryptionResponseModel result = new EncryptionResponseModel();
			result.setCipherText(Base64.getEncoder().encodeToString(cipherText));
			result.setIv(Base64.getEncoder().encodeToString(iv));
			result.setSalt(Base64.getEncoder().encodeToString(salt));
			
			byte[] byteArryResult = mapper.writeValueAsBytes(result);
			String response = Base64.getEncoder().encodeToString(byteArryResult);
			
			return response;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (DecoderException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return Constants.invalidRequest;
	}
	
	public String keyToB64EncodedString(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
	public PublicKey b64EncodedStringToKey(String base64EncodedKey) {		
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(base64EncodedKey.getBytes());
			
			return keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
