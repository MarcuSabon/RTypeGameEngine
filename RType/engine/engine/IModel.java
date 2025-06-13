package engine;

import java.util.Iterator;

import engine.model.Entity;
import engine.model.Player;

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
	}

	Config config();

	void config(Config c);

	double metric();

	int toCellCoordinate(double x);

	public void register(IView v);

	public void unregister(IView v);

	void tick(int elapsed);

	boolean move(Entity e, int nrows, int ncols);

}