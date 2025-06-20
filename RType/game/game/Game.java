package game;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import bot.Bot3HP;
import bot.PlayerBot;
import engine.IModel.Config;
import engine.brain.Brain;
import engine.brain.FSM;
import engine.controller.Controller;
import engine.model.Model;
import engine.model.PNJ;
import engine.view.View;
import entities.BasicPNJ;
import entities.Player;
import entities.Shooter;
import entities.Tracker;
import gal.ast.AST;
import gal.ast.export.Ast2FSM;
import gal.parser.Parser;
import oop.graphics.Canvas;

public class Game {
	private Canvas m_canvas;
	private Model m_model;
	private View m_view;
	private Controller m_controller;
	private Brain m_brain;
	private Ticker m_ticker;

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

		Player P = new Player(m_model, 5, 5, 0);
		new PlayerBot(m_brain, P);

		m_ticker = new Ticker(this);

		PNJ HP = new BasicPNJ(m_model, 15, 15, 0);
		new Bot3HP(m_brain, HP);

		new Shooter(m_model, 16, 16, 0);
		new Tracker(m_model, 17, 17, 0);
//
//		PNJ T = new PNJ(m_model, 10, 10, 0);
//		new TrackerBot(m_brain, T);
//
//		PNJ SH = new PNJ(m_model, 2, 2, 0);
//		new SafeHunterBot(m_brain, SH);

		m_fsm_list = loadAutomata("parser/gal/demo/test/GAL2025.gal");
	}

	public void paint(Canvas canvas, Graphics2D g) {
		m_view.paint(canvas, g);
	}

	public void tick(int elapsed) {
		m_model.tick(elapsed);
		m_view.tick(elapsed);
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

}
