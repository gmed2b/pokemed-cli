package cli;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import lib.Pair;
import server.ObjectStream;
import server.Server;
import server.Server.Commands;

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
      disconnect();
    }
  }

  @Override
  public void run() {
    try {
      while (true) {
        // Send trainer information to the server
        sendToServer(new ObjectStream(Commands.INIT_TRAINER, trainer));

        ObjectStream object;
        while ((object = (ObjectStream) in.readObject()) != null) {
          // Main.log(object, "MSG");
          Commands cmd = object.getCmd();
          Object data = object.getO();

          if (cmd.equals(Commands.INIT_TRAINER)) {
            if ((boolean) data) {
              System.out.println("Connected to server!");
              Pokemon choosePokemon = trainer.choosePokemon();

              sendToServer(new ObjectStream(Commands.IN_QUEUE, choosePokemon));
            }
          }

          if (cmd.equals(Commands.IN_QUEUE)) {
            int queueSize = (int) data;
            System.out.println("Waiting for opponent ... (" + queueSize + "/2)");
          }

          if (cmd.equals(Commands.BATTLE_STARTING)) {
            Main.log("Opponent found!", "SERVER");
            displayMenuBattle();
          }

          if (cmd.equals(Commands.BATTLE_END)) {
            if ((boolean) data) {
              System.out.println("You won!");
            } else {
              System.out.println("You lost!");
            }
          }

          if (cmd.equals(Commands.SET_ACTION)) {
            if ((boolean) data) {
              System.out.println("Waiting for opponent's action ...");
              sendToServer(new ObjectStream(Commands.EOT, null));
            }
          }

          if (cmd.equals(Commands.ATTACK)) {
            @SuppressWarnings("unchecked")
            Pair<Integer, Integer> newPokemonPair = (Pair<Integer, Integer>) data;
            Pokemon localPokemon = trainer.getPokemons().get(newPokemonPair.getFirst().intValue());
            int damage_taken = newPokemonPair.getSecond().intValue();
            // Update your pokemon's hp
            localPokemon.setHp(localPokemon.getHp() - damage_taken);
            System.out.println("You took " + damage_taken + " damage!");

            // displayMenuBattle();
          }

          System.out.println();
        }
      }
    } catch (EOFException e) {
      Main.log("Server closed", "SERVER");
    } catch (IOException e) {
      Main.log("Error: " + e.getMessage(), "SERVER");
      // e.printStackTrace();
      disconnect();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void displayMenuBattle() {
    System.out.println("Choose your action :");
    ObjectStream battleAction = trainer.getBattleAction();
    sendToServer(new ObjectStream(Commands.SET_ACTION, battleAction));
  }

  public void disconnect() {
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
