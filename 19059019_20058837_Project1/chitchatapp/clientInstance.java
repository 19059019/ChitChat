package chitchatapp;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.sql.Timestamp;

@SuppressWarnings("deprecation")
class clientInstance extends Thread {

    private DataInputStream clientMessage = null;
    private PrintStream output = null;
    private Socket client = null;
    private clientInstance[] clientThreads;
    private int clientLimit;
    private ArrayList<String> userNames;
    private String user;

    public clientInstance(Socket client, clientInstance[] clientThreads, ArrayList<String> userNames) {
        this.client = client;
        this.clientThreads = clientThreads;
        this.userNames = userNames;
        clientLimit = clientThreads.length;
    }

    @Override
    public void run() {
        clientInstance[] clientThreads = this.clientThreads;
        int clientLimit = this.clientLimit;

        try {
            clientMessage = new DataInputStream(client.getInputStream());
            output = new PrintStream(client.getOutputStream());

            // Send current usernames to client for nickname picking
            String userList = listToString(userNames);
            output.println(userList);
            user = clientMessage.readLine();
            
            // Add username to userNames list if the user completes the login
            synchronized (this) {
                if (user != null) {
                    userNames.add(user);
                }
            }

            output.println("Welcome to Chit Chat, it's where its at!"
                    + "\n To leave the chatroom send \'EXIT\'");

            Timestamp stamp = new Timestamp(System.currentTimeMillis());
            System.out.println(user + " Joined: " + stamp);

            for (int i = 0; i < clientLimit; i++) {
                String message = "*userNames*##";
                String users = listToString(userNames);

                if (clientThreads[i] != null) {
                    message += user + " is now where its at!" + users;
                    clientThreads[i].output.println(message);
                }
            }

            // Listen for messages from clients
            while (true) {
                String line = clientMessage.readLine();
                String whisper = "";
                Boolean validUser = false;

                if (line.startsWith("EXIT")) {
                    break;
                }
                

                // Decipher whisper requests
                if (line.startsWith("@")) {
                    for (int i = 1; i < line.length(); i++) {
                        if (Character.isWhitespace(line.charAt(i))) {
                            break;
                        } else {
                            whisper += line.charAt(i);
                        }
                    }
                }
                
                // Send messages to all relevant clients
                for (int i = 0; i < clientLimit; i++) {
                    if (whisper.equals("") && clientThreads[i] != null) {
                        clientThreads[i].output.println(user + ": " + line);
                        validUser = true;
                    } else if ((clientThreads[i] != null
                            && clientThreads[i].user.equals(whisper))
                            || clientThreads[i] == this) {
                        clientThreads[i].output.println("[WHISPERED]" + user
                                + ": " + line);
                        if (clientThreads[i] != this) {
                            validUser = true;
                        }
                    }
                }

                if (!validUser) {
                    this.output.println("You tried to whisper at an invalid user");
                }
            }

            stamp = new Timestamp(System.currentTimeMillis());
            System.out.println(user + " Disconnected: " + stamp);

            // remove user from list of usernames
            synchronized (this) {
                userNames.remove(user);
            }

            for (int i = 0; i < clientLimit; i++) {
                String message = "*userNames*##";
                String users = listToString(userNames);

                if (clientThreads[i] != null) {
                    message += user + " Is no longer where it's at!" + users;
                    clientThreads[i].output.println(message);
                }
            }

            // free up client that has left to open space for a new client
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
    
    /**
     * Method to turn a an array list to a String that uses ## as a Delimiter.
     * 
     * in:
     * input - String ArrayList to be turned into a string.
     * 
     * out:
     * out - String that uses ## as a Delimiter.
    **/
    private String listToString(ArrayList<String> input) {
        String out = "";
        out = input.stream().map((name) -> "##" + name).reduce(out, String::concat);
        return out;
    }
}
