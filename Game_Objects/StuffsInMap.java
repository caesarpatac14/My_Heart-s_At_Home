package SP.Game_Objects;

/**
 * Created by acer on 10/14/2016.
 */

import SP.Main_Game.GameFrame;
import SP.Tile_Map.Tile;
import SP.Tile_Map.TileMap;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public abstract class StuffsInMap {

    // for other collisions
    protected int rowCurr;
    protected int colCurr;
    protected double destX;
    protected double tempX;
    protected double destY;
    protected double tempY;
    protected boolean topLeft;
    protected boolean topRight;
    protected boolean botLeft;
    protected boolean botRight;

    // for move Attrib
    protected double moveSpeed;
    protected double maxSpeed;
    protected double stop;
    protected double fallSpeed;
    protected double maxFallSpd;
    protected double jumpStrt;
    protected double jumpStp;

    // for movements
    protected boolean left;
    protected boolean right;
    protected boolean up;
    protected boolean down;
    protected boolean isJumping;
    protected boolean isFalling;

    // for positions
    protected double x;
    protected double dx;
    protected double y;
    protected double dy;

    // for animations
    protected int currAct;
    protected int prevAct;
    protected boolean faceRight;
    protected Animation animation;

    // for tiles
    protected int tileSize;
    protected double xmap;
    protected double ymap;
    protected TileMap tileMap;

    // for dimensions
    protected int width;
    protected int height;

    // for collisions
    protected int widthReal;
    protected int heightReal;

    // game methods
    public StuffsInMap(TileMap tileMap) {
        this.tileMap = tileMap;
        tileSize = tileMap.getTileSize();
        faceRight = true;
        animation = new Animation();
    }

    public Rectangle createRect() {
        return new Rectangle((int) x - widthReal / 2, (int) y - heightReal / 2, widthReal, heightReal);
    }

    public boolean collides(StuffsInMap mo) {
        Rectangle rect1 = mo.createRect();
        Rectangle rect2 = createRect();
        return rect1.intersects(rect2);
    }

    public boolean collides(Rectangle rectangle) {
        return createRect().intersects(rectangle);
    }

    public void checkCollisionTile() {
//        System.out.println(destY);
        colCurr = (int) x / tileSize;
        rowCurr = (int) y / tileSize;

        destX = x + dx;
        destY = y + dy;

        tempX = x;
        tempY = y;

//        if (destY > tileMap.getHeight()) {
//            System.out.println(tileMap.getHeight());
////            gameOver();
//        }

        // for y
        cornerSolve(x, destY);

        if (dy < 0) {
            if (topLeft || topRight) {
                dy = 0;
                tempY = rowCurr * tileSize + heightReal / 2;
            }else {
                tempY += dy;
            }
        }
        if (dy > 0) {
            if (botLeft || botRight) {
                dy = 0;
                isFalling = false;
                tempY = (rowCurr + 1) * tileSize - heightReal / 2;
            }else {
                tempY += dy;
            }
        }

        // for x
        cornerSolve(destX, y);

        if (dx < 0) {
            if (topLeft || botLeft) {
                dx = 0;
                tempX = colCurr * tileSize + widthReal / 2;
            }else {
                tempX += dx;
            }
        }
        if (dx > 0) {
            if (topRight || botRight) {
                dx = 0;
                tempX = (colCurr + 1) * tileSize - widthReal / 2;
            }
            else {
                tempX += dx;
            }
        }

        if (!isFalling) {
            cornerSolve(x, destY + 1);

            if (!botLeft && !botRight) {
                isFalling = true;
            }
        }
    }

    public void cornerSolve(double x, double y) {
        int tileLeft = (int) (x - widthReal / 2) / tileSize;
        int tileRight = (int) (x + widthReal / 2 - 1) / tileSize;
        int tileTop = (int) (y - heightReal / 2) / tileSize;
        int tileBot = (int) (y + heightReal / 2 - 1) / tileSize;

        if (tileTop < 0 || tileBot >= tileMap.getRowNum() || tileLeft < 0 || tileRight >= tileMap.getColNum()) {
            topLeft = topRight = botLeft = botRight = false;
            return;
        }

        int tLeft = tileMap.getType(tileTop, tileLeft);
        int tRight = tileMap.getType(tileTop, tileRight);
        int bLeft = tileMap.getType(tileBot, tileLeft);
        int bRight = tileMap.getType(tileBot, tileRight);

        topLeft = (tLeft == Tile.BLOCKED);
        topRight =(tRight == Tile.BLOCKED);
        botLeft = (bLeft == Tile.BLOCKED);
        botRight = (bRight == Tile.BLOCKED);
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getRealWidth() {
        return widthReal;
    }

    public int getRealHeight() {
        return heightReal;
    }

    public boolean contains(Rectangle rectangle) {
        return createRect().contains(rectangle);
    }

    public boolean contains(StuffsInMap stuffsInMap) {
        Rectangle rectangle = createRect();
        Rectangle rectangle1 = stuffsInMap.createRect();
        return rectangle.contains(rectangle1);
    }

    public void setPos(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setDirection(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void setPosMap() {
        xmap = tileMap.getX();
        ymap = tileMap.getY();
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setJump(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public boolean drawOrNot() {
        return (x + xmap + width < 0 || x + xmap - width > GameFrame.WIDTH || y + ymap + height < 0 || y + ymap - height > GameFrame.HEIGHT);
    }

    public void draw(java.awt.Graphics2D g) {
        setPosMap();
        if (faceRight) {
            g.drawImage(animation.getImage(), (int) (x + xmap - width / 2), (int) (y + ymap - height / 2), null);
        }else {
            BufferedImage image = animation.getImage();
            AffineTransform affineTransform = AffineTransform.getScaleInstance(-1, 1);
            affineTransform.translate(-(image.getWidth(null)), 0);
            AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = affineTransformOp.filter(image, null);
            g.drawImage(image, (int) (x + xmap - width / 2), (int) (y + ymap - height / 2), null);
        }
    }
}
