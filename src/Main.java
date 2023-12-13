import java.io.File;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Main {
  static Pokedex pokedex = new Pokedex();
  static Scanner reader = new Scanner(System.in);

  public static void main(String[] args) {
    System.out.println();

    PlayMusic("./assets/JubilifeCityNight8bit.wav");

    Trainer trainer = null;

    // Check if there is a saved game
    File[] savedGame = Game.getSavedGame();
    if (savedGame != null) {
      System.out.println("Found " + savedGame.length + " saved game(s)");
      System.out.println();

      // list all the saved games
      System.out.println("0. Start a new game");
      for (int i = 0; i < savedGame.length; i++) {
        System.out.println(i + 1 + ". " + savedGame[i].getName());
      }
      System.out.print("-> ");

      int gameNumber = reader.nextInt();
      reader.nextLine(); // Consume the newline character

      if (gameNumber == 0) {
        System.out.println();
        System.out.println("Starting a new game");
        System.out.println();
        trainer = createNewTrainer();
      } else {
        // Deserialize the saved game
        trainer = (Trainer) Serializer.deserialize(savedGame[gameNumber -
            1].getName());
        System.out.println("Game loaded !");
      }

    }
    /**
     * No saved game found, start a new game
     */
    else {
      System.out.println("No saved game found, starting a new game");
      System.out.println();
      trainer = createNewTrainer();
    }

    Game game = new Game(trainer);
    game.start();

    System.out.println();
  }

  /**
   * Create a new trainer and save it
   * 
   * @return The new trainer created
   */
  private static Trainer createNewTrainer() {
    // Ask the user for their name
    System.out.print("What is your name? ");
    String name = reader.nextLine();

    // Create a new trainer with the name
    Trainer trainer = new Trainer(name);
    System.out.println("New trainer created. Welcome " + trainer.getName() + "!");

    // Save the game
    Game.saveGame(trainer);

    return trainer;
  }

  /**
   * Play a music file
   * 
   * @param location
   */
  public static void PlayMusic(String location) {
    try {
      File musicPath = new File(location);

      if (musicPath.exists()) {
        AudioInputStream ais = AudioSystem.getAudioInputStream(musicPath);
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
      } else {
        System.out.println("Can't find music file");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void Logger(String message) {
    System.out.println("[LOG] " + message);
  }
}