package com.biharigraphic.jilamart.utils;

import java.util.Random;

public class CodeGeneratorUtil {

    private static final Random random = new Random();

    public static String generateSixDigitCode() {
        int number = 100000 + random.nextInt(900000); // ensures number is 6-digit
        return String.valueOf(number);
    }

    // For testing
    public static void main(String[] args) {
        System.out.println("Generated Code: " + generateSixDigitCode());
    }
}
