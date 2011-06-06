/**
 * 
 */
package org.mitre.pushee.hub.exception;

/**
 * @author aanganes
 *
 */
public class PublisherNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final Long serialVersionUID = 1L;

	/**
	 * 
	 */
	public PublisherNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public PublisherNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public PublisherNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PublisherNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}