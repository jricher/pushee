/**
 * 
 */
package org.mitre.pushee.hub.exception;

/**
 * @author AANGANES
 *
 */
public class PermissionDeniedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public PermissionDeniedException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public PermissionDeniedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public PermissionDeniedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PermissionDeniedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
