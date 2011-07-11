package org.mitre.pushee.oauth.exception;

public class DuplicateClientIdException extends RuntimeException {

	public DuplicateClientIdException(String clientId) {
	    super("Duplicate client id: " + clientId);
    }

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    
}
