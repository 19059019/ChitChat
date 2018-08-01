import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class Server {
  private static ServerSocket server = null;
  private static Socket client = null;
  private static final int clientLimit = 5;
  private static final clientInstance[] clientThreads = new clientInstance[clientLimit];
  private static PrintStream output = null;
  private static Boolean status = true;

  public static void main(String[] args) {
    int port = 8000;

    // open ServerSocket
    try {
      server = new ServerSocket(port);
    } catch(IOException e) {
      System.err.println(e);
    }

    // create new socket for each new client that attempts to connect
    while (status) {
      int i;
      try{
        client = server.accept();
        for (i = 0; i < clientLimit; i++) {
          if (clientThreads[i] == null) {
            clientThreads[i] = new clientInstance(client, clientThreads);
            clientThreads[i].start();
            break;
          }
        }

        // Message if too many clients have connected
        if (i == clientLimit) {
          output = new PrintStream(client.getOutputStream());
          output.println("Chit Chat Chatroom full, Unlucky!");
          output.close();
          client.close();
        }
      } catch (IOException e) {
        System.err.println(e);
      }

    }
  }

}

class clientInstance extends Thread {

  private DataInputStream is = null;
  private PrintStream output = null;
  private Socket client = null;
  private final clientInstance[] clientThreads;
  private int clientLimit;

  public clientInstance(Socket client, clientInstance[] clientThreads) {
    this.client = client;
    this.clientThreads = clientThreads;
    clientLimit = clientThreads.length;
  }

  public void run() {
    clientInstance[] clientThreads = this.clientThreads;
    int clientLimit = this.clientLimit;

    try {
      /*
       * Create input and output streams for this client.
       */
      is = new DataInputStream(client.getInputStream());
      output = new PrintStream(client.getOutputStream());
      output.println("Enter your name.");
      String name = is.readLine().trim();
      output.println("Hello " + name
          + " to our chat room.\nTo leave enter /quit in a new line");
      for (int i = 0; i < clientLimit; i++) {
        if (clientThreads[i] != null && clientThreads[i] != this) {
          clientThreads[i].output.println("*** A new user " + name
              + " entered the chat room !!! ***");
        }
      }
      while (true) {
        String line = is.readLine();
        if (line.startsWith("/quit")) {
          break;
        }
        for (int i = 0; i < clientLimit; i++) {
          if (clientThreads[i] != null) {
            clientThreads[i].output.println("<" + name + "&gr; " + line);
          }
        }
      }
      for (int i = 0; i < clientLimit; i++) {
        if (clientThreads[i] != null && clientThreads[i] != this) {
          clientThreads[i].output.println("*** The user " + name
              + " is leaving the chat room !!! ***");
        }
      }
      output.println("*** Bye " + name + " ***");

      /*
       * Clean up. Set the current thread variable to null so that a new client
       * could be accepted by the server.
       */
      for (int i = 0; i < clientLimit; i++) {
        if (clientThreads[i] == this) {
          clientThreads[i] = null;
        }
      }

      /*
       * Close the output stream, close the input stream, close the socket.
       */
      is.close();
      output.close();
      client.close();
    } catch (IOException e) {
    }
  }
}
