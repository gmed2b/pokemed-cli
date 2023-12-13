import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Pokedex {
  static ArrayList<Pokemon> pokedex = new ArrayList<>();

  public Pokedex() {
    try {
      loadPokemon();
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    } catch (IOException e) {
      System.out.println("IO Exception");
    }
    System.out.println("[LOG] Loaded " + pokedex.size() + " pokemons");
  }

  /**
   * Load all existing pokemons from a csv file into the pokedex.
   * 
   * @throws IOException
   * @throws FileNotFoundException
   */
  private static void loadPokemon() throws FileNotFoundException, IOException {
    try (BufferedReader br = new BufferedReader(new FileReader("./assets/pokemons.csv"))) {
      String line;
      boolean firstLine = true;
      while ((line = br.readLine()) != null) {
        if (firstLine) {
          firstLine = false;
          continue;
        }
        // Create a new Pokemon for each line and add it to the Pokedex
        String[] data = line.split(",");

        String name = data[0];
        int hp = Integer.parseInt(data[1]);
        int attack = Integer.parseInt(data[2]);
        String type = data[3];
        int evolutionStage = Integer.parseInt(data[4]);
        String evolutionString = data[5];
        ArrayList<Pokemon> evolution = new ArrayList<>();
        if (!evolutionString.equals("NULL")) {
          String[] evolutionNames = evolutionString.split(";");
          for (String evolutionName : evolutionNames) {
            for (Pokemon pokemon : pokedex) {
              if (pokemon.getName().equals(evolutionName)) {
                evolution.add(pokemon);
              }
            }
          }
        }

        Pokemon pokemon = new Pokemon(name, hp, attack, PokemonType.valueOf(type.toUpperCase()), evolutionStage,
            evolution);
        pokedex.add(pokemon);
      }
    }
  }

  public static ArrayList<Pokemon> getPokedex() {
    return pokedex;
  }

  public static void setPokedex(ArrayList<Pokemon> pokedex) {
    Pokedex.pokedex = pokedex;
  }

}
