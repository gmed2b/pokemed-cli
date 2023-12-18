package cli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import server.ObjectStream;
import server.Server.Commands;

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
   * Attack another pokemon
   * 
   * @param p1 The pokemon to attack with
   * @param p2 The pokemon to attack
   * @return The damage dealt
   */
  public int attack(Pokemon p1, Pokemon p2) {
    // Calculate the damage dealt by the pokemon p1
    Random rand = new Random();
    int random = rand.nextInt(15) + 7;
    int lvl = p1.getEvolutionStage() * random;
    double attack_coeff = (2.0 * lvl + 10) / 250.0;
    int damage = (int) Math.floor((attack_coeff * (float) p1.getAttack()) + Game.ATTACK_COEFFICIENT);

    // Set the damage dealt to the pokemon p2
    p2.setHp(p2.getHp() - damage);

    // Return the damage dealt
    return damage;
  }

  /**
   * Choose a pokemon to attack with
   * 
   * @return The pokemon to attack with
   */
  public Pokemon choosePokemon() {
    System.out.println("Choose a pokemon to attack with");
    listOwnedPokemons();
    int pokemonChoice = Main.getIntInput();
    if (pokemonChoice > 0 && pokemonChoice <= this.getPokemons().size()) {
      Pokemon selectedPokemon = this.getPokemons().get(pokemonChoice - 1);
      return selectedPokemon;
    }
    return null;
  }

  /**
   * Get battle action from the trainer
   * 
   * @return The battle action
   */
  public ObjectStream getBattleAction() {
    System.out.println("1. Attack");
    int choice = Main.getIntInput();
    if (choice == 1) {
      return new ObjectStream(Commands.ATTACK, null);
    }
    return null;
  }

  /**
   * Catch a pokemon randomly
   * 
   * @param pokemon The pokemon to catch
   */
  public void catchPokemon(Pokemon pokemon) {
    // 75% chance to catch the pokemon
    if (Math.random() < 0.75) {
      System.out.println("You caught " + pokemon);

      // Add the pokemon to the trainer's pokemons (if possible)
      try {
        addPokemon(pokemon);

        // 60% chance to drop a rare candy of the pokemon's type
        if (Math.random() < 0.60) {
          RareCandy rareCandy = new RareCandy(pokemon.getType());
          rareCandies.add(rareCandy);
          System.out.println("The pokemon dropped a rare candy !");
        }
      } catch (UnsupportedOperationException e) {
        System.out.println("You can't have more than 6 pokemons");
        System.out.println("The pokemon escaped");
      }
    } else {
      System.out.println("The pokemon escaped");
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
