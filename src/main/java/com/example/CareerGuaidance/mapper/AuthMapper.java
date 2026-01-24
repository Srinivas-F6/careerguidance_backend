package com.example.CareerGuaidance.mapper;

import org.mapstruct.*;

import com.example.CareerGuaidance.dto.Registrationdto;
import com.example.CareerGuaidance.entity.User;

@Mapper(componentModel ="spring")
public interface AuthMapper {
	
	User toEntity(Registrationdto register);

}
