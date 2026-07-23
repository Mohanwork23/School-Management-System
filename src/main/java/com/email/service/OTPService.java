package com.email.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OTPService {

    @Autowired
    private JavaMailSender mailSender;

    private final String fromEmail = "mamidimohan250@gmail.com";
    private final ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> otpExpiry = new ConcurrentHashMap<>();
    private static final int OTP_VALIDITY_MINUTES = 5;

    public void sendOTP(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);
        otpExpiry.put(email, LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Your OTP for Password Reset");
        message.setText("Your OTP is: " + otp + "\nValid for " + OTP_VALIDITY_MINUTES + " minutes.");
        mailSender.send(message);
    }

    public boolean verifyOTP(String email, String otp) {
        if (!otpStorage.containsKey(email)) return false;
        if (LocalDateTime.now().isAfter(otpExpiry.getOrDefault(email, LocalDateTime.MIN))) {
            otpStorage.remove(email);
            otpExpiry.remove(email);
            return false;
        }
        return otpStorage.get(email).equals(otp);
    }

    public void clearOTP(String email) {
        otpStorage.remove(email);
        otpExpiry.remove(email);
    }
}
