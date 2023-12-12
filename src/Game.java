import java.util.ArrayList;

public class Game {
  private ArrayList<Pokemon> pokedex;
  private Trainer trainer;

  public Game(Trainer trainer, ArrayList<Pokemon> pokedex) {
    setTrainer(trainer);
    setPokedex(pokedex);
  }

  public void start() {
    System.out.println();
    System.out.println("--- Welcome to Pokemed! ---");
    System.out.println("This is a game where you can catch and evolve Pokemon");
    System.out.println("You can also battle with other trainers");
    System.out.println();
    System.out.println("- Main Menu -");
    System.out.println();
    System.out.println("1. List owned pokemons");
    System.out.println("2. Level-up a pokemon");
    System.out.println("3. Start a battle");
    System.out.println("4. Free a pokemon");
    System.out.println();
    System.out.println("5. List all pokemons in the pokedex");
    System.out.println("6. Save the game");
    System.out.println("7. Exit");

    System.out.print("-> ");
    int choice = Integer.parseInt(Main.reader.nextLine());

    System.out.println();
  }

  public ArrayList<Pokemon> getPokedex() {
    return pokedex;
  }

  public void setPokedex(ArrayList<Pokemon> pokedex) {
    this.pokedex = pokedex;
  }

  public Trainer getTrainer() {
    return trainer;
  }

  public void setTrainer(Trainer trainer) {
    this.trainer = trainer;
  }

}
