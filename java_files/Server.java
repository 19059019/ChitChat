import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class Server {

  public static void main(String[] args) {
    ServerSocket server = null;
    DataInputStream clientMessage = null;
    DataInputStream serverMessage = null;
    Socket client = null;
    PrintStream output = null;
    Boolean status;
    // open ServerSocket
    try {
      server = new ServerSocket(8000);
    } catch(IOException e) {
      System.err.println(e);
    }

    // act on Socket server
    try {
      client = server.accept();
      clientMessage = new DataInputStream(client.getInputStream());
      serverMessage = new DataInputStream(new BufferedInputStream(System.in));
      output = new PrintStream(client.getOutputStream());
      output.println("Welcome!");
      String response;
      status =  true;
      while(status && (response = clientMessage.readLine()) != null) {
        System.out.println(response);
        output.println(serverMessage.readLine());
      }
      // close connections and streams
      server.close();
      client.close();
      clientMessage.close();
      output.close();
    } catch(IOException e) {
      System.err.println(e);
    }

  }

}
