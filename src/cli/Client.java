package cli;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import server.Server;
import server.Server.StreamObject;

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
      System.out.println("Error: " + e.getMessage());
      shutdown();
    }
  }

  @Override
  public void run() {
    try {
      while (true) {
        // Send trainer information to the server
        sendToServer(new StreamObject(null, trainer));

        StreamObject object;
        while ((object = (StreamObject) in.readObject()) != null) {
          Main.log("[MSG] " + object);

          // if (object instanceof String[]) {

          // if (inMessage.startsWith(Server.Commands.START_BATTLE.getCmd())) {
          // System.out.println("Battle is starting ...");
          // System.out.println(
          // (String[]) inMessage.split(" ")[1]
          // );
          // // startOnlineBattle();
          // }
          // }

          // if (object instanceof String) {
          // String inMessage = (String) object;
          // if (inMessage.startsWith(Server.Commands.CONNECTION_COUNT.getCmd())) {
          // String count = inMessage.split(" ")[1];
          // System.out.println("[SERVER] > " + count + " player(s) in queue");
          // System.out.println("Waiting for an opponent ...");
          // }
          // }

        }
      }
    } catch (IOException e) {
      System.out.print("[ERROR] > ");
      e.printStackTrace();
      shutdown();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void startOnlineBattle() {
    System.out.println("[SERVER] > Opponent found !");
    System.out.println();
    System.out.print("Entering battle, let's fight !");
    try {
      battle();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void battle() throws IOException {
    System.out.println();
    System.out.println("1. Attack");
    System.out.println("2. Heal");
    System.out.println("0. Quit");
    int choice = Main.getIntInput();
    System.out.println(choice);
    switch (choice) {
      case 1:
        out.writeObject(Server.Commands.ATTACK.getCmd());
        out.flush();
        break;
      case 2:
        out.writeObject(Server.Commands.HEAL.getCmd());
        out.flush();
        break;
      default:
        out.writeObject(Server.Commands.QUIT.getCmd());
        out.flush();
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

  private void sendToServer(StreamObject object) {
    try {
      out.writeObject(object);
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
