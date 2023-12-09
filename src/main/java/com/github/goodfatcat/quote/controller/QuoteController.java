package com.github.goodfatcat.quote.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.github.goodfatcat.quote.dto.QuoteRequest;
import com.github.goodfatcat.quote.dto.QuoteResponse;
import com.github.goodfatcat.quote.exception.NoQuotesException;
import com.github.goodfatcat.quote.exception.NoSuchQuoteException;
import com.github.goodfatcat.quote.exception.NoSuchUserException;
import com.github.goodfatcat.quote.model.Quote;
import com.github.goodfatcat.quote.service.QuoteService;

@RestController
@RequestMapping("/api/quote")
public class QuoteController {
	@Autowired
	private QuoteService quoteService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<QuoteResponse> postQuote(
			@RequestBody QuoteRequest request) {
		try {
			Quote quote = quoteService.postQuote(request);
			QuoteResponse response = QuoteResponse
					.getQuoteResponseFromQuote(quote);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (NoSuchUserException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					e.getMessage(), e);
		}
	}

	@GetMapping("/random")
	public ResponseEntity<QuoteResponse> getQuote() {
		try {
			Quote randomQuote = quoteService.getRandomQuote();
			QuoteResponse response = QuoteResponse
					.getQuoteResponseFromQuote(randomQuote);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (NoQuotesException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					e.getMessage(), e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<QuoteResponse> getQuote(@PathVariable Long id) {
		try {
			Quote quote = quoteService.getQuoteById(id);
			QuoteResponse response = QuoteResponse
					.getQuoteResponseFromQuote(quote);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (NoSuchQuoteException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					e.getMessage(), e);
		}
	}

	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<QuoteResponse> updateQuote(
			@RequestBody QuoteRequest request, @PathVariable Long id) {
		try {
			Quote updatedQuote = quoteService.updateQuote(request, id);
			QuoteResponse response = QuoteResponse
					.getQuoteResponseFromQuote(updatedQuote);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (NoSuchUserException | NoSuchQuoteException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					e.getMessage(), e);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteQuote(@PathVariable Long id) {
		quoteService.deleteQuoteById(id);
		return ResponseEntity.ok("Deleted");
	}

	@PostMapping("/{quoteId}/vote")
	public ResponseEntity<String> upvote(@PathVariable Long quoteId,
			@RequestParam Long userId, @RequestParam boolean isUpvote) {
		try {
			quoteService.voteForQuote(quoteId, userId, isUpvote);
			return ResponseEntity.ok("You vote successfully");
		} catch (NoSuchUserException | NoSuchQuoteException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					e.getMessage(), e);
		}
	}

	@GetMapping("/top10")
	public ResponseEntity<List<QuoteResponse>> getTop10Quotes() {
		return new ResponseEntity<>(quoteService.findTop10QuotesByVotes(),
				HttpStatus.OK);
	}

	@GetMapping("/worst10")
	public ResponseEntity<List<QuoteResponse>> getWorst10Quotes() {
		return new ResponseEntity<>(quoteService.findWorst10QuotesByVotes(),
				HttpStatus.OK);
	}
}
