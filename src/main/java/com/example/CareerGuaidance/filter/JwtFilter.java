package com.example.CareerGuaidance.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.CareerGuaidance.service.CustomAuthService;
import com.example.CareerGuaidance.service.JwtService;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
	private CustomAuthService customAuthService;
	private JwtService jwtService;
	
	
	public JwtFilter(CustomAuthService customAuthService,JwtService jwtService) {
		this.customAuthService = customAuthService;
		this.jwtService = jwtService;
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
	    String path = request.getServletPath();
	    // skip JWT filter for login and register endpoints
	    return path.equals("/login") || path.equals("/register");
	}

	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
        // Bearer --token--         sent by client
		
		String authHeader = request.getHeader("Authorization");
		String username = null;
		String token = null;
		
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			username = jwtService.extractUsername(token);
		}
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			
			UserDetails userDetails =customAuthService.loadUserByUsername(username);
			
			if(jwtService.verifyUser(token,userDetails)) {
				UsernamePasswordAuthenticationToken authToken = 
						new  UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		
		filterChain.doFilter(request, response);
		
	}

}
