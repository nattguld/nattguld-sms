package com.nattguld.sms.tasks;

import java.util.Objects;

import com.nattguld.http.HttpClient;
import com.nattguld.sms.Platform;
import com.nattguld.sms.cfg.SMSConfig;
import com.nattguld.sms.numbers.SMSNumber;
import com.nattguld.sms.tasks.impl.GetSMSCode;
import com.nattguld.sms.tasks.impl.Manual;
import com.nattguld.sms.tasks.impl.SMSActivate;
import com.nattguld.sms.tasks.impl.SMSPVA;
import com.nattguld.util.Misc;

/**
 * 
 * @author randqm
 *
 */

public class SMSTask implements AutoCloseable {
	
	/**
	 * The platform.
	 */
	private final Platform platform;
	
	/**
	 * The SMS receival timeout.
	 */
	private final int timeout;
	
	/**
	 * The SMS session.
	 */
	private SMSSession session;
	
	/**
	 * The http client session if required.
	 */
	private HttpClient c;
	
	/**
	 * The sms number we're working with.
	 */
	private SMSNumber smsNumber;
	
	/**
	 * Whether we received an SMS yet.
	 */
	private boolean smsReceived;
	

	/**
	 * Creates a new SMS session.
	 * 
	 * @param platform The platform the session is for.
	 */
	public SMSTask(Platform platform) {
		this.platform = platform;
		this.timeout = SMSConfig.getConfig().getSMSReceiveTimeout();
		
		startSession();
	}
	
	/**
	 * Starts the SMS session.
	 * 
	 * @param cfg The current SMS config.
	 */
	private void startSession() {
		String code = Objects.isNull(platform) ? "" : SMSConfig.getConfig().getSMSProvider().getPlatformCode(platform);
		
		switch (SMSConfig.getConfig().getSMSProvider()) {
		case GETSMSCODE:
			this.c = new HttpClient();
			this.session = new GetSMSCode(SMSConfig.getConfig().getUsername(), SMSConfig.getConfig().getAPIKey(), c, code);
			break;
			
		case NONE:
			this.session = new Manual();
			break;
			
		case SMSPVA:
			this.c = new HttpClient();
			this.session = new SMSPVA(SMSConfig.getConfig().getAPIKey(), c, code);
			break;
			
		case SMS_ACTIVATE:
			this.c = new HttpClient();
			this.session = new SMSActivate(SMSConfig.getConfig().getAPIKey(), c, code);
			break;
			
		default:
			System.err.println("No session start present for " + SMSConfig.getConfig().getSMSProvider().getName());
			break;
		}
	}
	
	/**
	 * Requests an SMS number.
	 * 
	 * @return The SMS number.
	 */
	public SMSNumber requestSMSNumber() {
		try {
			smsNumber = session.requestNumber();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (Objects.isNull(smsNumber.getNumber())) {
			return null;
		}
		return smsNumber;
	}
	
	/**
	 * Retrieves the SMS.
	 * 
	 * @return The SMS.
	 */
	public String retrieveSMS() {
		String sms = null;
		int elapsed = 0;
		
		while (Objects.isNull(sms) && elapsed >= timeout) {
			Misc.sleep(20000);
			
			try {
				sms = session.retrieveSMS(smsNumber);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (Objects.nonNull(sms) && sms.equals("INTERRUPT")) {
				break;
			}
			elapsed += 20;
		}
		if (Objects.isNull(sms) || sms.equals("INTERRUPT")) {
			System.err.println("Failed to retrieve SMS");
			return null;
		}
		this.smsReceived = true;
		return sms;
	}
	
	/**
	 * Attempts to retrieve the balance.
	 * 
	 * @return The balance.
	 */
	public String retrieveBalance() {
		try {
			return session.getBalance();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return "NA";
		}
	}

	@Override
	public void close() {
		if (Objects.nonNull(c)) {
			c.close();
			c = null;
		}
		if (Objects.nonNull(session) && Objects.nonNull(smsNumber)) {
			if (!smsReceived) {
				session.banNumber(smsNumber);
			}
			session = null;
		}
	}

}
