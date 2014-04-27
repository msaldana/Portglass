package com.dhs.portglass.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.dhs.portglass.dto.Account;
import com.dhs.portglass.server.ServerListener;


/**
 * Builds on java.mail API to implement a mail manager that
 * is able to send automatic e-mails from the Portglass' 
 * system to foreign entities. Implements Runnable in child classes
 * to make use of ThreadPoolController and send messages without interfering 
 * with the application's main thread. 
 *
 * @author Manuel R. Saldana
 *
 */
public class MailManager 
{

	/*
	 * The following parameters represent the e-mail credentials and configuration
	 * required for a successful connection establishment. 
	 */
	private static String username = "noreply.portglass@gmail.com";
	private static String password = "n0replyP0rtgl4ss"; 
	private static String host = "smtp.gmail.com";
	private static String port = "587";
	
	//Remove comments when using through web services
	private static String imageDir =  ServerListener.getImageDirectory()+"/";
	
	//Remove comments when using locally (testing)
	//private static String imageDir=System.getProperty("user.dir")+
	//"/WebContent/img/";

	// Create a singleton instance for the mail manager class.
	private static final MailManager singleton = new MailManager();

	private MailManager(){

	}

	/**
	 * Returns the singleton instance of MailManager.
	 * @return
	 */
	public static final MailManager getInstance(){
		return singleton;
	}

	/**
	 * Constructs a simple e-mail with the provided body. This will be 
	 * sent from the registered system's email address, to the recipient enlisted.
	 * @param toAddress
	 * @param subject
	 * @param body
	 */
	public void sendEmail(String toAddress, String subject, String body)
	{
		/*
		 * Mail API requires the following properties to be set:
		 */
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);

