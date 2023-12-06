package ch.heig.dai.lab.smtp;

import java.util.ArrayList;

/**
 * This class represents a group of victims
 * @author Eva Ray
 * @author Rafael Dousse
 */
public class Group {

    private final String sender;
    private final ArrayList<String> victims;

    /**
     * Create a group with the given people. A person is represented by its email address.
     *
     * @param people The list of people in the group (the first one is the sender)
     */
    public Group(ArrayList<String> people) {
        this.sender = people.get(0);
        this.victims = new ArrayList<>(people.subList(1, people.size()));
    }

    /**
     * Get the sender of the group
     *
     * @return the sender of the group
     */
    public String getSender() {
        return sender;
    }

    /**
     * Get the victims of the group
     *
     * @return a copy of the victims of the group
     */
    public ArrayList<String> getVictims() {
        return victims;
    }
}
