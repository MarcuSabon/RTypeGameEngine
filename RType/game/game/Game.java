package game;

import java.awt.Graphics2D;

import engine.IModel.Config;
import engine.brain.Brain;
import engine.controller.Controller;
import engine.model.Model;
import engine.view.View;
import oop.graphics.Canvas;

public class Game {
	private Canvas m_canvas;
	private Model m_model;
	private View m_view;
	private Controller m_controller;
	private Brain m_brain;
	private Ticker m_ticker;
	private Controller0 newcontroller;
	private GameManager gameState;

	Game(Canvas canvas, int nrows, int ncols) {
		this.m_canvas = canvas;

		Config conf = new Config();
		conf.tore = false;
//		conf.continuous = true; // continuous model

		m_model = new Model(nrows, ncols);
		m_model.config(conf); // configure before adding entities

		m_view = new View0(canvas, m_model);

		m_controller = new Controller0(canvas, m_model, m_view);
		gameState = new GameManager(m_controller, m_view, m_model);

		m_ticker = new Ticker(this);

	}

	public void paint(Canvas canvas, Graphics2D g) {
		m_view.paint(canvas, g);
	}

	public void tick(int elapsed) {
		m_view.tick(elapsed);
		m_model.tick(elapsed);
		gameState.start(elapsed);

	}

}
