package edu.kit.ipd.chimpalot.util;

import java.util.Date;
import java.util.Properties;


import javax.mail.Authenticator;

import javax.mail.Message;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import javax.mail.internet.MimeMessage;




/**
 * This Class provides a basic e-Mail service which serves
 * to send automated e-Mails.
 * @author Robin 
 *
 */
public class Email {
	private static final String smtpHost = "smtp.gmail.com";
	private static final String username = "chimpalot.reward@gmail.com";
	private static final String password = "chimpalot";
	private static final String senderAddress = username;
    

    public Email() {
        // TODO Auto-generated constructor stub
    }
    
    /**
     * Sends an email to all mail addresses given in the recipientsAddress. Must separate additional mails
     * with a comma (,).
     * @param recipientsAddress the addresses to which the mail shall be sent.
     * @param subject the subject line.
     * @param text the text for the mail.
     */
    
    public boolean sendMail(String recipientsAddress,String subject,String text){
        MailAuthenticator auth = new MailAuthenticator(username, password);
 
        Properties properties = new Properties();
 

        properties.put("mail.smtp.host", smtpHost);
        /*If the logger is in Debug mode or higher(in case someone wants to do that)
         * The Mail will automatically debug as well.
        */
        properties.put("mail.debug", String.valueOf(Logger.isDebug()));
        
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", "587");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.socketFactory.fallback", "true");
        properties.put("mail.smtp.starttls.enable","true");

        Session session = Session.getDefaultInstance(properties, auth);
 
        try {

            Message msg = new MimeMessage(session);
 

            msg.setFrom(new InternetAddress(senderAddress));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(
                    recipientsAddress, false));
 
 
            msg.setSubject(subject);
            msg.setText(text);
 

            msg.setHeader("Test", "Test");
            msg.setSentDate(new Date());
 

            Transport.send(msg);
 
        }
        catch (Exception e) {
            e.printStackTrace( );
            return false;
        }
        return true;
    }
   
    class MailAuthenticator extends Authenticator {
 

        private final String user;
 

        private final String password;
 

        public MailAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }
 

        protected PasswordAuthentication getPasswordAuthentication() {
            System.out.println(this.user + " " + this.password);
            return new PasswordAuthentication(this.user, this.password);
        }
    }
    public static boolean isValidEmail(String email) {
    	boolean result = true;
    	   try {
    	      InternetAddress emailAddr = new InternetAddress(email);
    	      emailAddr.validate();
    	   } catch (AddressException ex) {
    	      result = false;
    	   }
    	   return result;
    }
   
}
