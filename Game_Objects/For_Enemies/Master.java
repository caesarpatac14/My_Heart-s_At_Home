package SP.Game_Objects.For_Enemies;

import SP.Controls.Content;
import SP.Game_Objects.BossPower;
import SP.Game_Objects.Enemy;
import SP.Game_Objects.Player;
import SP.Game_Objects.PlayerPower;
import SP.Tile_Map.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jcpatac on 11/25/2016.
 */
public class Master extends Enemy {

    private BufferedImage[] idle;
    private BufferedImage[] moving;
    private BufferedImage[] attack;
    private BufferedImage[] dead;
    private BufferedImage[] fly;
    private BufferedImage[] jump;

    private boolean isActive;

    private ArrayList<Enemy> enemies;
    private Player player;
    private boolean isJumping;

    public static final int IDLE = 0;
    public static final int WALK = 1;
    public static final int JUMP = 2;
    public static final int FLY = 3;
    public static final int ATTACK = 4;
    public static final int DEAD = 5;

    private int attackCount;
    private int attackDelay = 10;
    private int steps;
    private int stepCount = 0;

    public Master(TileMap tileMap, ArrayList<Enemy> enemies, Player player) {
        super(tileMap);
        this.enemies = enemies;
        this.player = player;

        hp = maxHP = 100;

        width = 274;
        height = 166;
        widthReal = 120;
        heightReal = 100;

        damage = 1;
        moveSpeed = maxSpeed = 1.5;
        fallSpeed = 0.15;
        maxFallSpd = 4.0;
        jumpStrt = -5;

        fly = Content.BossEnemy[0];
        attack = Content.BossEnemy[1];
        moving = Content.BossEnemy[2];
        dead = Content.BossEnemy[3];
        idle = Content.BossEnemy[4];
        jump = Content.BossEnemy[5];

        animation.setFrames(idle);
        animation.setPause(-1);

        attackCount = 0;

        isJumping = false;
        isFalling = true;

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

    public void setActive() {
        isActive = true;
    }

    @Override
    public void update() {
        if (isFlinched) {
            timeFlinched++;
            if (timeFlinched == 5) {
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

        if (steps == 0) {
            stepCount++;
            if (currAct != IDLE) {
                currAct = IDLE;
                animation.setFrames(idle);
                animation.setPause(-1);
            }
//            if (stepCount % 60 == 0) {
//                if (currAct != ATTACK) {
//                    currAct = ATTACK;
//                    animation.setFrames(attack);
//                    animation.setPause(3);
//                    BossPower bossPower = new BossPower(tileMap);
//                    bossPower.setPos(x, y);
//                    if (faceRight) {
//                        bossPower.setDirection(4, 0);
//                    }else {
//                        bossPower.setDirection(-4, 0);
//                    }
//                    enemies.add(bossPower);
//                }
//            }
            attackCount++;
            if (attackCount >= attackDelay && Math.abs(player.getX() - x) < 60) {
                steps++;
                attackCount = 0;
            }
        }
        if (steps == 1) {
            if (currAct != JUMP) {
                currAct = JUMP;
                animation.setFrames(jump);
                animation.setPause(-1);
            }
            isJumping = true;
            if (faceRight) {
                left = true;
            }else {
                right = true;
            }
            if (isFalling) {
                steps++;
            }
        }
        if (steps == 2) {
            if (dy > 0 && currAct != ATTACK) {
                currAct = ATTACK;
                animation.setFrames(attack);
                animation.setPause(3);
                BossPower bossPower = new BossPower(tileMap);
                bossPower.setPos(x, y);
                if (faceRight) {
                    bossPower.setDirection(4, 1);
                }else {
                    bossPower.setDirection(-4, 1);
                }
                enemies.add(bossPower);
            }
            if (currAct == ATTACK && animation.playedOnce()) {
                steps++;
                currAct = JUMP;
                animation.setFrames(jump);
                animation.setPause(-1);
            }
        }
        if (steps == 3) {
            if (dy == 0) {
                steps++;
            }
        }
        if (steps == 4) {
            steps = 0;
            left = right = isJumping = false;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (isFlinched) {
            if (timeFlinched % 4 < 2) {
                isFlinched = false;
            }
        }
        super.draw(g);
    }
}
