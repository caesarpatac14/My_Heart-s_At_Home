package SP.Game_Objects.For_Enemies;

import SP.Controls.Content;
import SP.Game_Objects.BossPower;
import SP.Game_Objects.Enemy;
import SP.Game_Objects.Player;
import SP.Tile_Map.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by jcpatac on 11/25/2016.
 */
public class JumpingEnemy extends Enemy {

    private ArrayList<Enemy> enemies;
    private Player player;
    private BufferedImage[] stationary;
    private BufferedImage[] attack;
    private BufferedImage[] jumping;
    private boolean isJumping;
    public static final int STATIONARY = 0;
    public static final int JUMPING = 1;
    public static final int ATTACKING = 2;

    private int atkCtr;
    private int atkDelay = 30;
    private int pattern;

    public JumpingEnemy(TileMap tileMap, ArrayList<Enemy> enemies, Player player) {
        super(tileMap);
        this.enemies = enemies;
        this.player = player;

        hp = maxHP = 5;

        width = 30;
        height = 30;
        widthReal = 20;
        heightReal = 26;

        damage = 1;
        moveSpeed = 1.5;
        fallSpeed = 0.15;
        maxFallSpd = 4.0;
        jumpStrt = -5;

        stationary = Content.JumpingEnemy[0];
        jumping = Content.JumpingEnemy[1];
        attack = Content.JumpingEnemy[2];

        animation.setFrames(stationary);
        animation.setPause(-1);

        atkCtr = 0;
    }

    private void getNextPos() {
        if (left) {
            dx = -moveSpeed;
        }else if (right) {
            dx = moveSpeed;
        }else {
            dx = 0;
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

    @Override
    public void update() {
        if (isFlinched) {
            timeFlinched++;
            if (timeFlinched == 6) {
                isFlinched = false;
            }
        }
        getNextPos();
        checkCollisionTile();
        setPos(tempX, tempY);

        animation.update();

        if (player.getX() < x) {
            faceRight = false;
        }else {
            faceRight = true;
        }

        if (pattern == 0) {
            if (currAct != STATIONARY) {
                currAct = STATIONARY;
                animation.setFrames(stationary);
                animation.setPause(-1);
            }
            atkCtr++;
            if (atkCtr >= atkDelay && Math.abs(player.getX() - x) < 60) {
                pattern++;
                atkCtr = 0;
            }
        }
        if (pattern == 1) {
            if (currAct != JUMPING) {
                currAct = JUMPING;
                animation.setFrames(jumping);
                animation.setPause(-1);
            }
            isJumping = true;
            if (faceRight) {
                left = true;
            }else {
                right = true;
            }
            if (isFalling) {
                pattern++;
            }
        }
        if (pattern == 2) {
            if (dy > 0 && currAct != ATTACKING) {
                currAct = ATTACKING;
                animation.setFrames(attack);
                animation.setPause(3);
                BossPower bp = new BossPower(tileMap);
                bp.setPos(x, y);
                if (faceRight) {
                    bp.setDirection(3, 3);
                }else {
                    bp.setDirection(-3, 3);
                }
                enemies.add(bp);
            }
            if (currAct == ATTACKING && animation.playedOnce()) {
                pattern++;
                currAct = JUMPING;
                animation.setFrames(jumping);
                animation.setPause(-1);
            }
        }

        if (pattern == 3) {
            if (dy == 0) {
                pattern++;
            }
        }
        if (pattern == 4) {
            pattern = 0;
            left = right = isJumping = false;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (isFlinched) {
            if (timeFlinched == 0 || timeFlinched == 2) {
                return;
            }
        }
        super.draw(g);
    }
}
