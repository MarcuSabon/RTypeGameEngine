package game;

import java.awt.Graphics2D;

import engine.IModel.Config;
import engine.controller.Controller;
import engine.model.Model;
import engine.view.View;
import game.GameManager.GameState;
import oop.graphics.Canvas;

public class Game {
	private Canvas m_canvas;
	private Model m_model;
	private View m_view;
	private Controller m_controller;
	private GameManager gameManager;

	private double scrollTimer = 0;
	private final double SCROLL_INTERVAL = 500;

	Game(Canvas canvas, int nrows, int ncols) {
		this.m_canvas = canvas;

		Config conf = new Config();
		conf.tore = false;
		conf.gameState = GameState.Intro;
//		conf.continuous = true; // continuous model

		m_model = new Model(nrows, ncols);
		m_model.config(conf); // configure before adding entities

		m_view = new View0(canvas, m_model);

		m_controller = new Controller0(canvas, m_model, m_view);
		gameManager = new GameManager(this, m_controller, m_view, m_model, conf.gameState);

		new Ticker(this);

	}

	public void paint(Canvas canvas, Graphics2D g) {
		m_view.paint(canvas, g);
	}

	public void tick(int elapsed) {
		m_view.tick(elapsed);
		m_model.tick(elapsed);
		gameManager.start(elapsed);
	}

}
