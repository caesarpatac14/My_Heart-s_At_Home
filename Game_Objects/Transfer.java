package SP.Game_Objects;

import SP.Tile_Map.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by jcpatac on 11/18/2016.
 */
public class Transfer extends StuffsInMap {

    private BufferedImage[] sprites;

    public Transfer(TileMap tileMap) {
        super(tileMap);
        faceRight = true;
        width = height = heightReal = 40;
        widthReal = 20;

        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/SP/For_the_game/Teleport.gif"));
            sprites = new BufferedImage[1];
            for (int i = 0; i < sprites.length; i++) {
                sprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);
            }
            animation.setFrames(sprites);
            animation.setPause(1);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        animation.update();
    }

    public void draw(Graphics2D graphics2D) {
        super.draw(graphics2D);
    }

}
