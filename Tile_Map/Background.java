package SP.Tile_Map;

/**
 * Created by acer on 10/14/2016.
 */

import SP.Main_Game.GameFrame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class    Background {
    private BufferedImage image;

    private double x;
    private double y;
    private double dx;
    private double dy;

    private double moveScale;

    public Background(String str, double ms) {
        try {
            image = ImageIO.read(getClass().getResourceAsStream(str));
            moveScale = ms;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPosition(double x, double y) {
        this.x = (x * moveScale) % GameFrame.WIDTH;
        this.y = (y * moveScale) % GameFrame.HEIGHT;
    }

    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void update() {
        x += (dx * moveScale) % GameFrame.WIDTH;
        y += (dy * moveScale) % GameFrame.HEIGHT;
    }

    public void draw(Graphics2D g) {
        g.drawImage(image, (int) x, (int) y, null);

//        g.drawImage(image.getScaledInstance((int) y, -1, Image.SCALE_SMOOTH), (int) x, (int) y, null);

        if (x < 0) {
            g.drawImage(image, (int) x + GameFrame.WIDTH, (int) y, null);
        }
        if(x > 0) {
            g.drawImage(image, (int) x - GameFrame.WIDTH, (int) y, null);
        }
    }
}
