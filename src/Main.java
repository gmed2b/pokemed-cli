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

    /**
     * Check if there is a saved game
     * Ask the user if they want to load the saved game
     * or start a new game
     */
    File[] savedGame = getSavedGame();
    if (savedGame != null) {
      System.out.println("Found " + savedGame.length + " saved game(s)");

      // Ask the user if they want to load the saved game
      System.out.print("Do you want to load a saved game? (y/n) ");
      String answer;
      do {
        answer = reader.nextLine();
        // If yes, load the game
        if (answer.toLowerCase().equals("y")) {
          System.out.println();
          System.out.println("Which game do you want to load? ");
          // list all the saved games
          for (int i = 0; i < savedGame.length; i++) {
            System.out.println(i + 1 + ". " + savedGame[i].getName());
          }
          System.out.print("-> ");

          int gameNumber = reader.nextInt();
          reader.nextLine(); // Consume the newline character

          // Deserialize the saved game
          trainer = (Trainer) Serializer.deserialize(savedGame[gameNumber -
              1].getName());

          System.out.println("Game loaded !");
          break;

        }
        // If no, start a new game
        else if (answer.toLowerCase().equals("n")) {
          System.out.println();
          System.out.println("Starting a new game");
          System.out.println();
          trainer = createNewTrainer();
          break;
        }
      } while (answer != "y" || answer != "n");

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
    saveGame(trainer);

    return trainer;
  }

  /**
   * Save the game to a file in the saves folder
   */
  private static void saveGame(Trainer trainer) {
    System.out.println();

    boolean canSave = false;

    if (!Serializer.fileExists(trainer.getName())) {
      canSave = true;
    } else {
      System.out.println("A saved game with the same name already exists.");
      System.out.print("Do you want to overwrite the saved game? (y/n) ");
      String answer;
      do {
        answer = reader.nextLine();
        if (answer.toLowerCase().equals("y")) {
          canSave = true;
          break;
        } else {
          System.out.println("Saving game aborted");
          return;
        }
      } while (answer != "y" || answer != "n");
    }

    if (!canSave) {
      return;
    }

    System.out.println("Saving game...");

    // ArrayList<Object> dataToSave = new ArrayList<>();
    // dataToSave.add(trainer);
    Serializer.serialize(trainer.getName(), trainer);

    System.out.println("Game saved !");
    System.out.println();
  }

  /**
   * Check if there is a saved game in the saves folder.
   * Return the list of files in the saves folder if there is.
   * Return null if there is no saved game.
   */
  private static File[] getSavedGame() {
    File folder = new File("./saves");
    File[] listOfFiles = folder.listFiles();

    if (listOfFiles.length == 0) {
      return null;
    } else {
      return listOfFiles;
    }
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
