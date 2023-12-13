import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class GameClient {
  private Trainer trainer;

  public GameClient(Trainer trainer) {
    this.trainer = trainer;
    connectToServer();
  }

  public static void connectToServer() {
    try {
      Socket socket = new Socket(InetAddress.getLocalHost(), 6942);

      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      System.out.println(in.readLine());

      sendMessageToServer(socket, "Hello server!");

      socket.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void sendMessageToServer(Socket socket, String message) {
    try {
      PrintWriter out = new PrintWriter(socket.getOutputStream());
      out.println(message);
      out.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
