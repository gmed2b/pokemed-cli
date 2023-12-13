package server;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class AcceptClient extends Thread {
  private ServerSocket server;
  private int nbClients;

  public AcceptClient(ServerSocket server) {
    this.server = server;
    this.nbClients = 0;
  }

  @Override
  public void run() {
    System.out.println("Server started on port " + server.getLocalPort());

    try {
      while (true) {
        nbClients++;

        Socket socket = server.accept();
        System.out.println("Client connected (" + nbClients + ")");

        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.println("Welcome to the server (" + nbClients + ")");
        out.flush();

        socket.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
