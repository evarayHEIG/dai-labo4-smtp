package ch.heig.dai.lab.smtp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MailGroup {

    // 2-5 victimes par groupe
    static public ArrayList<Group> createGroups(int nbGroup, ArrayList<String> victims)  {

        ArrayList<Group> groups = new ArrayList<>(nbGroup);
        ArrayList<String> remainingVictims = new ArrayList<>(victims);
        Collections.shuffle(remainingVictims);
        int currentNbGroups = 0;

        Random random = new Random();

        while(currentNbGroups < nbGroup && !remainingVictims.isEmpty()) {

            ArrayList<String> currentPeople = new ArrayList<>();

            int nbVictims = 2 + random.nextInt(3);

            for (int j = 0; j < nbVictims; j++) {

                currentPeople.add(remainingVictims.remove(0));
            }

            groups.add(new Group(currentPeople));
            currentNbGroups++;
        }

        return groups;
    }
}


