package com.all.first.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.stereotype.Component;

@Component
public class KeystoreUtil {
	private static ECPrivateKey firstEcPrivateKey;
	private static ECPublicKey secondEcPublicKey;
	private static RSAPrivateKey firstRsaPrivateKey;
	private static RSAPublicKey secondRsaPublicKey;
	
	public KeystoreUtil() {
		try {
			FileInputStream inputStream = new FileInputStream(Constants.keystorePath);
			KeyStore keystore = KeyStore.getInstance("PKCS12");
			String keyStorePassword = "changeit";
			keystore.load(inputStream, keyStorePassword.toCharArray());
			firstEcPrivateKey = (ECPrivateKey) keystore.getKey("first-ec-private-key", keyStorePassword.toCharArray());
			firstRsaPrivateKey = (RSAPrivateKey) keystore.getKey("first-rsa-private-key", keyStorePassword.toCharArray());
			
			Certificate secondEcCert = keystore.getCertificate("second-ec-public-key");
			secondEcPublicKey = (ECPublicKey) secondEcCert.getPublicKey();
			Certificate secondRsaCert = keystore.getCertificate("second-rsa-public-key");
			secondRsaPublicKey = (RSAPublicKey) secondRsaCert.getPublicKey();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		}
	}

	public static ECPrivateKey getFirstEcPrivateKey() {
		return firstEcPrivateKey;
	}

	public static ECPublicKey getSecondEcPublicKey() {
		return secondEcPublicKey;
	}
	
	public static RSAPrivateKey getFirstRsaPrivateKey() {
		return firstRsaPrivateKey;
	}
	
	public static RSAPublicKey getSecondRsaPublicKey() {
		return secondRsaPublicKey;
	}
}
