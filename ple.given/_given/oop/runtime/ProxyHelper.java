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

public class ProxyHelper {
  public static oop.graphics.Color newColor(java.awt.Color c) {
    return new ProxyColor(c);
  }
  public static oop.graphics.Font newFont(java.awt.Graphics g) {
    return new ProxyFont(g);
  }
  public static
  oop.graphics.Color newColor(int a, int r, int g, int b) {
    return new ProxyColor(a,r,g,b);
  }
}
