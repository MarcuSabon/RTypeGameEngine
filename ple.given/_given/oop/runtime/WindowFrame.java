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

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class WindowFrame extends JFrame {
  private static final long serialVersionUID = 1L;

  GameCanvas m_canvas;
  
  WindowFrame(Dimension d, GameCanvas canvas) {
    m_canvas = canvas;
    setFocusTraversalKeysEnabled(false); 
    enableEvents(RunnableEvent.EVENT_ID);
    setSize(d);
    addWindowListener(new WindowListener());
    // center the window on the screen
    setLocationRelativeTo(null);
    // make the vindow visible
    setVisible(true);
  }
  
  private class WindowListener implements java.awt.event.WindowListener {

    @Override
    public void windowOpened(WindowEvent e) {
      setLayout(new BorderLayout());
      add(m_canvas, BorderLayout.CENTER);
      m_canvas.windowOpened(WindowFrame.this);
    }

    @Override
    public void windowClosing(WindowEvent e) {
      m_canvas.windowClosed(WindowFrame.this);
    }

    @Override
    public void windowClosed(WindowEvent e) {
      // OG: don't know why, but we do not 
      //     get this event... but we do get
      //     the window-closing one.
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
  }

  public class RunnableEvent extends AWTEvent implements Runnable {
    private static final long serialVersionUID = 1L;
    public static final int EVENT_ID = AWTEvent.RESERVED_ID_MAX + 1;
    Runnable runnable;

    RunnableEvent(Object target, Runnable runnable) {
      super(target, EVENT_ID);
      this.runnable = runnable;
    }

    public void run() {
      runnable.run();
    }
  }

}
