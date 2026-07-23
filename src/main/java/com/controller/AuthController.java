package com.controller;

import com.dto.LoginDTO;
import com.email.service.OTPService;
import com.entity.users.User;
import com.repository.UserRepository;
import com.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Authentication", description = "Login and token generation")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final OTPService otpService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "Login", description = "Authenticate user and return JWT token")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("username", user.getUsername());
        response.put("role", user.getRole().name());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Change password", description = "Change password for authenticated user")
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword()))
            return ResponseEntity.badRequest().body(Map.of("message", "Old password is incorrect"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }

    @Operation(summary = "Forgot password - send OTP", description = "Send OTP to registered email for password reset")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No account found with this email"));
        otpService.sendOTP(email);
        return ResponseEntity.ok(Map.of("message", "OTP sent to " + email));
    }

    @Operation(summary = "Reset password with OTP", description = "Verify OTP and reset password")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String otp = body.get("otp");
        String newPassword = body.get("newPassword");

        if (!otpService.verifyOTP(email, otp))
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid or expired OTP"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        otpService.clearOTP(email);

        return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    }
}
