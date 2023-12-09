package com.github.goodfatcat.quote.service;

import java.util.List;

import com.github.goodfatcat.quote.dto.QuoteRequest;
import com.github.goodfatcat.quote.dto.QuoteResponse;
import com.github.goodfatcat.quote.model.Quote;

public interface QuoteService {
	Quote postQuote(QuoteRequest request);
	Quote getRandomQuote();
	Quote getQuoteById(Long id);
	Quote updateQuote(QuoteRequest request, Long id);
	void deleteQuoteById(Long id);
	void voteForQuote(Long quoteId, Long userId, boolean isUpvote);
	List<QuoteResponse> findTop10QuotesByVotes();
	List<QuoteResponse> findWorst10QuotesByVotes();
}
