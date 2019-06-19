package com.nattguld.sms.tasks.impl;

import com.google.gson.JsonObject;
import com.nattguld.http.HttpClient;
import com.nattguld.http.requests.impl.GetRequest;
import com.nattguld.http.response.RequestResponse;
import com.nattguld.sms.SMSProvider;
import com.nattguld.sms.numbers.SMSNumber;
import com.nattguld.sms.tasks.SMSSession;
import com.nattguld.util.Misc;

/**
 * 
 * @author randqm
 *
 */

public class SMSPVA extends SMSSession {
	
	
	/**
	 * Creates a new SMS session.
	 * 
	 * @param apiKey The API key.
	 * 
	 * @param c The http client session.
	 * 
	 * @param code The platform code.
	 */
	public SMSPVA(String apiKey, HttpClient c, String code) {
		super(SMSProvider.SMSPVA, null, apiKey, c, code);
	}

	@Override
	public SMSNumber requestNumber() {
		RequestResponse rr = getClient().dispatchRequest(new GetRequest(getSMSProvider().getEndpoint() + "?metod=get_number&country=RU&service=" 
				+ getCode() + "&id=1&apikey=" + getAPIKey()));
		
		if (!rr.validate()) {
			getLogger().error("Failed to receive number (" + rr.getCode() + ")");
			return null;
		}
		JsonObject jsonObject = rr.getAsJsonElement().getAsJsonObject();
		
		String number = jsonObject.get("number").getAsString();
		String id = jsonObject.get("id").getAsString();
		
		if (number.equals("2")) {
			getLogger().info("We got requested to wait 60 seconds before requesting a number");
			Misc.sleep(60000);
			return requestNumber();
		}
		return new SMSNumber(id, "7" + number);
	}

	@Override
	public String retrieveSMS(SMSNumber smsNumber) {
		RequestResponse rr = getClient().dispatchRequest(new GetRequest(getSMSProvider().getEndpoint() + "?metod=get_sms&country=RU&service=" 
				+ getCode() + "&id=" + smsNumber.getId() + "&apikey=" + getAPIKey()));
				
		if (!rr.validate()) {
			getLogger().warning("[" + smsNumber.getNumber() + "]: Failed to request SMS (" + rr.getCode() + ")");
			return null;
		}
		JsonObject jsonObject = rr.getAsJsonElement().getAsJsonObject();
					
		String response = jsonObject.get("response").getAsString();
					
		if (response.equals("1")) {
			return jsonObject.get("sms").getAsString();
		}
		return null;
	}

	@Override
	public void banNumber(SMSNumber smsNumber) {
		RequestResponse rr = getClient().dispatchRequest(new GetRequest(getSMSProvider().getEndpoint() + "?metod=ban&service=" 
				+ getCode() + "&apikey=" + getAPIKey() + "&id=" + smsNumber.getId()));
	
		if (!rr.validate()) {
			getLogger().warning("[" + smsNumber.getNumber() + "]: Failed to ban number (" + rr.getCode() + ")");
			return;
		}
	}

	@Override
	public String getBalance() {
		RequestResponse rr = getClient().dispatchRequest(new GetRequest(getSMSProvider().getEndpoint() + "?metod=get_balance&service=" 
				+ getCode() + "&apikey=" + getAPIKey()));
	
		if (!rr.validate()) {
			getLogger().warning("Failed to retrieve balance (" + rr.getCode() + ")");
			return "-1";
		}
		JsonObject jsonObject = rr.getAsJsonElement().getAsJsonObject();
		return jsonObject.get("balance").getAsString();
	}

}
