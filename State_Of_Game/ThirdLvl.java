package SP.State_Of_Game;

/**
 * Created by acer on 10/14/2016.
 */

import SP.Controls.Controls;
import SP.Game_Objects.*;
import SP.Game_Objects.For_Enemies.JumpingEnemy;
import SP.Game_Objects.For_Enemies.Ogre;
import SP.Game_Objects.For_Enemies.Vanky;
import SP.Main_Game.GameFrame;
import SP.Music.MusicPlayer;
import SP.Tile_Map.Background;
import SP.Tile_Map.TileMap;

import java.awt.*;
import java.util.*;

public class ThirdLvl extends GameState {

    private Background background;
    private TileMap tileMap;
    private PlayerStuffs hud;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Boom> explode;
    private Transfer portal;

    //happenings
    private boolean start;
    private boolean lvlFinish;
    private boolean isDead;
    private int eventCtr = 0;
    private boolean inBlock = false;
    private ArrayList<Rectangle> moveBox;

    //audio
    private MusicPlayer musicPlayer;

    public ThirdLvl(ManageGS gsManager) {
        super(gsManager);
        initialize();
    }

    @Override
    public void pressedKeys() {

        if (inBlock || player.getHP() == 0) {
            return;
        }

        if(Controls.isPressed(Controls.PAUSE)) {
            gsManager.setPaused(true);
        }

        player.setLeft(Controls.controlState[Controls.LEFT]);
        player.setRight(Controls.controlState[Controls.RIGHT]);
        player.setJump(Controls.controlState[Controls.JUMP]);
        player.setFlying(Controls.controlState[Controls.FLY]);

        if (Controls.isPressed(Controls.SHOOT)) {
            player.setShooting();
        }

        if (Controls.isPressed(Controls.ATK)) {
            player.setScratch();
        }

    }

    public void initialize() {
        tileMap = new TileMap(32);
        tileMap.loadTiles("/SP/For_the_game/lvl3_3.png");
        tileMap.loadMap("/SP/For_the_game/level3.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);

        background = new Background("/SP/For_the_game/lvl3bg.gif", 0.1);

        //player
        player = new Player(tileMap);
        player.setPos(62, 290);
        player.setHp(SavePlayer.getHp());
        player.setLife(SavePlayer.getLife());
        player.setTime(SavePlayer.getTime());

        //life, power, etc.
        hud = new PlayerStuffs(player);

        musicPlayer = new MusicPlayer("/SP/For_the_game/Volcano Theme.mp3");
        musicPlayer.play(true);
        musicPlayer = new MusicPlayer("/SP/For_the_game/flame.mp3");
        musicPlayer.play(true);

        //enemies
        enemies = new ArrayList<Enemy>();
        putEnemies();

        //explosion
        explode = new ArrayList<Boom>();

        //portal
        portal = new Transfer(tileMap);
        portal.setPos(5637, 260);

        //events
        start = true;
        moveBox = new ArrayList<Rectangle>();
        startMoving();

        //initialize the player
        player.initialize(enemies);

    }

    public void update() {

        //key pressed
        pressedKeys();

        if (portal.collides(player)) {
//            System.out.println("dqwev");
            lvlFinish = true;
            inBlock = true;
        }

        // check if should start if dead
        if (player.getHP() == 0 || (player.getY() > (tileMap.getHeight()))) {
            isDead = true;
            inBlock = true;
        }

        //play events
        if (start) {
//            System.out.println(tileMap.getXmax());
            startMoving();
        }
        if (lvlFinish) {
            finishLevel();
        }
        if (isDead) {
            deadNa();
        }

        // update player
        player.update();

        //update map (tiles)
        tileMap.setPosition(GameFrame.WIDTH / 2 - player.getX(), GameFrame.HEIGHT / 2 - player.getY());
        tileMap.update();
        tileMap.fixBounds();

        // BG
        background.setPosition(tileMap.getX(), tileMap.getY());

        //atking
        player.checkAtk(enemies);

        //enemies
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);

            enemy.update();

            if (enemy.checkDead()) {
                enemies.remove(i);
                i--;
                explode.add(new Boom(tileMap, enemy.getX(), enemy.getY()));
            }
        }

