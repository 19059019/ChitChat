package chitchatapp;

import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class Server {
  private static ServerSocket server = null;
  private static Socket client = null;
  private static final int clientLimit = 5;
  private static final ClientPane[] clientThreads = new ClientPane[clientLimit];
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
            clientThreads[i] = new ClientPane(client, clientThreads);
            clientThreads[i].start();
            break;
          }
        }

        // Message if too many clients have connected
        if (i == clientLimit) {
          output = new PrintStream(client.getOutputStream());
          output.println("ChitChat chatroom full, unlucky!");
          output.close();
          client.close();
        }
      } catch (IOException e) {
        System.err.println(e);
      }

    }
  }

}
