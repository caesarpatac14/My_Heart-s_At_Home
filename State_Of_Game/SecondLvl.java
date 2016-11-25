package SP.State_Of_Game;

import SP.Controls.Controls;
import SP.Game_Objects.*;
import SP.Game_Objects.For_Enemies.Ogre;
import SP.Game_Objects.For_Enemies.Vanky;
import SP.Main_Game.GameFrame;
import SP.Music.MusicPlayer;
import SP.Tile_Map.Background;
import SP.Tile_Map.TileMap;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jcpatac on 11/23/2016.
 */
public class SecondLvl extends GameState {

    private TileMap tileMap;
    private Background background;
    private PlayerStuffs playerStuffs;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Boom> booms;
    private Transfer transfer;

    //happenings
    private boolean start;
    private boolean finishLvl;
    private boolean isDead;
    private boolean inBlock = false;
    private int countAct = 0;
    private ArrayList<Rectangle> transBox;

    //music
    private MusicPlayer musicPlayer;

    public SecondLvl(ManageGS manageGS) {
        super(manageGS);
        initialize();
    }

    @Override
    public void pressedKeys() {
        if (Controls.isPressed(Controls.PAUSE)) {
            gsManager.setPaused(true);
        }
        if (inBlock || player.getHP() == 0) {
            return;
        }

        player.setLeft(Controls.controlState[Controls.LEFT]);
        player.setRight(Controls.controlState[Controls.RIGHT]);
        player.setFlying(Controls.controlState[Controls.FLY]);
        player.setJump(Controls.controlState[Controls.JUMP]);

        if (Controls.isPressed(Controls.SHOOT)) {
            player.setShooting();
        }
        if (Controls.isPressed(Controls.ATK)) {
            player.setScratch();
        }
    }

    private void putEnemies() {
        enemies = new ArrayList<Enemy>();
        Ogre ogre;
        Vanky vanky;
        Point[] enemyPos = new Point[] {
                new Point(216, 495),
                new Point(261, 355),
                new Point(351, 357),
                new Point(451, 627),
                new Point(660, 576),
                new Point(683, 483),
                new Point(723, 583),
                new Point(2893, 640),
                new Point(3001, 713),
                new Point(3108, 705),
                new Point(3118, 179),
                new Point(3165, 40),
                new Point(3210, 393),
                new Point(3408, 527),
                new Point(3468, 529),
                new Point(3423, 690),
                new Point(3528, 592),
                new Point(1149, 668),
                new Point(1295, 553),
                new Point(1379, 670),
                new Point(1486, 622),
                new Point(1735, 597),
                new Point(1859, 373),
                new Point(3697, 660),
                new Point(3941, 232),
                new Point(4328, 691)
        };
        for (int i = 0; i < enemyPos.length; i++) {
            ogre = new Ogre(tileMap, player);
            ogre.setPos(enemyPos[i].x, enemyPos[i].y);
            enemies.add(ogre);
        }

        Point[] vankies = new Point[] {
                new Point(302, 535),
                new Point(521, 549),
                new Point(915, 553),
                new Point(1210, 653),
                new Point(1353, 664),
                new Point(660, 566),
                new Point(1802, 481),
                new Point(1977, 462),
                new Point(2810, 682),
                new Point(3057, 325),
                new Point(3281, 956),
                new Point(3310, 139),
                new Point(3892, 247),
                new Point(4029, 258),
                new Point(3748, 384),
                new Point(3958, 494),
                new Point(4174, 679),
                new Point(4489, 669)
        };
        for (int j = 0; j < vankies.length; j++) {
            vanky = new Vanky(tileMap, player);
            vanky.setPos(vankies[j].x, vankies[j].y);
            enemies.add(vanky);
        }
    }

