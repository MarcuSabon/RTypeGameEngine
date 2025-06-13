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

/*
 * 
 * The default color space is sRGB, a proposed standard RGB color space.  
 * For further information on sRGB, see 
 *   http://www.w3.org/pub/WWW/Graphics/Color/sRGB.html
 *   
 * Every color has an implicit alpha value of 1.0 or
 * an explicit one provided in the constructor.  The alpha value
 * defines the transparency of a color and can be represented by
 * a value in the range [0-255]. An alpha value of 255 means 
 * that the color is completely opaque and an alpha value of 0 
 * means that the color is completely transparent.   
 */
public interface Color {
  int alpha();

  int red();

  int green();

  int blue();
}
