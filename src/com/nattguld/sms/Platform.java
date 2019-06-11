package com.nattguld.sms;

/**
 * 
 * @author randqm
 *
 */

public enum Platform {
	
	INSTAGRAM("Instagram"),
	OTHER("Other");
	
	
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
