package com.nattguld.sms.tasks.impl;

import com.nattguld.data.json.JsonReader;
import com.nattguld.http.HttpClient;
import com.nattguld.http.requests.impl.GetRequest;
import com.nattguld.http.response.RequestResponse;
import com.nattguld.sms.SMSProvider;
import com.nattguld.sms.cfg.SMSConfig;
import com.nattguld.sms.numbers.SMSNumber;
import com.nattguld.sms.tasks.SMSSession;
import com.nattguld.util.Misc;
import com.nattguld.util.locale.Country;

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
	 * 
	 * @param country The country.
	 */
	public SMSPVA(String apiKey, HttpClient c, String code, Country country) {
		super(SMSProvider.SMSPVA, null, apiKey, c, code, country);
	}

	@Override
	public SMSNumber requestNumber() {
		String countryCode = getCountry() == Country.UNITED_KINGDOM ? "UK" : getCountry().getCode().toUpperCase();
		
		RequestResponse rr = getClient().dispatchRequest(new GetRequest(getSMSProvider().getEndpoint() 
				+ "?metod=get_number&country=" + countryCode + "&service=" 
				+ getCode() + "&id=1&apikey=" + getAPIKey()));
		
		if (!rr.validate()) {
			getLogger().error("Failed to receive number (" + rr.getCode() + ")");
			return null;
		}
		if (!rr.getResponseContent().contains("response")) {
			getLogger().info(rr.getResponseContent());
			return null;
		}
		JsonReader jsonReader = rr.getJsonReader();
		
		String response = jsonReader.getAsString("response");
		
		if (!response.equals("1")) {
			switch (response) {
			case "2":
				getLogger().info("No numbers available at the moment");
				return null;
				
			case "5":
				getLogger().info("You have exceeded the number of requests per minute");
				Misc.sleep(60000);
				return requestNumber();
				
			case "6":
				getLogger().info("You will be banned for 10 minutes, because scored negative karma");
				SMSConfig.getConfig().setAPIKey(null);
				return null;
				
			case "7":
				getLogger().info("You have exceeded the number of concurrent streams. SMS Wait from previous orders");
				Misc.sleep(60000);
				return requestNumber();
			}
		}
		String number = jsonReader.getAsString("number");
		String id = jsonReader.getAsString("id");

		return new SMSNumber(id, getCountry().getPhoneCode() + number);
	}

	@Override
	public String retrieveSMS(SMSNumber smsNumber) {
		String countryCode = getCountry() == Country.UNITED_KINGDOM ? "UK" : getCountry().getCode().toUpperCase();
		
		RequestResponse rr = getClient().dispatchRequest(new GetRequest(getSMSProvider().getEndpoint() 
				+ "?metod=get_sms&country=" + countryCode + "&service=" 
				+ getCode() + "&id=" + smsNumber.getId() + "&apikey=" + getAPIKey()));
				
		if (!rr.validate()) {
			getLogger().warning("[" + smsNumber.getNumber() + "]: Failed to request SMS (" + rr.getCode() + ")");
			return null;
		}
		JsonReader jsonReader = rr.getJsonReader();
					
		String response = jsonReader.getAsString("response");
					
		if (response.equals("1")) {
			return jsonReader.getAsString("sms");
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
		JsonReader jsonReader = rr.getJsonReader();
		return jsonReader.getAsString("balance");
	}

}
