package ch.heig.dai.lab.smtp;

public class MailContent {

    private final String DOMAIN;

    public MailContent(String domain){
        this.DOMAIN = domain;
    }

    String mailFrom(String sender){

        // We only want the part of the string before "@"
        int atIndex = sender.indexOf('@');
        String address = sender.substring(0, atIndex);

        return "MAIL FROM: <" + address + "@" + DOMAIN + ">";
    }

    String mailTo(String victim){

        return "RCPT TO: <" + victim + ">";
    }


    String data(FileManager.Message message, String receiver){

        String data, date, from;

        date = java.time.LocalDateTime.now().toString() + "\n";

        return null;
    }
}
