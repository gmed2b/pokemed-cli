package cli;

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
      System.out.println("[LOG] Loaded " + pokedex.size() + " pokemons");
    } catch (FileNotFoundException e) {
      System.out.println("[ERROR] " + e.getMessage());
      System.exit(1);
    } catch (IOException e) {
    }
  }

  /**
   * Load all existing pokemons from a csv file into the pokedex.
   * 
   * @throws IOException
   * @throws FileNotFoundException
   */
  private static void loadPokemon() throws FileNotFoundException, IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(Main.POKEMONS_PATH))) {
      String line;
      boolean firstLine = true;
      while ((line = br.readLine()) != null) {
        if (firstLine) {
          firstLine = false;
          continue;
        }
        // Create a new Pokemon for each line and add it to the Pokedex
        String[] data = line.split(",");

        int id = Integer.parseInt(data[0]);
        String name = data[1];
        int hp = Integer.parseInt(data[2]);
        int attack = Integer.parseInt(data[3]);
        String type = data[4];
        int evolutionStage = Integer.parseInt(data[5]);

        Pokemon pokemon = new Pokemon(id, name, hp, attack, PokemonType.valueOf(type.toUpperCase()), evolutionStage);
        pokedex.add(pokemon);
      }
    }
    try (BufferedReader br = new BufferedReader(new FileReader(Main.POKEMONS_PATH))) {
      String line;
      boolean firstLine = true;
      while ((line = br.readLine()) != null) {
        if (firstLine) {
          firstLine = false;
          continue;
        }
        // Create a new Pokemon for each line and add it to the Pokedex
        String[] data = line.split(",");

        String name = data[1];
        String evolutionString = data[6];
        ArrayList<Pokemon> evolution = new ArrayList<>();
        if (!evolutionString.equals("NULL")) {
          String[] evolutionNames = evolutionString.split(";");
          for (String evolutionName : evolutionNames) {
            evolution.add(getPokemonByName(evolutionName));
          }
        }
        int idx = pokedex.indexOf(getPokemonByName(name));
        pokedex.get(idx).setEvolution(evolution);
      }
    }
  }

  public static ArrayList<Pokemon> getPokedex() {
    return pokedex;
  }

  public static void setPokedex(ArrayList<Pokemon> pokedex) {
    Pokedex.pokedex = pokedex;
  }

  public static Pokemon getPokemonByName(String name) {
    for (Pokemon pokemon : pokedex) {
      if (pokemon.getName().equals(name)) {
        return pokemon;
      }
    }
    return null;
  }

}
