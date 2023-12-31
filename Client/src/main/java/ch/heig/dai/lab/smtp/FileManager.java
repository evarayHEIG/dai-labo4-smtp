package ch.heig.dai.lab.smtp;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * This class is used to read the address and messages configuration files
 * @author Eva Ray
 * @author Rafael Dousse
 */
public class FileManager {

    private final File address;
    private final File message;


    /**
     * Create a new FileManager with the given address and messages files
     *
     * @param pathAddressFile  The path to the address file
     * @param pathMessagesFile The path to the messages file
     */
    public FileManager(String pathAddressFile, String pathMessagesFile) {
        address = new File(pathAddressFile);
        message = new File(pathMessagesFile);
    }

    /**
     * Get the victims from the address file
     *
     * @return an ArrayList of victims
     * @throws IOException if the file is empty or if the email address doesn't match the requested format
     */
    public ArrayList<String> getVictims() throws IOException {

        // If the file has less than 2 addresses, throw an exception
        if (address.length() < 2) {

            throw new IOException("Address file must contain at least two email adresses.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(address), StandardCharsets.UTF_8))) {

            ArrayList<String> victims = new ArrayList<>();
            String currentVictim;
            String emailRegex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

            while ((currentVictim = reader.readLine()) != null) {

                // If the email address doesn't match the requested format, throw an exception
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

    /**
     * Get the messages from the messages file
     *
     * @return an ArrayList containing all the messages
     * @throws IOException if the file is empty, if the headers "subject" or "body" are missing or if
     * the subject or the body of one of the messages is empty.
     */
    public ArrayList<Message> getMessage() throws IOException {

        // If the file is empty, throw an exception
        if (message.length() == 0) {

            throw new IOException("Messages file provided is empty.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(message), StandardCharsets.UTF_8))) {

            ArrayList<Message> messagesContent = new ArrayList<>();
            String currentMessage;
            String[] temp = {"", ""};

            while ((currentMessage = reader.readLine()) != null) {

                if (currentMessage.contains("subject")) {
                    String[] split = currentMessage.split("\"subject\":");

                    // If the subject is empty, throw an exception
                    if(split.length < 2){

                        throw new IOException("The subject of one of the messages is empty.");
                    }

                    temp[0] = split[1];

                } else if (currentMessage.contains("body")) {
                    String[] split = currentMessage.split("\"body\":");

                    // If the body is empty, throw an exception
                    if(split.length < 2){

                        throw new IOException("The body of one of the messages is empty.");
                    }

                    temp[1] = split[1];

                    messagesContent.add(new Message(temp[0].replaceAll("^ \"|\",", "").trim(), temp[1].replace("\"", "").trim()));

                }
            }


            for(Message message : messagesContent){

                // If the subject or the body is missing, throw an exception
                if(message.subject().isEmpty() || message.body().isEmpty()){

                    throw new IOException("The headers \"subject\" and \"body\" must be present, in this order.");
                }
            }

            return messagesContent;

        } catch (FileNotFoundException e) {
            System.err.println("Exception while reading file: " + e.getMessage());

        }

        return null;
    }
}
