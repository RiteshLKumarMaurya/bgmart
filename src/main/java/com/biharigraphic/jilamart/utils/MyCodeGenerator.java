package com.biharigraphic.jilamart.utils;
import com.biharigraphic.jilamart.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class MyCodeGenerator {

    private final UserRepository userRepository;

    public String generateUniqueUsername(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }

        // Normalize
        String normalized = fullName.trim().toLowerCase().replaceAll("[^a-z ]", "");
        String[] parts = normalized.split("\\s+");

        StringBuilder base = new StringBuilder();
        if (parts.length == 1) {
            base.append(parts[0]);
        } else {
            base.append(parts[0]); // first name
            base.append(parts[parts.length - 1]); // last name
        }

        String username;
        int attempt = 0;

        do {
            int randomNum = (int) (Math.random() * 900 + 100);
            username = base.toString() + randomNum;
            attempt++;
        } while (userRepository.existsByUsername(username) && attempt < 50);

        if (attempt >= 50) {
            // fallback if collisions are too many
            username = base.toString() + System.currentTimeMillis();
        }

        return username;
    }



    public String generateStrongPassword(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("Password length must be at least 8 characters");
        }

        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specials = "!@#$%^&*()-_=+<>?";

        String allChars = upper + lower + digits + specials;
        SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        // Ensure at least one of each
        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specials.charAt(random.nextInt(specials.length())));

        // Fill the rest
        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        // Shuffle the result for randomness
        return shuffleString(password.toString(), random);
    }

    private static String shuffleString(String input, SecureRandom random) {
        char[] a = input.toCharArray();
        for (int i = a.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }
        return new String(a);
    }


}
