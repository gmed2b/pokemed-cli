package cli;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Pokemon
 * 
 * Represents a Pokemon
 */
public class Pokemon implements Serializable {
  private int id;
  private String name;
  private int hp;
  private int attack;
  private PokemonType type;
  private int evolutionStage;
  private ArrayList<Pokemon> evolution = new ArrayList<>();

  /**
   * Constructor
   * 
   * @param id             The Pokemon's id
   * @param name           The Pokemon's name
   * @param hp             The Pokemon's HP
   * @param attack         The Pokemon's attack
   * @param type           The Pokemon's type
   * @param evolutionStage The Pokemon's evolution stage
   * @param evolution      The Pokemon's evolution
   */
  public Pokemon(int id, String name, int hp, int attack, PokemonType type, int evolutionStage) {
    setId(id);
    setName(name);
    setHp(hp);
    setAttack(attack);
    setType(type);
    setEvolutionStage(evolutionStage);
    setEvolution(evolution);
  }

  @Override
  public String toString() {
    return String.format("%s %s (HP: %d, Attack: %d) %s", this.type.getEmojiType(), getName(), getHp(),
        getAttack(), getEvolution());
  }

  /**
   * Eat a certain amount of candy to increase HP and attack
   * Can evolve if 5 candies are eaten
   * 
   * @param amount The amount of candy to eat
   */
  public void eat(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Amount must be greater than 0");
    }

    // Check if the trainer have enough rare candies
    if (amount > 5) {
      String answser;
      System.out.println("You have more than 5 candies, you can evolve your pokemon.");
      System.out.print("Do you want to evolve your pokemon ? (y/n) ");
      do {
        answser = Main.reader.nextLine();
        // If yes, evolve the pokemon, remove 5 candies and eat the rest
      } while (answser != "y" || answser != "n");
      return;
    }

    double increaseFactor = amount * 0.1; // Adjust the factor as needed
    int hpIncrease = (int) (getHp() * increaseFactor);
    int attackIncrease = (int) (getAttack() * increaseFactor);

    setHp(getHp() + hpIncrease);
    setAttack(getAttack() + attackIncrease);

    System.out.println("Eating candy ...");
    System.out.println(String.format("HP: %d (+%d)", getHp(), hpIncrease));
    System.out.println(String.format("Attack: %d (+%d)", getAttack(), attackIncrease));
    System.out.println();
  }

  /**
   * Evolve the Pokemon
   * 
   * @throws UnsupportedOperationException If the Pokemon cannot evolve
   */
  public void evolve() {
    System.out.println(getName() + " is evolving ...");
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Congratulations !");
    System.out.println(getName() + " evolved into " + getEvolution().get(0).getName() + " !");
  }

  /**
   * Get a random Pokemon from the Pokedex
   * 
   * @return A random first stage Pokemon
   */
  public static Pokemon getRandomPokemon() {
    Pokemon wildPokemon = null;
    do {
      int random = (int) (Math.random() * Pokedex.getPokedex().size());
      wildPokemon = Pokedex.getPokedex().get(random);
    } while (wildPokemon.getEvolutionStage() > 1);

    return wildPokemon;
  }

  /**
   * Edit the Pokemon
   */
  public void editPokemon(ArrayList<RareCandy> rareCandies) {
    System.out.println("Editing " + this);
    System.out.println("1. Edit name");
    System.out.println("2. Eat candy");
    System.out.println("0. Back");
    int choice = Main.getIntInput();
    switch (choice) {
      case 1:
        System.out.print("New name: ");
        String newName = Main.reader.nextLine();
        setName(newName);
        System.out.println("Name changed !");
        break;
      case 2:
        // Get the amount of candies that have the same type as the pokemon
        ArrayList<RareCandy> sameTypeCandies = new ArrayList<>();
        for (RareCandy candy : rareCandies) {
          if (candy.getType() == getType()) {
            sameTypeCandies.add(candy);
          }
        }
        if (sameTypeCandies.size() == 0) {
          System.out.println("You don't have any candies for this pokemon");
          break;
        }
        System.out.println("You have " + sameTypeCandies.size() + " rare candies");
        System.out.print("How many candies do you want to eat ? ");
        int amount = Main.getIntInput();
        try {
          eat(amount);
        } catch (IllegalArgumentException e) {
          System.out.println(e.getMessage());
        }
        break;
      case 0:
      default:
        break;
    }
  }

  // ------------------
  // Getters and setters
  // ------------------

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (name.length() < 2) {
      throw new IllegalArgumentException("Name must be at least 2 characters long");
    }
    this.name = name;
  }

  public int getHp() {
    return hp;
  }

  public void setHp(int hp) {
    this.hp = hp;
    if (this.hp < 0) {
      // Pokemon fainted
      this.hp = 0;
    }
  }

  public int getAttack() {
    return attack;
  }

  public void setAttack(int attack) {
    if (attack < 0) {
      throw new IllegalArgumentException("Attack must be greater than 0");
    }
    this.attack = attack;
  }

  public PokemonType getType() {
    return type;
  }

  public void setType(PokemonType type) {
    this.type = type;
  }

  public int getEvolutionStage() {
    return evolutionStage;
  }

  public void setEvolutionStage(int evolutionStage) {
    this.evolutionStage = evolutionStage;
  }

  public ArrayList<Pokemon> getEvolution() {
    return evolution;
  }

  public void setEvolution(ArrayList<Pokemon> evolution) {
    this.evolution = evolution;
  }

}