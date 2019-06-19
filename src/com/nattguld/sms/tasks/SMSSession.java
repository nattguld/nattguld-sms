package com.nattguld.sms.tasks;

import com.nattguld.http.HttpClient;
import com.nattguld.sms.SMSProvider;
import com.nattguld.sms.numbers.SMSNumber;
import com.nattguld.util.logging.Logger;

/**
 * 
 * @author randqm
 *
 */

public abstract class SMSSession {
	
	/**
	 * The SMS provider.
	 */
	private final SMSProvider provider;
	
	/**
	 * The provider username.
	 */
	private final String username;
	
	/**
	 * The provider API key.
	 */
	private final String apiKey;
	
	/**
	 * The http session if required.
	 */
	private final HttpClient c;
	
	/**
	 * The platform code.
	 */
	private final String code;
	
	/**
	 * The logger.
	 */
	private final Logger logger;
	
	
	/**
	 * Creates a new SMS session.
	 * 
	 * @param provider The SMS provider.
	 * 
	 * @param username The provider username.
	 * 
	 * @param apiKey The provider API key.
	 * 
	 * @param c The http session if required.
	 * 
	 * @param code The platform code.
	 */
	public SMSSession(SMSProvider provider, String username, String apiKey, HttpClient c, String code) {
		this.provider = provider;
		this.username = username;
		this.apiKey = apiKey;
		this.c = c;
		this.code = code;
		this.logger = new Logger(provider.getName());
	}
	
	/**
	 * Requests the SMS number.
	 * 
	 * @return The SMS number.
	 */
	public abstract SMSNumber requestNumber();
	
	/**
	 * Retrieves the SMS.
	 * 
	 * @param smsNumber The SMS number.
	 * 
	 * @return The SMS.
	 */
	public abstract String retrieveSMS(SMSNumber smsNumber);
	
	/**
	 * Bans an SMS number.
	 * 
	 * @param smsNumber The SMS number.
	 */
	public abstract void banNumber(SMSNumber smsNumber);
	
	/**
	 * Retrieves the provider balance.
	 * 
	 * @return The balance.
	 */
	public abstract String getBalance();
	
	/**
	 * Retrieves the logger.
	 * 
	 * @return The logger.
	 */
	public Logger getLogger() {
		return logger;
	}
	
	/**
	 * Retrieves the SMS provider.
	 * 
	 * @return The SMS provider.
	 */
	protected SMSProvider getSMSProvider() {
		return provider;
	}

	/**
	 * Retrieves the provider username.
	 * 
	 * @return The provider username.
	 */
	protected String getUsername() {
		return username;
	}

	/**
	 * Retrieves the provider API key.
	 * 
	 * @return The provider API key.
	 */
	protected String getAPIKey() {
		return apiKey;
	}

	/**
	 * Retrieves the http session if required.
	 * 
	 * @return The http session if required.
	 */
	protected HttpClient getClient() {
		return c;
	}

	/**
	 * Retrieves the platform code.
	 * 
	 * @return The platform code.
	 */
	protected String getCode() {
		return code;
	}

}
