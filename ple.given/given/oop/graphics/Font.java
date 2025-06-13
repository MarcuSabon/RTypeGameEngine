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

public interface Font {

  /**
   * The plain style constant.
   */
  public static final int PLAIN       = 0;

  /**
   * The bold style constant.  This can be combined with the other style
   * constants (except PLAIN) for mixed styles.
   */
  public static final int BOLD        = 1;

  /**
   * The italicized style constant.  This can be combined with the other
   * style constants (except PLAIN) for mixed styles.
   */
  public static final int ITALIC      = 2;

  /*
   * Returns the font size in pixels.
   */
  public int getSize();

  /*
   * Returns the font name.
   */
  public String getName();

  /*
   * Gets the standard height of a line of text in this font.  This
   * is the distance between the baseline of adjacent lines of text.
   * It is the sum of the leading + ascent + descent.
   */
  public int getHeight();

  /*
  * Determines the leading of this font.
  * The leading, also called inter-line spacing, is the logical amount 
  * of space to be reserved between the descent of one line of text 
  * and the ascent of the next line. 
  * The height metric is calculated to include this extra space.
  */
  public int getLeading();

  /*
   * Determines the ascent of this font.
   * The font ascent is the distance from the font's baseline to 
   * the top of most alphanumeric characters. 
   * Note however that some characters in this font 
   * might extend above the font ascent line.
   */
  public int getAscent();

  /*
   * Determines the descent of this font.
   * The font descent is the distance from the font's baseline 
   * to the bottom of most alphanumeric characters with descenders. 
   * Note however that some characters in the font might extend
   * below the font descent line.
   */
  public int getDescent();
  
  /*
   * Returns the advance width of the specified character in this
   * font. The advance is the distance from the leftmost point to 
   * the rightmost point on the character's baseline.
   *   
   * Note that the advance of a string is not necessarily the sum 
   * of the advances of its characters.
   */
  public int getWidth(char c);
  
  /*
   * Returns the total advance width for the specified string
   * in this font. The advance is the distance from the leftmost 
   * point to the rightmost point on the string's baseline.
   * 
   * Note that the advance of a {@code String} is
   * not necessarily the sum of the advances of its characters.
   */
  public int getWidth(String s);
}