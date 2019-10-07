package com.nattguld.sms.kits;

import java.util.Objects;

import com.google.gson.JsonObject;
import com.nattguld.http.HttpClient;
import com.nattguld.http.content.bodies.FormBody;
import com.nattguld.http.requests.impl.PostRequest;
import com.nattguld.http.response.RequestResponse;
import com.nattguld.sms.Platform;
import com.nattguld.sms.numbers.SMSNumber;
import com.nattguld.sms.tasks.SMSTask;
import com.nattguld.util.text.TextSeed;
import com.nattguld.util.text.TextUtil;

/**
 * 
 * @author randqm
 *
 */

public class FacebookAccountKit13 {
	
	/**
	 * The client session.
	 */
	private final HttpClient c;
	
	/**
	 * The phone number.
	 */
	private String phoneNumber;
	
	/**
	 * The access token.
	 */
	private final String accessToken;
	
	/**
	 * The logging reference.
	 */
	private final String loggingRef;
	
	/**
	 * The sms task.
	 */
	private final SMSTask smsTask;
	
	/**
	 * The login request code.
	 */
	private String loginRequestCode;
	
	
	/**
	 * Creates a new facebook account kit v1.3.
	 * 
	 * @param c The client session.
	 * 
	 * @param phoneNumber The phone number.
	 */
	public FacebookAccountKit13(HttpClient c) {
		this.c = c;
		this.accessToken = "AA|" + TextUtil.randomString(15, 15, TextSeed.DIGITS) + "|" + TextUtil.randomString(32, 32, TextSeed.DIGITS, TextSeed.LOWERCASE);
		this.loggingRef = "40bd1ff0-2dfe-459d-94e5-" + TextUtil.randomString(12, 12, TextSeed.DIGITS, TextSeed.LOWERCASE);
		this.smsTask = new SMSTask(Platform.FACEBOOK);
	}
	
	/**
	 * Attempts to authenticate.
	 * 
	 * @return The authentication response.
	 */
	public KitResponse authenticate() {
		SMSNumber smsNumber = smsTask.requestSMSNumber();
		
		if (Objects.isNull(smsNumber)) {
			smsTask.close();
			return new KitResponse(false, "Failed to retrieve phone number");
		}
		phoneNumber = smsNumber.getNumber().startsWith("+") ? smsNumber.getNumber().substring(1) : smsNumber.getNumber();
		
		String login = login();
		
		if (Objects.nonNull(login)) {
			smsTask.close();
			return new KitResponse(false, login);
		}
		String confirmationCode = smsTask.retrieveSMS();

		if (Objects.isNull(confirmationCode)) {
			smsTask.close();
			return new KitResponse(false, "Failed to receive SMS code");
		}
		confirmationCode = TextUtil.extractDigits(confirmationCode);
		smsTask.close();
		return confirmLogin(confirmationCode);
	}
	
	/**
	 * Attempts to login.
	 * 
	 * @return The response.
	 */
	protected String login() {
		FormBody fb = new FormBody();
		fb.add("credentials_type", "phone_number");
		fb.add("access_token", accessToken);
		fb.add("logging_ref", loggingRef);
		fb.add("fields", "terms_of_service,privacy_policy");
		fb.add("locale", "nl_NL");
		fb.add("phone_number", phoneNumber);
		fb.add("sdk", "android");
		fb.add("notif_medium", "sms");
		fb.add("sms_token", "P5Y0XG7W93/");
		fb.add("fb_app_events_enabled", "true");
		fb.add("response_type", "token");
		fb.add("", "");
		
		RequestResponse rr = c.dispatchRequest(new PostRequest("https://graph.accountkit.com/v1.3/start_login", fb));
		
		if (!rr.validate()) {
			return "Failed to login (" + rr.getCode() + ")";
		}
		JsonObject resp = rr.getAsJsonElement().getAsJsonObject();
		
		if (!resp.has("login_request_code")) {
			return "Failed to login (" + rr.getResponseContent() + ")";
		}
		loginRequestCode = resp.get("login_request_code").getAsString();
		
		if (Objects.isNull(loginRequestCode) || loginRequestCode.isEmpty()) {
			return "Failed to extract login request code";
		}
		return null;
	}
	
	/**
	 * Confirms the login.
	 * 
	 * @param confirmationCode The confirmation code.
	 * 
	 * @return The response.
	 */
	protected KitResponse confirmLogin(String confirmationCode) {
		FormBody fb = new FormBody();
		fb.add("credentials_type", "phone_number");
		fb.add("access_token", accessToken);
		fb.add("logging_ref", loggingRef);
		fb.add("locale", "nl_NL");
		fb.add("phone_number", phoneNumber);
		fb.add("sdk", "android");
		fb.add("confirmation_code", confirmationCode);
		fb.add("fb_app_events_enabled", "true");
		fb.add("login_request_code", loginRequestCode);
		fb.add("", "");
		
		RequestResponse rr = c.dispatchRequest(new PostRequest("https://graph.accountkit.com/v1.3/confirm_login", fb));
		
		if (!rr.validate()) {
			return new KitResponse(false, "Failed to login (" + rr.getCode() + ")");
		}
		JsonObject resp = rr.getAsJsonElement().getAsJsonObject();
		
		if (!resp.has("access_token")) {
			return new KitResponse(false, "Failed to confirm login (" + rr.getResponseContent() + ")");
		}
		String accessToken = resp.get("access_token").getAsString();
		@SuppressWarnings("unused")
		String id = resp.get("id").getAsString();
		
		return new KitResponse(true, accessToken);
	}

}
