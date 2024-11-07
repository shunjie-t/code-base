package com.all.zeroth.qrcode;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Component
public class QRGenerator {
	
	public byte[] generateQR(String value) throws IOException, WriterException {
		if (StringUtils.isBlank(value)) {
			return new byte[0];
		}

		QRCodeWriter writer = new QRCodeWriter();
		BitMatrix matrix = writer.encode(value, BarcodeFormat.QR_CODE, 1200, 1200);
		
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(matrix, "PNG", bytesOut);
		
		return bytesOut.toByteArray();
	}
}
