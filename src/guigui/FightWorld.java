package guigui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import cli.Pokemon;

public class FightWorld extends World {

    private Image wildPokemonImage;
    private Pokemon wildPokemon;

    public FightWorld(Dresseur dresseur, String playerName) {
        super(dresseur, playerName);
    }

    protected void init() {
        // System.out.println(Pokedex.getPokedex());
        backgroundImage = new ImageIcon("assets/img/decors/bgFight.png").getImage();
        loadBackgroundMusic("assets/sounds/arena.wav");
    }

    protected void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, this);
    }

    // override gameLoop method for add condition to change world
    @Override
    protected void gameLoop() {
        super.gameLoop();
    }

}
