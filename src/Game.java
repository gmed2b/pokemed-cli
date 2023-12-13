import java.io.File;

public class Game {
  private Trainer trainer;
  private GameClient client = null;

  public Game(Trainer trainer) {
    this.trainer = trainer;
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
      System.out.println();

      switch (choice) {
        case 0:
          catchAPokemon();
          break;
        case 1:
          listOwnedPokemons();
          break;
        case 2:
          // Level-up a pokemon
          break;
        case 3:
          startOnlineBattle();
          break;
        case 4:
          // Free a pokemon
          break;
        case 5:
          trainerProfile();
          break;
        case 6:
          listPokedex();
          break;
        case 7:
          saveGame(trainer);
          break;
        case 8:
          // Exit
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
    System.out.println("0. Catch a pokemon");
    System.out.println("1. List owned pokemons");
    System.out.println("2. Level-up a pokemon");
    System.out.println("3. Start a battle");
    System.out.println("4. Free a pokemon");
    System.out.println();
    System.out.println("5. Trainer profile");
    System.out.println("6. List all pokemons in the pokedex");
    System.out.println("7. Save the game");
    System.out.println("8. Exit");
  }

  /**
   * Catch a wild pokemon
   */
  private void catchAPokemon() {
    System.out.println("Entering the wild ...");
    try {
      Thread.sleep(1400);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Pokemon wildPokemon = Pokemon.getRandomPokemon();
    System.out.println("A wild " + wildPokemon.getName() + " appeared !");
    System.out.println();
    System.out.println("0. Catch");
    System.out.println("1. Run");
    System.out.print("-> ");
    int catchChoice = Integer.parseInt(Main.reader.nextLine());
    System.out.println();
    if (catchChoice == 0) {
      // if (pokemon.catchPokemon()) {
      this.trainer.addPokemon(wildPokemon);
      System.out.println("You caught " + wildPokemon.getName() + " !");
      // } else {
      // System.out.println("You failed to catch " + pokemon.getName() + " !");
      // }
    } else {
      // Run away
      System.out.println("You ran away");
    }
  }

  /**
   * List all owned pokemons
   */
  private void listOwnedPokemons() {
    trainer.listOwnedPokemons();
    System.out.println();
    System.out.println("0. Back");
    System.out.print("-> ");
    int backChoice = Integer.parseInt(Main.reader.nextLine());
    // Add a way to select a pokemon and display its profile
    if (backChoice != 0 && backChoice <= trainer.getPokemons().size()) {
      System.out.println(trainer.getPokemons().get(backChoice - 1));
    }
    // Modify the pokemon's name
  }

  /**
   * Start an online battle
   */
  private void startOnlineBattle() {
    if (this.client == null) {
      this.client = new GameClient(this.trainer);
    } else {
      System.out.println("Already connected to a server");
    }
  }

  /**
   * Display the trainer's profile
   */
  private void trainerProfile() {
    System.out.println(this.trainer);
  }

  /**
   * List all pokemons in the pokedex
   */
  private void listPokedex() {
    System.out.println();
    System.out.println("--------- Pokedex ---------");
    System.out.println();

    for (Pokemon pokemon : Pokedex.getPokedex()) {
      System.out.println(pokemon);
    }
  }

  // ------------------
  // ----- STATIC -----
  // ------------------

  /**
   * Save the game to a file in the saves folder
   * 
   * @param trainer
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
      Thread.sleep(1300);
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
   * Get all saved games
   * 
   * @return List of saved games
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
