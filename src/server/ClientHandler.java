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
      while ((object = (ObjectStream) in.readObject()) != null) {
        // Server.log(object, "OBJECT");
        String cmd = object.getCmd();
        Object data = object.getO();

        // Handle first connection (Trainer object)
        if (data instanceof Trainer) {
          Trainer trainer = (Trainer) data;
          Server.clients.put(this, trainer);
          Server.log(trainer.getName() + " added to queue");

          if (Server.clients.size() >= 2) {
            ArrayList<String> names = new ArrayList<String>();
            for (Trainer t : Server.clients.values()) {
              if (t != null) {
                names.add(t.getName());
              }
            }
            Server.log("Battle start: " + names.get(0) + " vs " + names.get(1));
            // Broadcast battle start
            broadcast(new ObjectStream(Commands.START_BATTLE, Server.clients.values().toArray()));
          } else {
            broadcast(new ObjectStream(Commands.CONNECTION_COUNT, Server.clients.size()));
          }
        }

        // Handle comands
        if (cmd != null) {

          if (cmd.equals(Commands.ATTACK.getCmd())) {
            Server.log(this + " > " + cmd + " " + data);
          }

        }

      }

    } catch (Exception e) {
      System.err.println("[ERROR] > " + e.getMessage());
      // e.printStackTrace();
      disconnect();
    }
  }

  public void broadcast(ObjectStream obj) {
    for (ClientHandler ch : Server.clients.keySet()) {
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

  public void disconnect() {
    try {
      Server.clients.remove(this);
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