    public void initialize() {
        tileMap = new TileMap(32);
        tileMap.loadTiles("/SP/For_the_game/lvl2_obs.png");
        tileMap.loadMap("/SP/For_the_game/level2.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);

        background = new Background("/SP/For_the_game/new_cave_bg.jpg", 0.1);

        //for player
        player = new Player(tileMap);
        player.setPos(120, 70);
        player.setHp(SavePlayer.getHp());
        player.setLife(SavePlayer.getLife());
        player.setTime(SavePlayer.getTime());

        //hud
        playerStuffs = new PlayerStuffs(player);

        musicPlayer = new MusicPlayer("/SP/For_the_game/level2BackMusic.mp3");
        musicPlayer.play(true);

        //antagonist
        enemies = new ArrayList<Enemy>();
        putEnemies();

        //explosion
        booms = new ArrayList<Boom>();

        //portal
        transfer = new Transfer(tileMap);
        transfer.setPos(5905, 265);

        //happenings
        start = true;
        transBox = new ArrayList<Rectangle>();
        start();

        //init player
        player.initialize(enemies);
    }

    public void update() {
        //pressed keys
        pressedKeys();

        if (transfer.collides(player)) {
            finishLvl = inBlock = true;
        }

        //check if dead event should start
        if (player.getHP() == 0 || (player.getY() > tileMap.getHeight())) {
            isDead = inBlock = true;
        }

        //play events
        if (start) {
            start();
        }
        if (isDead) {
            isDead();
        }
        if (finishLvl) {
            finishLvl();
        }

        //update player
        player.update();

        //update map
        tileMap.setPosition(GameFrame.WIDTH / 2 - player.getX(), GameFrame.HEIGHT / 2 - player.getY());
        tileMap.update();
        tileMap.fixBounds();

        //background
        background.setPosition(tileMap.getX(), tileMap.getY());

        //attack
        player.checkAtk(enemies);

        //enemies
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.update();
            if (enemy.checkDead()) {
                enemies.remove(i);
                i--;
                booms.add(new Boom(tileMap, enemy.getX(), enemy.getY()));
            }
        }

        //explosion
        for (int j = 0; j < booms.size(); j++) {
            booms.get(j).update();
            if (booms.get(j).mustRemoveInGame()) {
                booms.remove(j);
                j--;
            }
        }
        transfer.update();
    }

    public void draw(Graphics2D g) {
        //clear transBox
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT);

        //for bg
        background.draw(g);

        //player
        player.draw(g);

        //map
        tileMap.draw(g);

        //attack
        player.checkAtk(enemies);

        //portal
        transfer.draw(g);

        //hud
        playerStuffs.draw(g);

        //enemies
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        //explosion
        for (int j = 0; j < booms.size(); j++) {
            booms.get(j).draw(g);
        }

        //transBox
        for (int k = 0; k < transBox.size(); k++) {
            g.fill(transBox.get(k));
        }
    }


    /*
    **THE GAME
     */


    private void reset() {
        player.reset();
        player.setPos(120, 500);
        putEnemies();
        inBlock = true;
        countAct = 0;
        tileMap.setShaking(false, 0);
        start = true;
        start();
    }

    private void start() {
        countAct++;
        if (countAct == 1) {
            transBox.clear();
            transBox.add(new Rectangle(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT / 2));
            transBox.add(new Rectangle(0, 0, GameFrame.WIDTH / 2, GameFrame.HEIGHT));
            transBox.add(new Rectangle(0, GameFrame.HEIGHT / 2, GameFrame.WIDTH, GameFrame.HEIGHT / 2));
            transBox.add(new Rectangle(GameFrame.WIDTH / 2, 0, GameFrame.WIDTH / 2, GameFrame.HEIGHT));
        }

        if (countAct < 60 && countAct > 1) {
            transBox.get(0).height -= 8;
            transBox.get(1).width -= 12;
            transBox.get(2).y += 8;
            transBox.get(3).x += 12;
        }

        if (countAct == 60) {
            start = inBlock = false;
            countAct = 0;
            transBox.clear();
        }
    }

    private void finishLvl() {
        countAct++;
        if (countAct == 1) {
            player.setTransferring(true);
            player.stopThis();
        }else if (countAct == 120) {
            transBox.clear();
            transBox.add(new Rectangle(GameFrame.WIDTH / 2, GameFrame.HEIGHT / 2, 0, 0));
        }else if (countAct > 120) {
            transBox.get(0).x -= 8;
            transBox.get(0).y -= 12;
            transBox.get(0).height += 24;
            transBox.get(0).width += 16;
        }
        if (countAct == 180) {
            SavePlayer.setLife(player.getLife());
            SavePlayer.setHp(player.getHP());
            SavePlayer.setTime(player.getTime());
            gsManager.setState(ManageGS.LEVEL3);
            musicPlayer.stop();
        }
    }

    private void isDead() {
        countAct++;
        if (countAct == 1) {
            player.dead();
            player.stopThis();
        }
        if (countAct == 60) {
            transBox.clear();
            transBox.add(new Rectangle(GameFrame.WIDTH / 2, GameFrame.HEIGHT / 2, 0, 0));
        }else if (countAct > 60) {
            transBox.get(0).x -= 12;
            transBox.get(0).y -= 8;
            transBox.get(0).height += 16;
            transBox.get(0).width += 24;
        }
        if (countAct >= 120) {
            if (player.getLife() == 0) {
                musicPlayer.stop();
                gsManager.setState(ManageGS.MENUSTATE);
            }else {
                isDead = false;
                inBlock = false;
                countAct = 0;
                player.minusLife();
                reset();
            }
        }
    }

}
