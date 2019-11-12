package com.nattguld.sms.util;

import com.nattguld.data.json.JsonReader;
import com.nattguld.http.HttpClient;
import com.nattguld.http.requests.impl.PostRequest;
import com.nattguld.http.response.RequestResponse;
import com.nattguld.util.logging.Logger;

/**
 * 
 * @author randqm
 *
 */

public class FacebookAccountKit {
	
	/**
	 * The auth key.
	 */
	private final String key;
	
	/**
	 * The uid.
	 */
	private final String uid;
	
	/**
	 * The phone number to verify.
	 */
	private final String phoneNumber;
	
	/**
	 * The logger instance.
	 */
	private final Logger logger;
	
	/**
	 * The login request code.
	 */
	private String loginRequestCode;
	
	
	/**
	 * Creates a new facebook account kit.
	 * 
	 * @param The key.
	 * 
	 * @param key The auth key.
	 * 
	 * @param phoneNumber The phone number to verify.
	 */
	public FacebookAccountKit(String key, String uid, String phoneNumber) {
		this.key = key;
		this.uid = uid;
		this.phoneNumber = phoneNumber.startsWith("+") ? phoneNumber.substring(1) : phoneNumber;
		this.logger = new Logger("FB Account Kit (" + phoneNumber + ")");
	}
	
	/**
	 * Requests a confirmation code for a phone number.
	 * 
	 * @param c The client session to use.
	 * 
	 * @return Whether we requested successfully or not.
	 * 
	 * @throws Exception
	 */
	public String requestConfirmationCode(HttpClient c) throws Exception {
		logger.debug("Requesting confirmation code to " + phoneNumber);
		
		try {
			RequestResponse rr = c.dispatchRequest(new PostRequest("https://graph.accountkit.com/v1.2/start_login?access_token=" + key 
					+ "&credentials_type=phone_number&fb_app_events_enabled=1&fields=privacy_policy%2Cterms_of_service&locale=en_US&logging_ref=" 
					+ uid + "&phone_number=" + phoneNumber + "&response_type=token&sdk=ios", 200));
		
			if (!rr.validate()) {
				return "Failed to request confirmation code for " + phoneNumber;
			}
			if (rr.getAsDoc().text().contains("Please enter a valid phone number")) {
				return "Failed to request confirmation code, phone number " + phoneNumber + " is invalid";
			}
			JsonReader jsonReader = rr.getJsonReader();
	    	
	    	this.loginRequestCode = jsonReader.getAsString("login_request_code");
	    	
	    	return null;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.exception(ex);
			return "Exception occurred while requesting confirmation code for " + phoneNumber;
		}
	}
	
	/**
	 * Attempts to verify the confirmation code.
	 * 
	 * @param c The client session to use.
	 * 
	 * @param confirmationCode The confirmation code.
	 * 
	 * @return The response.
	 * 
	 * @throws Exception
	 */
	public Object verifyConfirmationCode(HttpClient c, String confirmationCode) throws Exception {
		try {
			RequestResponse rr = c.dispatchRequest(new PostRequest("https://graph.accountkit.com/v1.2/confirm_login?access_token=" 
		+ key + "&confirmation_code=" + confirmationCode + "&credentials_type=phone_number&fb_app_events_enabled=1&fields=privacy_policy%2Cterms_of_service&locale=en_US&logging_ref=" 
					+ uid + "&login_request_code=" + loginRequestCode + "&phone_number=" + phoneNumber + "&response_type=token&sdk=ios", 200));
		
			if (!rr.validate()) {
				if (!rr.validate(400)) {
					return "Failed to verify confirmation code";
				}
				rr = c.dispatchRequest(new PostRequest("https://graph.accountkit.com/v1.2/confirm_login?access_token=" 
				+ key + "&confirmation_code=" + confirmationCode + "&credentials_type=phone_number&fb_app_events_enabled=1&fields=privacy_policy%2Cterms_of_service&locale=en_US&logging_ref=" 
						+ uid + "&login_request_code=" + loginRequestCode + "&phone_number=" + phoneNumber + "&response_type=token&sdk=ios", 200));
				
				if (!rr.validate()) {
					return "Wrong confirmation code";
				}
			}
			JsonReader jsonReader = rr.getJsonReader();
    	
			String accessToken = jsonReader.getAsString("access_token");
			String id = jsonReader.getAsString("id");
		
			return new String[] {accessToken, id};
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.exception(ex);
			return "Exception occurred while verifying confirmation code";
		}
	}
	
}
