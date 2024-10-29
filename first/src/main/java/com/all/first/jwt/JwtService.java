package com.all.first.jwt;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.all.first.common.KeystoreUtil;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jose.JWEEncrypter;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.ECDHDecrypter;
import com.nimbusds.jose.crypto.ECDHEncrypter;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Service
public class JwtService {
	public String encryptJwe(JwtModel jwe) {
		if(StringUtils.isBlank(jwe.getRequestData())) {
			return "jwe empty";
		}
		
		Payload payload = new Payload(jwe.getRequestData());
		JWEHeader jweHeader = null;
		JWEEncrypter jweEncrypter = null;
		JWEObject jweObject = null;
		try {
			if(jwe.getEncryptionAlgo().equals("EC")) {
				jweHeader = new JWEHeader(JWEAlgorithm.ECDH_ES_A256KW, EncryptionMethod.A256GCM);
				jweEncrypter = new ECDHEncrypter(KeystoreUtil.getSecondEcPublicKey());
			}
			else {
				jweHeader = new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM);
				jweEncrypter = new RSAEncrypter(KeystoreUtil.getSecondRsaPublicKey());
			}
			
			jweObject = new JWEObject(jweHeader, payload);
			jweObject.encrypt(jweEncrypter);
		} catch (JOSEException e) {
			e.printStackTrace();
		}
		
