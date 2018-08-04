package chitchatapp;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

  private static Socket client =  null;
  private static DataInputStream serverMessage = null;
  private static DataInputStream clientMessage = null;
  private static PrintStream output = null;
  private static boolean status = true;
  private static String user = "Default";


  public static void main(String[] args) {
    int port = 8000;
    String host = "localhost";

    // connect to server socket and open input stream
    try {
      client = new Socket(host, port);
      serverMessage = new DataInputStream(client.getInputStream());
      clientMessage = new DataInputStream(new BufferedInputStream(System.in));
      output = new PrintStream(client.getOutputStream());
    } catch (UnknownHostException e) {
      System.err.println(e);
    } catch(IOException e) {
      System.err.println(e);
    }

    if (client != null && serverMessage != null && output != null) {
      try {
        new Thread(new Client()).start();

        while (status) {
          output.println(clientMessage.readLine().trim());
        }
        output.close();
        clientMessage.close();
        serverMessage.close();
        client.close();
      } catch(IOException e) {
        System.err.println(e);
      }
    }
  }

  public void run() {
    messageListener();
  }

  // Listens for messages from the connection
  public void messageListener() {
    String message;
    try {
      while ((message = serverMessage.readLine()) != null) {
        System.out.println(message);
      }
        status = false;
    } catch (IOException e) {
      System.err.println(e);
    }
  }

}
