package com.github.goodfatcat.quote.model;

import java.time.LocalDate;

import com.github.goodfatcat.quote.dto.UserRequest;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "quote_user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	private String password;
	private LocalDate dateOfCreation;
	
	private User(UserRequest userDto) {
		this.name = userDto.getName();
		this.email = userDto.getEmail();
		this.password = userDto.getPassword();
		this.dateOfCreation = LocalDate.now();
	}
	
	public static User getUserFromUserRequestUser(UserRequest userRequest) {
		return new User(userRequest);
	}
}
