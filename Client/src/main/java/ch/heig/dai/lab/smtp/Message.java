package ch.heig.dai.lab.smtp;

/**
 * This record represents a message that has a subject and a body.
 *
 * @author Eva Ray
 * @author Rafael Dousse
 */
public record Message(String subject, String body) {

    /**
     * Give a string representation of the message
     *
     * @return the message as a string
     */
    @Override
    public String toString() {
        return "Subject: " + subject + "\n" + "Body: " + body;
    }
}
