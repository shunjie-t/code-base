package com.all.second.common;

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
	private static ECPrivateKey secondEcPrivateKey;
	private static ECPublicKey  firstEcPublicKey;
	private static RSAPrivateKey secondRsaPrivateKey;
	private static RSAPublicKey firstRsaPublicKey;
	
	public KeystoreUtil() {
		try {
			FileInputStream input = new FileInputStream(Constants.keystorePath);
			KeyStore keystore = KeyStore.getInstance("PKCS12");
			String keystorePass = "changeit";
			keystore.load(input, keystorePass.toCharArray());
			secondEcPrivateKey = (ECPrivateKey) keystore.getKey("second-ec-private-key", keystorePass.toCharArray());
			secondRsaPrivateKey = (RSAPrivateKey) keystore.getKey("second-rsa-private-key", keystorePass.toCharArray());
			
			Certificate secondCert = keystore.getCertificate("first-ec-public-key");
			firstEcPublicKey = (ECPublicKey) secondCert.getPublicKey();
			Certificate firstCert = keystore.getCertificate("first-rsa-public-key");
			firstRsaPublicKey = (RSAPublicKey) firstCert.getPublicKey();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ECPrivateKey getSecondEcPrivateKey() {
		return secondEcPrivateKey;
	}

	public static ECPublicKey getFirstEcPublicKey() {
		return firstEcPublicKey;
	}

	public static RSAPrivateKey getSecondRsaPrivateKey() {
		return secondRsaPrivateKey;
	}

	public static RSAPublicKey getFirstRsaPublicKey() {
		return firstRsaPublicKey;
	}
}
