package ch.heig.dai.lab.smtp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MailGroup {

    // 2-5 victimes par groupe
    static public ArrayList<ArrayList<String>> createGroups(int nbGroup, ArrayList<String> victims)  {

        ArrayList<ArrayList<String>> groups = new ArrayList<>(nbGroup);
        ArrayList<String> remainingVictims = new ArrayList<>(victims);
        Collections.shuffle(remainingVictims);
        int currentNbGroups = 0;

        for(int i = 0; i < nbGroup; i++){

            groups.add(new ArrayList<>());
        }

        Random random = new Random();

        while(currentNbGroups < nbGroup && !remainingVictims.isEmpty()) {


            int nbVictims = 2 + random.nextInt(3);

            for (int j = 0; j < nbVictims; j++) {

                groups.get(currentNbGroups).add(remainingVictims.removeFirst());
            }
            currentNbGroups++;
        }

        return groups;
    }
}


