package SP.Game_Objects.For_Enemies;

import SP.Game_Objects.Boom;
import SP.Game_Objects.BossPower;
import SP.Game_Objects.Enemy;
import SP.Game_Objects.Player;
import SP.Main_Game.GameFrame;
import SP.Tile_Map.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jcpatac on 11/25/2016.
 */
public class Master extends Enemy {

    public BufferedImage[] master;
    private Player player;
    private ArrayList<Boom> booms;
    private ArrayList<Enemy> enemies;

    private int[] patterns = {0, 1, 0, 1, 2, 1, 0, 2, 1, 2};

    private boolean bestAtk;
    private boolean isActive;

    private int patternCount;
    private int pattern;

    private double ticks;

    public Master(TileMap tileMap, Player player, ArrayList<Boom> booms, ArrayList<Enemy> enemies) {
        super(tileMap);
        this.player = player;
        this.booms = booms;
        this.enemies = enemies;

        width = 40;
        height = 40;
        widthReal = 30;
        heightReal = 40;

        hp = maxHP = 100;
        fallSpeed = 0.15;
        maxFallSpd = 4.0;
        jumpStrt = -4.9;
        jumpStp = 0.4;

        moveSpeed = 1.5;

        try {
            BufferedImage sprite = ImageIO.read(getClass().getResourceAsStream("/SP/For_the_game/Spirit.gif"));
            master = new BufferedImage[4];
            for (int i = 0; i < master.length; i++) {
                master[i] = sprite.getSubimage(i * width, 0, width, height);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        damage = 1;
        animation.setFrames(master);
        animation.setPause(1);
        pattern = 0;
        patternCount = 0;
    }

    public void setActive() {
        isActive = true;
    }

    public void update() {
        System.out.println(hp);
        if (hp == 0) {
            return;
        }

        if (pattern == patterns.length) {
            pattern = 0;
        }

        ticks++;

        if (isFlinched) {
            long used = (System.nanoTime() - timeFlinched) / 1000000;

            if (used > 400) {
                isFlinched = false;
            }
        }

        x += dx;
        y += dy;

        if (!isActive) {
            return;
        }

        animation.update();

        player.checkAtk(enemies);

        checkCollisionTile();
        cornerSolve(x, y);

        if (player.getX() > x) {
            faceRight = true;
        }else {
            faceRight = false;
        }

        if (patterns[pattern] == 0) {
            patternCount++;

            if (patternCount == 310 || patternCount == 490 || patternCount == 90) {
                BossPower bp = new BossPower(tileMap);
                bp.setPos(x, y);
                if (faceRight) {
                    bp.setDirection(2, 0);
                }else {
                    bp.setDirection(-2, 0);
                }
                enemies.add(bp);
            }
            if (patternCount == 90 || patternCount == 410) {
                dy += jumpStrt;
                isJumping = true;
                isFalling = true;
            }
            if (isFalling) {
                dy += fallSpeed;
                if (dy > 0) {
                    isJumping = false;
                }
                if (dy < 0 && !isJumping) {
                    dy += jumpStp;
                }
                if (dy > maxFallSpd) {
                    dy = maxFallSpd;
                }
            }
            if (patternCount == 650) {
                patternCount = 0;
                pattern++;
            }
        }
        if (patterns[pattern] == 1) {
            patternCount++;
            if (patternCount == 10) {
                if (!faceRight) {
                    setDirection(-6, 0);
                    if (x == 80) {
                        dx = 0;
                    }
                }else {
                    setDirection(6, 0);
                    if (x == 1195) {
                        dx = 0;
                    }
                }
            }
            if (patternCount == 100) {
                patternCount = 0;
                pattern++;
            }
        }
        if (patterns[pattern] == 2) {
            patternCount++;
            if (patternCount == 9) {
                x = tileMap.getWidth() / 2;
                y = 40;
            }
            if (patternCount == 60) {
                dy = 7;
            }
            if (y >= tileMap.getHeight() - 96) {
                dy = 0;
            }
            if (patternCount > 60 && patternCount < 120 && patternCount % 5 == 0 && dy == 0) {
                BossPower bp = new BossPower(tileMap);
                bp.setPos(x, y);
                bp.setDirection(-4, 0);
                enemies.add(bp);
                bp = new BossPower(tileMap);
                bp.setPos(x, y);
                bp.setDirection(4, 0);
                enemies.add(bp);
            }
            if (patternCount == 150) {
                patternCount = 0;
                pattern++;
            }
        }
    }

    public void draw(Graphics2D g) {
        if (isFlinched) {
            if (timeFlinched % 4 < 2) {
                return;
            }
        }
        super.draw(g);
    }
}
