package game;

import java.awt.Graphics2D;
import java.util.List;

import engine.IModel.Config;
import engine.controller.Controller;
import engine.model.Model;
import engine.view.View;
import entities.BasicPNJ;
import map.MapLoader;
import map.SpawnData;
import oop.graphics.Canvas;

public class Game {
	private Canvas m_canvas;
	private Model m_model;
	private View m_view;
	private Controller m_controller;
	private GameManager gameManager;
	private MapLoader m_loader;

	private double scrollTimer = 0;
	private final double SCROLL_INTERVAL = 500;

	Game(Canvas canvas, int nrows, int ncols) {
		this.m_canvas = canvas;

		Config conf = new Config();
		conf.tore = false;
//		conf.continuous = true; // continuous model

		m_model = new Model(nrows, ncols);
		m_model.config(conf); // configure before adding entities

		m_view = new View0(canvas, m_model);

		m_controller = new Controller0(canvas, m_model, m_view);
		gameManager = new GameManager(m_controller, m_view, m_model);

		new Ticker(this);

	}

	public void paint(Canvas canvas, Graphics2D g) {
		m_view.paint(canvas, g);
	}

	public void tick(int elapsed) {
		m_view.tick(elapsed);
		m_model.tick(elapsed);
		gameManager.start(elapsed);
		if (gameManager.initialized)
			SpawnEntities(elapsed);
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
