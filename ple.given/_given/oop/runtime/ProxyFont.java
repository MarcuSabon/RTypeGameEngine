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

import oop.graphics.Font;

public class ProxyFont implements Font {

  public static final String PLAIN = "plain";
  public static final String BOLD = "bold";
  public static final String ITALIC = "italic";
  public static final String BOLD_ITALIC = "bolditalic";

  java.awt.Font font;
  java.awt.FontMetrics fm;

  ProxyFont(java.awt.Graphics g) {
    font = g.getFont();
    fm = g.getFontMetrics();
  }

  void update(java.awt.Graphics g) {
    font = g.getFont();
    fm = g.getFontMetrics();
  }

//  @Override
//  public int getMaxAdvance() {
//    return fm.getMaxAdvance();
//  }

  @Override
  public int getWidth(char c) {
    return fm.charWidth(c);
  }

  @Override
  public int getWidth(String s) {
    return fm.stringWidth(s);
//    int w=0,len = s.length();
//    for (int i=0;i<len;i++) {
//      char c = s.charAt(i);
//      w+=fm.charWidth(c);
//    }
//    return w;
  }

  @Override
  public int getSize() {
    return font.getSize();
  }

  @Override
  public String getName() {
    return font.getName();
  }

  @Override
  public int getHeight() {
    return getLeading() + getAscent() + getDescent();
  }

  @Override
  public int getLeading() {
    return fm.getLeading();
  }

  @Override
  public int getAscent() {
    return fm.getAscent();
  }

  @Override
  public int getDescent() {
    return fm.getDescent();
  }

}
