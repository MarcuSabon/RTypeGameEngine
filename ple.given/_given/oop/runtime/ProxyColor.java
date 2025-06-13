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

import oop.graphics.Graphics;

public class ProxyColor implements oop.graphics.Color {
  
  static void init() {
    Graphics.Colors.white = new ProxyColor(255, 255, 255);
    Graphics.Colors.lightGray = new ProxyColor(192, 192, 192);
    Graphics.Colors.gray = new ProxyColor(128, 128, 128);
    Graphics.Colors.darkGray = new ProxyColor(64, 64, 64);
    Graphics.Colors.black = new ProxyColor(0, 0, 0);
    Graphics.Colors.red = new ProxyColor(255, 0, 0);
    Graphics.Colors.pink = new ProxyColor(255, 175, 175);
    Graphics.Colors.orange = new ProxyColor(255, 200, 0);
    Graphics.Colors.yellow = new ProxyColor(255, 255, 0);
    Graphics.Colors.green = new ProxyColor(0, 255, 0);
    Graphics.Colors.magenta = new ProxyColor(255, 0, 255);
    Graphics.Colors.cyan = new ProxyColor(0, 255, 255);
    Graphics.Colors.blue = new ProxyColor(0, 0, 255);
  }
  
  private int argb; // a, r, g, b;

  public ProxyColor(int argb) {
    this.argb = argb;
  }

  public ProxyColor(int r, int g, int b) {
    argb = ((0xff)<<24) | ((r&0xff)<<16) | ((g&0xff)<<8) | ((b&0xff)<<0); 
  }

  public ProxyColor(int a, int r, int g, int b) {
    argb = ((a&0xff)<<24) | ((r&0xff)<<16) | ((g&0xff)<<8) | ((b&0xff)<<0); 
  }

  ProxyColor(java.awt.Color c) {
    this(c.getAlpha(), c.getRed(),c.getBlue(),c.getGreen());
  }

  @Override
  public int alpha() {
    return (argb>>24)&0xFF;
  }

  @Override
  public int red() {
    return (argb>>16)&0xFF;
  }

  @Override
  public int green() {
    return (argb>>8)&0xFF;
  }

  @Override
  public int blue() {
    return (argb>>0)&0xFF;
  }

}
