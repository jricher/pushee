/**
 * 
 */
package org.mitre.pushee.hub.exception;

/**
 * @author aanganes
 *
 */
public class SubscriberNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final Long serialVersionUID = 1L;

	/**
	 * 
	 */
	public SubscriberNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public SubscriberNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public SubscriberNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SubscriberNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
