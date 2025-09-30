package com.qr_meal_web.util;

import org.mindrot.jbcrypt.BCrypt;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Password {
    // Pepper (nếu dùng) — lưu trong file config hoặc environment variable, không commit vào git
    private static final  String PEPPER = System.getenv("PEPPER");

    // cost/work factor: 10-12 là phổ biến. 12 an toàn hơn nhưng chậm hơn.
    private static final int LOG_ROUNDS = 10;

    // Dùng config.properties
//    private static String PEPPER;
//    static {
//        Properties props = new Properties();
//        try {
//            props.load(Password.class.getClassLoader().getResourceAsStream("config.properties"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        PEPPER = props.getProperty("PEPPER");
//    }


    public static String hashPassword(String plainPassword) {
        String toHash = plainPassword;
        if (PEPPER != null && !PEPPER.isEmpty()) {
            toHash = plainPassword + PEPPER;
        }
        return BCrypt.hashpw(toHash, BCrypt.gensalt(LOG_ROUNDS));
    }

    public static boolean verifyPassword(String plainPassword, String storedHash) {
        String toCheck =plainPassword;
        if (PEPPER != null && !PEPPER.isEmpty()){
            toCheck = plainPassword + PEPPER;
        }
        return BCrypt.checkpw(toCheck, storedHash);
    }
}
