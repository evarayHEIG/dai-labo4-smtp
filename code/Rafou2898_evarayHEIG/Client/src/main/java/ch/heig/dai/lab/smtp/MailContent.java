package ch.heig.dai.lab.smtp;

import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;

public class MailContent {

    //Pas sure que ce soit nécessaire
    // private final String DOMAIN;

    public MailContent(/*String domain*/){
        /*this.DOMAIN = domain;*/
    }

    String hello(String DOMAIN){
        return "EHLO " + DOMAIN;
    }

    /**
     * Used to send the mail to the victim
     * @param sender the victim's mail
     * @param MAIL_FROM true if you want to send RECTP TO or false if you want to send to
     * @return the string to send to the server
     */
    String mailFrom(String sender, boolean MAIL_FROM){
        return MAIL_FROM ? "MAIL FROM: <" + sender + ">" : "From: " + sender;
    }

    /**
     * Used to send the mail to the victim
     * @param victim the victim's mail
     * @param RCPT_TP true if you want to send RECTP TO or false if you want to send to
     * @return the string to send to the server
     */
    String mailTo(String victim, boolean RCPT_TP){
        return RCPT_TP ? "RCPT TO: <" + victim + ">" : "To: " + victim ;
    }


    String data( Message message) throws UnsupportedEncodingException {
        return String.format(
                "Date: %s\n" +
                "Subject: %s\n" +
                "\n" +
                "%s\r\n" +
                //alors ici le MimeUtility.encodeText permet d'encoder le sujet et le body en UTF-8 pour le mail sur le serveur
                //mais du coup dans le terminal ça n'affiche pas bien les messages
                // Rectification... ça fait quand meme de la merde...
                ".\r\n", java.time.LocalDateTime.now().toString(),MimeUtility.encodeText( message.getSubject(), "UTF-8", "B"), message.getBody()/*MimeUtility.encodeText(message.getBody(), "UTF-8", "B")*/);
    }
}
