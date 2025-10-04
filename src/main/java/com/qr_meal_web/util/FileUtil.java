package com.qr_meal_web.util;

import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class FileUtil {
    public static String toBase64(Part part) throws IOException {
        try (InputStream inputStream = part.getInputStream()) {
            byte[] fileBytes = inputStream.readAllBytes();
            return Base64.getEncoder().encodeToString(fileBytes);
        }
    }
}
