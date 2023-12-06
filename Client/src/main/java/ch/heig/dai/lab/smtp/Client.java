package ch.heig.dai.lab.smtp;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * This class represents a client that will send prank messages to a list of victims,
 * using the SMTP protocol.
 * @author Eva Ray
 * @author Rafael Dousse
 */
public class Client {

    final static String SERVER_ADDRESS = "localhost";
    final static int SERVER_PORT = 1025;
    final static String ADDRESS_PATH = "./Client/config/address.utf8";
    final static String MESSAGES_PATH = "./Client/config/messages.utf8";
    final static String EOL = "\r\n";
    final static String DOMAIN = "trololol.com";
    ArrayList<Message> messages;
    ArrayList<Group> groups;

    /**
     * Main method of the client
     *
     * @param args The arguments of the program given in command line
     */
    public static void main(String[] args) {

        final int nbGroups;

        try {
            nbGroups = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.err.println("The number of groups must be an integer.");
            return;
        }

        if(nbGroups <= 0){
            System.err.println("The number of groups must be greater than 0.");
            return;
        }

        // Create a new client and run it
        Client client = new Client();
        client.run(nbGroups);
    }

    /**
     * Execute the prank campaign using the SMTP protocol
     *
     * @param nbGroups The number of victim groups to prank
     */
    private void run(int nbGroups) {

        try {

            FileManager mail = new FileManager(ADDRESS_PATH, MESSAGES_PATH);
            messages = mail.getMessage();
            groups = GroupGenerator.createGroups(nbGroups, mail.getVictims());
            MailContent mailContent = new MailContent();
            Random random = new Random();
            Iterator<Group> it = groups.iterator();

            // We send a prank message to each group
            while (it.hasNext()) {

                Group group = it.next();
                String sender = group.getSender();
                List<String> currentVictims = group.getVictims();

                try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                     var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                     var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

                    // Read the welcome message from the server
                    String serverMessage = in.readLine();
                    System.out.println("Server: " + serverMessage);
                    System.out.println("Client: " + mailContent.hello(DOMAIN) + EOL);
                    sendMessageToServer(out, mailContent.hello(DOMAIN));
                    do {
                        serverMessage = getServerMessage(in);
                        System.out.println("Server: " + serverMessage);

                    } while (serverMessage.contains("250-"));

                    //MAIL_FROM
                    System.out.println("Client: " + mailContent.mailFrom(sender, true) + EOL);
                    sendMessageToServer(out, mailContent.mailFrom(sender, true));

                    //MAIL_TO
                    serverMessage = getServerMessage(in);
                    System.out.println("Server: " + serverMessage);
                    System.out.println("Client : " + mailContent.mailTo(currentVictims, true));
                    sendMessageToServer(out, mailContent.mailTo(currentVictims, true));

                    //Data
                    serverMessage = getServerMessage(in);
                    System.out.println("Server: " + serverMessage);
                    System.out.println("Client: " + "DATA" + EOL);
                    sendMessageToServer(out, "DATA");

                    //From
                    System.out.println("Client: " + mailContent.mailFrom(sender, false) + EOL);
                    sendMessageToServer(out, mailContent.mailFrom(sender, false));

                    //To
                    System.out.println("Client : " + mailContent.mailTo(currentVictims, false));
                    sendMessageToServer(out, mailContent.mailTo(currentVictims, false));

                    //Date + sujet + message + .
                    Message currentMessage = messages.get(random.nextInt(messages.size() - 1));
                    System.out.println("Client: " + mailContent.data(currentMessage));
                    sendMessageToServer(out, mailContent.data(currentMessage));
                    serverMessage = getServerMessage(in);
                    System.out.println("Server: " + serverMessage);

                    //QUIT
                    System.out.println("Client: " + "QUIT" + EOL);
                    sendMessageToServer(out, "QUIT");
                    serverMessage = getServerMessage(in);
                    System.out.println("Server: " + serverMessage);


                } catch (IOException e) {
                    System.out.println("Client: exception while using client socket: " + e);
                }
            }
        } catch (Exception e){

            System.err.println("Exception in Client: " + e.getMessage());
        }
    }

    /**
     * Retrieve the server message and checks if it indicates an error
     *
     * @param in The input stream
     * @return The server message
     * @throws IOException If the server message indicates an error
     */
    private String getServerMessage(BufferedReader in) throws IOException{

        String serverMessage = in.readLine();

        if(!serverMessage.startsWith("2") && !serverMessage.startsWith("3")){
            throw new IOException("Server error: " + serverMessage);
        }

        return serverMessage;
    }

    /**
     * Send a message to the server
     *
     * @param out The output stream
     * @param message The message to send
     * @throws IOException If an error occurs while sending the message
     */
    private void sendMessageToServer(BufferedWriter out, String message) throws IOException{

        out.write(message + EOL);
        out.flush();
    }

}