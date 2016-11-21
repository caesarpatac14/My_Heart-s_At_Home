package SP.Game_Objects;

/**
 * Created by jcpatac on 11/17/2016.
 */
public class SavePlayer {

    private static int life = 3;
    private static int hp = 5;
    private static long time = 0;

    public static void initialize() {
        life = 3;
        hp = 5;
        time = 0;
    }

    public static int getLife() {
        return life;
    }

    public static void setLife(int life) {
        SavePlayer.life = life;
    }

    public static int getHp() {
        return hp;
    }

    public static void setHp(int hp) {
        SavePlayer.hp = hp;
    }

    public static long getTime() {
        return time;
    }

    public static void setTime(long time) {
        SavePlayer.time = time;
    }
}
