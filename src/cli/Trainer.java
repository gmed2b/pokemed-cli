package cli;

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
    return "Trainer [name=" + name + ", rareCandies=" + rareCandies + "]";
  }

  /**
   * Catch a pokemon randomly
   * 
   * @param pokemon The pokemon to catch
   * @return A chance to drop a rare candy of the pokemon's type
   */
  public RareCandy catchPokemon(Pokemon pokemon) {
    // 75% chance to catch the pokemon
    if (Math.random() < 0.75) {
      System.out.println("You caught " + pokemon);

      // Add the pokemon to the trainer's pokemons (if possible)
      try {
        addPokemon(pokemon);
      } catch (UnsupportedOperationException e) {
        System.out.println("You can't have more than 6 pokemons");
        System.out.println("The pokemon escaped");
        return null;
      }

      // 45% chance to drop a rare candy of the pokemon's type
      if (Math.random() < 0.45) {
        RareCandy rareCandy = new RareCandy(pokemon.getType());
        rareCandies.add(rareCandy);
        System.out.println("The pokemon dropped a rare candy !");
        return rareCandy;
      }

      return null;
    } else {
      System.out.println("The pokemon escaped");
      return null;
    }
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
   * @throws UnsupportedOperationException If the trainer already have 6 pokemons
   */
  public void addPokemon(Pokemon pokemon) {
    if (pokemons.size() >= 6) {
      throw new UnsupportedOperationException("A trainer can't have more than 6 pokemons");
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
