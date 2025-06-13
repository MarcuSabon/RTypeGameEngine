/*
 *  Copyright (C) Pr. Olivier Gruber <olivier dot gruber at acm dot org>
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package oop.runtime;

import java.awt.Graphics2D;

import oop.graphics.Color;
import oop.graphics.Font;

public class ProxyGraphics implements oop.graphics.Graphics {

  CanvasService m_cs;
  java.awt.Graphics2D m_g;
  Color m_c;
  ProxyFont m_font;

  public ProxyGraphics(CanvasService cs, java.awt.Graphics2D g) {
    m_cs = cs;
    m_font = new ProxyFont(g);
    m_c = new ProxyColor(g.getColor());
    m_g = g;
  }

  @Override
  public Color getColor(int a, int r, int g, int b) {
    return new ProxyColor(a, r, g, b);
  }
  
  @Override
  public Graphics2D getGraphics2D() {
    return m_g;
  }
  
  @Override
  public Font getFont(String name, int styles, int size) {
    java.awt.Font o = m_g.getFont();
    java.awt.Font f = new java.awt.Font(name,styles,size);
    m_g.setFont(f);
    Font font = new ProxyFont(m_g);
    m_g.setFont(o);
    return font;
  }
  
//  public static final String PLAIN = "plain";
//  public static final String BOLD = "bold";
//  public static final String ITALIC = "italic";
//  public static final String BOLD_ITALIC = "bolditalic";
//
//  @Override
//    public Font getFont(String name, String style, String size) {
//    java.awt.Font o = m_g.getFont();
//    java.awt.Font f = java.awt.Font.decode(name + "-" + style + "-" + size);
//    if (f == null)
//      return null;
//    m_g.setFont(f);
//    Font font = new ProxyFont(m_g);
//    m_g.setFont(o);
//    return font;
//  }

  @Override
  public Font getFont() {
    return m_font;
  }

  @Override
  public void setFont(Font f) {
    m_font = (ProxyFont)f;
    m_g.setFont(m_font.font);
  }

  @Override
  public void drawString(String str, int x, int y) {
    m_g.drawString(str, x, y);
  }

  @Override
  public Color getColor() {
    return m_c;
  }

  @Override
  public void setColor(Color c) {
    m_c = c;
    m_g.setColor(new java.awt.Color(c.red(), c.green(), c.blue()));
  }

  @Override
  public void drawLine(int x1, int y1, int x2, int y2) {
    m_g.drawLine(x1, y1, x2, y2);
  }

  @Override
  public void drawRect(int x, int y, int w, int h) {
    m_g.drawRect(x, y, w, h);
  }

  @Override
  public void fillRect(int x, int y, int w, int h) {
    m_g.fillRect(x, y, w, h);
  }

  @Override
  public void drawOval(int x, int y, int width, int height) {
    m_g.drawOval(x, y, width, height);
  }

  @Override
  public void fillOval(int x, int y, int width, int height) {
    m_g.fillOval(x, y, width, height);
  }

  @Override
  public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
    m_g.drawPolygon(xPoints, yPoints, nPoints);
  }

  @Override
  public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
    m_g.fillPolygon(xPoints, yPoints, nPoints);
  }

  @Override
  public void setClip(int x, int y, int width, int height) {
    m_g.setClip(x, y, width, height);
  }

}
