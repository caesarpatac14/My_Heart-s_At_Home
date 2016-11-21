package SP.Main_Game;

/**
 * Created by acer on 10/14/2016.
 */

import javax.swing.JFrame;

public class Game {
    public static void main(String[] args) {
        JFrame frame = new JFrame("My Heart's At Home");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new GameFrame());
        frame.pack();
//        frame.setResizable(false);
        frame.setVisible(true);
    }
}