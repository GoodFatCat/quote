package com.github.goodfatcat.quote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.goodfatcat.quote.dto.UserRequest;
import com.github.goodfatcat.quote.model.User;
import com.github.goodfatcat.quote.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> createUser(@RequestBody UserRequest userRequest) {
		log.info(userRequest.toString());
		User user = userService.createUser(userRequest);

		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
}
