package com.all.second.jwt;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.all.second.common.KeystoreUtil;
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
			return "plainText empty";
		}
		
		Payload payload = new Payload(jwe.getRequestData());
		JWEHeader jweHeader = null;
		JWEEncrypter jweEncrypter = null;
		JWEObject jweObject = null;
		try {
			if(jwe.getEncryptionAlgo().equals("EC")) {
				jweHeader = new JWEHeader(JWEAlgorithm.ECDH_ES_A256KW, EncryptionMethod.A256GCM);
				jweEncrypter = new ECDHEncrypter(KeystoreUtil.getFirstEcPublicKey());
			}
			else {
				jweHeader = new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM);
				jweEncrypter = new RSAEncrypter(KeystoreUtil.getFirstRsaPublicKey());
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
				jweDecrypter = new ECDHDecrypter(KeystoreUtil.getSecondEcPrivateKey());
			}
			else {
				jweDecrypter = new RSADecrypter(KeystoreUtil.getSecondRsaPrivateKey());
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
	
	public String signJws(JwtModel jws) {
		if(StringUtils.isBlank(jws.getRequestData())) {
			return "plainText empty";
		}
		
		Payload payload = new Payload(jws.getRequestData());
		JWSHeader jwsHeader = null;
		JWSSigner jwsSigner = null;
		JWSObject jwsObject = null;
		try {
			if(jws.getEncryptionAlgo().equals("EC")) {
				jwsHeader = new JWSHeader(JWSAlgorithm.ES256);
				jwsSigner = new ECDSASigner(KeystoreUtil.getSecondEcPrivateKey());
			}
			else {
				jwsHeader = new JWSHeader(JWSAlgorithm.RS256);
				jwsSigner = new RSASSASigner(KeystoreUtil.getSecondRsaPrivateKey());
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
				jwsVerifier = new ECDSAVerifier(KeystoreUtil.getFirstEcPublicKey());
			}
			else {
				jwsVerifier = new RSASSAVerifier(KeystoreUtil.getFirstRsaPublicKey());
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
				.issuer("secondServer").subject("firstServer").audience("audience")
				.expirationTime(new Date()).issueTime(new Date()).notBeforeTime(new Date())
				.jwtID("A1234").claim("data", jwt.getRequestData()).build();
		JWEHeader jweHeader = null;
		JWEEncrypter jweEncrypter = null;
		EncryptedJWT encryptedJWT = null;
		try {
			if(jwt.getEncryptionAlgo().equals("EC")) {
				jweHeader = new JWEHeader(JWEAlgorithm.ECDH_ES_A256KW, EncryptionMethod.A256GCM);
				jweEncrypter = new ECDHEncrypter(KeystoreUtil.getFirstEcPublicKey());
			}
			else {
				jweHeader = new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM);
				jweEncrypter = new RSAEncrypter(KeystoreUtil.getFirstRsaPublicKey());
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
		
		EncryptedJWT encryptedJWT = null;
		JWEDecrypter jwtDecrypter = null;
		try {
			if(jwt.getEncryptionAlgo().equals("EC")) {
				jwtDecrypter = new ECDHDecrypter(KeystoreUtil.getSecondEcPrivateKey());
			}
			else {
				jwtDecrypter = new RSADecrypter(KeystoreUtil.getSecondRsaPrivateKey());
			}
			
			encryptedJWT = EncryptedJWT.parse(jwt.getRequestData());
			encryptedJWT.decrypt(jwtDecrypter);
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
		
		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
				.issuer("secondServer").subject("firstServer").audience("audience")
				.expirationTime(new Date()).issueTime(new Date()).notBeforeTime(new Date())
				.jwtID("A1234").claim("data", jwt.getRequestData()).build();
		JWSHeader jwsHeader = null;
		JWSSigner jwtSigner = null;
		SignedJWT signedJWT = null;
		try {
			if(jwt.getEncryptionAlgo().equals("EC")) {
				jwsHeader = new JWSHeader(JWSAlgorithm.ES256);
				jwtSigner = new ECDSASigner(KeystoreUtil.getSecondEcPrivateKey());
			}
			else {
				jwsHeader = new JWSHeader(JWSAlgorithm.RS256);
				jwtSigner = new RSASSASigner(KeystoreUtil.getSecondRsaPrivateKey());
			}
			
			signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
			signedJWT.sign(jwtSigner);
		} catch (JOSEException e) {
			e.printStackTrace();
		}
		
		return signedJWT.serialize();
	}
	
	public String verifyJwt(JwtModel jwt) {
		if(StringUtils.isBlank(jwt.getRequestData())) {
			return "jwt empty";
		}
		
		boolean jwtValid = false;
		JWSVerifier jwsVerifier = null;
		SignedJWT signedJWT = null;
		try {
			if(jwt.getEncryptionAlgo().equals("EC")) {
				jwsVerifier = new ECDSAVerifier(KeystoreUtil.getFirstEcPublicKey());
			}
			else {
				jwsVerifier = new RSASSAVerifier(KeystoreUtil.getFirstRsaPublicKey());
			}
			
			signedJWT = SignedJWT.parse(jwt.getRequestData());
			jwtValid = signedJWT.verify(jwsVerifier);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JOSEException e) {
			e.printStackTrace();
		}
		
		if(jwtValid) {
			try {
				return signedJWT.getJWTClaimsSet().toString();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return "JWT has been altered";
	}
}
