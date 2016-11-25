package SP.Game_Objects.For_Enemies;

import SP.Game_Objects.Boom;
import SP.Game_Objects.BossPower;
import SP.Game_Objects.Enemy;
import SP.Game_Objects.Player;
import SP.Tile_Map.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by jcpatac on 11/18/2016.
 */

public class Level1Boss extends Enemy {

    public BufferedImage[] sprites;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Boom> explosions;

    private boolean active;
    private boolean finalAttack;

    private int step;
    private int stepCount;

    // attack pattern
    private int[] steps = {0, 1, 0, 1, 2, 1, 0, 2, 1, 2};

    ////attacks:
    // fly around throwing dark energy (0)
    // floor sweep (1)
    // crash down on floor to create shockwave (2)
    //// special:
    // after half hp, create shield
    // after quarter hp, bullet hell

    private BossPower[] shield;
    private double ticks;

    public Level1Boss(TileMap tm, Player p, ArrayList<Enemy> enemies, ArrayList<Boom> explosions) {

        super(tm);
        player = p;
        this.enemies = enemies;
        this.explosions = explosions;

        width = 40;
        height = 40;
        widthReal = 30;
        heightReal = 30;

        hp = maxHP = 80;

        moveSpeed = 1.4;

        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream("/SP/For_the_game/Spirit.gif")
            );
            sprites = new BufferedImage[4];
            for(int i = 0; i < sprites.length; i++) {
                sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        damage = 1;

        animation.setFrames(sprites);
        animation.setPause(1);

        shield = new BossPower[2];

        step = 0;
        stepCount = 0;

    }

    public void setActive() { active = true; }

    public void update() {

        if(hp == 0) return;

        // restart attack pattern
        if(step == steps.length) {
            step = 0;
        }

        ticks++;

        if(isFlinched) {
            timeFlinched++;
            if(timeFlinched == 8) isFlinched = false;
        }

        x += dx;
        y += dy;

        animation.update();

        if(!active) return;

        ////////////
        // special
        ////////////
        if(hp <= maxHP / 2) {
            if(shield[0] == null) {
                shield[0] = new BossPower(tileMap);
                shield[0].setPermanent(true);
                enemies.add(shield[0]);
            }
            if(shield[1] == null) {
                shield[1] = new BossPower(tileMap);
                shield[0].setPermanent(true);
                enemies.add(shield[1]);
            }
            double pos = ticks / 32;
            shield[0].setPos(x + 30 * Math.sin(pos), y + 30 * Math.cos(pos));
            pos += 3.1415;
            shield[1].setPos(x + 30 * Math.sin(pos), y + 30 * Math.cos(pos));
        }

        if(!finalAttack && hp <= maxHP / 4) {
            stepCount = 0;
            finalAttack = true;
        }

        if(finalAttack) {
            stepCount++;
            if(stepCount == 1) {
                explosions.add(new Boom(tileMap, (int)x, (int)y));
                x = -9000;
                y = 9000;
                dx = dy = 0;
            }
            if(stepCount == 60) {
                x = tileMap.getWidth() / 2;
                y = tileMap.getHeight() / 2;
                explosions.add(new Boom(tileMap, (int)x, (int)y));
            }
            if(stepCount >= 90 && stepCount % 30 == 0) {
                BossPower bossPower = new BossPower(tileMap);
                bossPower.setPos(x, y);
                bossPower.setDirection(3 * Math.sin(stepCount / 32), 3 * Math.cos(stepCount / 32));
                bossPower.setType(BossPower.bounce);
                enemies.add(bossPower);
            }
            return;
        }

        ////////////
        // attacks
        ////////////

        // fly around dropping bombs
        if(steps[step] == 0) {
            stepCount++;
            if(x > 1175) {
                dx = -4;
            }
            if(x < 1175) {
                dx = 0;
                x = 1175;
                dy = -1;
            }
            if(x == 1175) {
                if(dy == -1 && y < 470) {
                    dy = 1;
                }
                if(dy == 1 && y > 670) {
                    dy = -1;
                }
            }
            if(stepCount % 60 == 0) {
                BossPower bossPower = new BossPower(tileMap);
                bossPower.setType(BossPower.gravity);
                bossPower.setPos(x, y);
                int dir = Math.random() < 0.5 ? 1 : -1;
                bossPower.setDirection(0, dir);
                enemies.add(bossPower);
            }
            if(stepCount == 559) {
                step++;
                stepCount = 0;
                right = left = false;
            }
        }
        // floor sweep
        else if(steps[step] == 1) {
            stepCount++;
            if(stepCount == 1) {
//                explosions.add(new Boom(tileMap, (int)x, (int)y));
                x = -9000;
                y = 9000;
                dx = dy = 0;
            }
            if(stepCount == 60) {
                if(player.getX() > tileMap.getWidth() / 2) {
                    x = 30;
                    y = tileMap.getHeight() - 64;
                    dx = 4;
                }
                else {
                    x = tileMap.getWidth() - 32;
                    y = tileMap.getHeight() - 64;
                    dx = -4;
                }
//                explosions.add(new Boom(tileMap, (int)x, (int)y));
            }
            if((dx == -4 && x < 32) || (dx == 4 && x > tileMap.getWidth() - 32)) {
                stepCount = 0;
                step++;
                dx = dy = 0;
            }

        }
        // shockwave
        else if(steps[step] == 2) {
            stepCount++;
            if(stepCount == 1) {
                x = tileMap.getWidth() / 2;
                y = 40;
            }
            if(stepCount == 60) {
                dy = 7;
            }
            if(y >= tileMap.getHeight() - 64) {
                dy = 0;
            }
            if(stepCount > 60 && stepCount < 120 && stepCount % 5 == 0 && dy == 0) {
                BossPower bossPower = new BossPower(tileMap);
                bossPower.setPos(x, y);
                bossPower.setDirection(-3, 0);
                enemies.add(bossPower);
                bossPower = new BossPower(tileMap);
                bossPower.setPos(x, y);
                bossPower.setDirection(3, 0);
                enemies.add(bossPower);
            }
            if(stepCount == 120) {
                stepCount = 0;
                step++;
            }
        }

    }

    public void draw(Graphics2D g) {
        if(isFlinched) {
            if(timeFlinched % 4 < 2) return;
        }
        super.draw(g);
    }

}
