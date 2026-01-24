package com.example.CareerGuaidance.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.CareerGuaidance.entity.User;
import com.example.CareerGuaidance.repository.AuthRepository;

@Service
public class CustomAuthService implements UserDetailsService{
	
	private AuthRepository repository;
	
	public CustomAuthService(AuthRepository repository) {
		this.repository = repository;
	}
	
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
		
		Optional<User> user = repository.findByEmail(email);
		
		if(user.isEmpty()) {
			throw new UsernameNotFoundException("User not found with name: " + email);
		}
		
		return user.get();	
	}
	

}
