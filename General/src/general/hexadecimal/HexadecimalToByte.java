package general.hexadecimal;

import java.math.BigInteger;
import java.util.HexFormat;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.google.common.io.BaseEncoding;

public class HexadecimalToByte {
	public byte[] javaUtilHexFormatDecoding(String hexString) {
		return HexFormat.of().withUpperCase().parseHex(hexString);
	}
	
	public byte[] bigIntegerHexDecoding(String hexString) {
	    byte[] byteArray = new BigInteger(hexString, 16).toByteArray();
	    if (byteArray[0] == 0) {
	        byte[] output = new byte[byteArray.length - 1];
	        System.arraycopy(byteArray, 1, output, 0, output.length);
	        return output;
	    }
	    return byteArray;
	}

	public byte[] javaxXmlBindDecoding(String hexString) {
	    return DatatypeConverter.parseHexBinary(hexString);
	}
	
	public byte[] apacheCommonsCodecDecoding(String hexString) throws DecoderException {
		return Hex.decodeHex(hexString);
	}

	public byte[] googleCommonDecoding(String hexString) {
	    return BaseEncoding.base16().decode(hexString.toUpperCase());
	}
	
	public byte[] hexadecimalDecoding1(String hexString) {
		if (hexString.length() % 2 == 1) {
			throw new IllegalArgumentException("Invalid hexadecimal String supplied.");
		}
		
		byte[] bytes = new byte[hexString.length() / 2];
		for (int i = 0; i < hexString.length(); i += 2) {
			bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
		}
		return bytes;
	}
	
	private byte hexToByte(String hexString) {
		int firstDigit = toDigit(hexString.charAt(0));
		int secondDigit = toDigit(hexString.charAt(1));
		return (byte) ((firstDigit << 4) + secondDigit);
	}
	
	private int toDigit(char hexChar) {
		int digit = Character.digit(hexChar, 16);
		if(digit == -1) {
			throw new IllegalArgumentException("Invalid Hexadecimal Character: "+ hexChar);
		}
		return digit;
	}
	
	public static byte[] hexadecimalDecoding2(String hexString) {
		byte[] data = new byte[hexString.length() / 2];
		for (int i = 0; i < hexString.length(); i += 2) {
			data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i+1), 16));
		}
		return data;
	}
}
