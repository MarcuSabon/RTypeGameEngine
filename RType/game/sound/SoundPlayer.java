package sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SoundPlayer {
    private static final Random random = new Random();
    
    public static void shootProjectile() {
        int choice = random.nextInt(3) + 1;
        String filePath = "/Sounds/Projectile" + choice + ".wav";
        play(filePath);
    }    

    public static void play(String path) {
        try {
            URL soundURL = SoundPlayer.class.getResource(path);
            if (soundURL == null) {
                System.err.println("Sound file not found: " + path);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            clip.start();

        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio format: " + path);
        } catch (IOException e) {
            System.err.println("I/O error with audio file: " + path);
        } catch (LineUnavailableException e) {
            System.err.println("Line unavailable for audio playback.");
        }
    }

}
