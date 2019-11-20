/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.MarkMeIn.Handler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author test
 */
public class SendForgotEmail {

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;
    String fromUser = "forgotmmipwd@gmail.com";//just the id alone without @gmail.com
    String fromUserEmailPassword = "TestMMI@12345";

    public void sendMailPassword(String userName, int userId, String userMail) throws AddressException,
            MessagingException {

        SendForgotEmail javaEmail = new SendForgotEmail();

        javaEmail.setMailServerProperties();
        javaEmail.createEmailMessage(userMail, userName, userId);
        javaEmail.sendEmail();
    }

    public void setMailServerProperties() {

//        String emailPort = "465";//gmail's smtp port
        String emailPort = "587";//gmail's smtp port

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");

    }

    public void createEmailMessage(String userMail, String userName, int userId) throws AddressException,
            MessagingException {
        String[] toEmails = {userMail};

        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String dateStr = simpleDateFormat.format(new Date());
        System.out.println(dateStr);

        String emailSubject = "Reset Password";
        String emailBody = "Please go to the link and update your password. Link is valid for today itself."
                + "http://192.168.1.129:7001/MarkMeIn/Login.jsp?userId=" + userId + "&userName=" + userName + "&stamp=" + dateStr;

        mailSession = Session.getInstance(emailProperties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromUser, fromUserEmailPassword);
            }
        });

//        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        for (int i = 0; i < toEmails.length; i++) {
            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
        }

        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody, "text/html");//for a html email
        //emailMessage.setText(emailBody);// for a text email

    }

    public void sendEmail() throws AddressException, MessagingException {

        String emailHost = "smtp.gmail.com";

        Transport transport = mailSession.getTransport("smtp");

        transport.connect(emailHost, fromUser, fromUserEmailPassword);
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        System.out.println("Email sent successfully.");
    }

}
