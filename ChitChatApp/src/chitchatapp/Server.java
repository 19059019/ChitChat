package chitchatapp;

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
      System.out.println("Wololo");
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