package com.nattguld.sms.tasks.impl;

import javax.swing.JOptionPane;

import com.nattguld.sms.SMSProvider;
import com.nattguld.sms.numbers.SMSNumber;
import com.nattguld.sms.tasks.SMSSession;

/**
 * 
 * @author randqm
 *
 */

public class Manual extends SMSSession {
	
	
	/**
	 * Creates a new SMS session.
	 */
	public Manual() {
		super(SMSProvider.NONE, null, null, null, null);
	}

	@Override
	public SMSNumber requestNumber() {
		return new SMSNumber("NA", JOptionPane.showInputDialog("Enter your phone number: "));
	}

	@Override
	public String retrieveSMS(SMSNumber smsNumber) {
		return JOptionPane.showInputDialog("Enter the SMS (" + smsNumber.getNumber() + "): ");
	}

	@Override
	public void banNumber(SMSNumber smsNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getBalance() {
		return "";
	}

}