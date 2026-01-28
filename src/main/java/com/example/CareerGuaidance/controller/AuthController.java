package com.example.CareerGuaidance.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.CareerGuaidance.dto.LoginDto;
import com.example.CareerGuaidance.dto.Registrationdto;
import com.example.CareerGuaidance.service.AuthService;

@RestController
public class AuthController {
	
	private AuthService service;
	
	public AuthController(AuthService service) {
		this.service = service;
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> Registration(@RequestBody Registrationdto registration) {
		 boolean registered = service.register(registration);
		 if(registered) {
			 return ResponseEntity.ok(Map.of("message","succesfully registered"));
		 }
		 return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message","already Registered"));
		 
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDto login) {
	    return service.loginAndGetToken(login);
	}

}
