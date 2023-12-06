package ch.heig.dai.lab.smtp;

// import javax.mail.internet.MimeUtility;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

public class MailContent {

    //Pas sure que ce soit nécessaire
    // private final String DOMAIN;

    public MailContent(/*String domain*/) {
        /*this.DOMAIN = domain;*/
    }

    String hello(String DOMAIN) {
        return "EHLO " + DOMAIN;
    }

    /**
     * Used to send the mail to the victim
     *
     * @param sender    the victim's mail
     * @param MAIL_FROM true if you want to send MAIL FROM or false if you want to send From
     * @return the string to send to the server
     */
    String mailFrom(String sender, boolean MAIL_FROM) {
        return MAIL_FROM ? "MAIL FROM: <" + sender + ">" : "From: " + sender;
    }

    /**
     * Used to send the mail to the victim
     *
     * @param victim  the victim's mail
     * @param RCPT_TO true if you want to send RCTP TO or false if you want to send To
     * @return the string to send to the server
     */
    String mailTo(List<String> victim, boolean RCPT_TO) {

        if (victim.isEmpty()) {
            throw new IllegalArgumentException("The list of victims is empty");
        }

        StringBuilder victims = new StringBuilder();
        Iterator<String> it = victim.iterator();

        if (RCPT_TO) {
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


    String data(Message message) throws UnsupportedEncodingException {
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
                Base64.getEncoder().encodeToString(message.getSubject().getBytes()),
                message.getBody());
    }
}
