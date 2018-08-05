package chitchatapp;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.sql.Timestamp;

class clientInstance extends Thread {

    private DataInputStream clientMessage = null;
    private PrintStream output = null;
    private Socket client = null;
    private final clientInstance[] clientThreads;
    private int clientLimit;
    private ArrayList<String> userNames;

    public clientInstance(Socket client, clientInstance[] clientThreads, ArrayList<String> userNames) {
        this.client = client;
        this.clientThreads = clientThreads;
        this.userNames = userNames;
        clientLimit = clientThreads.length;
    }

    public void run() {
        clientInstance[] clientThreads = this.clientThreads;
        int clientLimit = this.clientLimit;

        Login lg = new Login();

        lg.setVisible(true);

        try {
            /*
       * Create input and output streams for this client.
             */
            clientMessage = new DataInputStream(client.getInputStream());
            output = new PrintStream(client.getOutputStream());
            //output.println("Enter your name.");
            //String name = "";
            String user = "";

            while (user.equals("")) {
                user = lg.toString();//chose which one of these to use
                //ClientPane.user = name;
                //System.out.println("lg.toString = " + user);
                System.out.print("");
                //System.out.println("user = " + ClientPane.user);
            }

            //String user = clientMessage.readLine().trim();
            //System.out.println("username = " + user);
            //TODO: deal with duplicate usernames
            if (!userNames.isEmpty()) {
                while (userNames.contains(user)) {
                    output.println(user + " is already taken, please select a new Username");
                    user = clientMessage.readLine().trim();

                    //add login thing again
                }
            }

            synchronized (this) {
                userNames.add(user);
            }

            //output.println("Welcome to Chit Chat, it's where its at!\n To leave the chatroom send \'EXIT\'");            
            JOptionPane.showMessageDialog(null, "Welcome to Chit Chat, it's where its at!\n To leave the chatroom send \'EXIT\'");

            Timestamp stamp = new Timestamp(System.currentTimeMillis());
            System.out.println(user + " Joined: " + stamp);

            lg.dispose();

            for (int i = 0; i < clientLimit; i++) {
                String message = "*userNames*##";
                String users = listToString(userNames);
                if (clientThreads[i] != null /*&& clientThreads[i] != this*/) {//uncomment this later
                    message += user + " is now where its at!" + users;
                    System.out.println(message);
                    clientThreads[i].output.println(message);
                }
            }

            while (true) {
                String line = clientMessage.readLine();
                if (line.startsWith("EXIT")) {
                    break;
                }

                for (int i = 0; i < clientLimit; i++) {
                    if (clientThreads[i] != null) {
                        clientThreads[i].output.println(user + ": " + line);
                    }
                }
            }

            // remove user from list of usernames
            stamp = new Timestamp(System.currentTimeMillis());
            System.out.println(user + " Disconnected: " + stamp);

            synchronized (this) {
                userNames.remove(user);
            }

            for (int i = 0; i < clientLimit; i++) {
                String message = "*userNames*##";
                String users = listToString(userNames);
                if (clientThreads[i] != null /*&& clientThreads[i] != this*/) {//uncomment this later
                    message += user + " Is no longer where it's at!" + users;
                    clientThreads[i].output.println(user + " Is no longer where it's at!");
                }
            }
            /*
       * Clean up. Set the current thread variable to null so that a new client
       * could be accepted by the server.
             */
            for (int i = 0; i < clientLimit; i++) {
                if (clientThreads[i] == this) {
                    clientThreads[i] = null;
                }
            }

            clientMessage.close();
            output.close();
            client.close();
        } catch (IOException e) {
        }
    }
    
    private String listToString (ArrayList<String> input) {
        String out = "";
        out = input.stream().map((name) -> "##" + name).reduce(out, String::concat);
        return out;
    }
}
