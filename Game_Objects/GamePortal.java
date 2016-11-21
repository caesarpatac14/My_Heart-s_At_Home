package SP.Game_Objects;

import SP.Tile_Map.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by jcpatac on 11/18/2016.
 */
public class GamePortal extends StuffsInMap {

    private BufferedImage[] spritesClosed;
    private BufferedImage[] spritesOpening;
    private BufferedImage[] spritesOpened;

    private boolean opening;
    private boolean opened;

    public GamePortal(TileMap tileMap) {
        super(tileMap);

        width = 81;
        height = 111;

        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/SP/For_the_game/Portal.gif"));
            spritesClosed = new BufferedImage[1];
            spritesClosed[0] = spriteSheet.getSubimage(0, 0, width, height);

            spritesOpening = new BufferedImage[6];
            for (int i = 0; i < spritesOpening.length; i++) {
                spritesOpening[i] = spriteSheet.getSubimage(i * width, height, width, height);
            }

            spritesOpened = new BufferedImage[3];
            for (int j = 0; j < spritesOpened.length; j++) {
                spritesOpened[j] = spriteSheet.getSubimage(j * width, height, width, height);
            }

            animation.setFrames(spritesClosed);
            animation.setPause(-1);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOpening() {
        opening = true;
        animation.setFrames(spritesOpening);
        animation.setPause(2);
    }

    public void setOpened() {
        if (opened) {
            return;
        }
        animation.setFrames(spritesOpened);
        animation.setPause(2);
        opened = true;
    }

    public void setClosed() {
        animation.setFrames(spritesClosed);
        animation.setPause(-1);
        opened = false;
    }

    public boolean isOpened() {
        return opened;
    }

    public void update() {
        animation.update();
        if (opening && animation.playedOnce()) {
            opened = true;
            animation.setFrames(spritesOpened);
            animation.setPause(2);
        }
    }

    public void draw(Graphics2D graphics2D) {
        super.draw(graphics2D);
    }

}
