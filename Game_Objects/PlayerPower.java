package SP.Game_Objects;

/**
 * Created by acer on 10/14/2016.
 */

import SP.Tile_Map.TileMap;

import java.awt.image.*;
import javax.imageio.*;
import java.awt.*;

public class PlayerPower extends StuffsInMap {

    private boolean powerHit;
    private boolean powerRemove;
    private BufferedImage[] sprites;
    private BufferedImage[] powerHitSprites;

    public PlayerPower (TileMap tileMap, boolean right) {
        super(tileMap);

        faceRight = right;

        moveSpeed = 3.8;

        if (right) {
            dx = moveSpeed;
        }else {
            dx = -moveSpeed;
        }

        width = 30;
        widthReal = 14;
        height = 30;
        heightReal = 14;

        //sprite load
        try {

            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/SP/For_the_game/powerShot.gif"));
            sprites = new BufferedImage[4];

            for (int i = 0; i < sprites.length; i++) {
                sprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);
            }

            powerHitSprites = new BufferedImage[3];
            for (int j = 0; j < powerHitSprites.length; j++) {
                powerHitSprites[j] = spriteSheet.getSubimage(j * width, height, width, height);
            }

            animation = new Animation();
            animation.setFrames(sprites);
            animation.setPause(2);

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setPowerHit() {
        if (powerHit) {
            return;
        }

        powerHit = true;
        animation.setFrames(powerHitSprites);
        animation.setPause(2);
        dx = 0;
    }

    public boolean mustPowerRemove() {
        return powerRemove;
    }

    public void update() {

        checkCollisionTile();
        setPos(tempX, tempY);
        animation.update();

        if (dx == 0 && !powerHit) {
            setPowerHit();
        }

        if(powerHit && animation.playedOnce()) {
            powerRemove = true;
        }
    }

    public void draw(Graphics2D g) {
        setPosMap();
        super.draw(g);
    }
}
