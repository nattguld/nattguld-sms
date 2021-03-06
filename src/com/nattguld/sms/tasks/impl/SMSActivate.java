package com.nattguld.sms.tasks.impl;

import java.util.Objects;

import com.nattguld.http.HttpClient;
import com.nattguld.http.content.bodies.FormBody;
import com.nattguld.http.requests.impl.PostRequest;
import com.nattguld.http.response.RequestResponse;
import com.nattguld.sms.SMSProvider;
import com.nattguld.sms.numbers.SMSNumber;
import com.nattguld.sms.tasks.SMSSession;
import com.nattguld.util.Misc;
import com.nattguld.util.locale.Country;

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
	 * 
	 * @param country The country.
	 */
	public SMSActivate(String apiKey, HttpClient c, String code, Country country) {
		super(SMSProvider.SMS_ACTIVATE, null, apiKey, c, code, country);
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
		fb.add("country", getCountryId(getCountry()));
		
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
			}
			if (!updateResponse.getResponseContent().equals("ACCESS_READY")) {
				getLogger().warning("[" + smsNumber.getNumber() + "] Failed to start listening for SMS (" + updateResponse.getResponseContent() + ")");
			}
			listening = true;
			Misc.sleep(2000);
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
	
	/**
	 * Retrieves the id for a given country.
	 * 
	 * @param cr The country.
	 * 
	 * @return The id.
	 */
	private static int getCountryId(Country cr) {
		switch (cr) {
		case RUSSIA:
			return 0;
			
		case UKRAINE:
			return 1;
			
		case KAZAKHSTAN:
			return 2;
			
		case CHINA:
			return 3;
			
		case PHILIPPINES:
			return 4;
			
		case MYANMAR:
			return 5;
			
		case INDONESIA:
			return 6;
			
		case MALAYSIA:
			return 7;
			
		case KENYA:
			return 8;
			
		case VIETNAM:
			return 10;
			
		case KYRGYZSTAN:
			return 11;
			
		case UNITED_STATES:
			return 12;
			
		case ISRAEL:
			return 13;
			
		case HONG_KONG:
			return 14;
			
		case POLAND:
			return 15;
			
		case UNITED_KINGDOM:
			return 16;
			
		case MADAGASCAR:
			return 17;
			
		case CONGO:
			return 18;
			
		case NIGERIA:
			return 19;
			
		case MACAO:
			return 20;
			
		case EGYPT:
			return 21;
			
		case INDIA:
			return 22;
			
		case IRELAND:
			return 23;
			
		case CAMBODIA:
			return 24;
			
		case HAITI:
			return 25;
			
		case GAMBIA:
			return 27;
			
		case SERBIA:
			return 29;
			
		case YEMEN:
			return 30;
			
		case SOUTH_AFRICA:
			return 31;
			
		case ROMANIA:
			return 32;
			
		case COLOMBIA:
			return 33;
			
		case ESTONIA:
			return 34;
			
		case AZERBAIJAN:
			return 35;
			
		case CANADA:
			return 36;
			
		case MOROCCO:
			return 37;
			
		case GHANA:
			return 38;
			
		case ARGENTINA:
			return 39;
			
		case UZBEKISTAN:
			return 40;
			
		case CAMEROON:
			return 41;
			
		case CHAD:
			return 42;
			
		case GERMANY:
			return 43;
			
		case LITHUANIA:
			return 44;
			
		case CROATIA:
			return 45;
			
		case SWEDEN:
			return 46;
			
		case IRAQ:
			return 47;
			
		case NETHERLANDS:
			return 48;
			
		case LATVIA:
			return 49;
			
		case AUSTRIA:
			return 50;
			
		case BELARUS:
			return 51;
			
		case THAILAND:
			return 52;
			
		case SAUDI_ARABIA:
			return 53;
				
		case MEXICO:
			return 54;
			
		case TAIWAN:
			return 55;
			
		case SPAIN:
			return 56;
			
		case IRAN:
			return 57;
			
		case ALGERIA:
			return 58;
			
		case SLOVENIA:
			return 59;
			
		case BANGLADESH:
			return 60;
			
		case SENEGAL:
			return 61;
			
		case TURKEY:
			return 62;
			
		case CZECH_REPUBLIC:
			return 63;
			
		case PERU:
			return 64;
			
		case PAKISTAN:
			return 66;
			
		case NEW_ZEALAND:
			return 67;
			
		case GUINEA:
			return 68;
			
		case MALI:
			return 69;
			
		case VENEZUELA:
			return 70;
			
		case ETHIOPIA:
			return 71;
			
		case MONGOLIA:
			return 72;
			
		case BRAZIL:
			return 73;
			
		case UGANDA:
			return 74;
			
		case ANGOLA:
			return 76;
			
		case CYPRUS:
			return 77;
			
		case FRANCE:
			return 78;
			
		case PAPUA_NEW_GUINEA:
			return 79;
			
		case MOZAMBIQUE:
			return 80;
			
		case NEPAL:
			return 81;
			
		default:
			return 0;
		}
	}

}
