package engine.model.entities;


import Stunts.StuntPNJ;
import engine.model.Entity;
import engine.model.Model;

public class Robot extends Entity {

	protected int botType;

	public Robot(Model m, int x, int y, int o, int botType) {
		super(m, x, y, o);
		stunt = new StuntPNJ(m_model, this);
		this.botType = botType;
		

	}
	
	public void move(double x, double y) {
		m_model.move(this, x, y);
	}

	
	protected void subTick(int ms) {

//
		}

	public void subCollision(Entity e) {
		// System.out.println("Aïe j'ai pri un gro "+e.getClass().getSimpleName()+" ca fé mal de ouf sa mere");
	}
	
	public int botType() {
		return botType;
	}

	@Override
	protected void collision(Entity entity) {
		// TODO Auto-generated method stub
		
	}



	


	
}
