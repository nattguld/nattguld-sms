package com.nattguld.sms.cfg;

import java.util.Objects;

import com.nattguld.data.cfg.Config;
import com.nattguld.data.cfg.ConfigManager;
import com.nattguld.data.json.JsonReader;
import com.nattguld.data.json.JsonWriter;
import com.nattguld.sms.SMSProvider;

/**
 * 
 * @author randqm
 *
 */

public class SMSConfig extends Config {
	
	/**
	 * The used SMS provider.
	 */
	private SMSProvider smsProvider = SMSProvider.NONE;
	
	/**
	 * The username.
	 */
	private String username;
	
	/**
	 * The API key.
	 */
	private String apiKey;
	
	/**
	 * The SMS receive timeout in seconds.
	 */
	private int smsReceiveTimeout = 180;
	
	
	@Override
	protected void read(JsonReader reader) {
		this.smsProvider = (SMSProvider)reader.getAsObject("sms_provider", SMSProvider.class, SMSProvider.NONE);
		this.username = reader.getAsString("username", null);
		this.apiKey = reader.getAsString("api_key", null);
		this.smsReceiveTimeout = reader.getAsInt("sms_receive_timeout", 180);
		
		if (smsReceiveTimeout == 0) {
			smsReceiveTimeout = 180;
			save();
		}
	}
	
	@Override
	protected void write(JsonWriter writer) {
		writer.write("sms_provider", smsProvider);
		writer.write("username", username);
		writer.write("api_key", apiKey);
		writer.write("sms_receive_timeout", smsReceiveTimeout);
	}

	@Override
	public String getSaveFileName() {
		return ".sms_config";
	}
	
	/**
	 * Updates the SMS configs.
	 * 
	 * @param smsProvider The new SMS provider.
	 * 
	 * @param username The username.
	 * 
	 * @param apiKey The API key.
	 * 
	 * @return The SMS config.
	 */
	public SMSConfig update(SMSProvider smsProvider, String username, String apiKey) {
		this.smsProvider = smsProvider;
		
		setUsername(smsProvider.requiresUsername() ? username : null);
		setAPIKey(smsProvider.requiresAPIKey() ? apiKey : null);
		
		save();
		return this;
	}
	
	/**
	 * Modifies the SMS receive timeout.
	 * 
	 * @param smsReceiveTimeout The new timeout.
	 * 
	 * @return The SMS config.
	 */
	public SMSConfig setSMSReceiveTimeout(int smsReceiveTimeout) {
		this.smsReceiveTimeout = smsReceiveTimeout;
		
		save();
		return this;
	}
	
	/**
	 * Retrieves the SMS receive timeout.
	 * 
	 * @return The timeout.
	 */
	public int getSMSReceiveTimeout() {
		return smsReceiveTimeout;
	}
	
	/**
	 * Validates the SMS configs.
	 * 
	 * @return The result.
	 */
	public String validate() {
		if (Objects.isNull(getSMSProvider())) {
			return "No SMS provider found";
		}
		if (getSMSProvider().requiresUsername() && Objects.isNull(getUsername())) {
			return "Username required for " + getSMSProvider().getName();
		}
		if (getSMSProvider().requiresAPIKey() && Objects.isNull(getAPIKey())) {
			return "API key required for " + getSMSProvider().getName();
		}
		return null;
	}
	
	/**
	 * Retrieves the SMS provider.
	 * 
	 * @return The SMS provider.
	 */
	public SMSProvider getSMSProvider() {
		return smsProvider;
	}
	
	/**
	 * Modifies the username.
	 * 
	 * @param username The new username.
	 * 
	 * @return The SMS config.
	 */
	protected SMSConfig setUsername(String username) {
		this.username = username;
		return this;
	}
	
	/**
	 * Retrieves the username.
	 * 
	 * @return The username.
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Modifies the API key.
	 * 
	 * @param apiKey The new API key.
	 * 
	 * @return The SMS config.
	 */
	protected SMSConfig setAPIKey(String apiKey) {
		this.apiKey = apiKey;
		return this;
	}
	
	/**
	 * Retrieves the API key.
	 * 
	 * @return The API key.
	 */
	public String getAPIKey() {
		return apiKey;
	}
	
	/**
	 * Retrieves the config.
	 * 
	 * @return The config.
	 */
	public static SMSConfig getConfig() {
		return (SMSConfig)ConfigManager.getConfig(new SMSConfig());
	}

}
