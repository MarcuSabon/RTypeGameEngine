package sound;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {
	private static final int POOL_SIZE = 5;

	private static class SoundData {
		AudioFormat format;
		byte[] data;
		List<Clip> clips = new ArrayList<>();
		int index = 0;
	}

	private static final Map<String, SoundData> soundMap = new HashMap<>();

	public static void preload(String path) {
		if (soundMap.containsKey(path)) return;

		try {
			URL soundURL = SoundPlayer.class.getResource(path);
			if (soundURL == null) {
				System.err.println("Sound file not found: " + path);
				return;
			}

			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL);
			AudioFormat format = ais.getFormat();
			byte[] data = ais.readAllBytes();
			ais.close();

			SoundData soundData = new SoundData();
			soundData.format = format;
			soundData.data = data;

			for (int i = 0; i < POOL_SIZE; i++) {
				Clip clip = AudioSystem.getClip();
				clip.open(format, data, 0, data.length);
				soundData.clips.add(clip);
			}

			soundMap.put(path, soundData);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void play(String path) {
		SoundData sd = soundMap.get(path);
		if (sd == null) {
			preload(path); // fallback si pas préchargé
			sd = soundMap.get(path);
			if (sd == null) return;
		}

		Clip clip = sd.clips.get(sd.index);
		sd.index = (sd.index + 1) % sd.clips.size();

		clip.stop(); // important : stop avant replay
		clip.setFramePosition(0);
		clip.start();
	}

	public static void preloadSounds() {
		preload("/Sounds/Projectile1.wav");
		preload("/Sounds/Projectile2.wav");
		preload("/Sounds/Projectile3.wav");
		preload("/Sounds/BossSpawn.wav");
		preload("/Sounds/BossDeath.wav");
		preload("/Sounds/BotDeath.wav");
		// ajoute d'autres sons ici
	}

	public static void shootProjectile() {
		int n = (int)(Math.random() * 3) + 1;
		play("/Sounds/Projectile" + n + ".wav");
	}
}
