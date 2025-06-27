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
import map.Synchronyser;
import sound.SoundPlayer;

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
	public static boolean lvl1 = true; // Level 1 flag
	private int time = 4000;
	private int time2 = 2000;
	private int time3 = 2000;
	private int time4 = 2000;
	private int time5 = 2000;
	private int i = 0; // Counter for level progression

	public boolean initialized;
	private int score = 0;

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
		if (gameState == GameState.Transition0) {
			time5 -= elapsed;
			if (time5 < 0) {
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
			if (player.getHP() <= 0 || player.col() == 0) {
				gameState = switchState(gameState, false);
				view.setGameState(gameState);
			} else if (boss != null && player.getHP() > 0 && boss.isDead()) {
				i++;
				controller.playerInit = false;
				gameState = switchState(gameState, true);
				view.setGameState(gameState);

			}
		}
		if (gameState == GameState.Restart) {
			// Synchronyser.start();
			if (controller.nameGiven) {
				time4 = time3 = 2000;
				if ((surligne % 2 + 2) % 2 == 0) {
					System.out.println(">> Nouvelle partie");
					i = 0;
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
			this.score = player.getScore();
			model.clear();
			m_loader.reset();
			boss = null;
			lvl1 = true;
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
			model.clear();
			m_loader.reset();
			boss = null;
			lvl1 = true;
			time4 -= elapsed;
			if (time4 < 0) {
				model.clear();
				controller.playerInit = false;
				controller.nameGiven = false;
				gameState = switchState(gameState, false);
				view.setGameState(gameState);
			}
		}
		if (gameState == GameState.End) {
			model.clear();
			m_loader.reset();
			boss = null;
			lvl1 = true;
			time2 -= elapsed;
			if (time2 < 0) {
				controller.playerInit = false;
				controller.nameGiven = false;
				gameState = switchState(gameState, false);
				view.setGameState(gameState);
			}
		}
		if (gameState == GameState.Charge) {
			initialize_charge_tester();
			view.setGameState(GameState.Charge);
			SpawnEntities(elapsed);
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
		Synchronyser.start();
		if (!playerInitialized()) {
			time2 = 2000;
			setNomDonnee();
			m_brain = new Brain(model);

			player = new Player(model, 5, 5, 0);

			Player.name = GameManager.pseudoBuilder.toString();
			initializePlayer();

			String path = "game/Ressources/Map1/Map1.txt";
			m_loader = new MapLoader(path, model);
			m_loader.reset();

			initialized = true;
		}
	}

	public void init_lvl2() {
		Synchronyser.start();
		if (!playerInitialized()) {
			lvl1 = false;
			time2 = 2000;
			m_brain = new Brain(model);
			player = new Player(model, 5, 5, 0);
			player.setScore(this.score);
			String path = "game/Ressources/Map2/Map2.txt";
			m_loader = new MapLoader(path, model);

			initializePlayer();
			m_loader.reset();

			initialized = true;
		}
	}

	public void initialize_charge_tester() {
		if (!playerInitialized()) {

			m_brain = new Brain(model);
			player = new Player(model, 5, 5, 0);

			String path = "game/Ressources/MapCharge/MapCharge.txt";
			m_loader = new MapLoader(path, model);

			initializePlayer();
			m_loader.reset();

			initialized = true;
		}
	}

	public enum GameState {
		Intro, Pseudo, Playing, End, LevelWin, Restart, Win, Transition0, Charge;
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
			gameState = GameState.Transition0;
			break;
		case Transition0:
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
		case Win:
			gameState = GameState.Restart;
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
					new Shooter(model, s.row, s.col, 0);
					break;
				case '3':
					new Tower(model, s.row, s.col, 0);
					break;
				case '4':
					new Tracker(model, s.row, s.col, 0);
					break;
				case '9':
					if (model.player().col() > 5) {
						if (model.entity(10, 5) != null) {
							model.entity(10, 5).die();
						}
						// model.player().at(10, 10);
						model.player().at(10.0, 5.0);
						model.player().move(0.0, 0.1);
					}
					for (int i = s.row - 5; i < s.row + 5; i++) {
						for (int j = s.col - 15; j < s.col - 5; j++) {
							if (model.entity(i, j) != null) {
								model.entity(i, j).die();
							}
						}
					}
					SoundPlayer.play("/Sounds/BossSpawn.wav");
					if (lvl1)
						this.boss = new Master(model, s.row, s.col - 10, 0, "/Boss/Level1", m_brain);
					else
						this.boss = new Master(model, s.row, s.col - 10, 0, "/Boss/Level2", m_brain);
					break;
				}
			}
		}
	}

}
