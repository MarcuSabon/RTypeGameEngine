package entities;

import engine.model.Model;
import engine.model.PNJ;
import stunts.StuntBasicPNJ;

public class BasicPNJ extends PNJ {

	public BasicPNJ(Model m, int r, int c, int o) {
		super(m, r, c, o);
		new StuntBasicPNJ(m_model, this);
	}

}
