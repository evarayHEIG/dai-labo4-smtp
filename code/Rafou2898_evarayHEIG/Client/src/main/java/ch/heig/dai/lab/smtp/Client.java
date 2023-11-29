package ch.heig.dai.lab.smtp;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    final String SERVER_ADDRESS = "localhost";
    final int SERVER_PORT = 1025;

    String address = "C:\\Users\\rafae\\Desktop\\configLaboSMTP\\address.utf8";
    String messages = "C:\\Users\\rafae\\Desktop\\configLaboSMTP\\messages.utf8";
    FileManager mail = new FileManager(address, messages);
    ArrayList<String> victimes = mail.getVictims();
    ArrayList<Message> listMessage = mail.getMessage();
    MailContent mailContent = new MailContent();

    final String EOL = "\r\n";
    final String DOMAIN = "trololol.com";


    public static void main(String[] args) {
        // Create a new client and run it
        Client client = new Client();
        client.run();
    }

    private void run() {

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
             var scanner = new Scanner(System.in)) {

            // Read the welcome message from the server


            String serverMessage = in.readLine();
            System.out.println("Server: " + serverMessage);
            System.out.println("Client: " + mailContent.hello(DOMAIN) +EOL);
            out.write(mailContent.hello(DOMAIN) + EOL);
            out.flush();
            do {
                serverMessage = in.readLine();
                System.out.println("Server: " + serverMessage);

            } while (serverMessage.contains("250-"));

            //MAIL_FROM
            System.out.println("Client: " +mailContent.mailFrom(victimes.get(0), true) + EOL);
            out.write(mailContent.mailFrom(victimes.get(0), true) + EOL);
            out.flush();

            //MAIL_TO
            serverMessage = in.readLine();
            System.out.println("Server: " + serverMessage);
            System.out.println("Client : " + mailContent.mailTo(victimes.get(1),true) + EOL );
            out.write(mailContent.mailTo(victimes.get(1), true) + EOL);
            out.flush();
            //MAIL_TO 2
            System.out.println("Client : " + mailContent.mailTo(victimes.get(2),true) + EOL );
            out.write(mailContent.mailTo(victimes.get(2), true) + EOL);
            out.flush();

            //Data
            serverMessage = in.readLine();
            System.out.println("Server: " + serverMessage);
            System.out.println("Client: " + "DATA" + EOL);
            out.write( "DATA" + EOL);
            out.flush();

            //From
            System.out.println("Client: " +mailContent.mailFrom(victimes.get(0), false) + EOL);
            out.write(mailContent.mailFrom(victimes.get(0), false) + EOL);
            out.flush();

            //To
            System.out.println("Client : " + mailContent.mailTo(victimes.get(1),false) + ", hihihi@trololol.org" + EOL );
            out.write(mailContent.mailTo(victimes.get(1), false) );
            out.write(", hihihi@trololol.org" + EOL);
            out.flush();


            //ici pas besoin de mettre le EOL car il est déjà dans la fonction data (voir si on change ça)
            //Date + sujet + message + .
            System.out.println("Client: " + mailContent.data(listMessage.get(0)));
            out.write( mailContent.data(listMessage.get(0)));
            out.flush();
            serverMessage = in.readLine();
            System.out.println(serverMessage);

        } catch (IOException e) {
            System.out.println("Client: exception while using client socket: " + e);
        }
    }

}