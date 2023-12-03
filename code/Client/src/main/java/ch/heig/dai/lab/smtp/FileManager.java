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

    public ArrayList<String> getVictims() throws IOException {

        if (address.length() < 2) {

            throw new IOException("Address file must contain at least two email adresses.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(address), StandardCharsets.UTF_8))) {

            ArrayList<String> victims = new ArrayList<>();
            String currentVictim;
            String emailRegex = "^[^@]+@[^@]+\\.[^@]+$";

            while ((currentVictim = reader.readLine()) != null) {

                if (!currentVictim.matches(emailRegex)) {

                    throw new IOException("One or more email address don't match the requested format.");
                }
                victims.add(currentVictim);
            }

            return victims;

        } catch (FileNotFoundException e) {
            System.out.println("Exception while reading file: " + e.getMessage());
        }

        return null;
    }

    ArrayList<Message> getMessage() throws IOException {


        if (message.length() == 0) {

            throw new IOException("Messages file provided is empty.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(message), StandardCharsets.UTF_8))) {
            //TODO: voir si y'a pas un moyen de faire ça plus proprement
            ArrayList<Message> messagesContent = new ArrayList<>();
            String currentMessage;
            String[] temp = {"", ""};
            Message newMessage = null;

            while ((currentMessage = reader.readLine()) != null) {


                if (currentMessage.contains("subject")) {
                    String[] split = currentMessage.split("\"subject\":");

                    if(split.length < 2){

                        throw new IOException("The subject of one of the messages is empty.");
                    }

                    temp[0] = split[1];

                } else if (currentMessage.contains("body")) {
                    String[] split = currentMessage.split("\"body\":");

                    if(split.length < 2){

                        throw new IOException("The body of one of the messages is empty.");
                    }

                    temp[1] = split[1];

                    newMessage = new Message(temp[0].replaceAll("^ \"|\",", ""), temp[1].replace("\"", ""));
                    messagesContent.add(newMessage);

                }

                    /*if(newMessage.getBody() == null || newMessage.getSubject() == null){

                        throw new IOException("The messages config file must match the Json format presented in the README.");
                    }*/


            }


            for(Message message : messagesContent){

                if(message.getSubject().isEmpty() || message.getBody().isEmpty()){

                    throw new IOException("The headers subject and body must be present.");
                }
            }

            return messagesContent;


        } catch (FileNotFoundException e) {
            System.err.println("Exception while reading file: " + e.getMessage());

        }

        return null;
    }

    public static void main(String... args) {

        String currentDirectory = FileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        System.out.println("The current working directory is " + currentDirectory);
        //String address = "/C:/Users/rafae/Desktop/Cours%20HEIG-VD/semestre%203/DAI/Labo/dai-labo4-smtp/code/Rafou2898_evarayHEIG/Client/config/address.utf8";
        //String address = "C:\\Users\\rafae\\Desktop\\configLaboSMTP\\address.utf8";
        String address = "./code/Client/config/address.utf8";
        //String messages = "/C:/Users/rafae/Desktop/Cours%20HEIG-VD/semestre%203/DAI/Labo/dai-labo4-smtp/code/Rafou2898_evarayHEIG/Client/config/messages.utf8";
        //String messages = "C:\\Users\\rafae\\Desktop\\configLaboSMTP\\messages.utf8";
        String messages = "./code/Client/config/messages.utf8";
        FileManager mail = new FileManager(address, messages);

        try {
            mail.getVictims();

            ArrayList<Message> listMessage = mail.getMessage();

            for (Message message : listMessage) {
                System.out.println("-----------------");
                System.out.println(message);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }

    }
}
