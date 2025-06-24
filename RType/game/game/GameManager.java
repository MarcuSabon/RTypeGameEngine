package game;

import java.util.List;

import boss.Master;
import engine.brain.Brain;
import engine.controller.Controller;
import engine.model.Model;
import engine.view.View;
import entities.Player;
import entities.Shooter;
import entities.Tower;
import entities.Tracker;
import map.MapLoader;
import map.SpawnData;

public class GameManager {
	private Controller0 controller;
	private View0 view;
	private Model model;
	private Brain m_brain;
	private Player player;
	private MapLoader m_loader;
	private Master boss;
	public GameState gameState;// Initial state set to PSEUDO
	protected static StringBuilder pseudoBuilder;
	public static int surligne;
	public static boolean restartButton;
	private int time = 2000;
	private int time2 = 2000;
	private int time3 = 2000;
	private int time4 = 2000;
	private int i = 0; // Counter for level progression

	public boolean initialized;

	public GameManager(Game game, Controller controller, View view, Model model, GameState gameState) {
		this.controller = (Controller0) controller;
		this.view = (View0) view;
		this.model = model;
		this.gameState = gameState; // Initialize game state
		this.view.setGameState(gameState);
		pseudoBuilder = new StringBuilder();
	}

	public void start(int elapsed) {
		if (gameState == GameState.Intro) {
			time -= elapsed;
			if (time < 0) {
				gameState = switchState(gameState, false);
				view.setGameState(gameState);
			}
		}
		if (gameState == GameState.Pseudo) {
			if (nameGiven()) {
				gameState = switchState(gameState, false);
				view.setGameState(gameState);
			}
		}
		if (gameState == GameState.Playing) {
			switch (i) {
			case 0:
				init();
				break;
			case 1:
				init_lvl2();
				break;
			default:
				break;
			}

			SpawnEntities(elapsed);
			if (player.getHP() <= 0) {
				gameState = switchState(gameState, false);
				view.setGameState(gameState);
			} else if (boss != null && player.getHP() > 0 && boss.isDead()) {
				i++;
				gameState = switchState(gameState, true);
				view.setGameState(gameState);

			}
		}
		if (gameState == GameState.End)

		{
			model.clear();
			time2 -= elapsed;
			if (time2 < 0) {
				controller.playerInit = false;
				controller.nameGiven = false;
				gameState = switchState(gameState, false);
				view.setGameState(gameState);
			}
		}
		if (gameState == GameState.Restart) {
			if (controller.nameGiven) {
				if ((surligne % 2 + 2) % 2 == 0) {
					System.out.println(">> Nouvelle partie");
					gameState = switchState(gameState, false);
					view.setGameState(gameState);
				} else {
					// ajout cinematique de fin
					System.out.println(">> Quitter le jeu");
					System.exit(0); // ou autre méthode de fermeture
				}
			}

		}
		if (gameState == GameState.LevelWin) {
			time3 -= elapsed;
			if (time3 < 0) {
				model.clear();
				controller.playerInit = false;
				controller.nameGiven = false;
				gameState = switchState(gameState, false);
				view.setGameState(gameState);
			}
		}
		if (gameState == GameState.Win) {
			time4 -= elapsed;
			if (time4 < 0) {
				model.clear();
				controller.playerInit = false;
				controller.nameGiven = false;
				gameState = switchState(gameState, false);
				view.setGameState(gameState);
			}
		}
	}

	public boolean nameGiven() {
		return controller.nameGiven;
	}

	public boolean playerInitialized() {
		return controller.playerInit;
	}

	public void initializePlayer() {
		controller.playerInit = true;
	}

	public void setNomDonnee() {
		view.setNomDonnee(nameGiven());
	}

	public void init() {
		if (!playerInitialized()) {
			time2 = 2000;
			setNomDonnee();
			m_brain = new Brain(model);

			player = new Player(model, 5, 5, 0);

			Player.name = GameManager.pseudoBuilder.toString();
			initializePlayer();

//			new Master(model, 35, 10, 180, "/Boss/Level1", m_brain);

//			Master M2 = new Master(model, 20, 10, 180, "/Boss/Level1", m_brain);
//			Master M3 = new Master(model, 15, 10, 180, "/Boss/Level1", m_brain);
//			Master M4 = new Master(model, 10, 10, 180, "/Boss/Level1", m_brain);

//			new Shooter(model, 16, 16, 0);
//			new Tracker(model, 17, 17, 0);
//			new Tower(model, 18, 41, 0);
//			new Tower(model, 1, 41, 0);

			String path = "game/Ressources/Map1/Map1.txt";
			m_loader = new MapLoader(path, model);
			m_loader.reset();

			initialized = true;
		}
	}

	public void init_lvl2() {
		if (!playerInitialized()) {
			time2 = 2000;
			m_brain = new Brain(model);

			player = new Player(model, 5, 5, 0);

			initializePlayer();

			// boss = new Master(model, 35, 10, 180, "/Boss/Level1", m_brain);
			m_loader.reset();
//			String path = "game/Ressources/Map1/Map1.txt";
//			m_loader = new MapLoader(path, model);

			initialized = true;
		}
	}

	public enum GameState {
		Intro, Pseudo, Playing, End, LevelWin, Restart, Win;
	}

	public GameState getGameState() {
		return gameState;
	}

	public GameState switchState(GameState gameState, boolean b) {
		switch (gameState) {
		case Intro:
			gameState = GameState.Pseudo;
			break;
		case Pseudo:
			gameState = GameState.Playing;
			break;
		case Playing:
			if (b && (i > 1)) {
				System.out.println(">> Vous avez gagné !");
				gameState = GameState.Win;
			} else if (b)
				gameState = GameState.LevelWin;

			else
				gameState = GameState.End;

			break;
		case LevelWin:
			gameState = GameState.Playing;
			break;
		case End:
			gameState = GameState.Restart;
			break;
		case Restart:
			gameState = GameState.Playing;
			break;
		default:
			return gameState;
		}
		return gameState;
	}

	private void SpawnEntities(int elapsed) {
		if (m_loader != null) {
			m_loader.tick(elapsed, model);
			List<SpawnData> spawns = m_loader.RemoveNewEntities();
			for (SpawnData s : spawns) {
				switch (s.type) {
				case '2':
					new Shooter(model, s.row, s.col - 1, 0);
					break;
				case '3':
					new Tower(model, s.row, s.col - 1, 0);
					break;
				case '4':
					new Tracker(model, s.row, s.col - 1, 0);
					break;
				case '9':
					this.boss = new Master(model, s.row, s.col - 10, 0, "/Boss/Level1", m_brain);
					break;
				}
			}
		}
	}

}