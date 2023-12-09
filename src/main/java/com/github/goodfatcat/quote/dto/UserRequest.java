package com.github.goodfatcat.quote.dto;

import com.github.goodfatcat.quote.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
	private String name;
	private String email;
	private String password;
	
	private UserRequest(User user) {
		this.name = user.getName();
		this.email = user.getEmail();
		this.password = user.getPassword();
	}
	
	public static UserRequest getUserDtoFromUser(User user) {
		return new UserRequest(user);
	}
}
