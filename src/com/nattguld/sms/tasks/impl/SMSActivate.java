package com.nattguld.sms.tasks.impl;

import java.util.Objects;

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
	 * Updates the status of a number.
	 * 
	 * @param smsNumber The number.
	 * 
	 * @param status The status.
	 * 
	 * @return The response.
	 */
	private RequestResponse updateStatus(SMSNumber smsNumber, int status) {
		FormBody fb = new FormBody();
		fb.add("api_key", getAPIKey());
		fb.add("action", "setStatus");
		fb.add("status", status);
		fb.add("id", smsNumber.getId());
		
		RequestResponse rr = getClient().dispatchRequest(new PostRequest(getSMSProvider().getEndpoint(), 200, fb));
		
		if (!rr.validate()) {
			getLogger().error("[" + smsNumber.getNumber() + "] Failed to update number status (" + status + ")");
			return null;
		}
		return rr;
	}

	@Override
	public SMSNumber requestNumber() {
		FormBody fb = new FormBody();
		fb.add("api_key", getAPIKey());
		fb.add("action", "getNumber");
		fb.add("service", getCode());
		
		RequestResponse rr = getClient().dispatchRequest(new PostRequest(getSMSProvider().getEndpoint(), 200, fb));
		
		if (!rr.validate()) {
			getLogger().error("Failed to request number (" + rr.getCode() + ")");
			return null;
		}
		String[] resp = rr.getResponseContent().split(":");
		String state = resp[0];
		
		if (!state.equals("ACCESS_NUMBER")) {
			getLogger().error("Failed to request number (" + state + ")");
			return null;
		}
		return new SMSNumber(resp[1], resp[2]);
	}

	@Override
	public String retrieveSMS(SMSNumber smsNumber) {
		if (!listening) {
			RequestResponse updateResponse = updateStatus(smsNumber, 1);
			
			if (Objects.isNull(updateResponse)) {
				getLogger().warning("[" + smsNumber.getNumber() + "] Failed to start listening for SMS (" + updateResponse.getCode() + ")");
				return null;
			}
			if (!updateResponse.getResponseContent().equals("ACCESS_READY")) {
				getLogger().warning("[" + smsNumber.getNumber() + "] Failed to start listening for SMS (" + updateResponse.getResponseContent() + ")");
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
			getLogger().warning("[" + smsNumber.getNumber() + "] Failed to retrieve SMS status (" + rr.getCode() + ")");
			return null;
		}
		String resp = rr.getResponseContent();
		
		if (resp.equals("STATUS_WAIT_CODE") || resp.equals("STATUS_WAIT_RETRY")) {
			return null;
		}
		if (resp.equals("STATUS_WAIT_RESEND")) {
			RequestResponse updateResponse = updateStatus(smsNumber, 6);
			
			if (Objects.isNull(updateResponse)) {
				getLogger().warning("[" + smsNumber.getNumber() + "] Failed to start listening for follow up SMS (" + updateResponse.getCode() + ")");
				return null;
			}
			if (!updateResponse.getResponseContent().equals("ACCESS_RETRY_GET")) {
				getLogger().warning("[" + smsNumber.getNumber() + "] Failed to start listening for follow up SMS (" + updateResponse.getResponseContent() + ")");
				return null;
			}
		}
		if (resp.equals("STATUS_CANCEL")) {
			getLogger().warning("[" + smsNumber.getNumber() + "] Number was canceled externally");
			return "INTERRUPT";
		}
		if (!resp.startsWith("STATUS_OK")) {
			getLogger().error("[" + smsNumber.getNumber() + "] Failed to retrieve SMS due unexpected response (" + resp + ")");
			return "INTERRUPT";
		}
		RequestResponse updateResponse = updateStatus(smsNumber, 6);
		
		if (Objects.isNull(updateResponse)) {
			getLogger().warning("[" + smsNumber.getNumber() + "] Failed to finish number use (" + updateResponse.getCode() + ")");
		}
		if (Objects.nonNull(updateResponse) && !updateResponse.getResponseContent().equals("ACCESS_ACTIVATION") 
				&& !updateResponse.getResponseContent().equals("ACCESS_CANCEL")) {
			getLogger().warning("[" + smsNumber.getNumber() + "] Failed to finish number use (" + updateResponse.getResponseContent() + ")");
		}
		String[] args = resp.split(":");
		return resp.substring(args[0].length(), resp.length());
	}

	@Override
	public void banNumber(SMSNumber smsNumber) {
		RequestResponse updateResponse = updateStatus(smsNumber, 8);
		
		if (Objects.isNull(updateResponse)) {
			getLogger().warning("[" + smsNumber.getNumber() + "] Failed to ban number (" + updateResponse.getCode() + ")");
			return;
		}
		if (!updateResponse.getResponseContent().equals("ACCESS_CANCEL")) {
			getLogger().warning("[" + smsNumber.getNumber() + "] Failed to ban number (" + updateResponse.getResponseContent() + ")");
		}
	}

	@Override
	public String getBalance() {
		FormBody fb = new FormBody();
		fb.add("api_key", getAPIKey());
		fb.add("action", "getBalance");
		
		RequestResponse rr = getClient().dispatchRequest(new PostRequest(getSMSProvider().getEndpoint(), 200, fb));
		
		if (!rr.validate()) {
			getLogger().warning("Failed to retrieve balance (" + rr.getCode() + ")");
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
