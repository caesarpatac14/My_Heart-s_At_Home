package SP.Main_Game;

/**
 * Created by acer on 10/14/2016.
 */

import SP.Controls.Controls;
import SP.State_Of_Game.ManageGS;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

@SuppressWarnings("serial")
public class GameFrame extends JPanel implements Runnable, KeyListener{
    // dimensions
//    public static final int WIDTH = 320;
//    public static final int LENGTH = 240;
    public static final int SCALE = 2;

    // game thread
    private Thread thread;
    private boolean running;
    private int fps = 200;
    private long targetTime = 1000 / fps;

    public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    // image
    private BufferedImage image;
    private Graphics2D g;

    // manager of game state
    private ManageGS gsm;

    public GameFrame() {
        super();
        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
//        setPreferredSize(new Dimension(WIDTH, LENGTH));
        setFocusable(true);
        requestFocus();
    }

    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }

    private void initialize() {
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();

        running = true;

        gsm = new ManageGS();
    }

    public void run() {
        initialize();

        long start;
        long elapsed;
        long wait;

        // game loop

        while(running) {
            start = System.currentTimeMillis();
            // System.out.println(start);

            update();
            draw();
            drawToScreen();

            elapsed = System.currentTimeMillis() - start;
            // System.out.println(elapsed);

            wait = targetTime - elapsed;

            try {
                Thread.sleep(wait);
            }catch (Exception e) {
//                e.printStackTrace();
            }
        }
    }

    private void update() {
        gsm.update();
    }

    private void draw() {
        gsm.draw(g);
    }

    private void drawToScreen() {
        Graphics g2 = getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }

    public void keyTyped(KeyEvent key) {

    }

    public void keyPressed(KeyEvent key) {
//        gsm.keyPressed(key.getKeyCode());
        Controls.setControls(key.getKeyCode(), true);
    }

    public void keyReleased(KeyEvent key) {
//        gsm.keyReleased(key.getKeyCode());
        Controls.setControls(key.getKeyCode(), false);
    }
}
