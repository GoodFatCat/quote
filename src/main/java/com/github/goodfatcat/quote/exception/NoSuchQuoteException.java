package com.github.goodfatcat.quote.exception;

public class NoSuchQuoteException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public NoSuchQuoteException() {
		super();
	}

	public NoSuchQuoteException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoSuchQuoteException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchQuoteException(String message) {
		super(message);
	}

	public NoSuchQuoteException(Throwable cause) {
		super(cause);
	}

}
