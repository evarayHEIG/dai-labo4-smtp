package ch.heig.dai.lab.smtp;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

/**
 * This class is used to create the content of SMTP messages
 * @author Eva Ray
 * @author Rafael Dousse
 */
public class MailContent {

    /**
     * Creates the SMTP EHLO message
     *
     * @param domain the domain of the SMTP server
     * @return string containing the SMTP EHLO message
     */
    public String hello(String domain) {
        return "EHLO " + domain;
    }

    /**
     * Creates the SMTP MAIL FROM message or the From part of the mail
     *
     * @param sender    the sender's email
     * @param isMailFrom true if you want to send MAIL FROM or false if you want to send From
     * @return string containing the SMTP MAIL FROM message or the From part of the mail
     */
    public String mailFrom(String sender, boolean isMailFrom) {
        return isMailFrom ? "MAIL FROM: <" + sender + ">" : "From: " + sender;
    }

    /**
     * Creates the SMTP RCPT TO message or the To part of the mail
     *
     * @param victimList  the victims email list
     * @param isRcptTo true if you want to send RCTP TO or false if you want to send To
     * @return string containing the SMTP RCPT TO message or the To part of the mail
     */
    public String mailTo(List<String> victimList, boolean isRcptTo) {

        if (victimList.isEmpty()) {
            throw new IllegalArgumentException("The list of victims is empty");
        }

        StringBuilder victims = new StringBuilder();
        Iterator<String> it = victimList.iterator();

        if (isRcptTo) {
            while (it.hasNext()) {
                victims.append("RCPT TO: <").append(it.next()).append(">");
                if (it.hasNext()) {
                    victims.append("\r\n");
                }
            }

        } else {
            victims.append("To: ");
            while (it.hasNext()) {
                victims.append(it.next());
                if (it.hasNext()) {
                    victims.append(", ");
                }
            }

        }
        return victims.toString();
    }


    /**
     * Creates an SMTP message containing the date, subject and body of the message.
     *
     * @param message the message to send
     * @return string containing the date, subject and body of the message and ends with a dot.
     * @throws UnsupportedEncodingException when the encoding provided is not supported
     */
    public String data(Message message) throws UnsupportedEncodingException {
        return String.format(
                "Content-Type: text/plain; charset=utf-8 \r\n" +
                        "Date: %s\r\n" +
                        "Subject: =?utf-8?B?" +
                        "%s"
                        + "?= \r\n" +
                        "\r\n" +
                        "%s\r\n" +
                        ".",
                java.time.LocalDateTime.now(),
                Base64.getEncoder().encodeToString(message.subject().getBytes()),
                message.body());
    }
}
