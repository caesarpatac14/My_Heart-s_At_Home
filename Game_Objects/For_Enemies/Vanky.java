package SP.Game_Objects.For_Enemies;

import SP.Controls.Content;
import SP.Game_Objects.Animation;
import SP.Game_Objects.Enemy;
import SP.Game_Objects.Player;
import SP.Main_Game.GameFrame;
import SP.Tile_Map.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by jcpatac on 11/23/2016.
 */
public class Vanky extends Enemy {

    private BufferedImage[] vankySprite;
    private Player player;
    private boolean isActive;


    public Vanky(TileMap tileMap, Player player) {
        super(tileMap);
        this.player = player;

        moveSpeed = 0.5;
        maxSpeed = 0.8;
        fallSpeed = 0.5;
        maxFallSpd = 6.0;

        width = 64;
        height = 74;
        widthReal = 40;
        heightReal = 40;

        hp = maxHP = 5;
        damage = 1;

        vankySprite = Content.VankyEnemy[0];

        animation = new Animation();
        animation.setFrames(vankySprite);
        animation.setPause(8);
        left = false;
        faceRight = true;
    }

    private void getNextPos() {
        if (left) {
            dx = -moveSpeed;
        }else if (right) {
            dx = moveSpeed;
        }
        if (isFalling) {
            dy += fallSpeed;

            if (dy > maxFallSpd) {
                dy = maxFallSpd;
            }
        }
        if (isJumping && !isFalling) {
            dy = jumpStrt;
        }
    }

    public void update() {
        if (!isActive) {
            if (Math.abs(player.getX() - x) < GameFrame.WIDTH) {
                isActive = true;
            }
        }
        if (isFlinched) {
            long used = (System.nanoTime() - timeFlinched) / 1000000;
            if (used > 400) {
                isFlinched = false;
            }
        }

        getNextPos();
        checkCollisionTile();
        cornerSolve(x, destY + 1);

        if (!botLeft) {
            left = false;
            right = true;
            faceRight = true;
        }
        if (!botRight) {
            left = true;
            faceRight = right = false;
        }
        setPos(tempX, tempY);

        if (dx == 0) {
            left = !left;
            right = !right;
            faceRight = !faceRight;
        }
        animation.update();
    }

    public void draw(Graphics2D graphics2D) {
        if (isFlinched) {
            if (timeFlinched == 0 || timeFlinched == 2) {
                return;
            }
        }
        super.draw(graphics2D);
    }

}
