package com.github.goodfatcat.quote.model;

import lombok.Getter;

@Getter
public enum VoteType {
	UP(1),
	DOWN(-1);
	
	private final int value;

	private VoteType(int value) {
		this.value = value;
	}
	
	public static VoteType findByIntegerValue(int value) {
		for (VoteType vote : values()) {
			if (vote.getValue() == value) {
				return vote;
			}
		}
		throw new IllegalArgumentException("No such vote with value = " + value);
	}
}
