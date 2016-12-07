package SP.State_Of_Game;

import SP.Controls.Controls;
import SP.Main_Game.GameFrame;
import SP.Music.MusicPlayer;
import SP.Tile_Map.Background;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by jcpatac on 12/2/2016.
 */
public class GameOver extends GameState {

    MusicPlayer musicPlayer;

    Background bg;

    public GameOver(ManageGS manageGS) {
        super(manageGS);

        try {
            bg = new Background("/SP/For_the_game/gameOver.jpg", 0.1);
            musicPlayer = new MusicPlayer("/SP/For_the_game/gameOverMusic.mp3");
            musicPlayer.play(false);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize() {

    }

    @Override
    public void update() {
        pressedKeys();
    }

    @Override
    public void draw(Graphics2D g) {
        bg.draw(g);
    }

    @Override
    public void pressedKeys() {
        if (Controls.isPressed(Controls.OK)) {
            gsManager.setState(ManageGS.MENUSTATE);
        }
    }
}
