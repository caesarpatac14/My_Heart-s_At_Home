package SP.Controls;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Created by jcpatac on 11/18/2016.
 */
public class Content {

    public static BufferedImage[][] load(String string, int width, int height) {
        BufferedImage[][] result;
        try {
            BufferedImage spriteSheet = ImageIO.read(Content.class.getResourceAsStream(string));
            int w = spriteSheet.getWidth() / width;
            int h = spriteSheet.getHeight() / height;
            result = new BufferedImage[h][w];
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    result[i][j] = spriteSheet.getSubimage(j * width, i * height, width, height);
                }
            }
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Loading!");
            System.exit(0);
        }
        return null;
    }

    public static BufferedImage[][] DarkEnergy = load("/SP/For_the_game/DarkEnergy.gif", 20, 20);
    public static BufferedImage[][] BoomSprite = load("/SP/For_the_game/boom.gif", 30, 30);
    public static BufferedImage[][] OgreEnemy = load("/SP/For_the_game/walkAttackEnemy1.png", 65, 66);
    public static BufferedImage[][] VankyEnemy = load("/SP/For_the_game/walkSpearEnemy2.png", 64, 74);
    public static BufferedImage[][] BossEnemy = load("/SP/For_the_game/Spirit.gif", 40, 40);
    public static BufferedImage[][] JumpingEnemy = load("/SP/For_the_game/Tengu.gif", 30, 30);

}
