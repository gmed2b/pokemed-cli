package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cli.Trainer;

public class Server implements Runnable {

  public static int PORT = 6942;
  private ArrayList<ConnectionHandler> connections;
  private ServerSocket server;
  private boolean done = false;
  private ExecutorService pool;

  private ArrayList<Trainer> queue = new ArrayList<Trainer>();

  public Server() {
    connections = new ArrayList<ConnectionHandler>();
  }

  @Override
  public void run() {
    try {
      server = new ServerSocket(PORT);
      System.out.println("Server is running on port " + PORT);
      pool = Executors.newCachedThreadPool();
      while (!done) {
        Socket client = server.accept();
        ConnectionHandler handler = new ConnectionHandler(client);
        connections.add(handler);
        // Run the handler run method in a new thread
        pool.execute(handler);
      }
    } catch (Exception e) {
      shutdown();
    }
  }

  public void broadcast(String message) {
    for (ConnectionHandler ch : connections) {
      if (ch != null) {
        ch.sendMessage(message);
      }
    }
  }

  public void broadcastCommands(Commands cmd, Object o) {
    for (ConnectionHandler ch : connections) {
      if (ch != null) {
        ch.sendMessage(cmd.getCmd() + " " + o);
      }
    }
  }

  public void shutdown() {
    done = true;
    try {
      if (!server.isClosed()) {
        server.close();
      }
      broadcast("Server is shutting down ...");
      for (ConnectionHandler ch : connections) {
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

  public enum Commands {
    CONNECTION_COUNT("/getConnectionsCount"),
    START_BATTLE("/startBattle"),
    OBJECT("/object"),
    QUIT("/quit"),
    ATTACK("/attack"),
    HEAL("/heal");

    private final String cmd;

    Commands(String cmd) {
      this.cmd = cmd;
    }

    public String getCmd() {
      return cmd;
    }
  }

  class ConnectionHandler implements Runnable {

    private Socket client;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ConnectionHandler(Socket client) {
      this.client = client;
      try {
        out = new ObjectOutputStream(client.getOutputStream());
        in = new ObjectInputStream(client.getInputStream());
      } catch (IOException e) {
        disconnect();
      }
    }

    @Override
    public void run() {
      try {
        // Broadcast number of connections
        broadcastCommands(Commands.CONNECTION_COUNT, connections.size());

        Object object;
        while ((object = in.readObject()) != null) {
          Server.log(object, "OBJECT");

          if (object instanceof Trainer) {
            Trainer trainer = (Trainer) object;
            if (queue.size() < 2) {
              queue.add(trainer);
              Server.log(trainer.getName() + " added to queue");
            }
            if (queue.size() == 2) {
              Server.log("2 trainers, battle can start");
              String[] names = queue.stream().map(t -> t.getName()).toArray(String[]::new);
              broadcastCommands(Commands.START_BATTLE, names);
              queue.clear();
            }
          }

          if (object instanceof String) {
            String message = (String) object;
            if (message.startsWith(Commands.QUIT.getCmd())) {
              disconnect();
              broadcastCommands(Commands.CONNECTION_COUNT, connections.size());
              break;
            } else if (message.startsWith(Commands.ATTACK.getCmd())) {
              broadcast(this + " attack");
            } else if (message.startsWith(Commands.HEAL.getCmd())) {
              broadcast(this + " heal");
            }
          }
        }

      } catch (Exception e) {
        disconnect();
      }
    }

    public void sendMessage(String message) {
      try {
        out.writeObject(message);
        out.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public void disconnect() {
      try {
        connections.remove(this);
        if (!client.isClosed()) {
          client.close();
        }
      } catch (Exception e) {
        // TODO: handle exception
      }
    }

  } // End of ConnectionHandler class

  public static void main(String[] args) {
    Server server = new Server();
    server.run();
  }

}
