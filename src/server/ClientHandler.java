package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import cli.Trainer;
import server.Server.Commands;

public class ClientHandler implements Runnable {

  private Socket client;
  private ObjectOutputStream out;
  private ObjectInputStream in;

  private Trainer trainer;

  public ClientHandler(Socket client) throws IOException {
    this.client = client;
    out = new ObjectOutputStream(client.getOutputStream());
    in = new ObjectInputStream(client.getInputStream());
  }

  @Override
  public void run() {
    try {
      // Server.log(Server.clients);

      ObjectStream object;
      while ((object = (ObjectStream) this.in.readObject()) != null) {
        System.out.println();
        Server.log(object, "NEW_MESSAGES");
        String cmd = object.getCmd();
        Object data = object.getO();

        // Handle first connection (Trainer object)
        if (data instanceof Trainer) {
          Trainer trainer = (Trainer) data;
          this.trainer = trainer;
          Server.log(trainer.getName() + " added to queue");

          if (Server.connections.size() >= 2) {
            ArrayList<String> names = new ArrayList<String>();
            for (ClientHandler ch : Server.connections) {
              if (ch != null) {
                names.add(ch.trainer.getName());
              }
            }
            Server.log("Battle start: " + names.get(0) + " vs " + names.get(1));
            broadcast(new ObjectStream(Commands.START_BATTLE, names));
          } else {
            broadcast(new ObjectStream(Commands.CONNECTION_COUNT, Server.connections.size()));
          }
        }

        // Handle comands
        if (cmd != null) {

          if (cmd.equals(Commands.READY.getCmd())) {

            if (!Server.queue.contains(this)) {
              Server.log(this + " is ready");
              Server.queue.add(this);
            }

            Server.log(Server.queue, "QUEUE");

            // if (Server.firstPlayerTurn && Server.queue.size() > 0) {
            // Server.firstPlayerTurn = false;
            // ClientHandler currentClient = Server.queue.poll();
            // while (currentClient != null) {
            // Server.log("current is " + currentClient);

            // currentClient.send(new ObjectStream(Commands.ASK_MOVE, null));
            // ObjectStream move = (ObjectStream) currentClient.in.readObject();
            // Server.log(move, "MOVE");

            // currentClient = Server.queue.poll();
            // }
            // Server.firstPlayerTurn = true;
            // Server.log("Round finished");
            // }
          }

        }

      }

    } catch (Exception e) {
      System.err.println("[ERROR] " + e.getClass().getName() + " > " + e.getMessage());
      e.printStackTrace();
      disconnect();
    }
  }

  public void broadcast(ObjectStream obj) {
    for (ClientHandler ch : Server.connections) {
      if (ch != null) {
        try {
          ch.out.writeObject(obj);
          ch.out.flush();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void send(ObjectStream obj) {
    try {
      this.out.writeObject(obj);
      this.out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void disconnect() {
    try {
      Server.connections.remove(this);
      // Close streams
      if (!out.equals(null)) {
        out.close();
      }
      if (!in.equals(null)) {
        in.close();
      }
      if (!client.isClosed()) {
        client.close();
      }
    } catch (IOException e) {
      return;
    } catch (Exception e) {
      System.err.println("Error in disconnect");
      e.printStackTrace();
    }
  }

}
