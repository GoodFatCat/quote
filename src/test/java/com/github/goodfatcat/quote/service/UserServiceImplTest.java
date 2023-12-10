package com.github.goodfatcat.quote.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.github.goodfatcat.quote.dto.UserRequest;
import com.github.goodfatcat.quote.model.User;
import com.github.goodfatcat.quote.repository.UserRepository;

class UserServiceImplTest {
	private UserRepository userRepository = Mockito.mock(UserRepository.class);
	private UserServiceImpl userService = new UserServiceImpl(userRepository);

	@Test
	void createUser() {
		UserRequest request = new UserRequest();
		request.setEmail("email");
		request.setName("User");
		request.setPassword("password");

		userService.createUser(request);

		Mockito.verify(userRepository).save(Mockito.any(User.class));
	}

	@Test
	void getUserById() {
		User userInDb = new User(1L, "User", "email", "password",
				LocalDate.now());

		Mockito.when(userRepository.findById(1L))
				.thenReturn(Optional.of(userInDb));

		User actual = userService.getUserById(1L);

		Mockito.verify(userRepository).findById(1L);
		assertEquals(userInDb, actual);
	}

}
