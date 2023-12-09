package com.github.goodfatcat.quote.service;

import com.github.goodfatcat.quote.dto.UserRequest;
import com.github.goodfatcat.quote.model.User;

public interface UserService {
	User createUser(UserRequest userDto);
	User getUserById(Long id);
}
