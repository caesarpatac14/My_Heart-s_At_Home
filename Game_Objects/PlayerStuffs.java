package SP.Game_Objects;

/**
 * Created by acer on 10/14/2016.
 */


import SP.Main_Game.GameFrame;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class PlayerStuffs {

    private Player player;
//    private Font font;
//    private BufferedImage hud;

    private BufferedImage hp;
    private BufferedImage life;

    public PlayerStuffs(Player player) {
        this.player = player;

        try {

            BufferedImage hud = ImageIO.read(getClass().getResourceAsStream("/SP/For_the_game/hud.png"));
//            font = new Font("Garamond", Font.PLAIN, 14);
            life = hud.getSubimage(0, 0, 59, 43);
            hp = hud.getSubimage(0, 43, 59, 43);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void draw(Graphics2D g) {

        for (int j = 0; j < player.getLife(); j++) {
            g.drawImage(life, 10 + j * 60, 10, null);
        }

        for (int i = 0; i < player.getHP(); i++) {
            g.drawImage(hp, 10 + i * 60, 55, null);
        }

//        g.drawImage(hud, 0, 10, null);
//        g.setFont(font);
//        g.setColor(Color.BLACK);
//        g.drawString(player.timeToString(), GameFrame.WIDTH - 150, 55);
//        g.drawString(player.getHP() + "/" + player.getMaxHP(), 30, 25);
//        g.drawString(player.getPower() / 100 + "/" + player.getMaxPow() / 100, 30, 45);
    }
}