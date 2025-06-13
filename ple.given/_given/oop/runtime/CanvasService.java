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

import oop.graphics.Canvas;

public abstract class CanvasService implements Canvas {

  public CanvasService() {
    ProxyColor.init();
  }

  abstract void windowOpened(GameCanvas gc);

  abstract void windowClosed();

  abstract void setBounds(int width, int height);

  abstract void pressed(int modifiers, int keyCode, char keyChar);

  abstract void released(int modifiers, int keyCode, char keyChar);

  abstract void typed(int modifiers, int keyCode, char keyChar);

  abstract void moved(int modifiers, int x, int y);

  abstract void pressed(int bno, int modifiers, int x, int y);

  abstract void released(int bno, int modifiers, int x, int y);
}
