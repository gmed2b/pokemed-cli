package guigui;

import java.util.ArrayList;

import cli.Pokemon;
import cli.PokemonType;
import cli.RareCandy;

public class Dresseur {
    public int playerX = 90;
    public int playerY = 90;

    private Sprite playerSprite;

    private String name;
    private ArrayList<Pokemon> pokemons = new ArrayList<>();
    private ArrayList<RareCandy> rareCandies = new ArrayList<>();
    public final static Integer nbMaxPokemon = 6;

    // constructor
    public Dresseur(String name, ArrayList<Pokemon> pokemons) {
        this.name = name;
        this.pokemons = pokemons;
        this.playerSprite = new Sprite("assets/img/character/", 2, "down");
    }

    @Override
    public String toString() {
        return "Trainer [name=" + name + ", rareCandies=" + rareCandies + "]" + "PokemonsTeam " + pokemons;
    }

    // getters and setters
    public Sprite getPlayerSprite() {
        return playerSprite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Pokemon> getPokemons() {
        return pokemons;
    }

    public static Integer getNbmaxpokemon() {
        return nbMaxPokemon;
    }

    // methods
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

    public void removePokemon(Pokemon pokemon) {
        this.pokemons.remove(pokemon);
    }

    // function browse rare candies return the number of rare candies of the type
    public int browseRareCandies(PokemonType type) {
        int nbRareCandies = 0;
        for (RareCandy rareCandy : rareCandies) {
            if (rareCandy.getType() == type) {
                nbRareCandies++;
            }
        }
        return nbRareCandies;
    }

    // function return array of diffrent candy pokemonType
    public ArrayList<PokemonType> getRareCandiesType() {
        ArrayList<PokemonType> rareCandiesType = new ArrayList<>();
        for (RareCandy rareCandy : rareCandies) {
            if (!rareCandiesType.contains(rareCandy.getType())) {
                rareCandiesType.add(rareCandy.getType());
            }
        }
        return rareCandiesType;
    }

    // incrementation position
    public void incrementPlayerX(int x) {
        if (this.playerX + x > 400) {
            this.playerX = 400;
        } else if (this.playerX + x < 0) {
            this.playerX = 0;
        } else {
            this.playerX += x;
        }
    }

    public void incrementPlayerY(int y) {
        if (this.playerY + y > 400) {
            this.playerY = 400;
        } else if (this.playerY + y < 30) {
            this.playerY = 30;
        } else {
            this.playerY += y;
        }
    }

    /**
     * Catch a pokemon randomly
     * 
     * @param pokemon The pokemon to catch
     * @return -1, if error; 0 if it escaped, 1 if the pokemon is caught, , 2 if it
     *         dropped a rare candy
     */
    public int catchPokemon(Pokemon pokemon) {
        int result = -1;
        // 75% chance to catch the pokemon
        if (Math.random() < 0.75) {
            System.out.println("You caught " + pokemon);

            // Add the pokemon to the trainer's pokemons (if possible)
            try {
                addPokemon(pokemon);
                result = 1;

                // 60% chance to drop a rare candy of the pokemon's type
                if (Math.random() < 0.60) {
                    RareCandy rareCandy = new RareCandy(pokemon.getType());
                    rareCandies.add(rareCandy);
                    System.out.println("The pokemon dropped a rare candy !");
                    result = 2;
                }

            } catch (UnsupportedOperationException e) {
                result = -1;
                System.out.println("You can't have more than 6 pokemons");
                System.out.println("The pokemon escaped");
            }

        } else {
            System.out.println("The pokemon escaped");
            result = 0;
        }
        return result;
    }

}