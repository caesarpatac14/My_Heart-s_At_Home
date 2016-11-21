package SP.Controls;

import java.awt.event.KeyEvent;

/**
 * Created by jcpatac on 11/17/2016.
 */
public class Controls {

    public static final int KEY_AMT = 16;
    public static boolean controlState[] = new boolean[KEY_AMT];
    public static boolean prevControl[] = new boolean[KEY_AMT];

    public static int LEFT = 0;
    public static int RIGHT = 1;
    public static int JUMP = 2;
    public static int FLY = 3;
    public static int SHOOT = 4;
    public static int ATK = 5;
    public static int OK = 6;
    public static int PAUSE = 7;
    public static int UP = 8;
    public static int DOWN = 9;

    public static void setControls(int key, boolean selected) {

        if (key == KeyEvent.VK_LEFT) {
            controlState[LEFT] = selected;
        }else if (key == KeyEvent.VK_RIGHT) {
            System.out.println();
            controlState[RIGHT] = selected;
        }else if (key == KeyEvent.VK_SPACE) {
            controlState[JUMP] = selected;
        }else if (key == KeyEvent.VK_X) {
            controlState[FLY] = selected;
        }else if (key == KeyEvent.VK_S) {
            controlState[SHOOT] = selected;
        }else if (key == KeyEvent.VK_A) {
            controlState[ATK] = selected;
        }else if (key == KeyEvent.VK_ENTER) {
            controlState[OK] = selected;
        }else if (key == KeyEvent.VK_UP) {
            controlState[UP] = selected;
        }else if (key == KeyEvent.VK_DOWN) {
            controlState[DOWN] = selected;
        }else if (key == KeyEvent.VK_ESCAPE) {
            controlState[PAUSE] = selected;
        }

    }

    public static void update() {
        for (int i = 0; i < KEY_AMT; i++) {
            prevControl[i] = controlState[i];
        }
    }

    public static boolean isPressed(int key) {
        return controlState[key] && !prevControl[key];
    }

    public static boolean randomPressing() {
        for (int i = 0; i < KEY_AMT; i++) {
            if (controlState[i]) {
                return true;
            }
        }
        return false;
    }

}
