package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;

import Avatars.AvatarPNJ;
import Avatars.AvatarPlayer;
import Avatars.BulletAvatar;
import engine.IModel;
import engine.model.Entity;
import engine.model.PNJ;
import engine.model.Player;
import engine.model.entities.Bullet;
import engine.view.Avatar;
import engine.view.View;
import game.GameManager.GameState;
import oop.graphics.Canvas;

public class View0 extends View {
	private ScrollingBackground background;
	private ViewBar viewBar;
	private BufferedImage backgroundImage;
	protected boolean nomDonnee;
	private GameState gameState;

	public View0(Canvas canvas, IModel model) {
		super(canvas, model);
		background = new ScrollingBackground(canvas, "/space.png");
		viewBar = new ViewBar(canvas, "/retroGaming.ttf");
		nomDonnee = false;
	}

	@Override
	public void subPaint(Canvas canvas, Graphics2D g) {
		switch (gameState) {
		case Intro:
			background(g, canvas, "/landscape_comp.jpeg");
			break;
		case Pseudo:
			background(g, canvas, "/main_fond3.jpg");
			choisirPseudo(canvas, g);
			break;
		case Playing:
			background.draw(g);
			p = m_model.player();
			viewBar.draw(g, p);
			g.scale(zoom, zoom);

			for (Avatar a : m_visibleAvatars)
				a.render(g);
			break;
		case End:
			background(g, canvas, "/gameOver.jpg");
			clear_avatars();
			break;
		case Restart:
			background(g, canvas, "/main_fond3.jpg");
			choisir_restart_leave(canvas, g);
			break;
		default:
			System.err.println("Unknown game state: " + gameState);
			break;
		}

	}

	private void clear_avatars() {
		Iterator<Avatar> it = m_visibleAvatars.iterator();
		while (it.hasNext()) {
			Avatar a = it.next();
			it.remove();

		}
	}

	@Override
	public void tick(int elapsed) {
		background.update(elapsed);
	}

	@Override
	public void birth(Entity e) {
		Avatar a = null;
		if (e instanceof Player)
			a = new AvatarPlayer(this, e);
		else if (e instanceof Bullet)
			a = new BulletAvatar(this, e);
		else if (e instanceof PNJ)
			a = new AvatarPNJ(this, e);

		if (a != null)
			m_visibleAvatars.add(a);
		else
			System.err.println("Avatar not found for entity: " + e.getClass().getSimpleName());
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
