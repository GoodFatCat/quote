package com.github.goodfatcat.quote.dto;

import java.time.LocalDate;

public interface QuoteResponseRaw {
	Long getId();
	String getContent();
	LocalDate getDateOfUpdate();
	Long getPublisher();
	Integer getVoteCount();
}
