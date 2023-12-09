package com.github.goodfatcat.quote.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class VoteConverter implements AttributeConverter<VoteType, Integer>{

	@Override
	public Integer convertToDatabaseColumn(VoteType vote) {
		return vote.getValue();
	}

	@Override
	public VoteType convertToEntityAttribute(Integer voteCode) {
		return VoteType.findByIntegerValue(voteCode);
	}
}