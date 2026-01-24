package com.example.CareerGuaidance.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.CareerGuaidance.dto.LoginDto;
import com.example.CareerGuaidance.dto.Registrationdto;
import com.example.CareerGuaidance.entity.User;
import com.example.CareerGuaidance.mapper.AuthMapper;
import com.example.CareerGuaidance.repository.AuthRepository;

@Service
public class AuthService {
	
	private AuthMapper mapper;
	private AuthRepository repository;
	private PasswordEncoder passwordEncoder;
	
	private AuthenticationManager authManager;
	private JwtService jwtService;
	
	public AuthService(AuthMapper mapper, AuthRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,AuthenticationManager authManager) {
		this.mapper = mapper;
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authManager = authManager;
	}
	
	public boolean register(Registrationdto register) {
		 Optional<User> user = repository.findByEmail(register.getEmail());
		 if(user.isEmpty()) {
			 User u = mapper.toEntity(register);
			 u.setPassword(passwordEncoder.encode(register.getPassword()));
			 repository.save(u);
			 return true;
		 }
		 return false;
	}


	public ResponseEntity<?> loginAndGetToken(LoginDto login) {
		
		Optional<User> user = repository.findByEmail(login.getEmail());
		if (user.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Email not registered"));
        }
        User u = user.get();
        if (!passwordEncoder.matches(login.getPassword(), u.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Incorrect password"));
        }
	    String token = jwtService.generateToken(login.getEmail());
	    return ResponseEntity.ok(Map.of("token",token));
	}

}
