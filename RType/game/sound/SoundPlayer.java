package sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {
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
