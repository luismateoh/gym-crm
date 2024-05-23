package com.luismateoh.gymcrm.utils;

import java.util.Random;

public class Utils {

    private Utils() {
    }

    private static final Random RANDOM = new Random();

    public static String generatePassword() {
        int passwordLength = 10;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < passwordLength; i++) {
            int index = RANDOM.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            password.append(randomChar);
        }

        return password.toString();
    }
}
