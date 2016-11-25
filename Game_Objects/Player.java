package SP.Game_Objects;

/**
 * Created by acer on 10/14/2016.
 */

import SP.Music.MusicPlayer;
import SP.Tile_Map.TileMap;

import java.util.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;

public class Player extends StuffsInMap {

    private ArrayList<Enemy> enemies;

    // for player info
    private int life;
    private int hp;
    private int maxHP;
    private int power;
    private int maxPow;
    private long timeFlinch;
    private boolean isAlive;
    private boolean flinch;

    // for power atk
    private int shootCost;
    private int shootDamage;
    private boolean shooting;
    private long time;
    private ArrayList<PlayerPower> playerPowers;

    // for physical atk
    private int damageScratch;
    private int rangeScratch;
    private boolean scratch;

    // for fly
    private boolean flying;

    //acts
    private boolean transferring;
    private boolean falling;

    // for animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] NUM_OF_FRAMES = {1, 9, 7, 1, 5, 6, 6};
    private final int[] WIDTH_OF_FRAMES = {42, 42, 42, 42, 42, 42, 84};
    private final int[] HEIGHT_OF_FRAMES = {58, 58, 58, 58, 58, 58, 58};
    private final int[] DELAY_OF_SPRITES = {-1, 3, 6, 5, 2, 2, 4};

    // for player movement
    private static final int STATIONARY = 0;
    private static final int MOVING = 1;
    private static final int JUMP = 2;
    private static final int FALL = 3;
    private static final int FLY = 5;
    private static final int POWER = 4;
    private static final int SCRATCH = 6;

    private HashMap<String, MusicPlayer> soundEffects;

