package com.nattguld.sms;

import com.nattguld.util.generics.kvps.impl.ObjectKeyValuePair;

/**
 * 
 * @author randqm
 *
 */

public enum SMSProvider {
	
	GETSMSCODE("GetSMSCode", "http://www.getsmscode.com/do.php", true, true, new ObjectKeyValuePair[] {
			new ObjectKeyValuePair(Platform.GOOGLE, "1"),
			new ObjectKeyValuePair(Platform.FACEBOOK, "2"),
			new ObjectKeyValuePair(Platform.WHATSAPP, "3"),
			new ObjectKeyValuePair(Platform.CHATROULETTE, "6"),
			new ObjectKeyValuePair(Platform.EBAY, "7"),
			new ObjectKeyValuePair(Platform.INSTAGRAM, "8"),
			new ObjectKeyValuePair(Platform.TELEGRAM, "10"),
			new ObjectKeyValuePair(Platform.YAHOO, "15"),
			new ObjectKeyValuePair(Platform.TINDER, "51"),
			new ObjectKeyValuePair(Platform.VIBER, "55"),
			new ObjectKeyValuePair(Platform.TWITTER, "66"),
			new ObjectKeyValuePair(Platform.SKYPE, "114"),
			new ObjectKeyValuePair(Platform.AMAZON, "146"),
			new ObjectKeyValuePair(Platform.TWILIO, "248"),
			new ObjectKeyValuePair(Platform.BADOO, "283"),
			new ObjectKeyValuePair(Platform.SNAPCHAT, "287"),
			new ObjectKeyValuePair(Platform.WEEBLY, "297"),
			new ObjectKeyValuePair(Platform.MICROSOFT, "338"),
			new ObjectKeyValuePair(Platform.MEETME, "344"),
			new ObjectKeyValuePair(Platform.YANDEX, "346"),
			new ObjectKeyValuePair(Platform.FIVERR, "356"),
			new ObjectKeyValuePair(Platform.ALIBABA, "387"),
			new ObjectKeyValuePair(Platform.PROTONMAIL, "404"),
			new ObjectKeyValuePair(Platform.STEAM, "427"),
			new ObjectKeyValuePair(Platform.CRAIGSLIST, "469"),
			new ObjectKeyValuePair(Platform.G2A, "476"),
			new ObjectKeyValuePair(Platform.MAILRU, "489"),
			new ObjectKeyValuePair(Platform.ZOOSK, "496"),
			new ObjectKeyValuePair(Platform.MUSICALLY, "501"),
			new ObjectKeyValuePair(Platform.LINKEDIN, "509"),
			new ObjectKeyValuePair(Platform.DISCORD, "544"),
			new ObjectKeyValuePair(Platform.SPOTIFY, "545"),
			new ObjectKeyValuePair(Platform.REDIFFMAIL, "616"),
			new ObjectKeyValuePair(Platform.NETFLIX, "622"),
			new ObjectKeyValuePair(Platform.APPLE, "644"),
			new ObjectKeyValuePair(Platform.BUMBLE, "646"),
			new ObjectKeyValuePair(Platform.OTHER, "ot"),
			new ObjectKeyValuePair(Platform.OTHER, "ot"),
			new ObjectKeyValuePair(Platform.OTHER, "ot"),
			new ObjectKeyValuePair(Platform.OTHER, "ot"),
	}),
	SMSPVA("SMSPVA", "http://smspva.com/priemnik.php", false, true, new ObjectKeyValuePair[] {
			new ObjectKeyValuePair(Platform.AMAZON, "opt44"),
			new ObjectKeyValuePair(Platform.BADOO, "opt56"),
			new ObjectKeyValuePair(Platform.BURGERKING, "opt3"),
			new ObjectKeyValuePair(Platform.CRAIGSLIST, "opt26"),
			new ObjectKeyValuePair(Platform.DISCORD, "opt45"),
			new ObjectKeyValuePair(Platform.FACEBOOK, "opt2"),
			new ObjectKeyValuePair(Platform.FASTMAIL, "opt43"),
			new ObjectKeyValuePair(Platform.FIVERR, "opt6"),
			new ObjectKeyValuePair(Platform.G2A, "opt68"),
			new ObjectKeyValuePair(Platform.GOOGLE, "opt1"),
			new ObjectKeyValuePair(Platform.INSTAGRAM, "opt16"),
			new ObjectKeyValuePair(Platform.LINKEDIN, "opt8"),
			new ObjectKeyValuePair(Platform.MAILRU, "opt33"),
			new ObjectKeyValuePair(Platform.MEETME, "opt17"),
			new ObjectKeyValuePair(Platform.MICROSOFT, "opt15"),
			new ObjectKeyValuePair(Platform.NETFLIX, "opt101"),
			new ObjectKeyValuePair(Platform.OTHER, "opt19"),
			new ObjectKeyValuePair(Platform.PAYPAL, "opt83"),
			new ObjectKeyValuePair(Platform.EBAY, "opt83"),
			new ObjectKeyValuePair(Platform.PROTONMAIL, "opt57"),
			new ObjectKeyValuePair(Platform.SNAPCHAT, "opt90"),
			new ObjectKeyValuePair(Platform.SPOTIFY, "opt98"),
			new ObjectKeyValuePair(Platform.STEAM, "opt58"),
			new ObjectKeyValuePair(Platform.TELEGRAM, "opt29"),
			new ObjectKeyValuePair(Platform.TINDER, "opt9"),
			new ObjectKeyValuePair(Platform.TWILIO, "opt66"),
			new ObjectKeyValuePair(Platform.TWITTER, "opt41"),
			new ObjectKeyValuePair(Platform.VIBER, "opt11"),
			new ObjectKeyValuePair(Platform.WEEBLY, "opt54"),
			new ObjectKeyValuePair(Platform.WHATSAPP, "opt20"),
			new ObjectKeyValuePair(Platform.YAHOO, "opt65"),
			new ObjectKeyValuePair(Platform.YANDEX, "opt23"),
	}),
	SMS_ACTIVATE("SMS-Activate", "http://sms-activate.ru/stubs/handler_api.php", false, true, new ObjectKeyValuePair[] {
			new ObjectKeyValuePair(Platform.INSTAGRAM, "ig"),
			new ObjectKeyValuePair(Platform.WHATSAPP, "wa"),
			new ObjectKeyValuePair(Platform.TELEGRAM, "tg"),
			new ObjectKeyValuePair(Platform.OTHER, "ot"),
			new ObjectKeyValuePair(Platform.GOOGLE, "go"),
			new ObjectKeyValuePair(Platform.FACEBOOK, "fb"),
			new ObjectKeyValuePair(Platform.TWITTER, "tw"),
			new ObjectKeyValuePair(Platform.MAILRU, "ma"),
			new ObjectKeyValuePair(Platform.MICROSOFT, "mm"),
			new ObjectKeyValuePair(Platform.YAHOO, "mb"),
			new ObjectKeyValuePair(Platform.STEAM, "mt"),
			new ObjectKeyValuePair(Platform.TINDER, "oi"),
			new ObjectKeyValuePair(Platform.MEETME, "fd"),
			new ObjectKeyValuePair(Platform.LINKEDIN, "tn"),
	}),
	NONE("None (manual)", null, false, false, new ObjectKeyValuePair[] {});
	
	
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
	private final ObjectKeyValuePair[] platformCodes;
	
	
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
	private SMSProvider(String name, String endpoint, boolean username, boolean apiKey, ObjectKeyValuePair[] platformCodes) {
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
		for (ObjectKeyValuePair nvp : platformCodes) {
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