		return jweObject.serialize();
	}
	
	public String decryptJwe(JwtModel jwe) {
		if(StringUtils.isBlank(jwe.getRequestData())) {
			return "jwe empty";
		}
		
		JWEObject jweObject = null;
		JWEDecrypter jweDecrypter = null;
		try {			
			if(jwe.getEncryptionAlgo().equals("EC")) {
				jweDecrypter = new ECDHDecrypter(KeystoreUtil.getFirstEcPrivateKey());
			}
			else {			
				jweDecrypter = new RSADecrypter(KeystoreUtil.getFirstRsaPrivateKey());
			}
			
			jweObject = JWEObject.parse(jwe.getRequestData());
			jweObject.decrypt(jweDecrypter);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JOSEException e) {
			e.printStackTrace();
		}
		
		return jweObject.getPayload().toString();
	}
	
	public String signJws(JwtModel jwe) {
		if(StringUtils.isBlank(jwe.getRequestData())) {
			return "plainText empty";
		}
		
		Payload payload = new Payload(jwe.getRequestData());
		JWSHeader jwsHeader = null;
		JWSSigner jwsSigner = null;
		JWSObject jwsObject = null;
		try {
			if(jwe.getEncryptionAlgo().equals("EC")) {
				jwsHeader = new JWSHeader(JWSAlgorithm.ES256);
				jwsSigner = new ECDSASigner(KeystoreUtil.getFirstEcPrivateKey());
			}
			else {
				jwsHeader = new JWSHeader(JWSAlgorithm.RS256);
				jwsSigner = new RSASSASigner(KeystoreUtil.getFirstRsaPrivateKey());
			}
			
			jwsObject = new JWSObject(jwsHeader, payload);
			jwsObject.sign(jwsSigner);
		} catch (JOSEException e) {
			e.printStackTrace();
		}
		
		return jwsObject.serialize();
	}
	
	public String verifyJws(JwtModel jws) {
		if(StringUtils.isBlank(jws.getRequestData())) {
			return "jws empty";
		}
		
		boolean validJws = false;
		JWSVerifier jwsVerifier = null;
		JWSObject jwsObject = null;
		try {
			if(jws.getEncryptionAlgo().equals("EC")) {
				jwsVerifier = new ECDSAVerifier(KeystoreUtil.getSecondEcPublicKey());
			}
			else {
				jwsVerifier = new RSASSAVerifier(KeystoreUtil.getSecondRsaPublicKey());
			}
			
			jwsObject = JWSObject.parse(jws.getRequestData());
			validJws = jwsObject.verify(jwsVerifier);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JOSEException e) {
			e.printStackTrace();
		}
		
		if(validJws) {
			return jwsObject.getPayload().toString();
		}
		
		return "JWS has been altered";
	}
	
	public String encryptJwt(JwtModel jwt) {
		if(StringUtils.isBlank(jwt.getRequestData())) {
			return "plainText empty";
		}
		
		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
				.issuer("firstServer").subject("secondServer").audience("audience")
				.expirationTime(new Date()).notBeforeTime(new Date())
				.issueTime(new Date()).jwtID("A1234").claim("data", jwt.getRequestData()).build();
		JWEHeader jweHeader = null;
		JWEEncrypter jweEncrypter = null;
		EncryptedJWT encryptedJWT = null;
		try {
			if(jwt.getEncryptionAlgo().equals("EC")) {
				jweHeader = new JWEHeader(JWEAlgorithm.ECDH_ES_A256KW, EncryptionMethod.A256GCM);
				jweEncrypter = new ECDHEncrypter(KeystoreUtil.getSecondEcPublicKey());
			}
			else {
				jweHeader = new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM);
				jweEncrypter = new RSAEncrypter(KeystoreUtil.getSecondRsaPublicKey());
			}
			
			encryptedJWT = new EncryptedJWT(jweHeader, jwtClaimsSet);
			encryptedJWT.encrypt(jweEncrypter);
		} catch (JOSEException e) {
			e.printStackTrace();
		}
		
		return encryptedJWT.serialize();
	}
	
	public String decryptJwt(JwtModel jwt) {
		if(StringUtils.isBlank(jwt.getRequestData())) {
			return "jwt empty";
		}
		
		JWEDecrypter jweDecrypter = null;
		EncryptedJWT encryptedJWT = null;
		try {
			if(jwt.getEncryptionAlgo().equals("EC")) {
				jweDecrypter = new ECDHDecrypter(KeystoreUtil.getFirstEcPrivateKey());
			}
			else {
				jweDecrypter = new RSADecrypter(KeystoreUtil.getFirstRsaPrivateKey());
			}
			
			encryptedJWT = EncryptedJWT.parse(jwt.getRequestData());
			encryptedJWT.decrypt(jweDecrypter);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JOSEException e) {
			e.printStackTrace();
		}
		
		return encryptedJWT.getPayload().toString();
	}
	
	public String signJwt(JwtModel jwt) {
		if(StringUtils.isBlank(jwt.getRequestData())) {
			return "plainText empty";
		}
		
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.issuer("firstServer").subject("secondServer").audience("audience")
				.expirationTime(new Date()).notBeforeTime(new Date())
				.issueTime(new Date()).jwtID("A1234").claim("data", jwt.getRequestData()).build();
		JWSHeader jwsHeader = null;
		JWSSigner jwsSigner = null;
		SignedJWT signedJWT = null;
		try {
			if(jwt.getEncryptionAlgo().equals("EC")) {
				jwsHeader = new JWSHeader(JWSAlgorithm.ES256);
				jwsSigner = new ECDSASigner(KeystoreUtil.getFirstEcPrivateKey());
			}
			else {
				jwsHeader = new JWSHeader(JWSAlgorithm.RS256);
				jwsSigner = new RSASSASigner(KeystoreUtil.getFirstRsaPrivateKey());
			}
			
			signedJWT = new SignedJWT(jwsHeader, claimsSet);
			signedJWT.sign(jwsSigner);
		} catch (JOSEException e) {
			e.printStackTrace();
		}
		
		return signedJWT.serialize();
	}
	
	public String verifyJwt(JwtModel jwt) {
		if(StringUtils.isBlank(jwt.getRequestData())) {
			return "jwt empty";
		}
		
		boolean validJWT = false;
		JWSVerifier jwsVerifier = null;
		SignedJWT signedJWT = null;
		try {
			if(jwt.getEncryptionAlgo().equals("EC")) {
				jwsVerifier = new ECDSAVerifier(KeystoreUtil.getSecondEcPublicKey());
			}
			else {
				jwsVerifier = new RSASSAVerifier(KeystoreUtil.getSecondRsaPublicKey());
			}
			signedJWT = SignedJWT.parse(jwt.getRequestData());
			validJWT = signedJWT.verify(jwsVerifier);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JOSEException e) {
			e.printStackTrace();
		}
		
		if(validJWT) {
			return signedJWT.getPayload().toString();
		}
		
		return "signed JWT has been altered";
	}
}
