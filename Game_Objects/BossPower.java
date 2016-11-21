package SP.Game_Objects;

import SP.Controls.Content;
import SP.Tile_Map.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by jcpatac on 11/18/2016.
 */
public class BossPower extends Enemy {

    private BufferedImage[] loadSprite;
    private BufferedImage[] sprites;

    private boolean start;
    private boolean permanent;

    private int type = 0;
    public static int vector = 0;
    public static int gravity = 1;
    public static int bounce = 2;

    private int bounceCtr = 0;

    public BossPower(TileMap tileMap) {
        super(tileMap);

        hp = maxHP = 1;

        width = 20;
        height = 20;
        widthReal = 12;
        heightReal = 12;

        damage = 1;
        moveSpeed = 5;

        loadSprite = Content.DarkEnergy[0];
        sprites = Content.DarkEnergy[1];

        animation.setFrames(loadSprite);
        animation.setPause(2);

        start = true;
        isFlinched = true;
        permanent = true;
    }

    public void setType(int n) {
        type = n;
    }

    public void setPermanent(boolean bool) {
        permanent = bool;
    }

    public void update() {

        if (start) {
            if (animation.playedOnce()) {
                animation.setFrames(sprites);
                animation.setFrame(3);
                animation.setPause(2);
                start = false;
            }
        }

        if (type == vector) {
            // ambot
        }else  if (type == gravity) {
            dy += 0.2;
        }else if (type == bounce) {
            double dx2 = dx;
            double dy2 = dy;
            checkCollisionTile();
            if (dx == 0) {
                dx = -dx2;
            }
            if (dy == 0) {
                dy = -dy2;
            }
        }
        x += dx;
        y += dy;

        animation.update();

        if (!permanent) {
            if (x < 0 || x > tileMap.getWidth() || y < 0 || y > tileMap.getHeight()) {
                remove = true;
            }
            if (bounceCtr == 3) {
                remove = true;
            }
        }

    }

    public void draw(Graphics2D graphics2D) {
        super.draw(graphics2D);
    }
}
