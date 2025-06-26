package engine;

import java.util.Iterator;

import engine.brain.Brain;
import engine.model.Entity;
import entities.Player;
import game.GameManager.GameState;

public interface IModel {

	int ncols();

	int nrows();

	Player player();

	Iterator<Entity> entities();

	Entity entity(int r, int c);

	public class Config {
		// true if the model behaves as a tore on both axis.
		public boolean tore;

		// true if the model is continuous, false if discrete
		public boolean continuous;

		public GameState gameState;

		public boolean immortal;

		public boolean trackerImmortal;
	}

	Config config();

	void config(Config c);

	double metric();

	int toCellCoordinate(double x);

	public void register(IView v);

	public void unregister(IView v);

	void tick(int elapsed);

	boolean move(Entity e, int nrows, int ncols);

	void cerebrate(Brain b);

	void decerebrate(Brain b);

	Brain getBrain();

}