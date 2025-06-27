package map;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import engine.model.Model;
import entities.Player;

public class MapLoader {
	private static final int CHUNK_WIDTH = 42;
	private static final int CHUNK_HEIGHT = 20;

	private final static List<char[][]> chunks = new ArrayList<>();
	static List<SpawnData> newEntities = new ArrayList<>();
	private static int offset = 0; // colonnes déjà supprimées dans actChunk
	private static int currentChunk = 0;
	private static char[][] actChunk;
	private static char[][] nextChunk;
	private static boolean endLvl;
	private boolean BossSpawned = false;

	private double scrollTimer = 0;
	private final double SCROLL_INTERVAL = 500;

	private static LinkedList<Wall> allWalls;

	public MapLoader(String filename, Model model) {
		loadChunks(filename);
		actChunk = chunks.get(0);
		nextChunk = chunks.get(1);
		this.endLvl = false;
		allWalls = new LinkedList<Wall>();
		new Synchronyser((int) SCROLL_INTERVAL);
	}

	public void tick(double elapsed, Model model) {
		if (endLvl) {
			Synchronyser.stop();
		}
		if (!endLvl) {
			scrollTimer += elapsed;
			if (scrollTimer >= SCROLL_INTERVAL) {
				loadActChunk(model);
				scrollTimer = 0; // Reset du timer
			}
		} else if (!BossSpawned) {
			newEntities.add(new SpawnData('9', 10, 35));
			BossSpawned = true;
		}
	}

	// cree les entites du actChunk
	public void loadActChunk(Model m) {
		int height = actChunk.length;
		int width = actChunk[0].length;
		if (currentChunk == 0 && offset == 0) {
			for (int row = 0; row < height; row++) {
				for (int col = 0; col < width; col++) {
					switch (actChunk[row][col]) {
					case '1':
						allWalls.add(new Wall(m, row, col, 0));
						break;
					default: // autres entités autre que Wall -> spawn dans Game
						newEntities.add(new SpawnData(actChunk[row][col], row, col));
						break;
					}
				}
			}
		}
		endLvl = updateMap(m);
	}

	// true if map finished
	public static boolean updateMap(Model m) {
		if (!endLvl) {
			decalageActChunk(m);
			ajoutColActChunk(m);
			Synchronyser.resetProg();
		}

		offset++;
		if (offset == CHUNK_WIDTH) {
			offset = 0;
			currentChunk++;
			if (currentChunk + 1 >= chunks.size()) {
				nextChunk = actChunk;
				Synchronyser.stop();
				return true;
			} else {
				actChunk = chunks.get(currentChunk);
				nextChunk = chunks.get(currentChunk + 1);
			}
		}
		for (int i = 0; i < m.nrows(); i++) {
			if (m.entity(i, 0) != null && !(m.entity(i, 0) instanceof Player)
					&& !(m.entity(i, 0) instanceof WallNull)) {
				m.entity(i, 0).die();
			}
		}
		return false;
	}

	// met à jour la dernière colonne de actChunk
	public static void ajoutColActChunk(Model m) {
		int lignes = actChunk.length;
		int colonnes = actChunk[0].length;

		for (int i = 0; i < lignes; i++) {
			char c = nextChunk[i][offset];
			actChunk[i][colonnes - 1] = c;
			switch (c) { // TODO switch pour spawn future entities
			case '1':
				allWalls.add(new Wall(m, i, colonnes - 1, 0));
				break;
			default:
				newEntities.add(new SpawnData(actChunk[i][colonnes - 1], i, colonnes - 1));
				break;
			}
		}
	}

	// decale les colonnes des Walls vers la gauche
	public static void decalageActChunk(Model m) {
		int lignes = actChunk.length;
		int colonnes = actChunk[0].length;

//		for (Wall w : allWalls) {
//			w.stunt.move(0, -1);
//		}
		for (int j = 0; j < m.ncols(); j++) {
			for (int i = 0; i < m.nrows(); i++) {
				if (m.entity(i, j) != null && !(m.entity(i, j) instanceof Player))
					if (m.entity(i, j) instanceof Wall) {
						m.entity(i, j).stunt.move(0, -1);
					} else {
						m.move(m.entity(i, j), 0, -1);

					}
			}
		}
		for (int i = 0; i < lignes; i++) {
			actChunk[i][colonnes - 1] = '0';
		}
	}

	// charge la liste de chunk à partir du fichier
	private void loadChunks(String filename) {
		if (!chunks.isEmpty()) {
			chunks.clear();
		}
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(filename));
		} catch (IOException e) {
			System.err.println("Erreur lors du chargement de la map : " + e.getMessage());
		}
		for (int i = 0; i + CHUNK_HEIGHT <= lines.size(); i += CHUNK_HEIGHT) {
			char[][] chunk = new char[CHUNK_HEIGHT][CHUNK_WIDTH];

			for (int row = 0; row < CHUNK_HEIGHT; row++) {
				String line = lines.get(i + row);
				for (int col = 0; col < CHUNK_WIDTH; col++) {
					if (col < line.length()) {
						chunk[row][col] = line.charAt(col);
					} else {
						chunk[row][col] = ' ';
					}
				}
			}
			chunks.add(chunk);
		}
	}

	public char[][] next() {
		if (currentChunk >= chunks.size()) {
			return null; // plus de chunk
		}
		return chunks.get(currentChunk++);
	}

	public void reset() {
		currentChunk = 0;
		offset = 0;
		BossSpawned = false;
		endLvl = false;
		actChunk = chunks.get(0);
	}

	public int totalChunks() {
		return chunks.size();
	}

	public List<SpawnData> RemoveNewEntities() {
		List<SpawnData> result = new ArrayList<>(newEntities);
		newEntities.clear();
		return result;
	}
}
