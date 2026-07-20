package com.email.service;

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

    private ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();
    private final String fromEmail = "mamidimohan250@gmail.com"; 
    public String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

    public void sendOTP(String email) {
        String otp = generateOTP();
        otpStorage.put(email, otp);
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Your OTP for Verification");
        message.setText("Your OTP is: " + otp);
        
        mailSender.send(message);
    }

    public boolean verifyOTP(String email, String otp) {
        return otpStorage.containsKey(email) && otpStorage.get(email).equals(otp);
    }
}
