import java.io.Serializable;
import java.util.ArrayList;

public class Trainer implements Serializable {
  private String name;
  private ArrayList<Pokemon> pokemons = new ArrayList<>();
  private ArrayList<RareCandy> rareCandies = new ArrayList<>();

  public Trainer(String name) {
    setName(name);
  }

  @Override
  public String toString() {
    return "Trainer [name=" + name + ", pokemons=" + pokemons + ", rareCandies=" + rareCandies + "]";
  }

  /**
   * Catch a pokemon randomly
   */
  private void catchPokemon() {
    //
  }

  /**
   * List all owned pokemons
   */
  public void listOwnedPokemons() {
    System.out.println("You have " + pokemons.size() + " pokemons");
    for (int i = 0; i < pokemons.size(); i++) {
      System.out.println(i + 1 + ". " + pokemons.get(i));
    }
  }

  /**
   * Add a pokemon to the trainer's pokemons
   * 
   * @param pokemon The pokemon to add
   */
  public void addPokemon(Pokemon pokemon) {
    if (pokemons.size() >= 6) {
      throw new IllegalArgumentException("A trainer can't have more than 6 pokemons");
    }
    pokemons.add(pokemon);
  }

  // ------------------
  // Getters & Setters
  // ------------------

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (name.length() < 2) {
      throw new IllegalArgumentException("Name must be at least 2 characters long");
    }
    this.name = name;
  }

  public ArrayList<Pokemon> getPokemons() {
    return pokemons;
  }

  public void setPokemons(ArrayList<Pokemon> pokemons) {
    if (pokemons.size() > 6) {
      throw new IllegalArgumentException("A trainer can't have more than 6 pokemons");
    }
    this.pokemons = pokemons;
  }

  public ArrayList<RareCandy> getRareCandies() {
    return rareCandies;
  }

  public void setRareCandies(ArrayList<RareCandy> rareCandies) {
    this.rareCandies = rareCandies;
  }

}
