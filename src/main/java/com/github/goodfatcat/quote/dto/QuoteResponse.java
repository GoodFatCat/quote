package com.github.goodfatcat.quote.dto;

import java.time.LocalDate;

import com.github.goodfatcat.quote.model.Quote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuoteResponse {
	private Long id;
	private String content;
	private LocalDate dateOfUpdate;
	private UserResponse publisher;
	private Integer voteCount;

	private QuoteResponse(Quote quote) {
		this.id = quote.getId();
		this.content = quote.getContent();
		this.dateOfUpdate = quote.getDateOfUpdate();
		this.publisher = UserResponse
				.getUserResponseFromUser(quote.getPublisher());
		if (quote.getVotes() != null) {
			this.voteCount = quote.getVotes().stream()
					.map(vote -> vote.getVote().getValue()).reduce(0,
							(voteValueFirst, voteValueSecond) -> voteValueFirst
									+ voteValueSecond);
		} else {
			this.voteCount = 0;
		}
		
	}

	public QuoteResponse(Quote quote, int voteCount) {
		this.id = quote.getId();
		this.content = quote.getContent();
		this.dateOfUpdate = quote.getDateOfUpdate();
		this.publisher = UserResponse
				.getUserResponseFromUser(quote.getPublisher());
		this.voteCount = voteCount;
	}

	public static QuoteResponse getQuoteResponseFromQuote(Quote quote) {
		return new QuoteResponse(quote);
	}
}
