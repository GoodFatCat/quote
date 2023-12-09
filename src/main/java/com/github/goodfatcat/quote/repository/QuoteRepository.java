package com.github.goodfatcat.quote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.goodfatcat.quote.dto.QuoteResponseRaw;
import com.github.goodfatcat.quote.model.Quote;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long>{
	@Query(nativeQuery = true, value = "SELECT q.id, q.content, q.date_of_update AS dateOfUpdate, q.publisher_id AS publisher, "
			+ "SUM(v.vote) as voteCount FROM QUOTE q "
			+ "JOIN VOTE v ON q.id = v.quote_id GROUP BY q.id  ORDER BY voteCount DESC LIMIT 10;")
	List<QuoteResponseRaw> findTop10QuotesByVotes();
	
	@Query(nativeQuery = true, value = "SELECT q.id, q.content, q.date_of_update AS dateOfUpdate, q.publisher_id AS publisher, "
			+ "SUM(v.vote) as voteCount FROM QUOTE q "
			+ "JOIN VOTE v ON q.id = v.quote_id GROUP BY q.id  ORDER BY voteCount LIMIT 10;")
	List<QuoteResponseRaw> findWorst10QuotesByVotes();
}
