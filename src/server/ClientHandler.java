package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import cli.Pokemon;
import cli.Trainer;
import lib.Pair;
import server.Server.Commands;

public class ClientHandler implements Runnable {

  private Socket client;
  private ObjectOutputStream out;
  private ObjectInputStream in;

  private Trainer trainer;
  private Pokemon currentPokemon = null;
  private ObjectStream action = null;

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
        // Server.log(object, "NEW_MESSAGES");
        Commands cmd = object.getCmd();
        Object data = object.getO();

        if (cmd.equals(Commands.INIT_TRAINER)) {
          this.trainer = (Trainer) data;
          send(new ObjectStream(Commands.INIT_TRAINER, true));
        }

        if (cmd.equals(Commands.IN_QUEUE)) {
          Server.log(this.trainer.getName() + " added to queue");
          this.currentPokemon = (Pokemon) data;

          // Check if both clients are ready
          ClientHandler opponent = getOpponent();
          if (Server.connections.size() == 2 && opponent != null && opponent.currentPokemon != null) {
            Server.log("A battle is starting!");
            // TODO: Send to each client against who they are battling!
            broadcast(new ObjectStream(Commands.BATTLE_STARTING, null));
          } else {
            send(new ObjectStream(Commands.IN_QUEUE, Server.connections.size()));
          }
        }

        if (cmd.equals(Commands.SET_ACTION)) {
          this.action = object;
          send(new ObjectStream(Commands.SET_ACTION, true));
        }

        if (cmd.equals(Commands.EOT)) {
          boolean validateAction = true;
          while (validateAction) {
            // Check if both clients have set their action
            for (ClientHandler ch : Server.connections) {
              if (ch != null) {
                if (ch.action == null) {
                  validateAction = false;
                  break;
                }
              }
            }

            // If both clients have set their action, execute the battle
            if (validateAction) {
              for (ClientHandler ch : Server.connections) {
                if (ch != null) {
                  // Can attack if pokemon is alive
                  if (ch.currentPokemon.getHp() > 0) {
                    processBattle(ch, ch.getOpponent());
                  }
                  ch.action = null;
                }
              }
            }
            break;
          }
        }

      }

    } catch (Exception e) {
      System.err.println("[ERROR] " + e.getClass().getName() + " > " + e.getMessage());
      e.printStackTrace();
      disconnect();
    }
  }

  private void processBattle(ClientHandler me, ClientHandler opponent) {
    ObjectStream data = (ObjectStream) me.action.getO();
    Commands cmd = data.getCmd();

    if (cmd.equals(Commands.ATTACK)) {
      Pokemon myPokemon = me.currentPokemon;
      Pokemon enemyPokemon = opponent.currentPokemon;
      int damage_dealt = me.trainer.attack(myPokemon, enemyPokemon);
      Server.log(me.trainer.getName() + "'s " + myPokemon.getName() + " dealt " + damage_dealt + " damage to "
          + opponent.trainer.getName() + "'s "
          + enemyPokemon.getName());

      // Check if enemy pokemon is dead
      if (enemyPokemon.getHp() <= 0) {
        Server.log(opponent.trainer.getName() + "'s " + enemyPokemon.getName() + " is dead");
        Server.log(opponent.trainer.getName() + " lost the battle!");
        Server.log(me.trainer.getName() + " won the battle!");
        me.send(new ObjectStream(Commands.BATTLE_END, true));
        opponent.send(new ObjectStream(Commands.BATTLE_END, false));
      } else {
        // Get index of enemy pokemon
        int enemyPokemonIndex = opponent.trainer.getPokemons().indexOf(enemyPokemon);
        // Send to enemy client that their pokemon took damage
        Pair<Integer, Integer> newPokemonEnemy = new Pair<>(enemyPokemonIndex, damage_dealt);
        opponent.send(new ObjectStream(Commands.ATTACK, newPokemonEnemy));
      }
    }
  }

  private ClientHandler getOpponent() {
    for (ClientHandler ch : Server.connections) {
      if (ch != null) {
        if (!ch.equals(this)) {
          return ch;
        }
      }
    }
    return null;
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
