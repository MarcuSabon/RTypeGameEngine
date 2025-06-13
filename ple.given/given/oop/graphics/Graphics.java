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
package oop.graphics;

import java.awt.Graphics2D;

public interface Graphics {

  /*
   * Predefined colors.
   */
  public class Colors {
    public static Color white;
    public static Color lightGray;
    public static Color gray;
    public static Color darkGray;
    public static Color black;
    public static Color red;
    public static Color pink;
    public static Color orange;
    public static Color yellow;
    public static Color green;
    public static Color magenta;
    public static Color cyan;
    public static Color blue;
  }
  
  public Graphics2D getGraphics2D();


  // gets a color for the given sRGB.
  public Color getColor(int a, int r, int g, int b);

  /*
   *  Gets a font with the given name, styles, and size.
   *  Look at the styles in the interface Font: PLAIN, ITALIC, BOLD.
   *  You can list all the font names from the canvas service
   *  (see the interface Canvas).
   */
  public Font getFont(String name, int styles, int size);

  /*
   * Gets the current font used by this graphics.
   */
  public Font getFont();

  /*
   * Sets the current font used by this graphics.
   */
  public void setFont(Font f);

  /*
   * Draws the given string, using this graphics context's current font 
   * and color. The baseline of the leftmost character is at the given 
   * position (x,y) in the coordinate system of this graphics.
   */
  public void drawString(String str, int x, int y);

  /*
   * Gets this graphics context's current color, use to draw
   * or fill shapes or display characters.
   */
  public Color getColor();

  /*
   * Sets this graphics context's current color, use to draw
   * or fill shapes or display characters.
   */
  public void setColor(Color c);

  /*
   * Draws a line, using the current color, between the given
   * points (x1,y1) and (x2,y2) in the coordinate system of 
   * this graphics.
   */
  public void drawLine(int x1, int y1, int x2, int y2);
  
  /*
   * Draws a rectangle, using the current color.
   * The left and right edges of the rectangle are at x and x+width-1.
   * The top and bottom edges are at y and y+height-1.
   * The resulting rectangle covers an area that is "w" pixels wide 
   * by "h" pixels tall.
   */
  public void drawRect(int x, int y, int w, int h);

  /*
   * Fills a rectangle, using the current color.
   * The left and right edges of the rectangle are at x and x+width-1.
   * The top and bottom edges are at y and y+height-1.
   * The resulting rectangle covers an area that is "w" pixels wide 
   * by "h" pixels tall.
   */
  public void fillRect(int x, int y, int w, int h);

  /*
   * Draws the outline of an oval.
   * The result is a circle or ellipse that fits within the
   * rectangle specified by the (x,y,w,h), that is, 
   * an area that is "w" pixels wide  and "h" pixels tall.
   * The left and right edges of the rectangle are
   * at x and x+width-1. The top and bottom edges 
   * are at y and y+height-1.
   */
  public void drawOval(int x, int y, int w, int h);

  /*
   * Fills the outline of an oval.
   * The result is a circle or ellipse that fits within the
   * rectangle specified by the (x,y,w,h), that is, 
   * an area that is "w" pixels wide  and "h" pixels tall.
   * The left and right edges of the rectangle are
   * at x and x+width-1. The top and bottom edges 
   * are at y and y+height-1.
   */
  public void fillOval(int x, int y, int width, int height);

  /*
   * Draws a closed polygon defined by the given arrays 
   * of (x,y) coordinates. The figure is automatically closed 
   * by drawing a line connecting the final point to the first point, 
   * if those points are different.
   */
  public void drawPolygon(int xPoints[], int yPoints[], int nPoints);

  /*
   * Fills a closed polygon defined by the given arrays 
   * of (x,y) coordinates. The figure is automatically closed 
   * by drawing a line connecting the final point to the first point, 
   * if those points are different.
   */
  public void fillPolygon(int xPoints[], int yPoints[], int nPoints);

  /*
   * Sets the current clip to the rectangle specified by the given
   * coordinates. Rendering operations have no effect outside of 
   * the clipping area.
   * Note: this method sets the user clip, which is independent 
   * of the clipping associated with device bounds and window visibility. 
   */
  public void setClip(int x, int y, int width, int height);

}
