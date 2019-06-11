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

public class SMSActivate extends SMSSession {
	
	/**
	 * Whether we indicated that we're listening for an SMS or not.
	 */
	private boolean listening;

	
	/**
	 * Creates a new SMS session.
	 * 
	 * @param apiKey The API key.
	 * 
	 * @param c The http client session.
	 * 
	 * @param code The platform code.
	 */
	public SMSActivate(String apiKey, HttpClient c, String code) {
		super(SMSProvider.SMS_ACTIVATE, null, apiKey, c, code);
	}
	
	/**
	 * Retrieves whether we started listening for the SMS.
	 * 
	 * @param id The SMS id.
	 * 
	 * @return The result.
	 */
	private boolean startListening(String id) {
		FormBody fb = new FormBody();
		fb.add("api_key", getAPIKey());
		fb.add("action", "setStatus");
		fb.add("status", 1);
		fb.add("id", id);
		
		RequestResponse rr = getClient().dispatchRequest(new PostRequest(getSMSProvider().getEndpoint(), 200, fb));
		
		if (!rr.validate()) {
			System.err.println("Failed to start listening for SMS code (" + rr.getCode() + ")");
			return false;
		}
		if (!rr.getResponseContent().equals("ACCESS_READY")) {
			System.err.println("Failed to start listening for SMS code: " + rr.getResponseContent());
			return false;
		}
		return true;
	}

	@Override
	public SMSNumber requestNumber() {
		FormBody fb = new FormBody();
		fb.add("api_key", getAPIKey());
		fb.add("action", "getNumber");
		fb.add("service", getCode());
		
		RequestResponse rr = getClient().dispatchRequest(new PostRequest(getSMSProvider().getEndpoint(), 200, fb));
		
		if (!rr.validate()) {
			System.err.println("Failed to request number (" + rr.getCode() + ")");
			return null;
		}
		String[] resp = rr.getResponseContent().split(":");
		String state = resp[0];
		
		if (!state.equals("ACCESS_NUMBER")) {
			System.err.println("Failed to request number (" + state + ")");
			return null;
		}
		return new SMSNumber(resp[1], resp[2]);
	}

	@Override
	public String retrieveSMS(SMSNumber smsNumber) {
		if (!listening) {
			if (!startListening(smsNumber.getId())) {
				return null;
			}
			listening = true;
		}
		FormBody fb = new FormBody();
		fb.add("api_key", getAPIKey());
		fb.add("action", "getStatus");
		fb.add("id", smsNumber.getId());
		
		RequestResponse rr = getClient().dispatchRequest(new PostRequest(getSMSProvider().getEndpoint(), 200, fb));
		
		if (!rr.validate()) {
			System.err.println("Failed to retrieve SMS (" + rr.getCode() + ")");
			return null;
		}
		String resp = rr.getResponseContent();
		
		if (resp.equals("NO_ACTIVATION") || resp.equals("ERROR_SQL") || resp.equals("BAD_KEY") || resp.equals("BAD_ACTION")) {
			System.err.println("Failed to retrieve SMS (" + resp + ")");
			return null;
		}
		if (!resp.startsWith("STATUS_OK")) {
			System.err.println("Failed to retrieve SMS, unexpected response (" + resp + ")");
			return null;
		}
		fb.add("status", -1);
		
		rr = getClient().dispatchRequest(new PostRequest(getSMSProvider().getEndpoint(), 200, fb));
		
		if (!rr.validate()) {
			System.err.println("Failed to cancel number (" + rr.getCode() + ")");
		}
		String[] args = resp.split(":");
		return resp.substring(args[0].length(), resp.length());
	}

	@Override
	public void banNumber(SMSNumber smsNumber) {
		FormBody fb = new FormBody();
		fb.add("api_key", getAPIKey());
		fb.add("action", "getStatus");
		fb.add("id", smsNumber.getId());
		fb.add("status", 6);
		
		RequestResponse rr = getClient().dispatchRequest(new PostRequest(getSMSProvider().getEndpoint(), 200, fb));
		
		if (!rr.validate()) {
			System.err.println("Failed to ban number");
			return;
		}
	}

	@Override
	public String getBalance() {
		FormBody fb = new FormBody();
		fb.add("api_key", getAPIKey());
		fb.add("action", "getBalance");
		
		RequestResponse rr = getClient().dispatchRequest(new PostRequest(getSMSProvider().getEndpoint(), 200, fb));
		
		if (!rr.validate()) {
			System.err.println("Failed to retrieve balance (" + rr.getCode() + ")");
			return "-1";
		}
		String[] resp = rr.getResponseContent().split(":");
		String state = resp[0];
		
		if (!state.equals("ACCESS_BALANCE")) {
			return state;
		}
		return resp[1] + " rub.";
	}

}
