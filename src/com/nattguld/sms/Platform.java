package com.nattguld.sms;

/**
 * 
 * @author randqm
 *
 */

public enum Platform {
	
	INSTAGRAM("Instagram"),
	OTHER("Other"), 
	GOOGLE("Google"), 
	FACEBOOK("Facebook"), 
	WHATSAPP("Whatsapp"), 
	CHATROULETTE("Chatroulette"), 
	EBAY("Ebay"),
	TELEGRAM("Telegram"),
	YAHOO("Yahoo"), 
	TINDER("Tinder"), 
	VIBER("Viber"),
	TWITTER("Twitter"),
	SKYPE("Skype"), 
	AMAZON("Amazon"),
	TWILIO("Twilio"), 
	BADOO("Badoo"), 
	SNAPCHAT("Snapchat"), 
	WEEBLY("Weebly"), 
	MICROSOFT("Microsoft"),
	MEETME("MeetMe"), 
	YANDEX("Yandex"), 
	FIVERR("Fiverr"), 
	ALIBABA("AliBaba"), 
	PROTONMAIL("ProtonMail"), 
	STEAM("Steam"), 
	CRAIGSLIST("Craigslist"),
	G2A("G2A"),
	MAILRU("Mail.ru"),
	ZOOSK("Zoosk"),
	MUSICALLY("Musical.ly"), 
	LINKEDIN("LinkedIn"),
	DISCORD("Discord"), 
	SPOTIFY("Spotify"), 
	REDIFFMAIL("RediffMail"),
	NETFLIX("Netflix"),
	APPLE("Apple"), 
	BUMBLE("Bumble"), 
	BURGERKING("BurgerKing"),
	FASTMAIL("FastMail"), 
	PAYPAL("PayPal");
	
	
	/**
	 * The name.
	 */
	private final String name;
	
	
	/**
	 * The platform.
	 * 
	 * @param name The name.
	 */
	private Platform(String name) {
		this.name = name;
	}
	
	/**
	 * Retrieves the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
