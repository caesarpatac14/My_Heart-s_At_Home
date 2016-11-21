package SP.Game_Objects.For_Enemies;

/**
 * Created by acer on 10/14/2016.
 */

import SP.Controls.Content;
import SP.Game_Objects.Animation;
import SP.Game_Objects.Enemy;
import SP.Game_Objects.Player;
import SP.Main_Game.GameFrame;
import SP.Tile_Map.TileMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;

public class Ogre extends Enemy {

    private Player player;
    private boolean isActive;
    private BufferedImage[] spriteEnemy;

    public Ogre(TileMap tileMap, Player player) {

        super(tileMap);

        this.player = player;

        moveSpeed = 0.3;
        maxSpeed = 0.3;
        fallSpeed = 0.2;
        maxFallSpd = 10.0;

        width = 65;
        widthReal = 30;
        height = 66;
        heightReal = 30;

        hp = maxHP = 2;
        damage = 1;

        spriteEnemy = Content.OgreEnemy[0];

        animation = new Animation();
        animation.setFrames(spriteEnemy);
        animation.setPause(10);
        left = true;
        faceRight = false;
    }

    private void getNextPos() {
        if (left) {
            dx = -moveSpeed;
//
//            if (dx < -maxSpeed) {
//                dx = -maxSpeed;
//            }

        }else if (right) {
            dx = moveSpeed;

//            if (dx > maxSpeed) {
//                dx = maxSpeed;
//            }
        }

        //fall
        if (isFalling) {
            dy += fallSpeed;

            if(dy > maxFallSpd) {
                dy = maxFallSpd;
            }
        }
        if (isJumping && ! isFalling) {
            dy = jumpStrt;
        }
    }

    public void update() {

        if (!isActive) {
            if (Math.abs(player.getX() - x) < GameFrame.WIDTH) {
                isActive = true;
            }
        }

        // flinching
        if (isFlinched) {
            long used = (System.nanoTime() - timeFlinched) / 1000000;

            if (used > 400) {
                isFlinched = false;
            }
        }

        //pos
        getNextPos();
        checkCollisionTile();
        cornerSolve(x, destY + 1);

        if (!botLeft) {
            left = false;
            right = faceRight = true;
        }
        if (!botRight) {
            left = true;
            right = faceRight = false;
        }
        setPos(tempX, tempY);

        // hits wall
        if (dx == 0) {
            left = !left;
            right = !right;
            faceRight = !faceRight;
        }
//        if (right && dx == 0) {
//            right = false;
//            left = true;
//            faceRight = false;
//
//        }else if (left && dx == 0) {
//            right = true;
//            left = false;
//            faceRight = true;
//        }

        //update anmtion
        animation.update();
    }

    public void draw(Graphics2D g) {
//        setPosMap();
//        super.draw(g);
        if (isFlinched) {
            if (timeFlinched == 0 || timeFlinched == 2) {
                return;
            }
        }
        super.draw(g);
    }
}