    // game methods
    public Player(TileMap tileMap) {
        super(tileMap);

        width = 42;
        height = 58;
        widthReal = 18;
        heightReal = 50;

        moveSpeed = 1.6;
        maxSpeed = 1.6;
        stop = 1.35;
        fallSpeed = 0.15;
        maxFallSpd = 4.0;
        jumpStrt = -4.9;
        jumpStp = 0.4;
        faceRight = true;

        life = 3;
        hp = maxHP = 5;
        power = maxPow = 1500;

        shootCost = 200;
        shootDamage = 5;
        playerPowers = new ArrayList<PlayerPower>();

        damageScratch = 3;
        rangeScratch = 60;

        // load sprites
        try {

            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/SP/For_the_game/ThanksGod.png"));
            sprites = new ArrayList<BufferedImage[]>();

            for (int i = 0; i < 7; i++) {

                BufferedImage[] image = new BufferedImage[NUM_OF_FRAMES[i]];

                for (int j = 0; j < NUM_OF_FRAMES[i]; j++) {

                    if (i != SCRATCH) {
                        image[j] = spriteSheet.getSubimage(j * width, i * height, width, height);
                    }else {
                        image[j] = spriteSheet.getSubimage(j * width * 2, i * height, width * 2, height);
                    }

                }

                sprites.add(image);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

//        animation = new Animation();
//        currAct = STATIONARY;
//        animation.setFrames(sprites.get(STATIONARY));
//        animation.setPause(100);

        animationSetting(STATIONARY);

        soundEffects = new HashMap<String, MusicPlayer>();
        soundEffects.put("jump", new MusicPlayer("/SP/For_the_game/playerjump.mp3"));
        soundEffects.put("attack", new MusicPlayer("/SP/For_the_game/swordslash.mp3"));
        soundEffects.put("power", new MusicPlayer("/SP/For_the_game/playerpower.mp3"));

    }

    private void animationSetting(int n) {
        currAct = n;
        animation.setFrames(sprites.get(currAct));
        animation.setPause(DELAY_OF_SPRITES[currAct]);
        width = WIDTH_OF_FRAMES[currAct];
        height = HEIGHT_OF_FRAMES[currAct];
    }

    public void initialize(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public void dead() {
        hp = 0;
        stopThis();
    }

    public String timeToString() {
        int mins = (int)(time / 3600);
        int sec = (int)((time % 3600) / 60);
        return sec < 10 ? mins + ":0" + sec : mins + ":" + sec;
    }

    public void setTransferring(boolean transferring) {
        this.transferring = transferring;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void stopThis() {
        left = right = up = down = flinch = isJumping = flying = scratch = shooting = false;
    }

    public void reset() {
        hp = maxHP;
        currAct = -1;
        faceRight = true;
        stopThis();
    }

    public int getLife() {
        return life;
    }

    public void minusLife() {
        life--;
    }

    public void plusLife() {
        life++;
    }

//    public void setTransfering(boolean transfering) {
//        this.transfering = transfering;
//    }

    public int getHP() {
        return hp;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getPower() {
        return power;
    }

    public int getMaxPow() {
        return maxPow;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setShooting() {
        shooting = true;
    }

    public void setScratch() {
        scratch = true;
    }

    public void setFlying(boolean flying) {
        this.flying = flying;
    }

    public void checkAtk(ArrayList<Enemy> enemies) {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            // scratch attack
            if (scratch) {
                if (faceRight) {
                    if (enemy.getX() > x && enemy.getX() < x + rangeScratch && enemy.getY() > y - height / 2 && enemy.getY() < y + height / 2) {
                        enemy.hit(damageScratch);
                    }
                }else {
                    if (enemy.getX() < x && enemy.getX() > x - rangeScratch && enemy.getY() > y - height / 2 && enemy.getY() < y + height / 2) {
                        enemy.hit(damageScratch);
                    }
                }
            }
            for (int j = 0; j < playerPowers.size(); j++) {
                if (playerPowers.get(j).collides(enemy)) {
                    enemy.hit(shootDamage);
                    playerPowers.get(j).setPowerHit();
                    break;
                }
            }
            // check if hit by enemy
            if (collides(enemy)) {
                hit(enemy.getDmg());
            }
        }
    }

    public void hit(int damage) {
        if (flinch) {
            return;
        }
        stopThis();
        hp -= damage;
        if (hp < 0) {
            hp = 0;
//            gameOver();
        }
        flinch = true;
        timeFlinch = 0;
        if (faceRight) {
            dx = 1;
            dy = -3;
        }
    }

    private void getNextPos() {
        // move left or right
        if (left) {
            dx -= moveSpeed;

            if (dx < -maxSpeed) {
                dx = -maxSpeed;
            }

        }else if (right) {
            dx += moveSpeed;

            if (dx > maxSpeed) {
                dx = maxSpeed;
            }

        }else {
            if (dx > 0) {
                dx -= stop;

                if (dx < 0) {
                    dx = 0;
                }
            }else if (dx < 0) {
                dx += stop;

                if (dx > 0) {
                    dx = 0;
                }
            }
        }

        // can't move while in atk mode (unless in air)
        if ((currAct == SCRATCH || currAct == POWER) && !(isJumping || isFalling)) {
            dx = 0;
        }

        // jumping stuffs
        if (isJumping && !isFalling) {
            soundEffects.get("jump").play(false);
            dy = jumpStrt;
            isFalling = true;
        }

        // falling stuffs
        if (isFalling) {

            if (dy > 0 && flying) {
                dy += fallSpeed * 0.1;
            }else {
                dy += fallSpeed;
            }

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
    }

    public void update() {
        time++;

        if (transferring) {

        }

        // update position
        getNextPos();
        checkCollisionTile();
        setPos(tempX, tempY);

        // stop attacking
//        boolean isFalling = falling;
        if (currAct == SCRATCH) {
            if (animation.playedOnce()) {
                scratch = false;
            }
        }

        if (currAct == POWER) {
            if(animation.playedOnce()) {
                shooting = false;
            }
        }

        //power
        power++;

        if (power > maxPow) {
            power = maxPow;
        }

        if (shooting && currAct != POWER) {
            if (power > shootCost) {
                power -= shootCost;
                PlayerPower playerPower = new PlayerPower(tileMap, faceRight);
                playerPower.setPos(x, y);
                playerPowers.add(playerPower);
            }
        }

        //update atk
        for (int i = 0; i < playerPowers.size(); i++) {
            playerPowers.get(i).update();

            if (playerPowers.get(i).mustPowerRemove()) {
                playerPowers.remove(i);
                i--;
            }
        }

        // isFlinched stop
        if (flinch) {
            timeFlinch++;
            if (timeFlinch > 120) {
                flinch = false;
            }
        }

        // set animate
        if (transferring) {
            if (currAct != STATIONARY) {
                animationSetting(STATIONARY);
            }
        }

        if (shooting) {
            if (currAct != POWER) {
                soundEffects.get("power").play(false);
//                currAct = POWER;
//                animation.setFrames(sprites.get(POWER));
//                animation.setPause(100);
                animationSetting(POWER);
//                width = 30;
            }
        }else if (scratch) {
            if (currAct != SCRATCH) {
                soundEffects.get("attack").play(false);
//                currAct = SCRATCH;
//                animation.setFrames(sprites.get(SCRATCH));
//                animation.setPause(50);
//                width = 60;
                animationSetting(SCRATCH);
            }
        }else if(dy > 0) {
            if (flying) {
                if (currAct != FLY) {
//                    currAct = FLY;
//                    animation.setFrames(sprites.get(FLY));
//                    animation.setPause(100);
//                    width = 30;
                    animationSetting(FLY);
                }
            }else if (currAct != FALL) {
//                currAct = FALL;
//                animation.setFrames(sprites.get(FALL));
//                animation.setPause(100);
//                width = 30;
                animationSetting(FALL);
            }
        }else if (dy < 0) {
            if(currAct != JUMP) {
//                currAct = JUMP;
//                animation.setFrames(sprites.get(JUMP));
//                animation.setPause(-1);
//                width = 30;
                animationSetting(JUMP);
            }
        }else if (left || right) {
            if (currAct != MOVING) {
//                currAct = MOVING;
//                animation.setFrames(sprites.get(MOVING));
//                animation.setPause(40);
//                width = 30;
                animationSetting(MOVING);
            }
        }else {
            if (currAct != STATIONARY) {
//                currAct = STATIONARY;
//                animation.setFrames(sprites.get(STATIONARY));
//                animation.setPause(400);
//                width = 30;
                animationSetting(STATIONARY);
            }
        }
        animation.update();

        // set facing
        if (currAct != SCRATCH && currAct != POWER) {
            if (right) {
                faceRight = true;
            }
            if (left) {
                faceRight = false;
            }
        }
    }

    public void draw(Graphics2D g) {
        setPosMap();

        //draw power
        for (int i = 0; i < playerPowers.size(); i++) {
            playerPowers.get(i).draw(g);
        }

        // draw character
        if (flinch) {
            if (timeFlinch % 10 < 5) {
                return;
            }
        }
        super.draw(g);
    }
}