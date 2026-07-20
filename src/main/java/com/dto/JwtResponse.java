package com.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    public JwtResponse(String jwt, Long id, String username, String email,
			Collection<? extends GrantedAuthority> authorities) {
	}

	private String token;
}
