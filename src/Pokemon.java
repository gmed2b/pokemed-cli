
/**
 * Pokemon
 * 
 * Represents a Pokemon
 */
public class Pokemon {
  private int id;
  private String name;
  private int hp;
  private int attack;
  private PokemonType type;

  /**
   * Constructor
   * 
   * @param id     The Pokemon's ID
   * @param name   The Pokemon's name
   * @param hp     The Pokemon's HP
   * @param attack The Pokemon's attack
   * @param type   The Pokemon's type
   */
  public Pokemon(int id, String name, int hp, int attack, PokemonType type) {
    setId(id);
    setName(name);
    setHp(hp);
    setAttack(attack);
    setType(type);
  }

  @Override
  public String toString() {
    return String.format("%s <%d> %s (HP: %d, Attack: %d)", this.type.getEmojiType(), getId(), getName(), getHp(),
        getAttack());
  }

  /**
   * Eat a certain amount of candy to increase HP and attack
   * <p>
   * Can evolve if 5 candies are eaten
   * </p>
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
      do {
        System.out.println("You have more than 5 candies, you can evolve your pokemon.");
        System.out.print("Do you want to evolve your pokemon ? (y/n) ");
        answser = Main.reader.nextLine();
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
   * @throws UnsupportedOperationException If the Pokemon cannot * evolve
   */
  public void evolve() {
    System.out.println("Evolving pokemon ...");
  }

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
    if (hp < 0) {
      throw new IllegalArgumentException("HP must be greater than 0");
    }
    this.hp = hp;
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
}