package SP.State_Of_Game;

/**
 * Created by acer on 10/14/2016.
 */

import SP.Controls.Controls;
import SP.Game_Objects.*;
import SP.Game_Objects.For_Enemies.Level1Boss;
import SP.Main_Game.GameFrame;
import SP.Music.MusicPlayer;
import SP.Tile_Map.Background;
import SP.Tile_Map.TileMap;

import java.awt.*;
import java.util.*;

public class BossLevel1 extends GameState {

    private TileMap tileMap;
    private Background bg;
    private PlayerStuffs hud;
    private Player myPlayer;
    private ArrayList<Enemy> enemies;
    private ArrayList<Boom> boom;
    private GamePortal portal;

    //happenings
    private boolean startMoving;
    private boolean finishLevel;
    private boolean deadNa;
    private int countAct = 0;
    private boolean inBlock = false;
    private ArrayList<Rectangle> moveBox;
    private boolean eventPortal;
    private boolean flashing;
    private boolean bossDefeated;

    //audio
    private MusicPlayer musicPlayer;

    private Level1Boss level1Boss;

    public BossLevel1(ManageGS gsManager) {
        super(gsManager);
        initialize();
    }

    @Override
    public void pressedKeys() {

        if(Controls.isPressed(Controls.PAUSE)) {
            gsManager.setPaused(true);
        }

        if (inBlock || myPlayer.getHP() == 0) {
            return;
        }

        myPlayer.setLeft(Controls.controlState[Controls.LEFT]);
        myPlayer.setRight(Controls.controlState[Controls.RIGHT]);
        myPlayer.setJump(Controls.controlState[Controls.JUMP]);
        myPlayer.setFlying(Controls.controlState[Controls.FLY]);

        if (Controls.isPressed(Controls.SHOOT)) {
            myPlayer.setShooting();
        }

        if (Controls.isPressed(Controls.ATK)) {
            myPlayer.setScratch();
        }

    }

    public void initialize() {
        tileMap = new TileMap(32);
        tileMap.loadTiles("/SP/For_the_game/bossmaptile.png");
        tileMap.loadMap("/SP/For_the_game/boss1.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);

        bg = new Background("/SP/For_the_game/krishia_back.png", 0.1);

        //player
        myPlayer = new Player(tileMap);
        myPlayer.setPos(100, 500);
        myPlayer.setHp(SavePlayer.getHp());
        myPlayer.setLife(SavePlayer.getLife());
        myPlayer.setTime(SavePlayer.getTime());

        //life, power, etc.
        hud = new PlayerStuffs(myPlayer);

        musicPlayer = new MusicPlayer("/SP/For_the_game/level1.mp3");
        musicPlayer.play(true);

        //enemies
        enemies = new ArrayList<Enemy>();
        putEnemies();

        //explosion
        boom = new ArrayList<Boom>();

        //portal
        portal = new GamePortal(tileMap);
        portal.setPos(160, 154);

        //events
        startMoving = true;
        moveBox = new ArrayList<Rectangle>();
        startMoving();

        //initialize the player
        myPlayer.initialize(enemies);

    }

    public void update() {

        //key pressed
        pressedKeys();

        if (!finishLevel && level1Boss.checkDead()) {
            bossDefeated = inBlock = true;
        }

        // check if should start if dead
        if (myPlayer.getHP() == 0 || (myPlayer.getY() > (tileMap.getHeight()))) {
            deadNa = inBlock = true;
        }

        //play events
        if (startMoving) {
//            System.out.println(tileMap.getXmax());
            startMoving();
        }
        if (deadNa) {
            deadNa();
        }
        if (finishLevel) {
            finishLevel();
        }
        if (eventPortal) {
            portalPoint();
        }
        if (bossDefeated) {
            bossDefeated();
        }

        // update myPlayer
        myPlayer.update();

        //update map (tiles)
        tileMap.setPosition(GameFrame.WIDTH / 2 - myPlayer.getX(), GameFrame.HEIGHT / 2 - myPlayer.getY());
        tileMap.update();
        tileMap.fixBounds();

        // BG
        bg.setPosition(tileMap.getX(), tileMap.getY());

        //atking
//        myPlayer.checkAtk(enemies);

        //enemies
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.update();
            if (enemy.checkDead() || enemy.mustRemove()) {
                enemies.remove(i);
                i--;
                boom.add(new Boom(tileMap, enemy.getX(), enemy.getY()));
            }
        }

        //boom
        for (int j = 0; j < boom.size(); j++) {
            boom.get(j).update();

            if (boom.get(j).mustRemoveInGame()) {
                boom.remove(j);
                j--;
            }
        }
        portal.update();
    }

