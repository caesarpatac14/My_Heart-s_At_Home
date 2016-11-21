package SP.Game_Objects;

import SP.Tile_Map.TileMap;

/**
 * Created by acer on 10/14/2016.
 */

public class Enemy extends StuffsInMap {

    protected int hp;
    protected int maxHP;
    protected int damage;
    protected long timeFlinched;
    protected boolean isFlinched;
    protected boolean isDead;

    protected boolean remove;

    public Enemy(TileMap tileMap) {
        super(tileMap);
        remove = false;
    }

    public boolean mustRemove() {
        return remove;
    }

    public boolean checkDead() {
        return isDead;
    }

    public int getDmg() {
        return damage;
    }

    public void hit(int damage) {
        if (isFlinched || isDead) {
            return;
        }

        hp -= damage;

        if (hp < 0) {
            hp = 0;
        }
        if (hp == 0) {
            isDead = true;
        }
        if (isDead) {
            remove =  true;
        }

        isFlinched = true;
        timeFlinched = System.nanoTime();
    }

    public void update() {

    }
}
