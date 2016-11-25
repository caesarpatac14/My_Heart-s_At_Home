package SP.State_Of_Game;

/**
 * Created by acer on 10/14/2016.
 */

import SP.Controls.Controls;
import SP.Game_Objects.*;
import SP.Game_Objects.For_Enemies.JumpingEnemy;
import SP.Game_Objects.For_Enemies.Level1Boss;
import SP.Game_Objects.For_Enemies.Ogre;
import SP.Main_Game.GameFrame;
import SP.Music.MusicPlayer;
import SP.Tile_Map.Background;
import SP.Tile_Map.TileMap;

import java.awt.*;
import java.util.*;

public class FirstLvl extends GameState {

    private TileMap tileMap;
    private Background bg;
    private PlayerStuffs hud;
    private Player myPlayer;
    private ArrayList<Enemy> enemies;
    private ArrayList<Boom> boom;
    private Transfer transfer;

    //happenings
    private boolean startMoving;
    private boolean finishLevel;
    private boolean deadNa;
    private int countAct = 0;
    private boolean inBlock = false;
    private ArrayList<Rectangle> moveBox;

    //audio
    private MusicPlayer musicPlayer;

    public FirstLvl(ManageGS gsManager) {
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
        myPlayer.setJump(Controls.controlState[Controls.JUMP]);
        myPlayer.setRight(Controls.controlState[Controls.RIGHT]);
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
        tileMap.loadTiles("/SP/For_the_game/lvl1_obs8.png");
        tileMap.loadMap("/SP/For_the_game/ic.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);

        bg = new Background("/SP/For_the_game/jungle.jpg", 0.1);

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

        //transfer
        transfer = new Transfer(tileMap);
        transfer.setPos(4836, 266);

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

        if (transfer.collides(myPlayer)) {
//            System.out.println("dqwev");
            finishLevel = inBlock = true;
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

        // update myPlayer
        myPlayer.update();

        //update map (tiles)
        tileMap.setPosition(GameFrame.WIDTH / 2 - myPlayer.getX(), GameFrame.HEIGHT / 2 - myPlayer.getY());
        tileMap.update();
        tileMap.fixBounds();

        // BG
        bg.setPosition(tileMap.getX(), tileMap.getY());

        //atking
        myPlayer.checkAtk(enemies);

        //enemies
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);

            enemy.update();

            if (enemy.checkDead()) {
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
        transfer.update();
    }

    public void draw(Graphics2D g) {

        // clear
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT);

        // for background
        bg.draw(g);

        // draw tilemap
        tileMap.draw(g);

        // draw player
        myPlayer.draw(g);

        //draw transfer
        transfer.draw(g);

        //draw PlayerStuffs
        hud.draw(g);

        // is player atking
        myPlayer.checkAtk(enemies);

        // draw enemy
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        //draw boom
        for (int i = 0; i < boom.size(); i++) {
//            boom.get(i).setPosMap((int) tileMap.getI(), (int) tileMap.getJ());
            boom.get(i).draw(g);
        }

        //draw transition boxes
        for (int i = 0; i < moveBox.size(); i++) {
            g.fill(moveBox.get(i));
        }
    }

    private void putEnemies() {
        enemies = new ArrayList<Enemy>();

        Ogre ogre;
        Point[] points = new Point[] {
                new Point(200, 535),
                new Point(170, 535),
                new Point(370, 510),
                new Point(570, 460),
                new Point(720, 495),
                new Point(850, 395),
                new Point(920, 305),
                new Point(1130, 165),
                new Point(1230, 135),
                new Point(1480, 35),
                new Point(1680, 170),
                new Point(1845, 195),
                new Point(1830, 385),
                new Point(1820, 385),
                new Point(1890, 335),
                new Point(2000, 500),
                new Point(2200, 500),
                //          new Point(2800, 550),

                new Point(2550, 555),
                new Point(2760, 485),
                new Point(2940, 485),

                new Point(3038, 515),
                new Point(3139, 485),
                new Point(3228, 455),

                new Point(3569, 385),
                new Point(3660, 405),
                new Point(3766, 400),
                new Point(3927, 475),
                new Point(3990, 475),
                new Point(4127, 475),
                new Point(4177, 390),
                new Point(4202, 455),
                new Point(4245, 420),
                new Point(4395, 355),
                new Point(4476, 355),
                new Point(4617, 355),
                new Point(4715, 325),
                new Point(4777, 295),

           /*     new Point(4800, 350),
                new Point(5000, 350),
                new Point(5200, 350),*/

//                new Point(1090, 100),

//                new Point(1525, 185),
        };

        for (int i = 0; i < points.length; i++) {
            ogre = new Ogre(tileMap, myPlayer);
            ogre.setPos(points[i].x, points[i].y);
            enemies.add(ogre);
        }
    }


    /*
* THE GAME!!!
* */

    private void startMoving() {
        countAct++;
        if (countAct == 1) {
            moveBox.clear();
            moveBox.add(new Rectangle(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT / 2));
            moveBox.add(new Rectangle(0, 0, GameFrame.WIDTH / 2, GameFrame.HEIGHT));
            moveBox.add(new Rectangle(0, GameFrame.HEIGHT / 2, GameFrame.WIDTH, GameFrame.HEIGHT / 2));
            moveBox.add(new Rectangle(GameFrame.WIDTH / 2, 0, GameFrame.WIDTH / 2, GameFrame.HEIGHT));
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
            moveBox.clear();
        }
    }

    private void finishLevel() {
        countAct++;

        if (countAct == 1) {
            myPlayer.setTransferring(true);
            myPlayer.stopThis();
        }else if (countAct == 120) {
            moveBox.clear();
            moveBox.add(new Rectangle(GameFrame.WIDTH / 2, GameFrame.HEIGHT / 2, 0, 0));
        }else if (countAct > 120) {
            moveBox.get(0).x -= 8;
            moveBox.get(0).y -= 12;
            moveBox.get(0).width += 16;
            moveBox.get(0).height += 24;
        }
        if (countAct == 180) {
            SavePlayer.setHp(myPlayer.getHP());
            SavePlayer.setLife(myPlayer.getLife());
            SavePlayer.setTime(myPlayer.getTime());
            gsManager.setState(ManageGS.LEVEL2);
            musicPlayer.stop();
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
                musicPlayer.stop();
                gsManager.setState(ManageGS.LEVEL2);
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
        tileMap.setShaking(false, 0);
        startMoving = true;
        startMoving();
    }

}