package com.github.goodfatcat.quote.exception;

public class NoQuotesException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public NoQuotesException() {
		super();
	}

	public NoQuotesException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoQuotesException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoQuotesException(String message) {
		super(message);
	}

	public NoQuotesException(Throwable cause) {
		super(cause);
	}

}
