package SP.Game_Objects.For_Enemies;

import SP.Controls.Content;
import SP.Game_Objects.*;
import SP.Tile_Map.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jcpatac on 11/24/2016.
 */
public class Boss extends Enemy {

    public BufferedImage[] sprites;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Boom> booms;

    public static final int JUMPING = 0;

    private boolean isActive;
    private boolean finalAtk;

    private int step;
    private int stepCount;

    //pattern
    private int[] steps = {0, 1, 0, 1, 2, 1, 0, 2, 1, 2};

    private BossPower[] shield;
    private double ticks;

    public Boss(TileMap tileMap, Player player, ArrayList<Enemy> enemies, ArrayList<Boom> booms) {
        super(tileMap);
        this.player = player;
        this.enemies = enemies;
        this.booms = booms;

        width = 73;
        height = 86;
        widthReal = 50;
        heightReal = 60;

        jumpStrt = -5.0;
        jumpStp = 0.5;
        fallSpeed = 0.20;
        maxFallSpd = 4.5;

        hp = maxHP = 100;

        moveSpeed = 2.0;

        faceRight = false;
        left = true;

        sprites = Content.BossEnemy[0];

        damage = 1;
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setPause(1);
        shield = new BossPower[2];

        step = stepCount = 0;
    }

    private void getNextPos() {
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

    public void update() {

        if (hp == 0) {
            return;
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
        setPos(tempX, tempY);

        if (dx == 127) {
            left = !left;
            right = !right;
            faceRight = !faceRight;
        }

        if (step == steps.length) {
            step = 0;
        }

        ticks++;

        if (isFlinched) {
            timeFlinched++;
            if (timeFlinched == 8) {
                isFlinched = false;
            }
        }

//        x += dx;
//        y += dy;

        /*
        movements
         */

        if (steps[step] == 0) {
            stepCount++;
            Random random = new Random();
            int rand = random.nextInt(300);
            if (rand == 50) {
//                dy = jumpStrt;
//                isFalling = true;
                if (currAct != JUMPING) {
                    currAct = JUMPING;
                }
                isJumping = true;
                isFalling = true;
            }
            else if (rand == 20) {
                if (left) { // FIXME must set facing (how?)
                    setDirection(-8, 0);
                    if (x == 420) {
                        dx = 0;
                    }
//                    if (x < 420) {
//                        x = 420;
//                        faceRight = true;
//                        left = false;
//                    }
                }else if (faceRight) {
                    setDirection(8, 0);
                    if (x == 1189) {
                        dx = 0;
                    }
//                    if (x > 1188) {
////                        setPos(1188, 500);
//                        x = 1188;
//                        faceRight = false;
//                        left = true;
//                    }

                }
            }
            if (player.getX() > x) {
                faceRight = true;
                left = false;
            }
            if (player.getX() < x) {
                faceRight = false;
                left = true;
            }
            if (isFalling) {
                int stop = random.nextInt(30);
                if (stop == 10) {
                    isJumping = false;
                }
//                dy += jumpStp;
                if (dy > maxFallSpd) {
                    dy = maxFallSpd;
                }
            }
            if (stepCount % 60 == 0) {
                BossPower bossPower = new BossPower(tileMap);
                bossPower.setPos(x, y);
                if (left) {
                    bossPower.setDirection(-4, 0);
                }else if (faceRight) {
                    bossPower.setDirection(4, 0);
                }
                int fire = random.nextInt(5);
                if (fire == 2) {
                    enemies.add(bossPower);
                }
            }
            if (step == 559) {
                step++;
                stepCount = 0;
                left = right = false;
            }
        }
        animation.update();
    }

    public void draw(Graphics2D graphics2D) {
        if (isFlinched) {
            if (timeFlinched % 4 < 2) {
                return;
            }
        }
        super.draw(graphics2D);
    }
}
