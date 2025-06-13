package engine.brain;

import java.util.LinkedList;
import java.util.List;

public class FSM {
	private List<Mode> modes;

	public final State intial;

	public FSM(State initial) {
		modes = new LinkedList<>();
		this.intial = initial;
	}

	public FSM(List<Mode> modes, State initial) {
		this.modes = modes;
		this.intial = initial;
	}

	/*
	 * [canonical] returns the unique Mode of the FSM corresponding to the given
	 * name. It creates a new Mode if it does not already exists in [modes].
	 */
	public Mode canonical(String name) {
		for (Mode mode : modes)
			if (mode.state.name.equals(name))
				return mode;

		// If not found, create a new Mode with the given name
		State newState = new State(name);
		Mode newMode = new Mode(newState);
		modes.add(newMode);
		return newMode;
	}

	public boolean transit(Bot b) {
		Mode currentMode = b.mode;

		for (Transition transition : currentMode.transitions)
			if (transition.condition.eval(b) && transition.action.acceptedBy(b)) {
				transition.action.exec(b);
				currentMode.state = transition.target.state;
				return true;
			}

		return false;
	}
}
