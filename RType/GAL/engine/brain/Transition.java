package engine.brain;

public class Transition {
	ConditionGAL condition;
	ActionGAL action;
	Mode target;

	public Transition(ConditionGAL condition, ActionGAL action, Mode target) {
		this.condition = condition;
		this.action = action;
		this.target = target;
	}
}
