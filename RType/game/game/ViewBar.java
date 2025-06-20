package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;

import entities.Player;
import oop.graphics.Canvas;

public class ViewBar {

	private Canvas canvas;
	private Font retroFont;

	public ViewBar(Canvas canvas, String fontPath) {
		this.canvas = canvas;
		try {
			InputStream is = getClass().getResourceAsStream(fontPath);
			if (is != null) {
				Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
				retroFont = baseFont.deriveFont(Font.PLAIN, 30f); // adapte la taille du texte
			}
		} catch (IOException | FontFormatException e) {
			System.err.println("Erreur de chargement de la police : " + e.getMessage());
		}
	}

	public void draw(Graphics2D g, Player p) {
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		int barHeight = (int) (height * 0.1); // prend 10% de la hauteur

		g.setColor(Color.BLACK);
		g.fillRect(0, height - barHeight, width, barHeight);

		g.setFont(retroFont);
		g.setColor(Color.GREEN);

		// Pseudo scanf a faire
		String text = "Pseudo: ABC    Score: " + p.getScore() + "    Vie: " + p.getHP();
		int textWidth = g.getFontMetrics().stringWidth(text);
		int textHeight = g.getFontMetrics().getHeight();
		g.drawString(text, (width - textWidth) / 2, height - barHeight / 2 + textHeight / 4);
	}

}
