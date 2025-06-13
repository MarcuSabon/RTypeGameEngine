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

import java.awt.image.BufferedImage;

/*
 * A canvas is a service giving access to a 
 * surface of pixels, a mouse, and a keyboard.
 */

public interface Canvas {

  public void setCursor(BufferedImage img);
  
  /*
   * Returns the width and height, in pixels,
   * of the pixel surface associated with this
   * canvas service. These methods may only be 
   * invoked once the pixel surface has been
   * notified as visible.
   */
  int getWidth();

  int getHeight();

  /*
   * Requests a repaint of the pixel surface
   * associated with this canvas service. 
   * This method may only be invoked once 
   * the pixel surface has been notified 
   * as visible.
   */
  void repaint();

  /*
   * Set the listener for the callbacks related
   * to the pixel surface associated with this
   * canvas service. The callbacks will be on
   * the task setting up this listener.
   * 
   * Nota Bene: this listener must be set and
   * it must be set as the first listener. It 
   * can only be set once.
   */
  void set(PaintListener l);

  /*
   * This is the listener for the pixel surface.
   * The first callback is about the surface
   * becoming visible. Then, there is a callback
   * every time the surface needs to be repainted.
   * Finally, the last callback informs that the
   * canvas service has shutdown and the pixel surface 
   * is no longer visible.
   */
  public interface PaintListener {

    /*
     * Callback to notify when the pixel surface
     * is up and visible. Paint callbacks will 
     * follow until this listener is revoked.
     */
    void visible(Canvas canvas);

    /*
     * Ask a repaint of the pixel surface,
     * using the given graphics. The repainting
     * must happen synchronously, on this callback.
     * The given graphics is only valid during this
     * call. Using it asynchronously is illegal and
     * has no undefined behavior.
     */
    void paint(Canvas canvas, Graphics g);

    /*
     * This is the last callback indicating
     * the canvas service has shutdown.
     */
    void revoked(Canvas canvas);
  }

  /*
   * This is the listener for key codes and characters.
   * The callbacks will happen on the task that has set
   * this listener. When the canvas service will shutdown,
   * this listener will be revoked.
   * 
   * The callbacks: pressed and released.
   *   Whenever a key is pressed on the keyboard, 
   *   it generates a callback "pressed" and a callback
   *   "released" when released.
   *   
   *   The known key codes are defined in the interface
   *   oop.streams.VirtualKeyCodes. Note also that there is
   *   not always a character matching every key code or
   *   a key code matching every characters. See 
   *   VirtualKeyCodes.CHAR_UNDEFINED and
   *   VirtualKeyCodes.VK_UNDEFINED.
   *
   * The callback: typed
   *   This callback provides the last unicode character 
   *   typed on the keyboard, but understand that it may not
   *   be the last key pressed. Indeed, many characters 
   *   require a composition of several key strokes, 
   *   like upper-case letters that require a composition
   *   of SHIFT and a regular lower-case key stroke.
   */
  public interface KeyListener {
    // See oop.streams.VirtualKeyCodes
    void pressed(Canvas canvas, int keyCode, char keyChar);

    // See oop.streams.VirtualKeyCodes
    void released(Canvas canvas, int keyCode, char keyChar);

    void typed(Canvas canvas, char keyChar);

  }

  /*
   * Nota Bene: this listener may not be set and
   * must not be set as the first listener. It 
   * can only be set once.
   * The callbacks will be on  the task setting up 
   * this listener.
   */
  void set(KeyListener l);

  /*
   * This is the listener for mouse events.
   *  - moved: provides the new location of the mouse,
   *  - pressed/released: provides button notifications
   *  
   * Nota Bene: 
   *   - the x-axis is the horizontal one and is oriented
   *     from left to right.
   *   - the y-axis is the vertical one and is oriented
   *     from top to bottom, that is, it is oriented 
   *     downward. 
   */
  public interface MouseListener {
    void moved(Canvas canvas, int x, int y);

    void pressed(Canvas canvas, int bno, int x, int y);

    void released(Canvas canvas, int bno, int x, int y);
  }

  /*
   * Nota Bene: this listener may not be set and
   * must not be set as the first listener. It 
   * can only be set once.
   * The callbacks will be on  the task setting up 
   * this listener.
   */
  void set(MouseListener l);

  String[] listFontNames();

}
