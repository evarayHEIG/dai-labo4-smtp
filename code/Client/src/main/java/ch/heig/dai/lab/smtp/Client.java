package ch.heig.dai.lab.smtp;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Client {

    final static String SERVER_ADDRESS = "localhost";
    final static int SERVER_PORT = 1025;
    final static String ADDRESS_PATH = "./code/Client/config/address.utf8";
    final static String MESSAGES_PATH = "./code/Client/config/messages.utf8";
    final static String EOL = "\r\n";
    final static String DOMAIN = "trololol.com";
    ArrayList<Message> messages;
    ArrayList<Group> groups;

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

    private String getServerMessage(BufferedReader in) throws IOException{

        String serverMessage = in.readLine();

        if(!serverMessage.startsWith("2") && !serverMessage.startsWith("3")){
            throw new IOException("Server error: " + serverMessage);
        }

        return serverMessage;
    }

    private void sendMessageToServer(BufferedWriter out, String message) throws IOException{

        out.write(message + EOL);
        out.flush();
    }

}