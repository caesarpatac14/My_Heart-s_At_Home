package SP.Game_Objects;

import java.awt.image.BufferedImage;

/**
 * Created by acer on 10/14/2016.
 */

public class Animation {

    private BufferedImage[] frames;
    private int frameCurr;
    private int frameNum;

    private long timeStart;
    private long pause;

    private int count;
//    private boolean once;
    private int timesPlayed;

    public Animation() {
//        once = false;
        timesPlayed = 0;
    }

    public void setFrames(BufferedImage[] frames) {
        this.frames = frames;
        frameCurr = 0;
//        timeStart = System.nanoTime();
//        once = false;
        count = 0;
        timesPlayed = 0;
        pause = 2;
        frameNum = frames.length;
    }

    public void setPause(long pause) {
        this.pause = pause;
    }

    public void setFrame(int frameCurr) {
        this.frameCurr = frameCurr;
    }

    public void  setFrameNum(int frameNum) {
        this.frameNum = frameNum;
    }

    public void update() {
        if (pause == -1) {
            return;
        }
        count++;

//        long used = (System.nanoTime() - timeStart) / 1000000;

        if (count == pause) {
            frameCurr++;
            count = 0;
        }

        if (frameCurr == frames.length) {
            frameCurr = 0;
//            once = true;
            timesPlayed++;
        }
    }

    public int getFrame() {
        return frameCurr;
    }

    public int getCount() {
        return count;
    }

    public BufferedImage getImage() {
        return frames[frameCurr];
    }

    public boolean playedOnce() {
        return timesPlayed > 0;
    }

    public boolean alreadyPlayed(int n) {
        return timesPlayed == n;
    }

}