package com.nattguld.sms.cfg;

import java.util.Objects;

import com.nattguld.data.cfg.Config;
import com.nattguld.data.cfg.ConfigManager;
import com.nattguld.data.json.JsonReader;
import com.nattguld.data.json.JsonWriter;
import com.nattguld.sms.SMSProvider;
import com.nattguld.util.locale.Country;

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
	 * The country.
	 */
	private Country country;
	
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
		this.country = (Country)reader.getAsObject("country", Country.class, Country.RUSSIA);
		
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
		writer.write("country", country);
	}

	@Override
	public String getSaveFileName() {
		return ".sms_config";
	}
	
	/**
	 * Modifies the SMS provider.
	 * 
	 * @param smsProvider The new SMS provider.
	 * 
	 * @return The config.
	 */
	public SMSConfig setSMSProvider(SMSProvider smsProvider) {
		this.smsProvider = smsProvider;
		return this;
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
	 * @return The config.
	 */
	public SMSConfig setUsername(String username) {
		this.username = username.isEmpty() ? null : username;
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
	 * @return The config.
	 */
	public SMSConfig setAPIKey(String apiKey) {
		this.apiKey = apiKey.isEmpty() ? null : apiKey;
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
	 * Modifies the country.
	 * 
	 * @param country The new country.
	 * 
	 * @return The config.
	 */
	public SMSConfig setCountry(Country country) {
		this.country = country;
		return this;
	}
	
	/**
	 * Retrieves the country.
	 * 
	 * @return The country.
	 */
	public Country getCountry() {
		return country;
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
	 * Retrieves whether the SMS config is valid or not.
	 * 
	 * @return The result.
	 */
	public boolean isValid() {
		return Objects.nonNull(getSMSProvider()) && (!getSMSProvider().requiresUsername() || Objects.nonNull(getUsername()))
				&& (!smsProvider.requiresAPIKey() || Objects.nonNull(getAPIKey()));
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
