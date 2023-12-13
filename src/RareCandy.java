import java.io.Serializable;

/**
 * RareCandy
 * 
 * A rare candy is a food that can be eaten by a Pokemon to increase its level.
 */
public class RareCandy implements Serializable {
  private PokemonType type;

  public RareCandy(PokemonType type) {
    setType(type);
  }

  public PokemonType getType() {
    return type;
  }

  public void setType(PokemonType type) {
    this.type = type;
  }

}
