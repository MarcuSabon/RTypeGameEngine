package gal.ast;

import java.util.LinkedList;
import java.util.List;

public class Automaton extends Node {

	public String name;
	public State initial_state;
	public List<Mode> modes;

	public Automaton(String name, State initial_state, List<Mode> modes) {
		this.name = name;
		this.initial_state = initial_state;
		this.modes = modes;
	}

	// REQUIRED
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public Object accept(IVisitor visitor) {
		visitor.enter(this);

		Object o_initial_state_name = initial_state.accept(visitor);
		
		LinkedList<Object> list = new LinkedList<Object>();
		for (Mode mode : modes) {
			visitor.visit(this);
			Object o = mode.accept(visitor);
			list.add(o);
		}
		
		visitor.exit(this);
		return visitor.build(this, o_initial_state_name, list);
	}

}