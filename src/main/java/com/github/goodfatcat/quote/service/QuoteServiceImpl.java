package com.github.goodfatcat.quote.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.goodfatcat.quote.dto.QuoteRequest;
import com.github.goodfatcat.quote.dto.QuoteResponse;
import com.github.goodfatcat.quote.dto.QuoteResponseRaw;
import com.github.goodfatcat.quote.dto.UserResponse;
import com.github.goodfatcat.quote.exception.NoQuotesException;
import com.github.goodfatcat.quote.exception.NoSuchQuoteException;
import com.github.goodfatcat.quote.model.Quote;
import com.github.goodfatcat.quote.model.User;
import com.github.goodfatcat.quote.model.Vote;
import com.github.goodfatcat.quote.model.VoteType;
import com.github.goodfatcat.quote.repository.QuoteRepository;
import com.github.goodfatcat.quote.repository.VoteRepository;

import jakarta.transaction.Transactional;

@Service
public class QuoteServiceImpl implements QuoteService {

	@Autowired
	private QuoteRepository quoteRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private VoteRepository voteRepository;

	@Transactional
	@Override
	public Quote postQuote(QuoteRequest request) {
		User user = userService.getUserById(request.getUserId());

		Quote quote = new Quote();
		quote.setContent(request.getContent());
		quote.setDateOfUpdate(LocalDate.now());
		quote.setPublisher(user);

		return quoteRepository.save(quote);
	}

	@Override
	public Quote getRandomQuote() {
		long countOfQuotes = quoteRepository.count() + 1;
		Random random = new Random();
		try {
			return quoteRepository.findById(random.nextLong(1, countOfQuotes))
					.orElseThrow(() -> new NoQuotesException(
							"Database doesn't have any of quotes"));
		} catch (IllegalArgumentException e) {
			throw new NoQuotesException("Database doesn't have any of quotes");
		}
	}

	@Override
	public Quote getQuoteById(Long id) {
		Quote quote = quoteRepository.findById(id)
				.orElseThrow(() -> new NoSuchQuoteException(
						"No such quote with id = " + id));
		quote.setVotes(voteRepository.findByQuote(quote));
		return quote;
	}
	
	@Transactional
	@Override
	public Quote updateQuote(QuoteRequest request, Long id) {
		Quote quote = getQuoteById(id);
		User publisher = userService.getUserById(request.getUserId());

		quote.setContent(request.getContent());
		quote.setDateOfUpdate(LocalDate.now());
		quote.setPublisher(publisher);

		return quoteRepository.save(quote);
	}

	@Transactional
	@Override
	public void deleteQuoteById(Long id) {
		quoteRepository.deleteById(id);
	}

	@Transactional
	@Override
	public void voteForQuote(Long quoteId, Long userId, boolean isUpvote) {
		User user = userService.getUserById(userId);

		Quote quote = getQuoteById(quoteId);

		Optional<Vote> optionalVote = voteRepository.findByUserAndQuote(user,
				quote);

		VoteType typeOfVote;

		if (isUpvote) {
			typeOfVote = VoteType.UP;
		} else {
			typeOfVote = VoteType.DOWN;
		}

		if (optionalVote.isPresent()) {
			Vote vote = optionalVote.get();
			vote.setVote(typeOfVote);
			voteRepository.save(vote);
		} else {
			Vote vote = new Vote();
			vote.setQuote(quote);
			vote.setUser(user);
			vote.setVote(typeOfVote);

			voteRepository.save(vote);
		}
	}

	@Override
	public List<QuoteResponse> findTop10QuotesByVotes() {
		List<QuoteResponseRaw> quoteResponseRaws = quoteRepository
				.findTop10QuotesByVotes();
		return getQuoteResponseList(quoteResponseRaws);
	}

	private List<QuoteResponse> getQuoteResponseList(
			List<QuoteResponseRaw> quoteResponseRaws) {
		List<QuoteResponse> resList = quoteResponseRaws.stream().map(t -> {
			QuoteResponse quoteResponse = new QuoteResponse();
			quoteResponse.setContent(t.getContent());
			quoteResponse.setDateOfUpdate(t.getDateOfUpdate());
			quoteResponse.setId(t.getId());
			quoteResponse.setPublisher(UserResponse.getUserResponseFromUser(
					userService.getUserById(t.getPublisher())));
			quoteResponse.setVoteCount(t.getVoteCount());
			return quoteResponse;
		}).toList();
		return resList;
	}

	@Override
	public List<QuoteResponse> findWorst10QuotesByVotes() {
		List<QuoteResponseRaw> quoteResponseRaws = quoteRepository
				.findWorst10QuotesByVotes();
		return getQuoteResponseList(quoteResponseRaws);
	}
}
