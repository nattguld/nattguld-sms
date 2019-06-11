package com.nattguld.sms;

import com.nattguld.util.generics.kvps.impl.BasicNameValuePair;

/**
 * 
 * @author randqm
 *
 */

public enum SMSProvider {
	
	GETSMSCODE("GetSMSCode", "http://www.getsmscode.com/do.php", true, true, new BasicNameValuePair[] {
			
	}),
	SMSPVA("SMSPVA", "http://smspva.com/priemnik.php", false, true, new BasicNameValuePair[] {
			
	}),
	SMS_ACTIVATE("SMS-Activate", "http://sms-activate.ru/stubs/handler_api.php", false, true, new BasicNameValuePair[] {
			new BasicNameValuePair(Platform.INSTAGRAM, "ig"),
			new BasicNameValuePair(Platform.OTHER, "ot"),
	}),
	NONE("None (manual)", null, false, false, new BasicNameValuePair[] {});
	
	
	/**
	 * The name.
	 */
	private final String name;
	
	/**
	 * The API endpoint.
	 */
	private final String endpoint;
	
	/**
	 * Whether a username is required to use the provider.
	 */
	private final boolean username;
	
	/**
	 * Whether an API key is required to use the provider.
	 */
	private final boolean apiKey;
	
	/**
	 * The available platform codes.
	 */
	private final BasicNameValuePair[] platformCodes;
	
	
	/**
	 * Creates a new SMS provider.
	 * 
	 * @param name The name.
	 * 
	 * @param endpoint The API endpoint.
	 * 
	 * @param username Whether a username is required to use the provider.
	 * 
	 * @param apiKey Whether an API key is required to use the provider.
	 * 
	 * @param platformCodes The available platform codes.
	 */
	private SMSProvider(String name, String endpoint, boolean username, boolean apiKey, BasicNameValuePair[] platformCodes) {
		this.name = name;
		this.endpoint = endpoint;
		this.username = username;
		this.apiKey = apiKey;
		this.platformCodes = platformCodes;
	}
	
	/**
	 * Retrieves the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retrieves the API endpoint.
	 * 
	 * @return The endpoint.
	 */
	public String getEndpoint() {
		return endpoint;
	}
	
	/**
	 * Retrieves whether a username is required to use the provider.
	 * 
	 * @return The result.
	 */
	public boolean requiresUsername() {
		return username;
	}
	
	/**
	 * Retrieves whether an API key is required to use the provider.
	 * 
	 * @return The result.
	 */
	public boolean requiresAPIKey() {
		return apiKey;
	}
	
	/**
	 * Retrieves a platform code.
	 * 
	 * @param platform The platform.
	 * 
	 * @return The platform code.
	 */
	public String getPlatformCode(Platform platform) {
		for (BasicNameValuePair nvp : platformCodes) {
			if ((Platform)nvp.getKey() == platform) {
				return nvp.getValueAsString();
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
