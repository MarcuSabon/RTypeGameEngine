package boss;

import java.io.IOException;

import bot.BossBot;
import bot.BossPartBot;
import engine.brain.Brain;
import engine.model.Entity;
import engine.model.Model;
import engine.model.PNJ;

public class Master extends PNJ {
	public Entity[][] body;

	// Le master a besoin du brain pour créer son corps

	public Master(Model m, int r, int c, int o, String filepath, Brain brain) {
		super(m, r, c, o, filepath + "/Master.png");
		new StuntMaster(m, this);
		new BossBot(m.getBrain(), this);
		String[][] StringBody = null;
		try {
			StringBody = new ParserBoss().parseTableauFromFile(filepath + "/Boss");
		} catch (IOException e) {
			System.out.println("Fichier de boss introuvable");
		}
		body = new Entity[StringBody.length][StringBody[0].length];
		int iMaster = 0, jMaster = 0;
		for (int i = 0; i < StringBody.length; i++) {
			for (int j = 0; j < StringBody[0].length; j++) {
				if (StringBody[i][j].equals("9")) {
					iMaster = i;
					jMaster = j;
					break;
				}
			}
		}
		for (int i = 0; i < StringBody.length; i++) {
			// System.out.print("\n");
			for (int j = 0; j < StringBody[0].length; j++) {
				// System.out.print(StringBody[i][j] + " : ");
				switch (StringBody[i][j]) {
				case "9":
					body[i][j] = this;
					break;
				case "0":
					break;
				default:
					Entity b = new BossPart(m_model, i + r - iMaster, j + c - jMaster, 180,
							filepath + "/" + StringBody[i][j], this);
					b.bot = new BossPartBot(brain, b);
					body[i][j] = b;
					break;
				}
			}
		}

	}

	@Override
	protected void collision(Entity entity) {
		bot.setCollision(true);
	}

	public Entity[][] body() {
		return body;
	}

	public void preDie() {
		for (int i = 0; i < body.length; i++) {
			for (int j = 0; j < body[0].length; j++) {
				Entity eExplored = body[i][j];
				if (eExplored != null && !(eExplored instanceof Master)) {
					if (eExplored.isDead()) {
						eExplored = null;
					} else {
						eExplored.die();

					}
				}
			}
		}
	}

}
