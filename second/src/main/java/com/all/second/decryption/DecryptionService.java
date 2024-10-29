package com.all.second.decryption;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.MGF1ParameterSpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
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

import com.all.second.common.Constants;
import com.all.second.common.KeystoreUtil;

@Service
public class DecryptionService {
	private static final String KEY_ALGO = "RSA/ECB/OAEPWithSHA-512AndMGF1Padding";
	private static final String TEXT_ALGO = "AES/GCM/NoPadding";
	private static final Charset UTF_8 = StandardCharsets.UTF_8;
	
	private static final String SECRET_KEY_ALGO = "PBKDF2WithHmacSHA1";
	private static final int AES_HASHING_ITERATION = 1000;
	
	public String decryptInfo(DecryptionRequestModel request, String version) {
		String response = Constants.invalidRequest;
		
		if(version.equals("v1") || version.equals("V1")) {
			response = decryptInfoV1(request);
		}
		else if(version.equals("v2") || version.equals("V2")) {
			response = decryptInfoV2(request);
		}
		
		return response;
	}
	
	private String decryptInfoV1(DecryptionRequestModel request) {
		try {
			byte[] cipherText = Base64.getDecoder().decode(request.getCipherText());
			byte[] secrectKey = Base64.getDecoder().decode(request.getSecretKey());
			byte[] iv = Base64.getDecoder().decode(request.getIv());
			byte[] tag = Base64.getDecoder().decode(request.getTag());
			
			Cipher keyCipher = Cipher.getInstance(KEY_ALGO);
			OAEPParameterSpec oaepParameterSpec = new OAEPParameterSpec("SHA-512", "MGF1", MGF1ParameterSpec.SHA512, PSource.PSpecified.DEFAULT);
			keyCipher.init(Cipher.DECRYPT_MODE, KeystoreUtil.getSecondRsaPrivateKey(), oaepParameterSpec);
			byte[] aesKey = keyCipher.doFinal(secrectKey);
			
			Cipher payloadCipher = Cipher.getInstance(TEXT_ALGO);
			GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
			SecretKeySpec secretKeySpec = new SecretKeySpec(aesKey, "AES");
			payloadCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
			payloadCipher.update(cipherText);
			byte[] plainText = payloadCipher.doFinal(tag);
			
			return new String(plainText, UTF_8);
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
		}
		
		return Constants.invalidRequest;
	}
	
	private String decryptInfoV2(DecryptionRequestModel request) {
		byte[] cipherText = Base64.getDecoder().decode(request.getCipherText());
		byte[] iv = Base64.getDecoder().decode(request.getIv());
		byte[] salt = Base64.getDecoder().decode(request.getSalt());
		
		try {
			Cipher cipher = Cipher.getInstance(TEXT_ALGO);
			GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SECRET_KEY_ALGO);
			KeySpec spec = new PBEKeySpec(Constants.password.toCharArray(), Hex.decodeHex(DatatypeConverter.printHexBinary(salt).toCharArray()), AES_HASHING_ITERATION, 128);
			SecretKey key = new SecretKeySpec(keyFactory.generateSecret(spec).getEncoded(), "AES");
			cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);
			byte[] plainText = cipher.doFinal(cipherText);
			
			return new String(plainText, UTF_8);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (DecoderException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		
		return Constants.invalidRequest;
	}
}
