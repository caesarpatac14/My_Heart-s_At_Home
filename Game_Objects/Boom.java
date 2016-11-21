package SP.Game_Objects;

/**
 * Created by acer on 10/17/2016.
 */

import SP.Controls.Content;
import SP.Tile_Map.TileMap;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Boom extends StuffsInMap {

    private BufferedImage[] sprites;
    private boolean removeInGame;
    private Point[] points;
    private int speed;
    private double diagonalSpeed;

    public Boom(TileMap tileMap, int x, int y) {
        super(tileMap);
        this.x = x;
        this.y = y;
        width = 30;
        height = 30;
        speed = 2;
        diagonalSpeed = 1.41;

        sprites = Content.BoomSprite[0];

        animation.setFrames(sprites);
        animation.setPause(6);

        points = new Point[8];

        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(x, y);
        }
    }

    public void update() {
        animation.update();

        if (animation.playedOnce()) {
            removeInGame = true;
        }

        points[0].x += speed;
        points[1].x += diagonalSpeed;
        points[1].y += diagonalSpeed;
        points[2].y += speed;
        points[3].x -= diagonalSpeed;
        points[3].y += diagonalSpeed;
        points[4].x -= speed;
        points[5].x -= diagonalSpeed;
        points[5].y -= diagonalSpeed;
        points[6].y -= speed;
        points[7].x += diagonalSpeed;
        points[7].y -= diagonalSpeed;

    }

    public boolean mustRemoveInGame() {
        return removeInGame;
    }

    public void draw(Graphics2D g) {

        setPosMap();
        for (int i = 0; i < points.length; i++) {
            g.drawImage(animation.getImage(), (int) (points[i].x + xmap - width / 2), (int) (points[i].y + ymap - height / 2), null);
        }

    }
}
