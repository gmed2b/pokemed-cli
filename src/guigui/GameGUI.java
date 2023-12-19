package guigui;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import cli.Pokemon;

public class GameGUI {

    public static void main(String[] args) {
        Dresseur giovanni = new Dresseur("Giovanni", new ArrayList<Pokemon>());
        new GameGUI().start(giovanni);

    }

    public void start(Dresseur dresseur) {
        SwingUtilities.invokeLater(() -> {
            HomeWorld game = new HomeWorld(dresseur, "Pokemon by antocreadev & Medjourney");
            game.setVisible(true);
        });
    }
}
