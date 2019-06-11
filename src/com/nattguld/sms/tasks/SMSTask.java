package com.nattguld.sms.tasks;

import java.util.Objects;

import com.nattguld.data.cfg.ConfigManager;
import com.nattguld.http.HttpClient;
import com.nattguld.sms.Platform;
import com.nattguld.sms.cfg.SMSConfig;
import com.nattguld.sms.numbers.SMSNumber;
import com.nattguld.sms.tasks.impl.*;
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
	 * The SMS config at the start of the task.
	 */
	private final SMSConfig cfg;
	
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
		this.cfg = (SMSConfig)ConfigManager.getConfig(new SMSConfig());
		
		startSession();
	}
	
	/**
	 * Starts the SMS session.
	 * 
	 * @param cfg The current SMS config.
	 */
	private void startSession() {
		String code = cfg.getSMSProvider().getPlatformCode(platform);
		
		switch (cfg.getSMSProvider()) {
		case GETSMSCODE:
			this.c = new HttpClient();
			this.session = new GetSMSCode(cfg.getUsername(), cfg.getAPIKey(), c, code);
			break;
			
		case NONE:
			this.session = new Manual();
			break;
			
		case SMSPVA:
			this.c = new HttpClient();
			this.session = new SMSPVA(cfg.getAPIKey(), c, code);
			break;
			
		case SMS_ACTIVATE:
			this.c = new HttpClient();
			this.session = new SMSActivate(cfg.getAPIKey(), c, code);
			break;
			
			default:
				System.err.println("No session start present for " + cfg.getSMSProvider().getName());
				break;
		}
	}
	
	/**
	 * Requests an SMS number.
	 * 
	 * @return The SMS number.
	 */
	public SMSNumber requestSMSNumber() {
		smsNumber = session.requestNumber();

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
		
		while (Objects.isNull(sms) && elapsed >= cfg.getSMSReceiveTimeout()) {
			Misc.sleep(20000);
			
			sms = session.retrieveSMS(smsNumber);
			
			elapsed += 20;
		}
		if (Objects.isNull(sms)) {
			System.err.println("Failed to retrieve SMS");
			return null;
		}
		this.smsReceived = true;
		return sms;
	}

	@Override
	public void close() throws Exception {
		if (Objects.nonNull(c)) {
			c.close();
			c = null;
		}
		if (Objects.nonNull(session)) {
			if (!smsReceived) {
				session.banNumber(smsNumber);
			}
			session = null;
		}
	}

}
