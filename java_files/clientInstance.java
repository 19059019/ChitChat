import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.sql.Timestamp;

/*
* TODO:
*   Change DataInputStreams to buffered readers maybe
*   Print recieved data to server
*   Sort out Sync issues
*
*/
class clientInstance extends Thread {

  private DataInputStream clientMessage = null;
  private ObjectOutputStream objectOutput = null;
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

    try {
      /*
       * Create input and output streams for this client.
       */
      clientMessage = new DataInputStream(client.getInputStream());
      objectOutput = new ObjectOutputStream(client.getOutputStream());
      output = new PrintStream(client.getOutputStream());
      output.println("Please Enter a Username");


      String user = clientMessage.readLine().trim();

      //TODO: deal with duplicate usernames
      if (!userNames.isEmpty()) {
        System.out.println("Checking For Duplicate Names");
        while (userNames.contains(user)) {
          output.println(user + " is already taken, please select a new Username");
          user = clientMessage.readLine().trim();
        }
      }

      synchronized (this) {
        userNames.add(user);
      }


      output.println("Welcome to Chit Chat, it's where its at!\n To leave the chatroom"
                      +" send \'EXIT\'");
      Timestamp stamp = new Timestamp(System.currentTimeMillis());
      System.out.println(user+" Joined: "+stamp);

      // Notifies All current connections of new user

for (int i = 0; i < clientLimit; i++) {
        if (clientThreads[i] != null && clientThreads[i] != this) {
          clientThreads[i].objectOutput.writeObject(userNames);
          clientThreads[i].output.println(user + " is now where its at!");
        }
      }

      while (true) {
        String line = clientMessage.readLine();
        if (line.startsWith("EXIT")) {
          break;
        }
        for (int i = 0; i < clientLimit; i++) {
          if (clientThreads[i] != null) {
            clientThreads[i].output.println("-" + user + "> " + line);
          }
        }
      }

      // remove user from list of usernames
      synchronized (this) {
        userNames.remove(user);
        System.out.println(userNames.indexOf(user));
      }
      for (int i = 0; i < clientLimit; i++) {
        if (clientThreads[i] != null && clientThreads[i] != this) {
          clientThreads[i].objectOutput.writeObject(userNames);
          clientThreads[i].output.println(user + " Is no longer Where its at!");
        }
      }
      output.println("You are leaving ChitChat!\nDisconnecting...");



      clientMessage.close();
      output.close();
      client.close();

      /*
       * Clean up. Set the current thread variable to null so that a new client
       * could be accepted by the server.
       */
      for (int i = 0; i < clientLimit; i++) {
        if (clientThreads[i] == this) {
          clientThreads[i] = null;
        }
      }

    } catch (IOException e) {
    }
  }
}
