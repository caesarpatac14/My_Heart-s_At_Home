package SP.Music;

import javax.sound.sampled.*;

/**
 * Created by jcpatac on 11/17/2016.
 */
public class MusicPlayer {

    private Clip clip;

    public MusicPlayer(String string) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(string));

            AudioFormat audioFormat = audioInputStream.getFormat();
            AudioFormat audioFormat1 = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16, audioFormat.getChannels(),audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false);
            AudioInputStream audioInputStream1 = AudioSystem.getAudioInputStream(audioFormat1, audioInputStream);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream1);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(boolean repeat) {
        if (clip == null) {
            return;
        }
        stop();
        clip.setFramePosition(0);
        clip.start();
        if (repeat) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
//        else {
//            FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
//            floatControl.setValue(-10.0f);
//        }
    }

    public void stop() {
        if (clip.isRunning()) {
            clip.stop();
        }
    }

    public void close() {
        stop();
        clip.close();
    }

}
