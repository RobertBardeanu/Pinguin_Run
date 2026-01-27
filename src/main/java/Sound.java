import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
/**
 * Musik Verwalter
 * @author Robert, Maik, Tobias
 * @version 1.0
 */
public class Sound {


    private Clip clip;
    private Clip clip2;
    private Clip clip3;
    private Clip clip4;


    /**
     * Audiospur starten
     */

    public void starteMusik() {
        try {
            File file = new File("src/main/resources/Media/Audio/513427__mrthenoronha__cartoon-game-theme-loop-3.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *Todessound abspielen
     */
    public void deadsound(){
        try {
            File file = new File("src/main/resources/Media/Audio/dead2.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip2 = AudioSystem.getClip();
            clip2.open(audioStream);
            clip2.start();


        }
        catch (Exception e){
            System.err.println(e);
        }
    }


    /**
     * Den Pinguinjump Sound abspielen
     */
    public void jump(){
        try {
            File file = new File("src/main/resources/Media/Audio/jump2.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip3 = AudioSystem.getClip();
            clip3.open(audioStream);
            clip3.start();
        }
        catch (Exception e){
            System.err.println(e);
        }
    }

    /**
     * Nach einem Game Over die Musik stoppen
     */
    public void stoppeMusik() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
    public void resetMusik(){
        clip.setMicrosecondPosition(0);
    }

}