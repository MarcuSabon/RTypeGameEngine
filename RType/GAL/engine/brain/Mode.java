package engine.brain;

import java.util.LinkedList;
import java.util.List;

public class Mode {
	State state;
	List<Transition> transitions;

	public Mode(State newState) {
		this.state = newState;
		this.transitions = new LinkedList<>();
	}

	public void add(ConditionGAL condition, ActionGAL action, State cible) {
		transitions.add(new Transition(condition, action, new Mode(cible)));
	}

	public void add(Transition transition) {
		transitions.add(transition);
	}
}
