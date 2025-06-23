package game;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import boss.Master;
import engine.IModel.Config;
import engine.brain.Brain;
import engine.brain.FSM;
import engine.controller.Controller;
import engine.model.Model;
import engine.view.View;
import entities.BasicPNJ;
import entities.Player;
import entities.Shooter;
import entities.Tower;
import entities.Tracker;
import gal.ast.AST;
import gal.ast.export.Ast2FSM;
import gal.parser.Parser;
import map.MapLoader;
import map.SpawnData;
import oop.graphics.Canvas;

public class Game {
	private Canvas m_canvas;
	private Model m_model;
	private View m_view;
	private Controller m_controller;
	private Brain m_brain;
	private Ticker m_ticker;
	private MapLoader m_loader;

	private double scrollTimer = 0;
	private final double SCROLL_INTERVAL = 500;

	private List<FSM> m_fsm_list;

	Game(Canvas canvas, int nrows, int ncols) {
		this.m_canvas = canvas;

		Config conf = new Config();
		conf.tore = false;
//		conf.continuous = true; // continuous model

		m_model = new Model(nrows, ncols);
		m_model.config(conf); // configure before adding entities

		m_view = new View0(canvas, m_model);
		m_controller = new Controller0(canvas, m_model, m_view);

		m_brain = new Brain(m_model);

		m_fsm_list = loadAutomata("parser/gal/demo/test/GAL2025.gal");

		new Player(m_model, 5, 5, 0);

		m_ticker = new Ticker(this);

		new Master(m_model, 35, 10, 180, "/Boss/Level1", m_brain);

//		Master M2 = new Master(m_model, 20, 10, 180, "/Boss/Level1", m_brain);
//		Master M3 = new Master(m_model, 15, 10, 180, "/Boss/Level1", m_brain);
//		Master M4 = new Master(m_model, 10, 10, 180, "/Boss/Level1", m_brain);

		new Shooter(m_model, 16, 16, 0);
		new Tracker(m_model, 17, 17, 0);
		new Tower(m_model, 18, 41, 0);
		new Tower(m_model, 1, 41, 0);

		String path = "game/Ressources/Maps/Map1.txt";
		m_loader = new MapLoader(path, m_model);

	}

	public void paint(Canvas canvas, Graphics2D g) {
		m_view.paint(canvas, g);
	}

	public void tick(int elapsed) {
		m_model.tick(elapsed);
		m_view.tick(elapsed);
		SpawnEntities(elapsed);
	}

	// ------------ Private Methods ------------
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

	// spawn les entités de la grille Map.txt
	private void SpawnEntities(int elapsed) {
		if (m_loader != null) {
			m_loader.tick(elapsed, m_model);
			List<SpawnData> spawns = m_loader.RemoveNewEntities();
			for (SpawnData s : spawns) {
				switch (s.type) {
				case '2':
					new BasicPNJ(m_model, s.row, s.col, 0);
					break;
				}
			}
		}
	}

}
