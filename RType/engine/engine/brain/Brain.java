package engine.brain;

import java.util.LinkedList;
import java.util.List;

import engine.IBrain;
import engine.IModel;
import engine.model.Entity;

public class Brain implements IBrain {

	private IModel model;
	protected List<IBot> bots;

	public Brain(IModel model) {
		this.setModel(model);
		this.bots = new LinkedList<>();
		model.cerebrate(this);
	}

	public IModel getModel() {
		return model;
	}

	public void setModel(IModel model) {
		this.model = model;
	}
    
	public void killed(Entity entity) {
        if (entity.bot.getNbCollision() >= 1) {
			model.player().setScore(entity);
		}
		bots.remove(entity.bot);
	}
		
}
