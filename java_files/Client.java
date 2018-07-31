import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

  public static void main(String[] args) {
    Socket s =  null;
    DataInputStream is = null;
    DataInputStream il = null;
    PrintStream o = null;

    // connect to server socket and open input stream
    try {
      s = new Socket("localhost", 8000);
      is = new DataInputStream(s.getInputStream());
      il = new DataInputStream(new BufferedInputStream(System.in));
      o = new PrintStream(s.getOutputStream());
    } catch(IOException e) {
      System.out.println(e);
    }

    if (s != null && is != null && o != null) {
      try {

        String response;
        o.println("Client Connected");
        while ((response = is.readLine()) != null) {
          System.out.println(response);
          o.println(il.readLine());
        }

        o.close();
        is.close();
        s.close();
      } catch (UnknownHostException e) {
        System.err.println("UnknownHostException: " + e);
      } catch(IOException e) {
        System.out.println(e);
      }
    }

  }

}
