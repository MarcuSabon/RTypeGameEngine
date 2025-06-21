package game;

import java.util.LinkedList;
import java.util.List;

import engine.brain.Brain;
import engine.brain.FSM;
import engine.controller.Controller;
import engine.model.Model;
import engine.model.PNJ;
import engine.model.Player;
import engine.view.View;
import gal.ast.AST;
import gal.ast.export.Ast2FSM;
import gal.parser.Parser;

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
	private List<FSM> m_fsm_list;
	private int time;

	public GameManager(Controller controller, View view, Model model) {
		this.controller = (Controller0) controller;
		this.view = (View0) view;
		this.model = model;
		this.gameState = GameState.Intro; // Initialize game state
		this.view.setGameState(gameState);
		pseudoBuilder = new StringBuilder();
		this.time = 2000;
	}

	public void start(int elapsed) {
		if (gameState == GameState.Intro) {
			time -= elapsed;
			if (time < 0) {
				gameState = switchState(gameState);
				view.setGameState(gameState);
			}
		}
		if (gameState == GameState.Pseudo) {
			if (getBoole()) {
				gameState = switchState(gameState);
				view.setGameState(gameState);
			}
		}

		if (gameState == GameState.Playing) {
			init();

			if (player.getHP() <= 0) {
				gameState = switchState(gameState);
				view.setGameState(gameState);
			}
		}
		if (gameState == GameState.End) {
			model.clear();
			// proposeRestart();

		}
	}

	public boolean getBoole() {
		return controller.boole;
	}

	public boolean getBooleRealse() {
		return controller.boolRealse;
	}

	public void setboolRealse() {
		controller.boolRealse = true;
	}

	public void setNomDonnee() {
		view.setNomDonnee(getBoole());
	}

	public void init() {
		if (!getBooleRealse()) {
			setNomDonnee();
			m_brain = new Brain(model);

			player = new Player(model, 5, 5, 0);

			Player.name = GameManager.pseudoBuilder.toString();
			setboolRealse();

			PNJ W = new PNJ(model, 15, 15, 0);
			// new Bot3HP(m_brain, W);

			//
			// PNJ T = new PNJ(m_model, 10, 10, 0);
			// new TrackerBot(m_brain, T);
			//
			// PNJ SH = new PNJ(m_model, 2, 2, 0);
			// new SafeHunterBot(m_brain, SH);

			m_fsm_list = loadAutomata("parser/gal/demo/test/GAL2025.gal");
		}
	}

	private List<FSM> loadAutomata(String filename) {
		try {
			AST ast = Parser.from_file(filename);

			Ast2FSM converter = new Ast2FSM();

			Object result = ast.accept(converter);

			if (result instanceof List<?>) {
				@SuppressWarnings("unchecked")
				List<FSM> fsmList = (List<FSM>) result;
				return fsmList;

			} else {
				System.err.println("Erreur: Le résultat de la conversion n'est pas une liste de FSM");
				return new LinkedList<FSM>();
			}
		} catch (Exception ex) {
			System.err.println("Erreur lors du chargement des automates: " + ex.getMessage());
			return new LinkedList<FSM>();
		}
	}

	public enum GameState {
		Intro, Pseudo, Playing, End;
	}

	public GameState getGameState() {
		return gameState;
	}

	public GameState switchState(GameState gameState) {
		switch (gameState) {
		case Intro:
			gameState = GameState.Pseudo;
			break;
		case Pseudo:
			gameState = GameState.Playing;
			break;
		case Playing:
			gameState = GameState.End;
			break;
		default:
			return gameState;
		}
		return gameState;
	}

}