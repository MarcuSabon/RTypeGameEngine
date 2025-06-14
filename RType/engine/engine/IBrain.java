package engine;

import engine.brain.Category;
import engine.brain.FSM;

public interface IBrain {

	public interface IBot {
		Category category();

		void think(int elapsed);

		void setFSM(FSM fsm);

		int getPointsValue();

	}
}
