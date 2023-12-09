package com.github.goodfatcat.quote.dto;

import java.time.LocalDate;

import com.github.goodfatcat.quote.model.User;

import lombok.Data;

@Data
public class UserResponse {
	private Long id;
	private String name;
	private String email;
	private LocalDate dateOfCreation;
	
	private UserResponse(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();
		this.dateOfCreation = user.getDateOfCreation();
	}
	
	public static UserResponse getUserResponseFromUser(User user) {
		return new UserResponse(user);
	}
}
