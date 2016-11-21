package SP.State_Of_Game;

import SP.Controls.Controls;
import SP.Main_Game.GameFrame;

import java.awt.*;

/**
 * Created by jcpatac on 11/17/2016.
 */
public class Pause extends GameState {

    private Font font;

    public Pause(ManageGS manageGS) {
        super(manageGS);
        font = new Font("Verdana", Font.BOLD, 28);
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
//        g.setColor(Color.BLACK);
//        g.fillRect(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString("Game Paused", (GameFrame.WIDTH / 2) - 153, GameFrame.HEIGHT / 3 + 15);
    }

    @Override
    public void pressedKeys() {
        if (Controls.isPressed(Controls.PAUSE)) {
            gsManager.setPaused(true);
        }
        if (Controls.isPressed(Controls.OK)) {
            gsManager.setPaused(false);
//            gsManager.setState(ManageGS.MENUSTATE);
        }
    }
}
