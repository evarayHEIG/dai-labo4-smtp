package ch.heig.dai.lab.smtp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GroupGenerator {

    /**
     * Create a list of groups with the given number of groups and victims
     *
     * @param nbGroup The number of groups to create
     * @param victims The list of victims
     * @return The list of groups created
     */
    static public ArrayList<Group> createGroups(int nbGroup, ArrayList<String> victims) {

        ArrayList<Group> groups = new ArrayList<>(nbGroup);
        int currentNbGroups = 0;

        Random random = new Random();

        while (currentNbGroups < nbGroup) {

            ArrayList<String> currentPeople = new ArrayList<>();
            Collections.shuffle(victims);

            // There can be 2-5 victims per group (but not more than the total number of victims)
            int nbVictims = Math.min(2 + random.nextInt(3), victims.size());

            for (int j = 0; j < nbVictims; j++) {

                currentPeople.add(victims.get(j));
            }

            groups.add(new Group(currentPeople));
            currentNbGroups++;
        }

        return groups;
    }
}