    public void draw(Graphics2D g) {

        // clear
//        g.setColor(Color.WHITE);
//        g.fillRect(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT);

        // for background
        bg.draw(g);

        // draw tilemap
        tileMap.draw(g);

        //portal
        portal.draw(g);

        // is player atking
//        myPlayer.checkAtk(enemies);

        // draw enemy
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        //draw boom
        for (int i = 0; i < boom.size(); i++) {
//            boom.get(i).setPosMap((int) tileMap.getI(), (int) tileMap.getJ());
            boom.get(i).draw(g);
        }

        // draw player
        myPlayer.draw(g);

        //draw PlayerStuffs
        hud.draw(g);

        //draw transition boxes
        for (int i = 0; i < moveBox.size(); i++) {
            g.fill(moveBox.get(i));
        }

        //flash
        if (flashing) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT);
        }
    }

    private void putEnemies() {
        enemies.clear();
        level1Boss = new Level1Boss(tileMap, myPlayer, enemies, boom);
        level1Boss.setPos(1000, 500);
        enemies.add(level1Boss);
    }


    /*
* THE GAME!!!
* */

    private void portalPoint() {
        countAct++;
        if (countAct == 1) {
            if (portal.isOpened()) {
                countAct = 360;
            }
        }
        if (countAct > 60 && countAct < 180) {

        }
        if (countAct >= 160 && countAct <= 180) {
            if (countAct % 4 == 0 || countAct % 4 == 1) {
                flashing = true;
            }else {
                flashing = false;
            }
        }
        if (countAct == 181) {
            tileMap.setShaking(false, 0);
            flashing = false;
        }
        if (countAct == 300) {
            portal.setOpening();
        }
        if (countAct == 360) {
            flashing = true;
            level1Boss.setPos(160, 160);
            BossPower bossPower;
            for (int i = 0; i < 20; i++) {
                bossPower = new BossPower(tileMap);
                bossPower.setPos(160, 160);
                bossPower.setDirection(Math.random() * 10 - 5, Math.random() * -2 - 3);
                enemies.add(bossPower);
            }
        }
        if (countAct == 362) {
            flashing = false;
        }
        if (countAct == 420) {
            eventPortal = inBlock = false;
            countAct = 0;
            level1Boss.setActive();
        }
    }

    private void bossDefeated() {
        countAct++;
        if (countAct == 1) {
            myPlayer.stopThis();
            enemies.clear();
        }
        if (countAct <= 120 && countAct % 15 == 0) {
            boom.add(new Boom(tileMap, level1Boss.getX(), level1Boss.getY()));
        }
        if (countAct == 180) {

        }
        if (countAct == 390) {
            bossDefeated = false;
            countAct = 0;
            finishLevel = true;
        }
    }

    private void startMoving() {
        countAct++;
        if (countAct == 1) {
            moveBox.clear();
            moveBox.add(new Rectangle(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT / 2));
            moveBox.add(new Rectangle(0, 0, GameFrame.WIDTH / 2, GameFrame.HEIGHT));
            moveBox.add(new Rectangle(0, GameFrame.HEIGHT / 2, GameFrame.WIDTH, GameFrame.HEIGHT / 2));
            moveBox.add(new Rectangle(GameFrame.WIDTH / 2, 0, GameFrame.WIDTH / 2, GameFrame.HEIGHT));

            if (!portal.isOpened()) {
                tileMap.setShaking(true, 10);
            }
        }

        if (countAct > 1 && countAct < 60) {
            moveBox.get(0).height -= 8;
            moveBox.get(1).width -= 12;
            moveBox.get(2).y += 8;
            moveBox.get(3).x += 12;
        }

        if (countAct == 60) {
            startMoving = inBlock = false;
            countAct = 0;
            eventPortal = inBlock = true;
            moveBox.clear();
        }
    }

    private void finishLevel() {
        countAct++;

        if (countAct == 1) {
            moveBox.clear();
            moveBox.add(new Rectangle(GameFrame.WIDTH / 2, GameFrame.HEIGHT / 2, 0, 0));
        }else if (countAct > 1) {
            moveBox.get(0).x -= 8;
            moveBox.get(0).y -= 12;
            moveBox.get(0).width += 16;
            moveBox.get(0).height += 24;
        }
        if (countAct == 60) {
            SavePlayer.setHp(myPlayer.getHP());
            SavePlayer.setLife(myPlayer.getLife());
            SavePlayer.setTime(myPlayer.getTime());
            gsManager.setState(ManageGS.MENUSTATE);
        }
    }

    private void deadNa() {
        countAct++;

        if (countAct == 1) {
            myPlayer.dead();
            myPlayer.stopThis();
        }

        if (countAct == 60) {
            moveBox.clear();
            moveBox.add(new Rectangle(GameFrame.WIDTH / 2, GameFrame.HEIGHT / 2, 0, 0));
        }else if (countAct > 60) {
            moveBox.get(0).x -= 12;
            moveBox.get(0).y -= 8;
            moveBox.get(0).width += 24;
            moveBox.get(0).height += 16;
        }

        if (countAct >= 120) {
            if (myPlayer.getLife() == 0) {
                gsManager.setState(ManageGS.MENUSTATE);
            }else {
                deadNa = inBlock = false;
                countAct = 0;
                myPlayer.minusLife();
                reset();
            }
        }
    }

    private void reset() {
        myPlayer.reset();
        myPlayer.setPos(100, 500);
        putEnemies();
        inBlock = true;
        countAct = 0;
//        tileMap.setShaking(false, 0);
        startMoving = true;
        startMoving();
    }

}