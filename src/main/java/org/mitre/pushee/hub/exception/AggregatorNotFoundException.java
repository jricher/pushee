package org.mitre.pushee.hub.exception;

public class AggregatorNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final Long serialVersionUID = 1L;

	/**
	 * 
	 */
	public AggregatorNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public AggregatorNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public AggregatorNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AggregatorNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
}
