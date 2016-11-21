package SP.State_Of_Game;

/**
 * Created by acer on 10/14/2016.
 */

import SP.Controls.Controls;
import SP.Music.MusicPlayer;
import SP.Tile_Map.Background;
import SP.Main_Game.GameFrame;

import java.awt.*;
import java.util.HashMap;

public class MenuState extends GameState {

    private String[] options = {"Start", "Quit"};
    private int currChoice = 0;
    private Font titleFont;
    private Font optFont;
    private Color titleFontColor;
    private Color optFontColor;
    private Background background;

    private HashMap<String, MusicPlayer> menuMusics;

    public MenuState(ManageGS gsManager) {

        super(gsManager);

        try {
            background = new Background("/SP/For_the_game/menuBack.png", 1);
            background.setVector(-0.1, 0);
            titleFontColor = Color.DARK_GRAY;
            optFontColor = Color.RED;
            titleFont = new Font("Algerian", Font.PLAIN, 80);
            optFont = new Font("Ar Julian", Font.PLAIN, 45);

            menuMusics = new HashMap<String, MusicPlayer>();
            menuMusics.put("menuBG", new MusicPlayer("/SP/For_the_game/backgroundChill.wav"));
            menuMusics.put("selection", new MusicPlayer("/SP/For_the_game/simpleclick.wav"));

            menuMusics.get("menuBG").play(true);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {

    }

    public void update() {
        background.update();
        pressedKeys();
    }

    public void draw(Graphics2D g) {

        // background
        background.draw(g);

        // title
        g.setColor(titleFontColor);
        g.setFont(titleFont);
        g.drawString("My Heart's", (GameFrame.WIDTH / 2) - 205, GameFrame.HEIGHT / 4);
        g.drawString("At Home", (GameFrame.WIDTH / 2) - 153, GameFrame.HEIGHT / 3 + 15);

        // menu options
        g.setColor(optFontColor);
        g.setFont(optFont);
        for (int i = 0; i < options.length; i++) {
            if (i == currChoice) {
                g.setColor(Color.RED);
            }else {
                g.setColor(Color.BLACK);
            }
            g.drawString(options[i], (GameFrame.WIDTH / 2) - 53, (GameFrame.HEIGHT / 2) + (i * 67));
        }
    }

    private void select() {
        if (currChoice == 0) {
            gsManager.setState(ManageGS.LEVEL1);
            menuMusics.get("menuBG").stop();
        }
//        if (currChoice == 1) {
//
//        }
        if (currChoice == 1) {
            System.exit(0);
        }
    }

    @Override
    public void pressedKeys() {

        if (Controls.isPressed(Controls.OK)) {
            select();
        }

        if (Controls.isPressed(Controls.UP)) {
//            currChoice--;

            if (currChoice > 0) {
                menuMusics.get("selection").play(false);
                currChoice--;
            }
        }
        if (Controls.isPressed(Controls.DOWN)) {
//            currChoice++;

            if (currChoice < options.length - 1) {
                menuMusics.get("selection").play(false);
                currChoice++;
            }
        }

    }

//    public void keyPressed(int key) {
//        if (key == KeyEvent.VK_ENTER) {
//            select();
//        }
//        if (key == KeyEvent.VK_UP) {
//            currChoice--;
//
//            if (currChoice == -1) {
//                currChoice = options.length - 1;
//            }
//        }
//        if (key == KeyEvent.VK_DOWN) {
//            currChoice++;
//
//            if (currChoice == options.length) {
//                currChoice = 0;
//            }
//        }
//    }
//
//    public void keyReleased(int key) {
//
//    }
}
