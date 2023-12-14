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

    // Main.PlayMusic("./assets/JubilifeCityNight8bit.wav");

    // Main loop
    while (true) {
      // Display the main menu
      displayMainMenu();

      // Get the user's choice
      int choice = Main.getIntInput();
      System.out.println();

      switch (choice) {
        case 1:
          catchAPokemon();
          break;
        case 2:
          listOwnedPokemons();
          break;
        case 3:
          startOnlineBattle();
          break;
        case 4:
          listBackpackItems();
          break;
        case 8:
          listPokedex();
          break;
        case 9:
          saveGame(trainer);
          break;
        case 0:
        default:
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
    System.out.println("1. Catch a pokemon");
    System.out.println("2. List owned pokemons");
    System.out.println("3. Start a battle");
    System.out.println("4. List backpack's items");
    System.out.println();
    System.out.println("8. List all pokemons in the pokedex");
    System.out.println("9. Save the game");
    System.out.println("0. Exit");
  }

  /**
   * Catch a wild pokemon
   */
  private void catchAPokemon() {
    System.out.println("Entering the wild ...");

    try {
      Thread.sleep(400);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Pokemon wildPokemon = Pokemon.getRandomPokemon();

    System.out.println("A wild " + wildPokemon.getName() + " appeared !");
    System.out.println();
    System.out.println("1. Catch");
    System.out.println("2. Run");
    int choice = Main.getIntInput();
    switch (choice) {
      case 1:
        trainer.catchPokemon(wildPokemon);
        Main.pressToContinue();
        break;
      case 2:
      default:
        System.out.println("You ran away");
        break;
    }
  }

  /**
   * List all owned pokemons
   */
  private void listOwnedPokemons() {
    trainer.listOwnedPokemons();
    System.out.println();
    System.out.println("0. Back");
    int backChoice = Main.getIntInput();
    // Add a way to select a pokemon and display its profile
    if (backChoice != 0 && backChoice <= trainer.getPokemons().size()) {
      Pokemon selectedPokemon = trainer.getPokemons().get(backChoice - 1);
      System.out.println();
      selectedPokemon.editPokemon();
    }
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

  private void listBackpackItems() {
    System.out.println("--------- Backpack ---------");
    System.out.println();
    for (PokemonType type : PokemonType.values()) {
      int count = 0;
      for (RareCandy rareCandy : trainer.getRareCandies()) {
        if (rareCandy.getType() == type) {
          count++;
        }
      }
      if (count > 0) {
        System.out.println(String.format("%s %s: %d", type.getEmojiType(), type.toString(), count));
      }
    }
    Main.pressToContinue();
  }

  /**
   * List all pokemons in the pokedex
   */
  private void listPokedex() {
    System.out.println("--------- Pokedex ---------");
    System.out.println();
    for (Pokemon pokemon : Pokedex.getPokedex()) {
      System.out.println(pokemon);
    }
    Main.pressToContinue();

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