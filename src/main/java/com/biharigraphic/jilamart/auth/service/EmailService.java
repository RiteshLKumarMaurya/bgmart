package com.biharigraphic.jilamart.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
@ Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtp(String to, String otp) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmail); // 🔥 MUST (auto fix your issue)
            message.setTo(to);
            message.setSubject("Reset Password OTP - JilaMart");

            message.setText(
                    "Hello,\n\n" +
                            "Your OTP for password reset is: " + otp + "\n\n" +
                            "This OTP is valid for 5 minutes.\n\n" +
                            "If you did not request this, please ignore this email.\n\n" +
                            "Regards,\n" +
                            "JilaMart Team"
            );

            mailSender.send(message);

            log.info("✅ OTP email sent successfully to: {}", to);

        } catch (Exception e) {

            log.error("❌ Failed to send OTP email to: {}", to, e);

            // 🔥 IMPORTANT: propagate exception
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }
}