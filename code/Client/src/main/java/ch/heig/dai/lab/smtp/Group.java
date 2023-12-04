package ch.heig.dai.lab.smtp;

import java.util.ArrayList;

public class Group {

    private final String sender;
    private final ArrayList<String> victims;

    public Group(ArrayList<String> people) {
        this.sender = people.get(0);
        this.victims = new ArrayList<>(people.subList(1, people.size()));
    }

    public String getSender() {
        return sender;
    }

    public ArrayList<String> getVictims() {
        return victims;
    }
}
