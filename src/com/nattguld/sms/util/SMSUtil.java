package com.nattguld.sms.util;

import java.util.Objects;

import com.nattguld.util.maths.Maths;

/**
 * 
 * @author randqm
 *
 */

public class SMSUtil {
	
	
	/**
	 * Retrieves a formatted version of a phone number.
	 * 
	 * @param phoneNumber The phone number.
	 * 
	 * @return The formatted version.
	 */
	public static String formatPhoneNumber(String phoneNumber) {
		if (Objects.isNull(phoneNumber) || phoneNumber.isEmpty()) {
			System.err.println("No phone number received");
			return null;
		}
		String format = phoneNumber.replace("+", "").replace(" ", "").replace("-", "");
		
		if (!Maths.isLong(format) && !Maths.isInteger(format)) {
			System.err.println("Invalid phone number (" + format + ")");
			return null;
		}
		return format;
	}

}
