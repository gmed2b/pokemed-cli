import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
  static ArrayList<Pokemon> pokedex = new ArrayList<>();
  static Scanner reader = new Scanner(System.in);

  // public static Trainer trainer;

  public static void main(String[] args) {
    System.out.println();

    // Load all existing pokemons from a csv file into the pokedex.
    try {
      loadPokemon();
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    } catch (IOException e) {
      System.out.println("IO Exception");
    }
    System.out.println("[LOG] Loaded " + pokedex.size() + " pokemons");

    // for (Pokemon pokemon : pokedex) {
    // System.out.println(pokemon);
    // }
    // System.out.println();
    System.out.println();

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

    Game game = new Game(trainer, pokedex);
    game.start();

    System.out.println();
  }

  /**
   * Create a new game
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
   * Load all existing pokemons from a csv file into the pokedex.
   * 
   * @throws IOException
   * @throws FileNotFoundException
   */
  private static void loadPokemon() throws FileNotFoundException, IOException {
    try (BufferedReader br = new BufferedReader(new FileReader("./assets/pokemons.csv"))) {
      String line;
      boolean isFirstLine = true; // Flag to skip the header line
      while ((line = br.readLine()) != null) {
        if (isFirstLine) {
          isFirstLine = false;
          continue; // Skip the header line
        }

        // Create a new Pokemon for each line and add it to the Pokedex
        String[] data = line.split(",");

        int id = Integer.parseInt(data[0]);
        String name = data[1];
        int hp = Integer.parseInt(data[2]);
        int attack = Integer.parseInt(data[3]);
        String type = data[4];

        Pokemon pokemon = new Pokemon(id, name, hp, attack, PokemonType.valueOf(type.toUpperCase()));
        pokedex.add(pokemon);
      }
    }
  }
}
