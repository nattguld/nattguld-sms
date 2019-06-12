package com.nattguld.sms;

import com.nattguld.util.generics.kvps.impl.BasicNameValuePair;

/**
 * 
 * @author randqm
 *
 */

public enum SMSProvider {
	
	GETSMSCODE("GetSMSCode", "http://www.getsmscode.com/do.php", true, true, new BasicNameValuePair[] {
			new BasicNameValuePair(Platform.GOOGLE, "1"),
			new BasicNameValuePair(Platform.FACEBOOK, "2"),
			new BasicNameValuePair(Platform.WHATSAPP, "3"),
			new BasicNameValuePair(Platform.CHATROULETTE, "6"),
			new BasicNameValuePair(Platform.EBAY, "7"),
			new BasicNameValuePair(Platform.INSTAGRAM, "8"),
			new BasicNameValuePair(Platform.TELEGRAM, "10"),
			new BasicNameValuePair(Platform.YAHOO, "15"),
			new BasicNameValuePair(Platform.TINDER, "51"),
			new BasicNameValuePair(Platform.VIBER, "55"),
			new BasicNameValuePair(Platform.TWITTER, "66"),
			new BasicNameValuePair(Platform.SKYPE, "114"),
			new BasicNameValuePair(Platform.AMAZON, "146"),
			new BasicNameValuePair(Platform.TWILIO, "248"),
			new BasicNameValuePair(Platform.BADOO, "283"),
			new BasicNameValuePair(Platform.SNAPCHAT, "287"),
			new BasicNameValuePair(Platform.WEEBLY, "297"),
			new BasicNameValuePair(Platform.MICROSOFT, "338"),
			new BasicNameValuePair(Platform.MEETME, "344"),
			new BasicNameValuePair(Platform.YANDEX, "346"),
			new BasicNameValuePair(Platform.FIVERR, "356"),
			new BasicNameValuePair(Platform.ALIBABA, "387"),
			new BasicNameValuePair(Platform.PROTONMAIL, "404"),
			new BasicNameValuePair(Platform.STEAM, "427"),
			new BasicNameValuePair(Platform.CRAIGSLIST, "469"),
			new BasicNameValuePair(Platform.G2A, "476"),
			new BasicNameValuePair(Platform.MAILRU, "489"),
			new BasicNameValuePair(Platform.ZOOSK, "496"),
			new BasicNameValuePair(Platform.MUSICALLY, "501"),
			new BasicNameValuePair(Platform.LINKEDIN, "509"),
			new BasicNameValuePair(Platform.DISCORD, "544"),
			new BasicNameValuePair(Platform.SPOTIFY, "545"),
			new BasicNameValuePair(Platform.REDIFFMAIL, "616"),
			new BasicNameValuePair(Platform.NETFLIX, "622"),
			new BasicNameValuePair(Platform.APPLE, "644"),
			new BasicNameValuePair(Platform.BUMBLE, "646"),
			new BasicNameValuePair(Platform.OTHER, "ot"),
			new BasicNameValuePair(Platform.OTHER, "ot"),
			new BasicNameValuePair(Platform.OTHER, "ot"),
			new BasicNameValuePair(Platform.OTHER, "ot"),
	}),
	SMSPVA("SMSPVA", "http://smspva.com/priemnik.php", false, true, new BasicNameValuePair[] {
			new BasicNameValuePair(Platform.AMAZON, "opt44"),
			new BasicNameValuePair(Platform.BADOO, "opt56"),
			new BasicNameValuePair(Platform.BURGERKING, "opt3"),
			new BasicNameValuePair(Platform.CRAIGSLIST, "opt26"),
			new BasicNameValuePair(Platform.DISCORD, "opt45"),
			new BasicNameValuePair(Platform.FACEBOOK, "opt2"),
			new BasicNameValuePair(Platform.FASTMAIL, "opt43"),
			new BasicNameValuePair(Platform.FIVERR, "opt6"),
			new BasicNameValuePair(Platform.G2A, "opt68"),
			new BasicNameValuePair(Platform.GOOGLE, "opt1"),
			new BasicNameValuePair(Platform.INSTAGRAM, "opt16"),
			new BasicNameValuePair(Platform.LINKEDIN, "opt8"),
			new BasicNameValuePair(Platform.MAILRU, "opt33"),
			new BasicNameValuePair(Platform.MEETME, "opt17"),
			new BasicNameValuePair(Platform.MICROSOFT, "opt15"),
			new BasicNameValuePair(Platform.NETFLIX, "opt101"),
			new BasicNameValuePair(Platform.OTHER, "opt19"),
			new BasicNameValuePair(Platform.PAYPAL, "opt83"),
			new BasicNameValuePair(Platform.EBAY, "opt83"),
			new BasicNameValuePair(Platform.PROTONMAIL, "opt57"),
			new BasicNameValuePair(Platform.SNAPCHAT, "opt90"),
			new BasicNameValuePair(Platform.SPOTIFY, "opt98"),
			new BasicNameValuePair(Platform.STEAM, "opt58"),
			new BasicNameValuePair(Platform.TELEGRAM, "opt29"),
			new BasicNameValuePair(Platform.TINDER, "opt9"),
			new BasicNameValuePair(Platform.TWILIO, "opt66"),
			new BasicNameValuePair(Platform.TWITTER, "opt41"),
			new BasicNameValuePair(Platform.VIBER, "opt11"),
			new BasicNameValuePair(Platform.WEEBLY, "opt54"),
			new BasicNameValuePair(Platform.WHATSAPP, "opt20"),
			new BasicNameValuePair(Platform.YAHOO, "opt65"),
			new BasicNameValuePair(Platform.YANDEX, "opt23"),
	}),
	SMS_ACTIVATE("SMS-Activate", "http://sms-activate.ru/stubs/handler_api.php", false, true, new BasicNameValuePair[] {
			new BasicNameValuePair(Platform.INSTAGRAM, "ig"),
			new BasicNameValuePair(Platform.WHATSAPP, "wa"),
			new BasicNameValuePair(Platform.TELEGRAM, "tg"),
			new BasicNameValuePair(Platform.OTHER, "ot"),
			new BasicNameValuePair(Platform.GOOGLE, "go"),
			new BasicNameValuePair(Platform.FACEBOOK, "fb"),
			new BasicNameValuePair(Platform.TWITTER, "tw"),
			new BasicNameValuePair(Platform.MAILRU, "ma"),
			new BasicNameValuePair(Platform.MICROSOFT, "mm"),
			new BasicNameValuePair(Platform.YAHOO, "mb"),
			new BasicNameValuePair(Platform.STEAM, "mt"),
			new BasicNameValuePair(Platform.TINDER, "oi"),
			new BasicNameValuePair(Platform.MEETME, "fd"),
			new BasicNameValuePair(Platform.LINKEDIN, "tn"),
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
