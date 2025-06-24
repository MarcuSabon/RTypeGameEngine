package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;

import engine.IModel;
import engine.model.Model;
import engine.view.View;
import entities.Player;
import oop.graphics.Canvas;

public class ViewBar {

	private Canvas canvas;
	private Font retroFont;
	private Model model;
	private View view;

	public ViewBar(Canvas canvas, String fontPath, IModel model, View view) {
		this.canvas = canvas;
		this.model = (Model) model;
		this.view = view;
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
		g.fillRect(0, model.nrows() * view.pxPerMeter(), width + view.pxPerMeter(), height);
		g.fillRect(model.ncols() * view.pxPerMeter() - 2 * view.pxPerMeter(), 0, model.ncols() * view.pxPerMeter(),
				height);
		g.setFont(retroFont);
		g.setColor(Color.GREEN);
		String name = "BDI"; // Valeur par défaut si Player.name est null
		if (Player.name != null && !Player.name.isEmpty()) {
			name = Player.name;
		}
		// Pseudo scanf a faire
		String text = "Pseudo: " + name + "    Score: " + p.getScore() + "    Vie: " + p.getHP();
		int textWidth = g.getFontMetrics().stringWidth(text);
		int textHeight = g.getFontMetrics().getHeight();
		g.drawString(text, (width - textWidth) / 2, height - barHeight / 2 + textHeight / 4);
	}

}
