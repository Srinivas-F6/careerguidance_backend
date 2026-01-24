package com.example.CareerGuaidance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CareerGuaidance.entity.User;

public interface AuthRepository extends JpaRepository<User,Long> {

	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);

}
