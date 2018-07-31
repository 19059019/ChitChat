import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class Server {

  public static void main(String[] args) {
    ServerSocket ss = null;
    DataInputStream is = null;
    DataInputStream il = null;
    Socket c = null;
    PrintStream o = null;
    Boolean server;
    // open ServerSocket
    try {
      ss = new ServerSocket(8000);
    } catch(IOException e) {
      System.out.println(e);
    }

    // act on Socket server
    try {
      c = ss.accept();
      is = new DataInputStream(c.getInputStream());
      il = new DataInputStream(new BufferedInputStream(System.in));
      o = new PrintStream(c.getOutputStream());
      o.println("Wololo");
      String l;
      String response;
      server =  true;
      while(server) {
        l = is.readLine();
        System.out.println(l);
        o.println(il.readLine());
      }
      // close connections and streamsS
      ss.close();
      c.close();
      is.close();
      o.close();
    } catch(IOException e) {
      System.out.println(e);
    }

  }

}
