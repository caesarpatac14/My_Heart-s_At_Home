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

public class FourthLvl extends GameState {

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

    public FourthLvl(ManageGS gsManager) {
        super(gsManager);
        initialize();
    }

    @Override
    public void pressedKeys() {

        if(Controls.isPressed(Controls.PAUSE)) {
            gsManager.setPaused(true);
        }

        if (inBlock || player.getHP() == 0) {
            return;
        }

        player.setRight(Controls.controlState[Controls.RIGHT]);
        player.setLeft(Controls.controlState[Controls.LEFT]);
        player.setFlying(Controls.controlState[Controls.FLY]);
        player.setJump(Controls.controlState[Controls.JUMP]);

        if (Controls.isPressed(Controls.SHOOT)) {
            player.setShooting();
        }

        if (Controls.isPressed(Controls.ATK)) {
            player.setScratch();
        }

    }

    public void initialize() {
        tileMap = new TileMap(32);
        tileMap.loadTiles("/SP/For_the_game/lvl4.png");
        tileMap.loadMap("/SP/For_the_game/level4.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);

        background = new Background("/SP/For_the_game/lvl4bg.jpg", 0.1);

        //player
        player = new Player(tileMap);
        player.setPos(77, 700);
        player.setHp(SavePlayer.getHp());
        player.setLife(SavePlayer.getLife());
        player.setTime(SavePlayer.getTime());

        //life, power, etc.
        hud = new PlayerStuffs(player);

        musicPlayer = new MusicPlayer("/SP/For_the_game/level4.mp3");
        musicPlayer.play(true);

        //enemies
        enemies = new ArrayList<Enemy>();
        putEnemies();

        //explosion
        explode = new ArrayList<Boom>();

        //portal
        portal = new Transfer(tileMap);
        portal.setPos(7422, 2000);

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
        if (isDead) {
            deadNa();
        }
        if (lvlFinish) {
            finishLevel();
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

        //draw portal
        portal.draw(g);

        // is player atking
        player.checkAtk(enemies);

        //draw PlayerStuffs
        hud.draw(g);

        // draw enemy
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        //draw explode
        for (int i = 0; i < explode.size(); i++) {
//            explode.get(i).setPosMap((int) tileMap.getI(), (int) tileMap.getJ());
            explode.get(i).draw(g);
        }

        //draw transition boxes
        for (int i = 0; i < moveBox.size(); i++) {
            g.fill(moveBox.get(i));
        }
    }

    private void putEnemies() {
        enemies = new ArrayList<Enemy>();

        Vanky vanky;
        Point[] points = new Point[] {
                new Point (154, 1899),
                new Point(514, 1657),
                new Point(1222, 1434),
                new Point(2082, 1461),
                new Point(855, 1453),
                new Point(904, 1462),
                new Point(2765, 1417),
                new Point(3184, 1632),
                new Point(2863, 1460),
                new Point(1489, 1419),
                new Point(5036, 1148),
                new Point(6668, 873),
                new Point(7412, 16),
                new Point(4043, 1390),
                new Point(4068, 1393),
                new Point(7264, 25),
                new Point(4188, 1398),
                new Point(3854, 1394),
        };

        for (int i = 0; i < points.length; i++) {
            vanky = new Vanky(tileMap, player);
            vanky.setPos(points[i].x, points[i].y);
            enemies.add(vanky);
        }

        JumpingEnemy je;
        Point[] jes = new Point[] {
                new Point(258, 1745),
                new Point(326, 1585),
                new Point(407, 1623),
                new Point(367, 1621),

                new Point(628, 1652),
                new Point(779, 1659),

                new Point(1009, 1460),
                new Point(1141, 1475),

                new Point(1420, 1408),
                new Point(1537, 1415),

                new Point(1618, 1461),
                new Point(1760, 1484),
                new Point(1963, 1503),

                new Point(2207, 1413),
                new Point(2369, 1504),
                new Point(2696, 1533),

                new Point(2887, 1382),
                new Point(3026, 1386),
                new Point(3177, 1376),
                new Point(3249, 1388),

                new Point(2894, 1744),
                new Point(2777, 1726),
                new Point(3085, 1568),

                new Point(3335, 1629),
                new Point(3462, 1647),
                new Point(3579, 1584),
                new Point(3752, 1456),
                new Point(4237, 1396),
                new Point(4552, 1193),
                new Point(4491, 1211),
                new Point(4859, 1153),
                new Point(4803, 1159),

                new Point(5173, 1142),
                new Point(5300, 1136),
                new Point(5523, 1046),
                new Point(5872, 1045),
                new Point(6087, 1043),
                new Point(6039, 1056),
                new Point(6426, 911),
                new Point(6557, 918),
                new Point(6258, 842),

                new Point(6467, 400),
                new Point(6178, 378),
                new Point(6679, 361),
                new Point(6685, 465),
                new Point(6862, 259),
                new Point(7013, 199),
                new Point(7106, 246),
                new Point(7181, 59),
                new Point(7341, 19),
                new Point(7463, 17),
                new Point(7499, 1882),
                new Point(7481, 1880),
                new Point(7475, 1879),
                new Point(7447, 1879),
                new Point(7429, 1877),
        };
        for (int i = 0; i < jes.length; i++) {
            je = new JumpingEnemy(tileMap, enemies, player);
            je.setPos(jes[i].x, jes[i].y);
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
        }else if (eventCtr > 120) {
            moveBox.get(0).x -= 8;
            moveBox.get(0).y -= 12;
            moveBox.get(0).width += 16;
            moveBox.get(0).height += 24;
        }
        if (eventCtr == 180) {
            SavePlayer.setHp(player.getHP());
            SavePlayer.setLife(player.getLife());
            SavePlayer.setTime(player.getTime());
            gsManager.setState(ManageGS.MENUSTATE);
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
                gsManager.setState(ManageGS.MENUSTATE);
            }else {
                isDead = inBlock = false;
                eventCtr = 0;
                player.minusLife();
                reset();
            }
        }
    }

    private void reset() {
        player.reset();
        player.setPos(77, 1234);
        putEnemies();
        inBlock = true;
        eventCtr = 0;
        tileMap.setShaking(false, 0);
        start = true;
        startMoving();
    }

}