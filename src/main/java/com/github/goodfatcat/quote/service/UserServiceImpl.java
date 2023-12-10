package com.github.goodfatcat.quote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.goodfatcat.quote.dto.UserRequest;
import com.github.goodfatcat.quote.exception.NoSuchUserException;
import com.github.goodfatcat.quote.model.User;
import com.github.goodfatcat.quote.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	private UserRepository userRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User createUser(UserRequest userDto) {
		User user = User.getUserFromUserRequestUser(userDto);
		return userRepository.save(user);
	}
	
	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new NoSuchUserException("No such user with id = " + id));
	}

}
