package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.LinkedList;

import Avatars.*;
import engine.IModel;
import engine.model.Entity;
import engine.view.Avatar;
import engine.view.View;
import oop.graphics.Canvas;

public class View0 extends View {
	
	LinkedList<Avatar> listA;

	public View0(Canvas canvas, IModel model) {
		super(canvas, model);
		listA = new LinkedList();
	}

	public void focus(int px, int py) {
		super.px = px;
		super.py = py;
	}

	public void move(double x, double y) {
		// pas de mouvement venant des touches
	}

	

	public void subPaint(Graphics2D g, double x, double y, float zoom) {
		g.setColor(java.awt.Color.WHITE);
		p = m_model.player();
		Iterator i = listA.iterator();
		g.scale(zoom, zoom);
		while (i.hasNext()) {
			Avatar a = (Avatar) i.next();
			if(a != null) {
				a.render(g);
			}
			
		}
		

	}

	@Override
	public void tick(int ms) {

		oX = 0;
		oY = 0;

	}

	@Override
	public void birth(Entity e) {
		Avatar a = null;
		switch(e.getClass().getSimpleName()) {
		case "Player":
			a = new AvatarPlayer(this,e);
			break;
		case "Bullet":
			a = new BulletAvatar(this,e);
			break;
		case "PNJ":
			a = new AvatarPNJ(this,e);
			break;
			
		}
		
		if(a != null) {
			listA.add(a);
			e.avatar = a;
		}
		
		
	}

	@Override
	public void death(Entity e) {
		listA.remove(e.avatar);
		
	}

	@Override
	public void scrollLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scrollRight() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scrollUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scrollDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void zoomIn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void zoomOut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetZoom() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int px() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int py() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double toPixel(double x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double toMetricPixel(double x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double mouseViewportX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double mouseViewportY() {
		// TODO Auto-generated method stub
		return 0;
	}



}
