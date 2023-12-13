package server;

import java.net.ServerSocket;

public class Server {
  public static void main(String[] args) {
    try {
      ServerSocket server = new ServerSocket(6942);
      AcceptClient client = new AcceptClient(server);
      client.start();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
