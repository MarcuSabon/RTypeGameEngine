package boss;

import engine.model.Model;
import engine.model.PNJ;

public class BossPart extends PNJ {
	public Master master;

	protected BossPart(Model m, int r, int c, int o, String sprite, Master master) {
		super(m, r, c, o, sprite);
		this.master = master;
		new StuntBossPart(m, this);
	}

}