        //explode
        for (int j = 0; j < explode.size(); j++) {
            explode.get(j).update();

            if (explode.get(j).mustRemoveInGame()) {
                explode.remove(j);
                j--;
            }
        }
        portal.update();
    }

    public void draw(Graphics2D g) {

        // clear
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT);

        // for background
        background.draw(g);

        // draw tilemap
        tileMap.draw(g);

        // draw player
        player.draw(g);

        // is player atking
        player.checkAtk(enemies);

        //draw portal
        portal.draw(g);

        //draw PlayerStuffs
        hud.draw(g);

        // draw enemy
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        //draw transition boxes
        for (int i = 0; i < moveBox.size(); i++) {
            g.fill(moveBox.get(i));
        }

        //draw explode
        for (int i = 0; i < explode.size(); i++) {
//            explode.get(i).setPosMap((int) tileMap.getI(), (int) tileMap.getJ());
            explode.get(i).draw(g);
        }
    }

    private void putEnemies() {
        enemies = new ArrayList<Enemy>();

        Ogre ogre;
        Point[] points = new Point[] {
                new Point(272, 613),
                new Point(419, 624),
                new Point(1330, 620),
                new Point(527, 536),
                new Point(2135, 390),
                new Point(1576, 473),
                new Point(765, 609),
                new Point(704, 497),
                new Point(2606, 307),
                new Point(1136, 533),
                new Point(977, 629),
                new Point(1237, 572),
                new Point(2913, 683),
                new Point(2615, 488),
                new Point(2930, 683),
        };

        for (int i = 0; i < points.length; i++) {
            ogre = new Ogre(tileMap, player);
            ogre.setPos(points[i].x, points[i].y);
            enemies.add(ogre);
        }
        Vanky vanky;
        Point[] vankies = new Point[] {
                new Point(629, 606),
                new Point(924, 615),
                new Point(1070, 510),
                new Point(1195, 564),
                new Point(1294, 600),
                new Point(1354, 527),
                new Point(1508, 471),
                new Point(1724, 426),
                new Point(1875, 333),
                new Point(1053, 463),
                new Point(2188, 279),
                new Point(2374, 122),
                new Point(2410, 952),
                new Point(2638, 276),
                new Point(2752, 249),
                new Point(3564, 670),
                new Point(3698, 648),
                new Point(4650, 310),
                new Point(3860, 648),
                new Point(2676, 676),
                new Point(5113, 283),
                new Point(4060, 334),
                new Point(3239, 495),
                new Point(2809, 675),
                new Point(3072, 648),
                new Point(3510, 404),
                new Point(2527, 681),
                new Point(2913, 683),
                new Point(4816, 334),
                new Point(4390, 160),
        };
        for (int i = 0; i < vankies.length; i++) {
            vanky = new Vanky(tileMap, player);
            vanky.setPos(vankies[i].x, vankies[i].y);
            enemies.add(vanky);
        }
        JumpingEnemy je;
        Point[] jumpingE = new Point[] {
                new Point(3322, 462),
                new Point(3721, 648),
                new Point(4506, 233),
                new Point(5020, 334),
                new Point(5136, 334),
                new Point(5358, 350),
                new Point(5513, 329),
                new Point(5598, 280)
        };
        for (int i = 0; i < jumpingE.length; i++) {
            je = new JumpingEnemy(tileMap, enemies, player);
            je.setPos(jumpingE[i].x, jumpingE[i].y);
            enemies.add(je);
        }
    }


    /*
* THE GAME!!!
* */

    private void startMoving() {
        eventCtr++;
        if (eventCtr == 1) {
//            System.out.println("na clear?");
            moveBox.clear();
            moveBox.add(new Rectangle(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT / 2));
            moveBox.add(new Rectangle(0, 0, GameFrame.WIDTH / 2, GameFrame.HEIGHT));
            moveBox.add(new Rectangle(0, GameFrame.HEIGHT / 2, GameFrame.WIDTH, GameFrame.HEIGHT / 2));
            moveBox.add(new Rectangle(GameFrame.WIDTH / 2, 0, GameFrame.WIDTH / 2, GameFrame.HEIGHT));
        }

        if (eventCtr > 1 && eventCtr < 60) {
            moveBox.get(0).height -= 8;
            moveBox.get(1).width -= 12;
            moveBox.get(2).y += 8;
            moveBox.get(3).x += 12;
        }

        if (eventCtr == 60) {
            start = inBlock = false;
            eventCtr = 0;
            moveBox.clear();
        }
    }

    private void finishLevel() {
        eventCtr++;

        if (eventCtr == 1) {
            player.setTransferring(true);
            player.stopThis();
        }else if (eventCtr == 120) {
            moveBox.clear();
            moveBox.add(new Rectangle(GameFrame.WIDTH / 2, GameFrame.HEIGHT / 2, 0, 0));
        }else if (eventCtr >= 121) {
            moveBox.get(0).x -= 8;
            moveBox.get(0).y -= 12;
            moveBox.get(0).width += 16;
            moveBox.get(0).height += 24;
        }
        if (eventCtr == 180) {
            SavePlayer.setLife(player.getLife());
            SavePlayer.setTime(player.getTime());
            SavePlayer.setHp(player.getHP());
            gsManager.setState(ManageGS.LEVEL4);
            musicPlayer.stop();
        }
    }

    private void deadNa() {
        eventCtr++;

        if (eventCtr == 1) {
            player.dead();
            player.stopThis();
        }

        if (eventCtr == 60) {
            moveBox.clear();
            moveBox.add(new Rectangle(GameFrame.WIDTH / 2, GameFrame.HEIGHT / 2, 0, 0));
        }else if (eventCtr > 60) {
            moveBox.get(0).x -= 12;
            moveBox.get(0).y -= 8;
            moveBox.get(0).width += 24;
            moveBox.get(0).height += 16;
        }

        if (eventCtr >= 120) {
            if (player.getLife() == 0) {
                musicPlayer.stop();
                gsManager.setState(ManageGS.GAMEOVER);
            }else {
                isDead = inBlock = false;
                player.minusLife();
                eventCtr = 0;
                reset();
            }
        }
    }

    private void reset() {
        player.reset();
        player.setPos(62, 290);
        putEnemies();
        eventCtr = 0;
        inBlock = true;
        tileMap.setShaking(false, 0);
        start = true;
        startMoving();
    }

}