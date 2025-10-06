package com.qr_meal_web.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

public class QRCode {
    public static String generateBase64QRCode(int idTable) throws IOException {
        Properties props = new Properties();
        props.load(QRCode.class.getClassLoader().getResourceAsStream("config.properties"));
        String contextPath = props.getProperty("URL_CLIENT_PATH");

        contextPath += idTable; // nội dung QR
        int width = 300;
        int height = 300;

        try {
            // Sinh QR Code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(contextPath, BarcodeFormat.QR_CODE, width, height);

            // Ghi QR vào ByteArrayOutputStream thay vì file
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();

            // Chuyển sang Base64
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(pngData);
        } catch (WriterException | IOException e) {
            throw new IOException(e);
        }
    }
}
