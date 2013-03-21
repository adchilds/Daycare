package models;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import lib.Logger;
import lib.OSProperties;

/**
 * <p>Contains methods related to emailing. Allows the Daycare
 * Management System to provide bug reporting, and various features
 * that may require email.
 * 
 * @author Adam Childs
 * @since 3/11/2013
 */
public class Bug_Report_Model 
{
	OSProperties osp = new OSProperties();
	private String emailAddress = null;

	/**
	 * <p>Constructor for this class.
	 * 
	 * @param ea The email address to send the email to
	 */
	public Bug_Report_Model(String ea)
	{
		this.emailAddress = ea;
	}

	/**
	 * <p>Sends the email tied to the specific instance of this class.
	 * 
	 * @param u A String object of the user's email username (i.e. example@gmail.com)
	 * @param p A String object representing the user's password for the email
	 * provided above.
	 * @param subject The subject of the email
	 * @param content The content of the email
	 * @param file The path of the file to be sent along with the email (null if none provided)
	 */
	public boolean sendEmail(String u, String p, String subject, String content, String file)
	{
		final String username = u;
		final String password = p;

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
			new javax.mail.Authenticator()
			{
				protected PasswordAuthentication getPasswordAuthentication()
				{
					return new PasswordAuthentication(username, password);
				}
			}
		);

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("adchilds@eckerd.edu"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailAddress));
			message.setSubject(subject);
			
			BodyPart contentArea = new MimeBodyPart();
			contentArea.setText(content);

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(contentArea);
			
			// If an attachment is included
			if (file != null)
			{
				BodyPart attachment = new MimeBodyPart();
				DataSource source = new FileDataSource(file);
				attachment.setDataHandler(new DataHandler(source));
				System.out.println("File name: " + file.substring(file.lastIndexOf(osp.getSeparator())));
				attachment.setFileName(file.substring(file.lastIndexOf(osp.getSeparator())));
				
				multipart.addBodyPart(attachment);
			}

			message.setContent(multipart);
 
			Transport.send(message);
		} catch (Exception e) {
			if (e instanceof javax.mail.AuthenticationFailedException)
				Logger.write("Error while attempting to send bug report. Could not validate username and/or password.", Logger.Level.ERROR);
			else
				Logger.write(e.getMessage(), Logger.Level.ERROR);
			return false;
		}
		return true;
	}
}