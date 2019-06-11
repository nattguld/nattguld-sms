package com.nattguld.sms.numbers;

import com.nattguld.sms.util.SMSUtil;

/**
 * 
 * @author randqm
 *
 */

public class SMSNumber {
	
	/**
	 * The session id.
	 */
	private final String id;
	
	/**
	 * The number.
	 */
	private final String number;
	
	
	/**
	 * Creates a new SMS number.
	 * 
	 * @param id The session id.
	 * 
	 * @param number The number.
	 */
	public SMSNumber(String id, String number) {
		this.id = id;
		this.number = SMSUtil.formatPhoneNumber(number);
	}
	
	/**
	 * Retrieves the session id.
	 * 
	 * @return The session id.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Retrieves the number.
	 * 
	 * @return The number.
	 */
	public String getNumber() {
		return number;
	}

}
