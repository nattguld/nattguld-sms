package com.nattguld.sms.tasks.impl;

import com.nattguld.http.HttpClient;
import com.nattguld.http.content.bodies.FormBody;
import com.nattguld.http.requests.impl.PostRequest;
import com.nattguld.http.response.RequestResponse;
import com.nattguld.sms.SMSProvider;
import com.nattguld.sms.numbers.SMSNumber;
import com.nattguld.sms.tasks.SMSSession;

/**
 * 
 * @author randqm
 *
 */

public class GetSMSCode extends SMSSession {
	
	/**
	 * Whether we logged in or not.
	 */
	private boolean loggedIn;
	
	/**
	 * The balance at the provider.
	 */
	private String balance;
	
	
	/**
	 * Creates a new SMS session.
	 * 
	 * @param username The provider username.
	 * 
	 * @param apiKey The API key.
	 * 
	 * @param c The http client session.
	 * 
	 * @param code The platform code.
	 */
	public GetSMSCode(String username, String apiKey, HttpClient c, String code) {
		super(SMSProvider.GETSMSCODE, username, apiKey, c, code);
		
		this.balance = "-1";
	}
	
	/**
	 * Logs in to GetSMSCode
	 */
	private boolean login() {
		FormBody fb = new FormBody();
		fb.add("action", "login");
		fb.add("username", getUsername());
		fb.add("token", getAPIKey());
		
		RequestResponse rr = getClient().dispatchRequest(new PostRequest(getSMSProvider().getEndpoint(), 200, fb));
		
		if (!rr.validate()) {
			System.err.println("Failed to login (" + rr.getCode() + ")");
			return false;
		}
		this.balance = rr.getAsDoc().text().split("\\|")[1];
		return true;
	}

	@Override
	public SMSNumber requestNumber() {
		if (!loggedIn) {
			if (!login()) {
				return null;
			}
			loggedIn = true;
		}
		FormBody fb = new FormBody();
		fb.add("action", "getmobile");
		fb.add("username", getUsername());
		fb.add("token", getAPIKey());
		fb.add("pid", getCode()); //2=facebook
	
		RequestResponse rr = getClient().dispatchRequest(new PostRequest(getSMSProvider().getEndpoint(), 200, fb));
	
		if (!rr.validate()) {
			System.err.println("Failed to request number (" + rr.getCode() + ")");
			return null;
		}
		return new SMSNumber("na", rr.getResponseContent());
	}

	@Override
	public String retrieveSMS(SMSNumber smsNumber) {
		FormBody fb = new FormBody();
		fb.add("action", "getsms");
		fb.add("username", getUsername());
		fb.add("token", getAPIKey());
		fb.add("pid", getCode()); //TODO 2=facebook
		fb.add("mobile", smsNumber.getNumber());
		
		RequestResponse rr = getClient().dispatchRequest(new PostRequest(getSMSProvider().getEndpoint(), 200, fb));
		
		if (!rr.validate()) {
			System.err.println("Failed to retrieve SMS (" + rr.getCode() + ")");
			return null;
		}
		String response = rr.getResponseContent();
		
		if (!response.equals("Message|not receive")) {
			return response.split("\\|")[1];
		}
		return null;
	}

	@Override
	public void banNumber(SMSNumber smsNumber) {
		FormBody fb = new FormBody();
		fb.add("action", "addblack");
		fb.add("username", getUsername());
		fb.add("token", getAPIKey());
		fb.add("pid", getCode());
		fb.add("mobile", smsNumber.getNumber());
	
		RequestResponse rr = getClient().dispatchRequest(new PostRequest(getSMSProvider().getEndpoint(), 200, fb));
		
		if (!rr.validate()) {
			System.err.println("Failed to ban number (" + rr.getCode() + ")");
			return;
		}
	}

	@Override
	public String getBalance() {
		return balance;
	}

}
