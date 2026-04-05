package com.biharigraphic.jilamart.utils;

public class PhoneNumberUtil {

    /**
     * Extracts the last 10-digit phone number from the given input string.
     * @param fullPhone the full phone number (may include country code, spaces, etc.)
     * @return the last 10-digit phone number if found, otherwise null
     */
    public static String extractTenDigitPhone(String fullPhone) {
        if (fullPhone == null) return null;

        // Remove all non-digit characters
        String digitsOnly = fullPhone.replaceAll("\\D", "");

        // Get the last 10 digits (which is typically the actual mobile number)
        if (digitsOnly.length() >= 10) {
            return digitsOnly.substring(digitsOnly.length() - 10);
        }

        return null; // Or throw exception_handler if preferred
    }
}
