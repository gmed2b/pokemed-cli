package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cli.Trainer;

public class Server implements Runnable {

  public static final int PORT = 6942;
  private ServerSocket server;
  private ExecutorService pool = Executors.newFixedThreadPool(2);

  public static Map<ClientHandler, Trainer> clients = new HashMap<ClientHandler, Trainer>();

  @Override
  public void run() {
    try {
      server = new ServerSocket(PORT);
      System.out.println("Server is running on port " + PORT);

      while (true) {
        Socket client = server.accept();
        ClientHandler clientThread = new ClientHandler(client);
        clients.put(clientThread, null);
        pool.execute(clientThread);
      }

    } catch (Exception e) {
      shutdown();
    }
  }

  public void shutdown() {
    try {
      if (!server.isClosed()) {
        server.close();
      }
      for (ClientHandler ch : clients.keySet()) {
        ch.broadcast(new ObjectStream(null, "Server is shutting down"));
        if (ch != null) {
          ch.disconnect();
        }
      }
    } catch (Exception e) {
      // TODO: handle exception
    }
  }

  public static void log(Object o) {
    System.out.println("[LOG] > " + o);
  }

  public static void log(Object o, String prefix) {
    System.out.println("[" + prefix + "] > " + o);
  }

  public static enum Commands {
    CONNECTION_COUNT("/getConnectionsCount"),
    START_BATTLE("/startBattle"),
    SEND_1ST_POKEMON("/send1stPokemon"),
    ATTACK("/attack"),
    HEAL("/heal"),
    QUIT("/quit");

    private final String cmd;

    Commands(String cmd) {
      this.cmd = cmd;
    }

    public String getCmd() {
      return cmd;
    }
  }

  public static void main(String[] args) {
    Server server = new Server();
    server.run();
  }

}
