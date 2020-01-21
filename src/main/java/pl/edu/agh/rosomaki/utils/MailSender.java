package pl.edu.agh.rosomaki.utils;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

    private static String MAIL_ADDRESS = " ";

    public static void setMailAddress(String mailAddress) {
        MailSender.MAIL_ADDRESS = mailAddress;
    }

    public static void send(){
        if(!Pattern.matches("^(.+)@(.+)$", MailSender.MAIL_ADDRESS))
            throw new IllegalArgumentException("wrong mail address");


        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myAccountEmail = "galeriaPowiadomienia@gmail.com";
        String password = "Galeria123";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        Message message = theMessage(session, myAccountEmail, MailSender.MAIL_ADDRESS);

        try {
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    private static Message theMessage(Session session, String myAccountEmail, String recipientsMailAddress){

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientsMailAddress));
            message.setSubject("New Photo");
            message.setText("Hey! \n New Photo added");
            return message;
        } catch (MessagingException e) {
            Logger.getLogger(MailSender.class.getName()).log(Level.SEVERE, null, e);
        }

        return null;
    }
}
