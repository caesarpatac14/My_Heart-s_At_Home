package SP.State_Of_Game;

/**
 * Created by jcpatac on 12/2/2016.
 */

import SP.Controls.Controls;
import SP.Game_Objects.*;
import SP.Main_Game.GameFrame;
import SP.Music.MusicPlayer;
import SP.Tile_Map.Background;
import SP.Tile_Map.TileMap;

import java.awt.*;
import java.util.ArrayList;

public class FinalLevel extends GameState {

    private TileMap tileMap;
    private Background bg;
    private PlayerStuffs hud;
    private Player myPlayer;
    private ArrayList<Enemy> enemies;

    //happenings
    private boolean startMoving;
    private boolean finishLevel;
    private int countAct = 0;
    private boolean inBlock = false;
    private ArrayList<Rectangle> animate;

    //audio
    private MusicPlayer musicPlayer;
    private MusicPlayer winner;

    public FinalLevel(ManageGS gsManager) {
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

        myPlayer.setRight(Controls.controlState[Controls.RIGHT]);
        myPlayer.setLeft(Controls.controlState[Controls.LEFT]);
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
        tileMap.loadTiles("/SP/For_the_game/lvl5_3.png");
        tileMap.loadMap("/SP/For_the_game/level5.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);

        bg = new Background("/SP/For_the_game/lvl5BG.png", 0.1);

        //player
        myPlayer = new Player(tileMap);
        myPlayer.setPos(100, 500);
        myPlayer.setHp(SavePlayer.getHp());
        myPlayer.setLife(SavePlayer.getLife());
        myPlayer.setTime(SavePlayer.getTime());

        //life, power, etc.
        hud = new PlayerStuffs(myPlayer);

        musicPlayer = new MusicPlayer("/SP/For_the_game/Intro Theme.mp3");
        musicPlayer.play(true);
        winner = new MusicPlayer("/SP/For_the_game/win.mp3");

        //events
        startMoving = true;
        animate = new ArrayList<Rectangle>();
        startMoving();

        //initialize the player
        myPlayer.initialize(enemies);

    }

    public void update() {

        if (myPlayer.getX() >= 2222) {
            finishLevel = true;
            inBlock = true;
            myPlayer.stopThis();
        }

        //key pressed
        pressedKeys();

        //play events
        if (startMoving) {
            startMoving();
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
    }

    public void draw(Graphics2D g) {

        // clear
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT);

        // for background
        bg.draw(g);

        // draw tilemap
        tileMap.draw(g);

        // draw player
        myPlayer.draw(g);

        //draw PlayerStuffs
        hud.draw(g);

        //draw transition boxes
        for (int i = 0; i < animate.size(); i++) {
            g.fill(animate.get(i));
        }
    }
    /*
* THE GAME!!!
* */

    private void startMoving() {
        countAct++;
        if (countAct > 1 && countAct < 60) {
            animate.get(0).height -= 8;
            animate.get(1).width -= 12;
            animate.get(2).y += 8;
            animate.get(3).x += 12;
        }

        if (countAct == 1) {
            animate.clear();
            animate.add(new Rectangle(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT / 2));
            animate.add(new Rectangle(0, 0, GameFrame.WIDTH / 2, GameFrame.HEIGHT));
            animate.add(new Rectangle(0, GameFrame.HEIGHT / 2, GameFrame.WIDTH, GameFrame.HEIGHT / 2));
            animate.add(new Rectangle(GameFrame.WIDTH / 2, 0, GameFrame.WIDTH / 2, GameFrame.HEIGHT));
        }

        if (countAct == 60) {
            startMoving = inBlock = false;
            countAct = 0;
            animate.clear();
        }
    }

    private void finishLevel() {
        countAct++;

        if (countAct == 1) {
            myPlayer.setTransferring(true);
            myPlayer.stopThis();
        }else if (countAct == 120) {
            animate.clear();
            animate.add(new Rectangle(GameFrame.WIDTH / 2, GameFrame.HEIGHT / 2, 0, 0));
        }else if (countAct > 120) {
            animate.get(0).x -= 8;
            animate.get(0).y -= 12;
            animate.get(0).width += 16;
            animate.get(0).height += 24;
        }
        if (countAct == 180) {
            SavePlayer.setHp(myPlayer.getHP());
            SavePlayer.setLife(myPlayer.getLife());
            SavePlayer.setTime(myPlayer.getTime());
            musicPlayer.stop();
            winner.play(false);
            gsManager.setState(ManageGS.WINNER);
        }
    }

}