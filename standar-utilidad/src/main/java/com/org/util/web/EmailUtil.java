package com.org.util.web;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class EmailUtil {

	private EmailUtil() {

	}

	public static void sendMail(String subject, String message, String mailto) throws EmailException {
		Email email = new SimpleEmail();
		email.setHostName("smtp.googlemail.com");
		email.setSmtpPort(465);
		email.setAuthenticator(new DefaultAuthenticator("jarenderosr@gmail.com", "abc100.1"));
		email.setSSLOnConnect(true);
		email.setFrom("jarenderosr@gmail.com");
		email.setSubject(subject);
		email.setMsg(message);
		email.addTo(mailto);
		email.send();
	}
}
