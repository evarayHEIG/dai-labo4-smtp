package ch.heig.dai.lab.smtp;

/**
 * This class represents a message that has a subject and a body.
 * @author Eva Ray
 * @author Rafael Dousse
 */
public class Message {
    private String subject;
    private String body;

    /**
     * Create a message with the given subject and body
     *
     * @param subject The subject of the message
     * @param body    The body of the message
     */
    public Message(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    /**
     * Create a message with no subject and no body
     */
    public Message() {
        this(null, null);
    }

    /**
     * Get the subject of the message
     *
     * @return the subject of the message
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Get the body of the message
     *
     * @return the body of the message
     */
    public String getBody() {
        return body;
    }

    /**
     * Give a string representation of the message
     *
     * @return the message as a string
     */
    @Override
    public  String toString() {
        return "Subject: " + subject + "\n" + "Body: " + body;
    }


}
