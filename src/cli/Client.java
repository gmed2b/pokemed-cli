package cli;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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
              // Cast data to ArrayList<String>
              ArrayList<String> trainersNames = (ArrayList<String>) data;

              Main.log("Opponent found ! " + trainersNames.get(0) + " vs " + trainersNames.get(1), "SERVER");
              System.out.println("Entering battle, let's fight !");

              battleHandler();
            }

            if (cmd.equals(Server.Commands.ASK_MOVE.getCmd())) {
              // Display menu
              displayMenu();
            }

          }

          System.out.println();
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

  private void battleHandler() throws IOException {
    System.out.println();

    // // Print enemy first pokemon
    // for (Trainer t : trainers) {
    // if (!t.getName().equals(trainer.getName())) {
    // System.out.println(t.getName() + " has sent " +
    // t.getPokemons().get(0).getName() + " !");
    // }
    // }

    // // Print trainer first pokemon
    // System.out.println("You sent " + trainer.getPokemons().get(0).getName() + "
    // !");

    // Client is ready to battle, inform the server
    sendToServer(new ObjectStream(Server.Commands.READY, null));

  }

  private void displayMenu() {
    System.out.println("1. Attack");
    // System.out.println("2. Heal");
    System.out.println("0. Quit");
    int choice;
    boolean validChoice = false;
    while (!validChoice) {
      choice = Main.getIntInput();
      switch (choice) {
        case 1:
          // Get first pokemon
          // Pokemon firstPokemon = trainer.getPokemons().get(0);
          sendToServer(new ObjectStream(Server.Commands.ATTACK, null));
          validChoice = true;
          break;
      }
    }

    System.out.println("Waiting for opponent's action ...");
  }

  public void shutdown() {
    try {
      // SEND TO OTHERS CLIENT THAT WE QUIT
      // sendToServer(new ObjectStream(Server.Commands.QUIT, null));

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
