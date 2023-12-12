import java.io.File;
import java.util.ArrayList;

public class Game {
  private ArrayList<Pokemon> pokedex;
  private Trainer trainer;

  public Game(Trainer trainer, ArrayList<Pokemon> pokedex) {
    this.trainer = trainer;
    this.pokedex = pokedex;
  }

  /**
   * Start the game main loop
   */
  public void start() {
    System.out.println();
    System.out.println("--------- Welcome to Pokemed! ---------");
    System.out.println("This is a game where you can catch and evolve Pokemon");
    System.out.println("You can also battle with other trainers");
    System.out.println();

    // Main loop
    while (true) {
      // Display the main menu
      displayMainMenu();

      // Get the user's choice
      System.out.print("-> ");
      int choice = Integer.parseInt(Main.reader.nextLine());

      switch (choice) {
        case 1:
          break;
        case 2:
          break;
        case 3:
          break;
        case 4:
          break;
        case 5:
          listPokedex();
          break;
        case 6:
          saveGame(trainer);
          break;
        case 7:
          System.out.println("Exiting ...");
          System.exit(0);
          break;
        default:
          System.out.println("Invalid choice");
          System.out.println("Exiting ...");
          System.exit(0);
          break;
      }

      System.out.println();
    }

  }

  /**
   * Display the main menu
   */
  private void displayMainMenu() {
    System.out.println();
    System.out.println(String.format("--- %s's Profile ---", this.trainer.getName()));
    System.out.println();
    System.out.println("1. List owned pokemons");
    System.out.println("2. Level-up a pokemon");
    System.out.println("3. Start a battle");
    System.out.println("4. Free a pokemon");
    System.out.println();
    System.out.println("5. List all pokemons in the pokedex");
    System.out.println("6. Save the game");
    System.out.println("7. Exit");
  }

  /**
   * List all pokemons in the pokedex
   */
  private void listPokedex() {
    System.out.println();
    System.out.println("--------- Pokedex ---------");
    System.out.println();

    for (Pokemon pokemon : this.pokedex) {
      System.out.println(pokemon);
    }
  }

  /**
   * Save the game to a file in the saves folder
   */
  public static void saveGame(Trainer trainer) {
    System.out.println();

    boolean canSave = false;

    if (!Serializer.fileExists(trainer.getName())) {
      canSave = true;
    } else {
      System.out.println("A saved game with the same name already exists.");
      System.out.print("Do you want to overwrite the saved game? (y/n) ");
      String answer;
      do {
        answer = Main.reader.nextLine();
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
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

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
  public static File[] getSavedGame() {
    File folder = new File("./saves");
    File[] listOfFiles = folder.listFiles();

    if (listOfFiles.length == 0) {
      return null;
    } else {
      return listOfFiles;
    }
  }

}
