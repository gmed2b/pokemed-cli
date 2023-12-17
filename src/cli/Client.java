package cli;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import server.ObjectStream;
import server.Server;

public class Client implements Runnable {

  private Trainer trainer;
  private Socket client;
  private ObjectOutputStream out;
  private ObjectInputStream in;

  public Client(Trainer trainer) {
    try {
      this.trainer = trainer;
      client = new Socket("127.0.0.1", Server.PORT);
      out = new ObjectOutputStream(client.getOutputStream());
      in = new ObjectInputStream(client.getInputStream());
    } catch (Exception e) {
      Main.log("Error: " + e.getMessage());
      shutdown();
    }
  }

  @Override
  public void run() {
    try {
      while (true) {
        // Send trainer information to the server
        sendToServer(new ObjectStream(null, trainer));

        ObjectStream object;
        while ((object = (ObjectStream) in.readObject()) != null) {
          // Main.log(object, "MSG");
          String cmd = object.getCmd();
          Object data = object.getO();

          if (cmd != null) {

            if (cmd.equals(Server.Commands.CONNECTION_COUNT.getCmd())) {
              int count = (int) data;
              Main.log(count + " player(s) in queue", "SERVER");
              System.out.println("Waiting for an opponent ...");
            }

            if (cmd.equals(Server.Commands.START_BATTLE.getCmd())) {
              String[] names = (String[]) data;
              Main.log("Opponent found ! " + names[0] + " vs " + names[1], "SERVER");
              System.out.println("Entering battle, let's fight !");
              battleMenu();
            }

          }
        }
      }
    } catch (EOFException e) {
      Main.log("Server closed", "SERVER");
    } catch (IOException e) {
      Main.log("Error: " + e.getMessage(), "SERVER");
      // e.printStackTrace();
      shutdown();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void battleMenu() throws IOException {
    System.out.println();
    System.out.println("1. Attack");
    // System.out.println("2. Heal");
    System.out.println("0. Quit");
    int choice = Main.getIntInput();
    switch (choice) {
      case 1:
        // Get first pokemon
        Pokemon firstPokemon = trainer.getPokemons().get(0);
        sendToServer(new ObjectStream(Server.Commands.ATTACK, firstPokemon));
        break;
      // case 2:
      // out.writeObject(Server.Commands.HEAL.getCmd());
      // out.flush();
      // break;
      default:
        sendToServer(new ObjectStream(Server.Commands.QUIT, null));
        break;
    }
  }

  public void shutdown() {
    try {
      out.writeChars(Server.Commands.QUIT.getCmd());
      if (!client.isClosed()) {
        client.close();
      }
      if (in != null) {
        in.close();
      }
      if (out != null) {
        out.close();
      }
    } catch (Exception e) {
    }
    throw new RuntimeException("Disconnected from server!");
  }

  private void sendToServer(ObjectStream object) {
    try {
      out.writeObject(object);
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
