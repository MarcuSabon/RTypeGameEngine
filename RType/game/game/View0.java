package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import avatars.AvatarBullet;
import avatars.AvatarMissile;
import avatars.AvatarPNJ;
import avatars.AvatarPlayer;
import avatars.AvatarShooter;
import avatars.AvatarTower;
import avatars.AvatarTracker;
import avatars.AvatarWall;
import boss.AvatarBossPart;
import boss.AvatarMaster;
import boss.BossPart;
import boss.Master;
import engine.IModel;
import engine.model.Entity;
import engine.view.Avatar;
import engine.view.View;
import entities.BasicPNJ;
import entities.Bullet;
import entities.Missile;
import entities.Player;
import entities.Shooter;
import entities.Tower;
import entities.Tracker;
import game.GameManager.GameState;
import map.Wall;
import oop.graphics.Canvas;

public class View0 extends View {
	private ScrollingBackground background;
	private ViewBar viewBar;
	private BufferedImage backgroundImage;
	private GameState gameState;
	private Animator animExplo;
	private Animator anim;
	private Animator animLaser;
	private Graphics2D g;

	protected boolean nomDonnee;

	public View0(Canvas canvas, IModel model) {
		super(canvas, model);
		background = new ScrollingBackground(canvas, "/space.png");
		viewBar = new ViewBar(canvas, "/retroGaming.ttf");
		animExplo = new Animator("/Explosion", 47, 1);
		anim = new Animator("/Flame", 3, 10);
		animLaser = new Animator("/LaserShot", 16, 1);
		nomDonnee = false;
	}

	@Override
	public void subPaint(Canvas canvas, Graphics2D g) {
		switch (gameState) {
		case Intro:
			background(g, canvas, "/landscape_comp.jpeg");
			break;
		case Pseudo:
			background(g, canvas, "/main_fond.png");
			choisirPseudo(canvas, g);
			break;
		case Playing:
			this.g = g;
			background.draw(g);
			Player p = m_model.player();
			viewBar.draw(g, p);
			g.scale(zoom, zoom);

			for (Avatar a : m_visibleAvatars)
				a.render(g);
			break;
		case End:
			background(g, canvas, "/gameOver.jpg");
			break;
		case Restart:
			background(g, canvas, "/main_fond.png");
			choisir_restart_leave(canvas, g);
			break;
		default:
			System.err.println("Unknown game state: " + gameState);
			break;
		}

	}

	@Override
	public void subtick(int elapsed) {
		background.update(elapsed);
	}

	@Override
	public void birth(Entity e) {
		Avatar a = null;
		if (e instanceof Player)
			a = new AvatarPlayer(this, e);
		else if (e instanceof Bullet)
			a = new AvatarBullet(this, e);
		else if (e instanceof Tracker)
			a = new AvatarTracker(this, e);
		else if (e instanceof Shooter)
			a = new AvatarShooter(this, e);
		else if (e instanceof BasicPNJ)
			a = new AvatarPNJ(this, e);
		else if (e instanceof Master)
			a = new AvatarMaster(this, e, e.sprite());
		else if (e instanceof BossPart)
			a = new AvatarBossPart(this, e, e.sprite());
		else if (e instanceof Missile)
			a = new AvatarMissile(this, e);
		else if (e instanceof Wall)
			a = new AvatarWall(this, e);
		else if (e instanceof Tower)
			a = new AvatarTower(this, e);

		if (a != null)
			m_visibleAvatars.add(a);
		else
			System.err.println("Avatar not found for entity: " + e.getClass().getSimpleName());
	}

	public int explosion(double x, double y, int frame, double size, int orientation) {
		return animExplo.print(g, x, y, frame, size, orientation);
	}

	public int flamme(double x, double y, int frame, double size, int orientation) {
		return anim.print(g, x, y, frame, size, orientation);
	}

	public int laser(double x, double y, int frame, double size, int orientation) {
		return animLaser.print(g, x, y, frame, size, orientation);
	}

	public class Animator {
		private BufferedImage[] images;
		private int actualFrame = 0;
		private int delay;

