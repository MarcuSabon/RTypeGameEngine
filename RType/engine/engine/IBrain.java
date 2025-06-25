package engine;

import engine.brain.Category;
import engine.brain.FSM;
import engine.model.Entity;

public interface IBrain {

	public interface IBot {

		Category category();

		void think(int elapsed);

		void setFSM(FSM fsm);

		int getPointsValue();

		boolean collision(Entity e); // return si oui ou non il y'a eu collision

		void setCollision(boolean b);

		int getHP();

		void setCollisionWithEntity(Entity entity);

		Entity getCollisionWithEntity();

		public int getNbCollision();

		void setNbCollision(); // permet de savoir au moment d'incrémenter le score du joueur si l'entité est
								// morte d'une collision ou des murs
	}
}
