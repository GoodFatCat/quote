package com.github.goodfatcat.quote.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.goodfatcat.quote.dto.UserRequest;
import com.github.goodfatcat.quote.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class UserControllerTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldProperlySaveUser() throws Exception {
		UserRequest userRequest = new UserRequest();
		userRequest.setEmail("email");
		userRequest.setName("name");
		userRequest.setPassword("password");
		String requestJson = objectMapper.writeValueAsString(userRequest);
		
		mvc.perform(post("/api/user").contentType(MediaType.APPLICATION_JSON).content(requestJson))
		.andExpectAll(status().isCreated(), content().contentType(MediaType.APPLICATION_JSON));
		
		assertTrue(userRepository.count() > 0);
	}

}