		public Animator(String folderpath, int nbframes, int delay) {
			this.delay = delay;
			images = new BufferedImage[nbframes];
			for (int i = 0; i < nbframes; i++) {
				try {
					images[i] = ImageIO.read(getClass().getResource(folderpath + "/" + i + ".png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public BufferedImage actualImage() {
			return images[actualFrame];
		}

		int Frames = delay;

		public int print(Graphics2D g, double posX, double posY, int frame, double size, double orientation) {
			int cellWidth = pxPerMeter();
			BufferedImage image = images[frame];

			AffineTransform transform = new AffineTransform();
			transform.translate(posX, posY);

			double scale = (double) cellWidth / Math.max(image.getWidth(), image.getHeight());
			scale *= size;
			transform.scale(scale, scale);

			transform.translate(-image.getWidth() / 2.0, -image.getHeight() / 2.0);

			double rad = Math.toRadians(orientation);
			transform.rotate(rad);

			g.drawImage(image, transform, null);
			if (frame >= images.length - 1) {
				return 0;
			} else {
				return frame + 1;
			}
		}
	}

	public void setNomDonnee(boolean bool) {
		this.nomDonnee = bool;
	}

	// Private Methods

	private Font getFont(float taille) {

		Font retroFont = null;
		try {
			InputStream is = getClass().getResourceAsStream("/retroGaming.ttf");
			if (is != null) {
				Font baseFont1 = Font.createFont(Font.TRUETYPE_FONT, is);
				retroFont = baseFont1.deriveFont(Font.PLAIN, taille); // adapte la taille du texte
			}
		} catch (IOException | FontFormatException e) {
			System.err.println("Erreur de chargement de la police : " + e.getMessage());
		}
		return retroFont;
	}

	private void choisirPseudo(Canvas canvas, Graphics2D g) {
		String titre = "Choisissez un pseudo";
		String sousTexte = GameManager.pseudoBuilder.toString();
		String instruction = "Appuyez sur Entrée pour valider";

		// Fonts
		Font titreFont = getFont(36f).deriveFont(Font.BOLD);
		Font pseudoFont = getFont(28f);
		Font instructionFont = getFont(16f);

		// Mesures
		FontMetrics fontMetricTitre = g.getFontMetrics(titreFont);
		FontMetrics fontMetricPseudo = g.getFontMetrics(pseudoFont);
		FontMetrics fontMetricInstruction = g.getFontMetrics(instructionFont);

		int width = m_canvas.getWidth();
		int height = m_canvas.getHeight();

		// Taille du bloc à dessiner
		int boxWidth = 600;
		int boxHeight = 200;
		int boxX = (width - boxWidth) / 2;
		int boxY = (height - boxHeight) / 2;

		// Fond semi-transparent avec coins arrondis
		g.setColor(new Color(0, 0, 0, 180)); // noir transparent
		g.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 30, 30);

		// Dessin des textes
		g.setColor(Color.WHITE);

		g.setFont(titreFont);
		int titreX = boxX + (boxWidth - fontMetricTitre.stringWidth(titre)) / 2;
		int titreY = boxY + 40;
		g.drawString(titre, titreX, titreY);

		g.setFont(pseudoFont);
		int pseudoX = boxX + (boxWidth - fontMetricPseudo.stringWidth(sousTexte)) / 2;
		int pseudoY = titreY + 40;
		g.drawString(sousTexte, pseudoX, pseudoY);

		g.setFont(instructionFont);
		int instructionX = boxX + (boxWidth - fontMetricInstruction.stringWidth(instruction)) / 2;
		int instructionY = boxY + boxHeight - 20;
		g.drawString(instruction, instructionX, instructionY);

	}

	private void background(Graphics2D g, Canvas canvas, String imagePath) {
		try {
			backgroundImage = ImageIO.read(getClass().getResource(imagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, m_canvas.getWidth(), m_canvas.getHeight(), null);
		} else {
			g.setColor(java.awt.Color.LIGHT_GRAY);
			g.fillRect(0, 0, m_canvas.getWidth(), m_canvas.getHeight());
		}
	}

	private void choisir_restart_leave(Canvas canvas, Graphics2D g) {
		String titre = "New Game";
		String instruction = "Quit";

		// Fontes
		Font pseudoFont = getFont(28f);

		// Mesures
		FontMetrics fontMetricPseudo = g.getFontMetrics(pseudoFont);

		int width = m_canvas.getWidth();
		int height = m_canvas.getHeight();

		// Taille du bloc à dessiner
		int boxWidth = 600;
		int boxHeight = 200;
		int boxX = (width - boxWidth) / 2;
		int boxY = (height - boxHeight) / 2;

		// Fond semi-transparent avec coins arrondis
		g.setColor(new Color(0, 0, 0, 180)); // noir transparent
		g.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 30, 30);

		// Dessin des textes
		g.setColor(Color.WHITE);

		if (((GameManager.surligne % 2) + 2) % 2 == 0) {
			g.setColor(Color.YELLOW); // toujours restart
		} else {
			g.setColor(Color.WHITE);
		}
		g.setFont(pseudoFont);
		int titreX = boxX + (boxWidth - fontMetricPseudo.stringWidth(titre)) / 2;
		int titreY = boxY + 60;
		g.drawString(titre, titreX, titreY);

		if (((GameManager.surligne % 2) + 2) % 2 == 1) {
			g.setColor(Color.YELLOW); // toujours quit
		} else {
			g.setColor(Color.WHITE);
		}
		g.setFont(pseudoFont);
		int instructionX = boxX + (boxWidth - fontMetricPseudo.stringWidth(instruction)) / 2;
		int instructionY = boxY + boxHeight - 50;
		g.drawString(instruction, instructionX, instructionY);

	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
}
