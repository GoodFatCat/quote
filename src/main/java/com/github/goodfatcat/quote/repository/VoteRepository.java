package com.github.goodfatcat.quote.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.goodfatcat.quote.model.Quote;
import com.github.goodfatcat.quote.model.User;
import com.github.goodfatcat.quote.model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long>{
	Optional<Vote> findByUserAndQuote(User user, Quote quote);
	List<Vote> findByQuote(Quote quote);
}
