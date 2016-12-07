package SP.Tile_Map;

/**
 * Created by acer on 10/14/2016.
 */

import SP.Main_Game.GameFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Background {
    private BufferedImage image;
    private BufferedImage[] images;

    private int x1 = 0;
    private Timer timer;

    private double x;
    private double y;
    private double dx;
    private double dy;

    private double moveScale;

    public Background(String str, double ms) {
        try {
            image = ImageIO.read(getClass().getResourceAsStream(str));
            moveScale = ms;
        }catch (Exception e) {
        }
    }

    public Background(String[] strings, double ms) {
        try {
            images = new BufferedImage[strings.length];
            for (int i = 0; i < strings.length; i++) {
                images[i] = ImageIO.read(getClass().getResourceAsStream(strings[i]));
            }
            moveScale = ms;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPosition(double x, double y) {
        this.x = (x * moveScale) % GameFrame.WIDTH;
        this.y = (y * moveScale) % GameFrame.HEIGHT;
    }

    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

//    private void setImageSize(int i) {
//        ImageIcon icon = new ImageIcon(images[i]);
//        Image image = icon.getImage();
//        Image newImage = image.getScaledInstance(GameFrame.WIDTH, GameFrame.HEIGHT, Image.SCALE_SMOOTH);
////        ImageIcon newImc = new ImageIcon(newImage);
//    }

    public void controlTimer(boolean control) {
        if (control) {
            timer.start();
        }else {
            timer.stop();
        }
    }

    public void update() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                x1++;
                if (x1 >= images.length) {
                    x1 = 0;
                }
//                System.out.println(x1);
            }
        });
        controlTimer(true);
//        x += (dx * moveScale) % GameFrame.WIDTH;
//        y += (dy * moveScale) % GameFrame.HEIGHT;
    }

    public void draw(Graphics2D g) {
        g.drawImage(image, (int) x, (int) y, null);
//        for (int i = 0; i < images.length; i++) {
//            g.drawImage(images[i], (int) x, (int) y, null);
//        }

        if (x < 0) {
            g.drawImage(image, (int) x + GameFrame.WIDTH, (int) y, null);
        }
        if(x > 0) {
            g.drawImage(image, (int) x - GameFrame.WIDTH, (int) y, null);
        }
    }

    public void draw1(Graphics2D g) {
//        g.drawImage(image, (int) x, (int) y, null);
        for (int i = 0; i < images.length; i++) {
            g.drawImage(images[x1], (int) x, (int) y, null);
        }
//        g.drawImage(images[x1], (int) x, (int) y, null);

//        if (x < 0) {
//            g.drawImage(image, (int) x + GameFrame.WIDTH, (int) y, null);
//        }
//        if(x > 0) {
//            g.drawImage(image, (int) x - GameFrame.WIDTH, (int) y, null);
//        }
    }
}
