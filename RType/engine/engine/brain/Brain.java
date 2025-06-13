package engine.brain;

import java.util.LinkedList;
import java.util.List;

import engine.IBrain;
import engine.IModel;

public class Brain implements IBrain {

	protected IModel model;
	protected List<IBot> bots;

	public Brain(IModel model) {
		this.model = model;

		this.bots = new LinkedList<>();
	}

}
