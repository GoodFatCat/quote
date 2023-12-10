package com.github.goodfatcat.quote.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.github.goodfatcat.quote.repository.QuoteRepository;
import com.github.goodfatcat.quote.repository.VoteRepository;

class QuoteServiceImplTest {
	private QuoteRepository quoteRepository = Mockito.mock(QuoteRepository.class);
	private UserService userService = Mockito.mock(UserService.class);
	private VoteRepository voteRepository = Mockito.mock(VoteRepository.class);

	@Test
	void postQuote() {

	}

}
