package com.nattguld.sms.kits;

/**
 * 
 * @author randqm
 *
 */

public class KitResponse {
	
	/**
	 * Whether the response is valid or not.
	 */
	private final boolean valid;
	
	/**
	 * The message.
	 */
	private final String message;
	
	
	/**
	 * Creates a new kit response.
	 * 
	 * @param valid Whether the response is valid or not.
	 * 
	 * @param message The message.
	 */
	public KitResponse(boolean valid, String message) {
		this.valid = valid;
		this.message = message;
	}
	
	/**
	 * Retrieves whether the response is valid or not.
	 * 
	 * @return The result.
	 */
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * Retrieves the message.
	 * 
	 * @return The message.
	 */
	public String getMessage() {
		return message;
	}

}
