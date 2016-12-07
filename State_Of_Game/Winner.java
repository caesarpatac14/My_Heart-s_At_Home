package SP.State_Of_Game;

import SP.Controls.Controls;
import SP.Main_Game.GameFrame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by jcpatac on 12/2/2016.
 */
public class Winner extends GameState {

    private float shade;
    private Color color;
    private double angle;
    private BufferedImage image;

    public Winner(ManageGS manageGS) {
        super(manageGS);

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/SP/For_the_game/ThanksGod.png")).getSubimage(0, 0, 42, 58);
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
        color = Color.getHSBColor(shade, 1f, 1f);
        shade += 0.01;
        if (shade > 1) {
            shade = 0;
        }
        angle += 0.01;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillRect(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT);
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(GameFrame.WIDTH / 2, GameFrame.HEIGHT / 2);
        affineTransform.rotate(angle);
        g.drawImage(image, affineTransform, null);
    }

    @Override
    public void pressedKeys() {
        if (Controls.isPressed(Controls.OK)) {
            gsManager.setState(ManageGS.MENUSTATE);
        }
    }
}
