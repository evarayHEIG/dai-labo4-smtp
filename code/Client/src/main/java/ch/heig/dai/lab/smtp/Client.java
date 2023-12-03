package ch.heig.dai.lab.smtp;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Client {

    final String SERVER_ADDRESS = "localhost";
    final int SERVER_PORT = 1025;

    String address = "C:\\Users\\rafae\\Desktop\\configLaboSMTP\\address.utf8";
    String messages = "C:\\Users\\rafae\\Desktop\\configLaboSMTP\\messages.utf8";

    final String EOL = "\r\n";
    final String DOMAIN = "trololol.com";




    public static void main(String[] args) {
        // Create a new client and run it

        final int nbGroups;


        try {
           nbGroups = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.err.println("Please enter a valid number of groups.");
            return;
        }

        Client client = new Client();

        client.run(nbGroups);
    }

    private void validation(){
        //
    }

    private void run(int nbGroups) {

        try {

            FileManager mail = new FileManager(address, messages);
            ArrayList<String> victimes = mail.getVictims();
            ArrayList<Message> listMessage = mail.getMessage();
            MailContent mailContent = new MailContent();
            Random random = new Random();
            ArrayList<ArrayList<String>> groups = MailGroup.createGroups(nbGroups, victimes);
            Iterator<ArrayList<String>> it = groups.iterator();

            while (it.hasNext()) {

                ArrayList<String> group = it.next();
                String sender = group.get(0);
                List<String> victims = group.subList(1, group.size());

                try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                     var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                     var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

                    // Read the welcome message from the server


                    String serverMessage = in.readLine();
                    System.out.println("Server: " + serverMessage);
                    System.out.println("Client: " + mailContent.hello(DOMAIN) + EOL);
                    out.write(mailContent.hello(DOMAIN) + EOL);
                    out.flush();
                    do {
                        serverMessage = in.readLine();
                        System.out.println("Server: " + serverMessage);

                    } while (serverMessage.contains("250-"));

                    //MAIL_FROM
                    System.out.println("Client: " + mailContent.mailFrom(sender, true) + EOL);
                    out.write(mailContent.mailFrom(sender, true) + EOL);
                    out.flush();

                    //MAIL_TO
                    serverMessage = in.readLine();
                    System.out.println("Server: " + serverMessage);
                    System.out.println("Client : " + mailContent.mailTo(victims, true));
                    out.write(mailContent.mailTo(victims, true));
                    out.flush();


                    //Data
                    serverMessage = in.readLine();
                    System.out.println("Server: " + serverMessage);
                    System.out.println("Client: " + "DATA" + EOL);
                    out.write("DATA" + EOL);
                    out.flush();

                    //From
                    System.out.println("Client: " + mailContent.mailFrom(sender, false) + EOL);
                    out.write(mailContent.mailFrom(sender, false) + EOL);
                    out.flush();

                    //To
                    System.out.println("Client : " + mailContent.mailTo(victims, false));
                    out.write(mailContent.mailTo(victims, false));
                    out.flush();


                    //ici pas besoin de mettre le EOL car il est déjà dans la fonction data (voir si on change ça)
                    //Date + sujet + message + .
                    System.out.println("Client: " + mailContent.data(listMessage.get(random.nextInt(listMessage.size() - 1))));
                    out.write(mailContent.data(listMessage.get(random.nextInt(listMessage.size() - 1))));
                    out.flush();
                    serverMessage = in.readLine();
                    System.out.println("Server: " + serverMessage);

                    //QUIT
                    System.out.println("Client: " + "QUIT" + EOL);
                    out.write("QUIT" + EOL);
                    out.flush();
                    serverMessage = in.readLine();
                    System.out.println("Server: " + serverMessage);


                } catch (IOException e) {
                    System.out.println("Client: exception while using client socket: " + e);
                }
            }
        } catch (Exception e){

            System.err.println("Exception in run: " + e.getMessage());
        }
    }

}