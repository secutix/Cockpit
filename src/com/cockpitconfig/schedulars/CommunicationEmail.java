package com.cockpitconfig.schedulars;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CommunicationEmail {

	// use http://www.quartz-scheduler.org/docs/tutorial/TutorialLesson02.html to receive email address and email text
	public void sendEmail (String recipent, String emailText) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("username","password");	//Username and Password
				}
			});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("myemailID@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(recipent));
			message.setSubject("DONE");
			message.setText(emailText);

			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}
}