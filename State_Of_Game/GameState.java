package SP.State_Of_Game;

/**
 * Created by acer on 10/14/2016.
 */


public abstract class GameState {
    protected ManageGS gsManager;

    public GameState(ManageGS manageGS) {
        gsManager = manageGS;
    }

    public abstract void initialize();
    public abstract void update();
    public abstract void draw(java.awt.Graphics2D g);
    public abstract void pressedKeys();
//    public abstract void keyPressed(int key);
//    public abstract void keyReleased(int key);
}