		/*
		 *  Try a new session with provided credentials, fails if the provided
		 *  credentials do not belong to the sender's email address.
		 */
		Session session = Session.getInstance(properties,new javax.mail.Authenticator() 
		{
			protected PasswordAuthentication getPasswordAuthentication() 
			{
				return new PasswordAuthentication(username, password);
			}
		}
				);
		try
		{
			Message message = new MimeMessage(session);
			// Fleetbook's email address
			message.setFrom(new InternetAddress(username));
			// Recipient of message
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toAddress));
			// E-mail Subject
			message.setSubject(subject);
			// E-mail Body
			message.setText(body);

			Transport.send(message);

			/*
			 * Remove comment to test if message is sent.
			 * System.out.println("Done.");
			 */

		}
		catch (MessagingException e) 
		{
			throw new RuntimeException(e);
		}
	}


	/**
	 * Constructs an email formated with an HTML body and with in-line
	 * images. This will be sent from the registered system address, to the 
	 * provided recipient e-mail address.
	 * @param toAddress - recipient e-mail address.
	 * @param subject - e-mail subject
	 * @param htmlBody - HTML formated body
	 * @param mapInlineImages - Map of images to be presented in HTML body
	 * @throws AddressException 
	 * @throws MessagingException
	 */
	public void send(String toAddress, String subject, String htmlBody, 
			Map<String, String> mapInlineImages) {
		// sets SMTP server properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.user", username);
		properties.put("mail.password", password);

		/*
		 *  Try a new session with provided credentials, fails if the provided
		 *  credentials do not belong to the sender's email address.
		 */
		Session session = Session.getInstance(properties,new javax.mail.Authenticator() 
		{
			protected PasswordAuthentication getPasswordAuthentication() 
			{
				return new PasswordAuthentication(username, password);
			}
		}
				);

		try{
			// creates a new e-mail message
			Message msg = new MimeMessage(session);

			msg.setFrom(new InternetAddress(username));


			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));


			msg.setSubject(subject);
			msg.setSentDate(new Date());

			// creates message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(htmlBody, "text/html");

			// creates multi-part
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// adds inline image attachments
			if (mapInlineImages != null && mapInlineImages.size() > 0) {
				Set<String> setImageID = mapInlineImages.keySet();

				for (String contentId : setImageID) {
					MimeBodyPart imagePart = new MimeBodyPart();
					imagePart.setHeader("Content-ID", "<" + contentId + ">");
					imagePart.setDisposition(MimeBodyPart.INLINE);

					String imageFilePath = mapInlineImages.get(contentId);
					try {
						imagePart.attachFile(imageFilePath);
					} catch (IOException ex) {
						ex.printStackTrace();
					}

					multipart.addBodyPart(imagePart);
				}
			}

			msg.setContent(multipart);

			Transport.send(msg);
		}
		catch (MessagingException e) 
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sends a formated e-mail to all of the system's administrators, that states
	 * that a request for a new user account has been made. Populates the HTML
	 * e-mail with the soliciter's data.
	 * @param user - Account object belonging to the user that solicited
	 * account creation.
	 */

	private void sendNewAccountEmail(Account user)
	{
		/*
		 * The following contains the HTML document that will be sent 
		 * to the user through e-mail.
		 */
		StringBuffer body= new StringBuffer();
		// Start of HTML
		body.append("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>New Account</title><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/></head><body style=\"margin: 0; padding: 0;\"><table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td bgcolor=\"#7EAAC3 \"><table align=\"center\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\"><tr><td align=\"center\" bgcolor=\"#FFFFFF\" style=\"padding: 40px 0 30px 0;\"><a href=\"www.portglass.com\"><img src=\"cid:image1\" alt=\"Portglass\" width=\"530px\"height=\"170px\" style=\"display: block;\" /></a></td></tr><tr><td bgcolor=\"#ffffff\" style=\"padding: 40px 30px 40px 30px;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"font-family:Georgia,serif; color:#4E443C; font-variant: small-caps; text-transform: none; font-weight: 100; margin-bottom: 0;\"><tr><td align=\"center\" style=\"padding: " +
				"25px 0 25px 0; font-size:20px\">");
		// Include request type
		body.append("New Account Request of type:  "+user.getType()+".");
		body.append("</td></tr><tr><td align=\"center\" style=\"padding: 5px 0 25px 0; font-size:12px\"></td></tr><tr><td><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td style=\"padding:10px 0px 10px 30px;\"><a style=\"font-variant:normal;\"><img src=\"cid:image2\" style=\"padding: 0 0 0 30px;\"/>");
		// Include user's name 
		body.append("Name: "+user.getFirstName() + " "+ user.getLastName());	
		body.append("</a></td></tr><tr><td style=\"padding:10px 0px 10px 30px;\"><a style=\"font-variant:normal;\"><img src=\"cid:image3\" style=\"padding: 0 0 0 30px;\"/>");
		// Include username
		body.append("Username: "+user.getEmail());
		// Rest of HTML
		body.append("</a></td></tr><tr><td align=\"center\" style=\"padding:10px 0px 10px 30px;\"></td></tr></table></td></tr></table></td></tr><tr><td bgcolor=\"#FFFFFF\" style=\"padding: 30px 30px 30px 30px;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td width=\"40%\"><a style=\"font-family:arial,helvetica,sans-serif; font-size: 10px;\">This notification was sent because you are an administrator at Portglass.<br></br>Developed by msaldana</a></td><td align=\"right\"><a style=\"font-family:arial,helvetica,sans-serif; font-size: 10px;\">Research and Development Center<br>PO Box 9000<br>Mayagüez PR 00681-9000  <br>Tel. (919) 824-6309</a> </td></tr></table></td></tr></table></td></tr></table></body>" +
				"</html>");

		// Map Application Images to HTML document.
		Map<String, String> inlineImages = new HashMap<String, String>();
		inlineImages.put("image1", imageDir+"logo.png");
		inlineImages.put("image2", imageDir+"user.png");
		inlineImages.put("image3", imageDir+"email.png");


		ArrayList<Object> admins = AccountManager.getInstance().getAdministratorList();
		// Send e-mail to all administrators
		for (int i= 0; i<admins.size(); i++)
		{
			MailManager.getInstance().send(((Account) admins.get(i)).getEmail(),
					"New Account Request - Username: " +
					user.getEmail(), body.toString(), inlineImages);
		}
	}
	
	/**
	 * Returns a Runnable object to send New Account Emails to all
	 * administrators using the sendNewAccountEmail(Account) method.
	 * @param user Account object that belongs to new user
	 * @return Runnable instance to run in new thread.
	 */
	public Runnable sendAsyncNewAccountEmail(Account user){
		return(new AsyncNewAccountEmail(user));
	}
	
	
	/**
	 * Returns a Runnable object to send a password recovery Email to 
	 * the address provided.
	 * sendPasswordRecoveryLinkEmail(String, String) method.
	 * @param email Email that belongs to user requesting recovery
	 * @return Runnable instance to run in new thread.
	 */
	public Runnable sendAsyncPasswordRecoveryEmail(String email, String url){
		return(new AsyncNewPasswordRecoveryEmail(email, url));
	}


	/**
	 * Sends a formated e-mail that states that the account for an
	 * user has been activated. This e-mail includes the user's username
	 * and password. It is sent to the e-mail corresponding to the 
	 * provided Account object.
	 * @param user - Account object that holds user's information
	 */
	public void sendNewAccountCompletionEmail(Account user)
	{

		/*
		 * The following contains the HTML document that will be sent 
		 * to the user through e-mail.
		 */
		StringBuffer body= new StringBuffer();
		// Start of HTML
		body.append("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>New Account</title><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/></head><body style=\"margin: 0; padding: 0;\"><table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td bgcolor=\"#7EAAC3 \"><table align=\"center\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\"><tr><td align=\"center\" bgcolor=\"#FFFFFF\" style=\"padding: 40px 0 30px 0;\"><a href=\"www.portglass.com\"><img src=\"cid:image1\" alt=\"Portglass\" width=\"530px\"height=\"170px\" style=\"display: block;\" /></a></td></tr><tr><td bgcolor=\"#ffffff\" style=\"padding: 40px 30px 40px 30px;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"font-family:Georgia,serif; color:#4E443C; font-variant: small-caps; text-transform: none; font-weight: 100; margin-bottom: 0;\"><tr><td align=\"center\" style=\"padding: " +
				"25px 0 25px 0; font-size:20px\">");
		// Include user's first name in the e-mail
		body.append("Welcome to Portglass, "+user.getFirstName()+"!");
		body.append("</td></tr><tr><td align=\"center\" style=\"padding: 5px 0 25px 0; font-size:12px\">Get started managing and viewing Tamrio's fleet data today!</td></tr><tr><td><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td style=\"padding:10px 0px 10px 30px;\"><a style=\"font-variant:normal;\"><img src=\"cid:image2\" style=\"padding: 0 0 0 30px;\"/>");
		// Include user's email 
		body.append("Username: "+user.getEmail());	
		body.append("</a></td></tr><tr><td style=\"padding:10px 0px 10px 30px;\"><a style=\"font-variant:normal;\"><img src=\"cid:image3\" style=\"padding: 0 0 0 30px;\"/>");
		// Include user's password
		body.append("Password: "+user.getPassword());
		// Rest of HTML
		body.append("</a></td></tr><tr><td align=\"center\" style=\"padding:10px 0px 10px 30px;\"></td></tr></table></td></tr></table></td></tr><tr><td bgcolor=\"#FFFFFF\" style=\"padding: 30px 30px 30px 30px;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td width=\"40%\"><a style=\"font-family:arial,helvetica,sans-serif; font-size: 10px;\">This notification was sent because you requested registration at Portglass.<br></br>Developed by msaldana</a></td><td align=\"right\"><a style=\"font-family:arial,helvetica,sans-serif; font-size: 10px;\">Research and Development Center<br>PO Box 9000<br>Mayag�ez PR 00681-9000  <br>Tel. (919) 824-6309</a> </td></tr></table></td></tr></table></td></tr></table></body>" +
				"</html>");

		// Map Application Images to HTML document.
		Map<String, String> inlineImages = new HashMap<String, String>();
		inlineImages.put("image1", imageDir+"logo.png");
		inlineImages.put("image2", imageDir+"user.png");
		inlineImages.put("image3", imageDir+"lock.png");


		// Send e-mail
		MailManager.getInstance().send(user.getEmail(), "Your Portglass account has been " +
				"activated!", body.toString(), inlineImages);

	}


	/**
	 * Sends a formated e-mail that states that a recovery link has
	 * been provided for a user. This e-mail includes the user's username
	 * and recovery link. It is sent to the e-mail corresponding to the 
	 * provided Account object.
	 * @param user - Account object that holds user's information
	 * @param url - Contains URL for password recovery service with embedded key.
	 */
	private void sendPasswordRecoveryLinkEmail(Account user, String url)
	{

		/*
		 * The following contains the HTML document that will be sent 
		 * to the user through e-mail.
		 */
		StringBuffer body= new StringBuffer();
		// Start of HTML
		body.append("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>New Account</title><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/></head><body style=\"margin: 0; padding: 0;\"><table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td bgcolor=\"#7EAAC3 \"><table align=\"center\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\"><tr><td align=\"center\" bgcolor=\"#FFFFFF\" style=\"padding: 40px 0 30px 0;\"><a href=\"www.portglass.com\"><img src=\"cid:image1\" alt=\"Portglass\" width=\"530px\"height=\"170px\" style=\"display: block;\" /></a></td></tr><tr><td bgcolor=\"#ffffff\" style=\"padding: 40px 30px 40px 30px;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"font-family:Georgia,serif; color:#4E443C; font-variant: small-caps; text-transform: none; font-weight: 100; margin-bottom: 0;\"><tr><td align=\"center\" style=\"padding: " +
				"25px 0 25px 0; font-size:20px\">");
		// Include user's first name in the e-mail
		body.append("A password recovery link has been created for you, "+user.getFirstName()+"!");
		body.append("</td></tr><tr><td align=\"center\" style=\"padding: 5px 0 25px 0; font-size:12px\">To get back to your Portglass account, you'll need to create a new password</td></tr><tr><td><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td style=\"padding:10px 0px 10px 30px;\"><a style=\"font-variant:normal;\"><img src=\"cid:image2\" style=\"padding: 0 0 0 30px;\"/>");
		// Include user's email 
		body.append("Username: "+user.getEmail());	
		body.append("</a></td></tr><tr><td style=\"padding:10px 0px 10px 30px;\"><a style=\"font-variant:normal;\"><img src=\"cid:image3\" style=\"padding: 0 0 0 30px;\"/>");
		// Include recovery link
		body.append("Link to reset password: <a href=\""+url+"\"> Reset Pass Now! </a>");
		// Rest of HTML
		body.append("</a></td></tr><tr><td align=\"center\" style=\"padding:10px 0px 10px 30px;\"></td></tr></table></td></tr></table></td></tr><tr><td bgcolor=\"#FFFFFF\" style=\"padding: 30px 30px 30px 30px;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td width=\"40%\"><a style=\"font-family:arial,helvetica,sans-serif; font-size: 10px;\">This notification was sent because you requested registration at Portglass.<br></br>Developed by msaldana</a></td><td align=\"right\"><a style=\"font-family:arial,helvetica,sans-serif; font-size: 10px;\">Research and Development Center<br>PO Box 9000<br>Mayag�ez PR 00681-9000  <br>Tel. (919) 824-6309</a> </td></tr></table></td></tr></table></td></tr></table></body>" +
				"</html>");

		// Map Application Images to HTML document.
		Map<String, String> inlineImages = new HashMap<String, String>();
		inlineImages.put("image1", imageDir+"logo.png");
		inlineImages.put("image2", imageDir+"user.png");
		inlineImages.put("image3", imageDir+"lock.png");


		// Send e-mail
		MailManager.getInstance().send(user.getEmail(), "How to reset your Portglass " +
				"password", body.toString(), inlineImages);

	}
	

	/**
	 * Utility Class to Send New Account Emails Asynchronously.
	 * @author Manuel R Saldana Pueyo
	 *
	 */
	private class AsyncNewAccountEmail implements Runnable{

		Account user;
		public AsyncNewAccountEmail(Account user){
			this.user = user;
		}

		@Override
		/**
		 * Used to send messages in a different thread than the that of the 
		 * main application. 
		 */
		public void run() {

			getInstance().sendNewAccountEmail(user);
		}
	}
	
	/**
	 * Utility Class to Send New Account Emails Asynchronously.
	 * @author Manuel R Saldana Pueyo
	 *
	 */
	private class AsyncNewPasswordRecoveryEmail implements Runnable{

		String email;
		String url;
		public AsyncNewPasswordRecoveryEmail(String email, String url){
			this.email = email;
			this.url = url;
			
		}

		@Override
		/**
		 * Used to send messages in a different thread than the that of the 
		 * main application. 
		 */
		public void run() {
			Account account = AccountManager.getInstance().getUser(email);
			getInstance().sendPasswordRecoveryLinkEmail(account, url);
		}
	}
}