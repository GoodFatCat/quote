package com.github.goodfatcat.quote.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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
import com.github.goodfatcat.quote.dto.QuoteRequest;
import com.github.goodfatcat.quote.dto.QuoteResponse;
import com.github.goodfatcat.quote.dto.UserRequest;
import com.github.goodfatcat.quote.dto.UserResponse;
import com.github.goodfatcat.quote.model.Quote;
import com.github.goodfatcat.quote.model.User;
import com.github.goodfatcat.quote.model.Vote;
import com.github.goodfatcat.quote.model.VoteType;
import com.github.goodfatcat.quote.repository.QuoteRepository;
import com.github.goodfatcat.quote.repository.VoteRepository;
import com.github.goodfatcat.quote.service.UserService;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class QuoteControllerTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private QuoteRepository quoteRepository;
	@Autowired
	private VoteRepository voteRepository;
	private User user;

	@BeforeAll
	void initUser() {
		this.user = userService
				.createUser(new UserRequest("user", "email", "password"));
	}

	@AfterEach
	void clearDb() {
		quoteRepository.deleteAll();
	}

	@Test
	void shouldProperlyExecutePostRequest() throws Exception {
		QuoteRequest request = new QuoteRequest("Content", 1L);
		String requestJsonString = objectMapper.writeValueAsString(request);

		mvc.perform(post("/api/quote").contentType(MediaType.APPLICATION_JSON)
				.content(requestJsonString)).andExpectAll(status().isCreated(),
						content().contentType(MediaType.APPLICATION_JSON));
		assertTrue(quoteRepository.count() > 0);
	}

	@Test
	void shouldProperlyExecuteGetRequestById() throws Exception {
		Quote quote = saveQuote();

		QuoteResponse response = new QuoteResponse();
		response.setContent(quote.getContent());
		response.setDateOfUpdate(quote.getDateOfUpdate());
		response.setId(quote.getId());
		response.setPublisher(UserResponse.getUserResponseFromUser(user));
		response.setVoteCount(0);

		String quoteJson = objectMapper.writeValueAsString(response);

		mvc.perform(get("/api/quote/1")).andExpectAll(status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON),
				content().json(quoteJson));
	}

	private Quote saveQuote() {
		Quote quote = new Quote();
		quote.setContent("Content1");
		quote.setDateOfUpdate(LocalDate.now());
		quote.setPublisher(user);
		quote.setVotes(null);

		quote = quoteRepository.save(quote);
		return quote;
	}

	@Test
	void shouldThrowNoQuotesException() throws Exception {
		mvc.perform(get("/api/quote/random"))
				.andExpectAll(status().isNotFound());
	}

	@Test
	void shouldProperlyUpdateQuote() throws Exception {
		Quote quote = saveQuote();

		QuoteRequest request = new QuoteRequest();
		request.setContent("New content");
		request.setUserId(1L);

		String requestJson = objectMapper.writeValueAsString(request);

		Long quoteIdThatNeedUpdate = quote.getId();

		mvc.perform(put("/api/quote/" + quoteIdThatNeedUpdate)
				.contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpectAll(status().isOk(),
						content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void shouldProperlyDeleteQuote() throws Exception {
		Quote quote = saveQuote();
		mvc.perform(delete("/api/quote/" + quote.getId())).andExpectAll(
				status().isOk(), content().string("Quote deleted"));
		assertTrue(quoteRepository.count() < 1);
	}

	@Test
	void shoulPropertlyVote() throws Exception {
		Quote quote = saveQuote();

		mvc.perform(post("/api/quote/" + quote.getId() + "/vote")
				.param("userId", user.getId().toString())
				.param("isUpvote", String.valueOf(true)))
				.andExpectAll(status().isOk(),
						content().string("You vote successfully"));
		assertTrue(voteRepository.count() > 0);
		voteRepository.deleteAll();
	}

	@Test
	void shouldProperlyFindTop10Quotes() throws Exception {
		Quote quote = saveQuote();
		Vote vote = new Vote();
		vote.setQuote(quote);
		vote.setUser(user);
		vote.setVote(VoteType.UP);
		voteRepository.save(vote);

		UserRequest secondUser = new UserRequest();
		secondUser.setEmail("newEmail");
		secondUser.setName("newUser");
		secondUser.setPassword("new password");

		Quote newQuote = new Quote();
		newQuote.setContent("new quote");
		newQuote.setDateOfUpdate(LocalDate.now());
		newQuote.setPublisher(user);
		newQuote.setVotes(null);
		newQuote = quoteRepository.save(newQuote);
		
		Vote newVote = new Vote();
		newVote.setQuote(newQuote);
		newVote.setUser(user);
		newVote.setVote(VoteType.DOWN);
		voteRepository.save(newVote);
		
		List<QuoteResponse> expected = new ArrayList<>();
		QuoteResponse response = new QuoteResponse();
		response.setContent("Content1");
		response.setDateOfUpdate(quote.getDateOfUpdate());
		response.setId(quote.getId());
		response.setPublisher(UserResponse.getUserResponseFromUser(user));
		response.setVoteCount(1);
		expected.add(response);
		response = new QuoteResponse();
		response.setPublisher(UserResponse.getUserResponseFromUser(user));
		response.setContent("new quote");
		response.setDateOfUpdate(newQuote.getDateOfUpdate());
		response.setId(newQuote.getId());
		response.setVoteCount(-1);
		expected.add(response);
		
		String expectedJson = objectMapper.writeValueAsString(expected);

		mvc.perform(get("/api/quote/top10")).andExpectAll(status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON), content().json(expectedJson));
		voteRepository.deleteAll();
	}
	
	@Test
	void shouldProperlyFindWorst10Quotes() throws Exception {
		Quote quote = saveQuote();
		Vote vote = new Vote();
		vote.setQuote(quote);
		vote.setUser(user);
		vote.setVote(VoteType.UP);
		voteRepository.save(vote);

		UserRequest secondUser = new UserRequest();
		secondUser.setEmail("newEmail");
		secondUser.setName("newUser");
		secondUser.setPassword("new password");

		Quote newQuote = new Quote();
		newQuote.setContent("new quote");
		newQuote.setDateOfUpdate(LocalDate.now());
		newQuote.setPublisher(user);
		newQuote.setVotes(null);
		newQuote = quoteRepository.save(newQuote);
		
		Vote newVote = new Vote();
		newVote.setQuote(newQuote);
		newVote.setUser(user);
		newVote.setVote(VoteType.DOWN);
		voteRepository.save(newVote);
		
		List<QuoteResponse> expected = new ArrayList<>();
		QuoteResponse response = new QuoteResponse();
		response.setPublisher(UserResponse.getUserResponseFromUser(user));
		response.setContent("new quote");
		response.setDateOfUpdate(newQuote.getDateOfUpdate());
		response.setId(newQuote.getId());
		response.setVoteCount(-1);
		expected.add(response);
		response = new QuoteResponse();
		response.setContent("Content1");
		response.setDateOfUpdate(quote.getDateOfUpdate());
		response.setId(quote.getId());
		response.setPublisher(UserResponse.getUserResponseFromUser(user));
		response.setVoteCount(1);
		expected.add(response);
		
		String expectedJson = objectMapper.writeValueAsString(expected);

		mvc.perform(get("/api/quote/worst10")).andExpectAll(status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON), content().json(expectedJson));
		voteRepository.deleteAll();
	}
}
