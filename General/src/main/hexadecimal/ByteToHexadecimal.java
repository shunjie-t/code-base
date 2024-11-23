package main.hexadecimal;

import java.math.BigInteger;
import java.util.HexFormat;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.google.common.io.BaseEncoding;

public class ByteToHexadecimal {	
	public String javaUtilHexFormatEncoding(byte[] bytes) {
		return HexFormat.of().withUpperCase().formatHex(bytes);
	}
	
	public String bigIntegerHexEncoding1(byte[] bytes) {
	    BigInteger bigInteger = new BigInteger(1, bytes);
	    return String.format("%0" + (bytes.length << 1) + "x", bigInteger);
	}
	
	public String bigIntegerHexEncoding2(byte[] bytes) {
	    BigInteger bigInteger = new BigInteger(1, bytes);
	    return bigInteger.toString(16);
	}
	
	public String javaxXmlBindEncoding(byte[] bytes) {
	    return DatatypeConverter.printHexBinary(bytes);
	}
	
	public String apacheCommonsCodecEncoding(byte[] bytes) throws DecoderException {
		return Hex.encodeHexString(bytes);
	}
	
	public String googleCommonEncoding(byte[] bytes) {
	    return BaseEncoding.base16().encode(bytes);
	}
	
	public String hexadecimalEncoding1(byte[] data) {
		StringBuffer result = new StringBuffer();
		char[] hexDigits = new char[2];
		for (int i = 0; i < data.length; i++) {
			hexDigits[0] = Character.forDigit((data[i] >> 4) & 0xF, 16);
			hexDigits[1] = Character.forDigit((data[i] & 0xF), 16);
			result.append(hexDigits);
		}
		return result.toString();
	}
	
	public String hexadecimalEncoding2(byte[] data) {
		StringBuilder result = new StringBuilder();
		for(byte d : data) {
			result.append(String.format("%02x", d));
		}
		return result.toString();
	}
	
	public String hexadecimalEncoding3(byte[] data) {
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < data.length; i++) {
			String hex = Integer.toHexString(0xff & data[i]);
			if(hex.length() == 1) {
				result.append('0');
			}
			result.append(hex);
		}
		return result.toString();
	}
}
