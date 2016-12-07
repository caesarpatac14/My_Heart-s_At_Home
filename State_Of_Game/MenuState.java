package SP.State_Of_Game;

/**
 * Created by acer on 10/14/2016.
 */

import SP.Controls.Controls;
import SP.Game_Objects.SavePlayer;
import SP.Music.MusicPlayer;
import SP.Tile_Map.Background;
import SP.Main_Game.GameFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class MenuState extends GameState {

//    private String[] options = {"Start", "Quit"};
    private BufferedImage indicator;
    private BufferedImage[] options;
//    private BufferedImage title;
    private int currChoice = 0;
//    private Font titleFont;
//    private Font optFont;
//    private Color titleFontColor;
//    private Color optFontColor;
    private Background background;

    private String[] backgrounds = {"/SP/For_the_game/lain1.png", "/SP/For_the_game/lain2.png", "/SP/For_the_game/lain3.png",
                                    "/SP/For_the_game/lain4.png", "/SP/For_the_game/lain5.png", "/SP/For_the_game/lain6.png"};

    private HashMap<String, MusicPlayer> menuMusics;

    public MenuState(ManageGS gsManager) {

        super(gsManager);

//        JButton button = new JButton();

        try {

//            title = ImageIO.read(getClass().getResourceAsStream("/SP/For_the_game/My-Hearts-At-Home.png"));

            indicator = ImageIO.read(getClass().getResourceAsStream("/SP/For_the_game/hud.png")).getSubimage(0, 0, 59, 43);

            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/SP/For_the_game/button_play.png"));
            BufferedImage image1 = ImageIO.read(getClass().getResourceAsStream("/SP/For_the_game/button_quit.png"));

            options = new BufferedImage[2];
            options[0] = image;
            options[1] = image1;


//            button.setIcon(new ImageIcon(image));
//            button.setBorder(BorderFactory.createEmptyBorder());

//            background = new Background("/SP/For_the_game/lain1.png", 1);
            background = new Background(backgrounds, 1);
            background.setVector(-0.1, 0);
//            titleFontColor = Color.DARK_GRAY;
//            optFontColor = Color.RED;
//            titleFont = new Font("Algerian", Font.PLAIN, 80);
//            optFont = new Font("Ar Julian", Font.PLAIN, 45);

            menuMusics = new HashMap<String, MusicPlayer>();
            menuMusics.put("menuBG", new MusicPlayer("/SP/For_the_game/Intro Theme.mp3"));
            menuMusics.put("selection", new MusicPlayer("/SP/For_the_game/selected.mp3"));
            menuMusics.put("switching", new MusicPlayer("/SP/For_the_game/switching.mp3"));

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

        int ctr = 0;

        // background
        background.draw1(g);

//        g.drawImage(title, (GameFrame.WIDTH / 2) - 400, GameFrame.HEIGHT / 4, null);

        // title
//        g.setColor(titleFontColor);
//        g.setFont(titleFont);
//        g.drawString("My Heart's", (GameFrame.WIDTH / 2) - 205, GameFrame.HEIGHT / 4);
//        g.drawString("At Home", (GameFrame.WIDTH / 2) - 153, GameFrame.HEIGHT / 3 + 15);

        for (int i = 0; i < options.length; i++) {
            g.drawImage(options[i], (GameFrame.WIDTH / 2) - 90, (GameFrame.HEIGHT / 2) + (i * 67) - 50, null);
            int newWidth = options[i].getWidth() * 2;
            int neHeight = options[i].getHeight() * 2;
            BufferedImage resized = new BufferedImage(newWidth, neHeight, BufferedImage.TYPE_3BYTE_BGR);
//            if (i == currChoice) {
//                g = resized.createGraphics();
//                g.drawImage(options[i], (GameFrame.WIDTH / 2) - 90, (GameFrame.HEIGHT / 2) + (i * 67) - 50, newWidth, neHeight, null);
//                g.dispose();
//            }
        }

        // menu options
//        g.setColor(optFontColor);
//        g.setFont(optFont);
//        for (int i = 0; i < options.length; i++) {
//            if (i == currChoice) {
//                g.setColor(Color.RED);
//            }else {
//                g.setColor(Color.BLACK);
//            }
//            g.drawString(options[i], (GameFrame.WIDTH / 2) - 53, (GameFrame.HEIGHT / 2) + (i * 67));
//        }
        if (currChoice == 0) {
            g.drawImage(indicator, (GameFrame.WIDTH / 2) - 120, (GameFrame.HEIGHT / 2) - 40, null);
        }else {
            g.drawImage(indicator,(GameFrame.WIDTH / 2) - 120, (GameFrame.HEIGHT / 2) + 28, null);
        }
    }

    private void select() {
        if (currChoice == 0) {
            menuMusics.get("menuBG").stop();
            menuMusics.get("selection").play(false);
            SavePlayer.initialize();
            background.controlTimer(false);
            gsManager.setState(ManageGS.LEVEL1);
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
            menuMusics.get("switching").play(false);

            if (currChoice > 0) {
                currChoice--;
            }
        }
        if (Controls.isPressed(Controls.DOWN)) {
//            currChoice++;
            menuMusics.get("switching").play(false);

            if (currChoice < options.length - 1) {
                currChoice++;
            }
        }

    }
}
