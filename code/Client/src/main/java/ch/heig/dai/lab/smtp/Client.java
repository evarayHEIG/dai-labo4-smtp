package ch.heig.dai.lab.smtp;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Client {

    final String SERVER_ADDRESS = "localhost";
    final int SERVER_PORT = 1025;

    String address = "./code/Client/config/address.utf8";
    String messages = "./code/Client/config/messages.utf8";

    final String EOL = "\r\n";
    final String DOMAIN = "trololol.com";


    public static void main(String[] args) {

        final int nbGroups;

        try {
            nbGroups = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.err.println("Please enter a valid number of groups.");
            return;
        }

        // Create a new client and run it
        Client client = new Client();
        client.run(nbGroups);
    }

    private void run(int nbGroups) {

        try {

            FileManager mail = new FileManager(address, messages);
            ArrayList<String> victims = mail.getVictims();
            ArrayList<Message> messages = mail.getMessage();
            MailContent mailContent = new MailContent();
            Random random = new Random();
            ArrayList<Group> groups = MailGroup.createGroups(nbGroups, victims);
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

                    //ici pas besoin de mettre le EOL car il est déjà dans la fonction data (voir si on change ça)
                    //Date + sujet + message + .
                    System.out.println("Client: " + mailContent.data(messages.get(random.nextInt(messages.size() - 1))));
                    // Choose a random message to send
                    sendMessageToServer(out, mailContent.data(messages.get(random.nextInt(messages.size() - 1))));
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

        if(!serverMessage.startsWith("250")){

            throw new IOException("Server error: " + serverMessage);
        }

        return serverMessage;
    }

    private void sendMessageToServer(BufferedWriter out, String message) throws IOException{

        out.write(message + EOL);
        out.flush();
    }

}