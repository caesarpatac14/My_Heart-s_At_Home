package SP.State_Of_Game;

/**
 * Created by acer on 10/14/2016.
 */


public class ManageGS{

    private GameState[] gameStates;
    private int currentState;

    private Pause pause;
    private boolean isPaused;

    public static final int GSNUM = 3;
    public static final int MENUSTATE = 0;
    public static final int LEVEL1 = 1;
//    public static final int LEVEL1BOSS = 2; // FIXME work in progress

    public ManageGS() {
        gameStates = new GameState[GSNUM];

        pause = new Pause(this);
        isPaused = false;

        currentState = MENUSTATE;
        stateLoad(currentState);
    }

    private void stateLoad(int state) {
        if (state == MENUSTATE) {
            gameStates[state] = new MenuState(this);
        }
        if (state == LEVEL1) {
            gameStates[state] = new FirstLvl(this);
        }
//        if (state == LEVEL1BOSS) {
//            gameStates[state] = new BossLevel1(this);
//        } // FIXME still working on this
    }

    private void stateUnload(int state) {
        gameStates[state] = null;
    }

    public void setState(int state) {
        stateUnload(currentState);
        currentState = state;
        stateLoad(currentState);
//        gameStates[currentState].initialize();
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public void update() {

        if (isPaused) {
            pause.update();
            return;
        }

        if (gameStates[currentState] != null) {
            gameStates[currentState].update();
        }
    }

    public void draw(java.awt.Graphics2D g) {

        if (isPaused) {
            pause.draw(g);
            return;
        }

        if (gameStates[currentState] != null) {
            gameStates[currentState].draw(g);
        }
    }
}
