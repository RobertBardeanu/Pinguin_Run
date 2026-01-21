import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class BackgroundMusic {


    private Clip clip;

    public void starteMusik() {
        try {
            File file = new File("src/Media/Audio/513427__mrthenoronha__cartoon-game-theme-loop-3.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void stoppeMusik() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}