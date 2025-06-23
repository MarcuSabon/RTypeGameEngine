package game;

import engine.brain.Brain;
import engine.controller.Controller;
import engine.model.Model;
import engine.model.PNJ;
import engine.model.Player;
import engine.view.View;

//public enum GameState {
//	INTRO, PSEUDO, PLAYING, END
//}

class GameManager {
	private Controller0 controller;
	private View0 view;
	private Model model;
	private Brain m_brain;
	private Player player;
	public GameState gameState;// Initial state set to PSEUDO
	protected static StringBuilder pseudoBuilder;
	public static int surligne;
	public static boolean restartButton;
	private int time;
	private int time2;
	private boolean tr;

	public GameManager(Controller controller, View view, Model model) {
		this.controller = (Controller0) controller;
		this.view = (View0) view;
		this.model = model;
		this.gameState = GameState.Intro; // Initialize game state
		this.view.setGameState(gameState);
		pseudoBuilder = new StringBuilder();
		this.time = 2000;
		this.time2 = 2000;
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
			init();

			if (player.getHP() <= 0) {
				gameState = switchState(gameState, false);
				view.setGameState(gameState);
			} else if (player.getHP() > 0 && player.getHP() <= 0) { // && boss.getHP() <= 0) {
				gameState = switchState(gameState, true);
				view.setGameState(gameState);
			}
		}
		if (gameState == GameState.End) {
			model.clear();
			// clean la view /////////////// IMPORTANT
			// proposeRestart();
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

			PNJ W = new PNJ(model, 15, 15, 0);
			// new Bot3HP(m_brain, W);

			//
			// PNJ T = new PNJ(m_model, 10, 10, 0);
			// new TrackerBot(m_brain, T);
			//
			// PNJ SH = new PNJ(m_model, 2, 2, 0);
			// new SafeHunterBot(m_brain, SH);

		}
	}

	public enum GameState {
		Intro, Pseudo, Playing, End, Level1Win, Restart, Level2;
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
			if (b)
				gameState = GameState.Level1Win;
			else
				gameState = GameState.End;

			break;
		case Level1Win:
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

}