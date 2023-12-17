package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
      Server.log(Server.clients);

      ObjectStream object;
      while ((object = (ObjectStream) in.readObject()) != null) {
        // Server.log(object, "OBJECT");
        String cmd = object.getCmd();
        Object data = object.getO();

        if (data instanceof Trainer) {
          Trainer trainer = (Trainer) data;
          if (Server.queue.size() < 2) {
            Server.queue.add(trainer);
            Server.log(trainer.getName() + " added to queue");
            broadcast(new ObjectStream(Commands.CONNECTION_COUNT, Server.queue.size()));
          }
          if (Server.queue.size() == 2) {
            Server.log("2 trainers, battle can start");
            // Get trainers names
            String[] names = new String[2];
            for (Trainer t : Server.queue) {
              names[Server.queue.indexOf(t)] = t.getName();
            }
            // Broadcast battle start
            broadcast(new ObjectStream(Commands.START_BATTLE, names));
            Server.queue.clear();
          }
        }

        // if (object instanceof String) {
        // String message = (String) object;
        // if (message.startsWith(Commands.QUIT.getCmd())) {
        // disconnect();
        // broadcast(Commands.CONNECTION_COUNT, Server.clients.size());
        // break;
        // } else if (message.startsWith(Commands.ATTACK.getCmd())) {
        // broadcast(this + " attack");
        // } else if (message.startsWith(Commands.HEAL.getCmd())) {
        // broadcast(this + " heal");
        // }
        // }

      }

    } catch (Exception e) {
      System.err.println("[ERROR] > " + e.getMessage());
      // e.printStackTrace();
      disconnect();
    }
  }

  public void broadcast(ObjectStream obj) {
    for (ClientHandler ch : Server.clients) {
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
      Server.queue.clear();
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
