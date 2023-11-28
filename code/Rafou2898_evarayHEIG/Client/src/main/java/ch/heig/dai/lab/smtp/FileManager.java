package ch.heig.dai.lab.smtp;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

//import javafx.util.Pair;

public class FileManager {

    private final File address;
    private final File message;




    public FileManager(String pathAddressFile, String pathMessagesFile) {
        address = new File(pathAddressFile);
        message = new File(pathMessagesFile);
    }

    public ArrayList<String> getVictims() {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(address), StandardCharsets.UTF_8))) {

            ArrayList<String> victims = new ArrayList<>();
            String currentVictim;

            while ((currentVictim = reader.readLine()) != null) {
                System.out.println(currentVictim);
                victims.add(currentVictim);
            }

            return victims;

        } catch (IOException e) {
            System.out.println("Exception while reading file: " + e.getMessage());
        }


        return null;
    }

    int getNbGroup() {

        return 0;
    }

    ArrayList<Message> getMessage() {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(message), StandardCharsets.UTF_8))) {
            //TODO: voir si y'a pas un moyen de faire ça plus proprement
            ArrayList<Message> messagesContent = new ArrayList<>();
            String currentMessage;
            String[] temp = {"",""};


            while ((currentMessage = reader.readLine()) != null) {
                // System.out.println(currentMessage);
                if (currentMessage.contains("subject") ) {
                    String[] split = currentMessage.split("\"subject\":");
                    temp[0] = split[1];

                } else if (currentMessage.contains("body")) {
                    String[] split = currentMessage.split("\"body\":");
                    temp[1] = split[1];
                    messagesContent.add(new Message(temp[0], temp[1]));

                }

            }
            return messagesContent;


        } catch (IOException e) {
            System.out.println("Exception while reading file: " + e.getMessage());

        }
        return null;
    }

    public static void main(String... args) {

        String currentDirectory = FileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        System.out.println("The current working directory is " + currentDirectory);
        //String address = "/C:/Users/rafae/Desktop/Cours%20HEIG-VD/semestre%203/DAI/Labo/dai-labo4-smtp/code/Rafou2898_evarayHEIG/Client/config/address.utf8";
        String address = "C:\\Users\\rafae\\Desktop\\configLaboSMTP\\address.utf8";
        //String messages = "/C:/Users/rafae/Desktop/Cours%20HEIG-VD/semestre%203/DAI/Labo/dai-labo4-smtp/code/Rafou2898_evarayHEIG/Client/config/messages.utf8";
        String messages = "C:\\Users\\rafae\\Desktop\\configLaboSMTP\\messages.utf8";
        FileManager mail = new FileManager(address, messages);
        mail.getVictims();

        ArrayList<Message> listMessage = mail.getMessage();

        for (Message message : listMessage) {
            System.out.println("-----------------");
            System.out.println(message);
        }


    }

}
