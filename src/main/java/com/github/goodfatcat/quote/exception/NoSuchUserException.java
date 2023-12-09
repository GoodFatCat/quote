package com.github.goodfatcat.quote.exception;

public class NoSuchUserException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NoSuchUserException() {
		super();
	}

	public NoSuchUserException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoSuchUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchUserException(String message) {
		super(message);
	}

	public NoSuchUserException(Throwable cause) {
		super(cause);
	}

}
