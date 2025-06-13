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

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class GameCanvas extends Canvas {

  private Image m_buffer1, m_buffer2;
  private Image m_renderBuffer;
  private Image m_drawBuffer;
  private int m_width, m_height;
  private boolean m_swap;
  private CanvasService m_cs;
  private JFrame m_frame;
  private Cursor m_cursor;

  GameCanvas(CanvasService cs) {
    m_cs = cs;
    setBackground(Color.gray);
    setFocusTraversalKeysEnabled(false);
  }

  void setCursor(BufferedImage img) {
    m_cursor = Toolkit.getDefaultToolkit().createCustomCursor(img, new Point(0, 0), "blank cursor");
    if(m_frame!=null)
      m_frame.getContentPane().setCursor(m_cursor);
  }

  void windowOpened(JFrame frame) {
    m_frame = frame;
    if(m_cursor!=null)
      m_frame.getContentPane().setCursor(m_cursor);
    Listener l = new Listener();
    addKeyListener(l);
    addMouseListener(l);
    addMouseMotionListener(l);
    setFocusable(true);
    requestFocusInWindow();
    m_cs.windowOpened(this);
  }

  void windowClosed(JFrame frame) {
    m_frame = null;
    m_cs.windowClosed();
  }

  Graphics2D grabGraphics() {
    if (m_drawBuffer != null) {
      Graphics2D g;
      g = (Graphics2D) m_drawBuffer.getGraphics();
      g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
      //      g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, 
      //          RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
      return g;
    }
    return null;
  }

  private void initDoubleBuffering(int width, int height) {

    if (width != m_width || height != m_height) {
      m_width = width;
      m_height = height;
      m_buffer1 = new BufferedImage(m_width, m_height, BufferedImage.TYPE_INT_RGB);
      m_buffer2 = new BufferedImage(m_width, m_height, BufferedImage.TYPE_INT_RGB);

      Graphics gc = m_buffer1.getGraphics();
      gc.setColor(getBackground());
      gc.fillRect(0, 0, m_width, m_height);
      gc = m_buffer2.getGraphics();
      gc.setColor(getBackground());
      gc.fillRect(0, 0, m_width, m_height);
      m_renderBuffer = m_buffer2;
      m_drawBuffer = m_buffer1;
    }
  }

  void swap() {
    if (m_renderBuffer == m_buffer1) {
      m_renderBuffer = m_buffer2;
      m_drawBuffer = m_buffer1;
    } else {
      m_renderBuffer = m_buffer1;
      m_drawBuffer = m_buffer2;
    }
    repaint();
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);
    if (width > 0 && height > 0) {
      initDoubleBuffering(width, height);
    }
    m_cs.setBounds(width, height);
  }

  @Override
  public final void paint(Graphics g) {
    g.drawImage(m_renderBuffer, 0, 0, this);
    Toolkit.getDefaultToolkit().sync();
  }

  @Override
  public final void update(Graphics g) {
    paint(g);
  }

  public class Listener implements MouseListener, MouseMotionListener, KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
      //System.out.printf("typed: mods:%d code:%d char:%c\n", e.getModifiersEx(), e.getKeyCode(), e.getKeyChar());
      m_cs.typed(e.getModifiersEx(), e.getKeyCode(), e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
      //System.out.printf("pressed: mods:%d code:%d char:%c\n", e.getModifiersEx(), e.getKeyCode(), e.getKeyChar());
      m_cs.pressed(e.getModifiersEx(), e.getKeyCode(), e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {
      //System.out.printf("released: mods:%d code:%d char:%c\n", e.getModifiersEx(), e.getKeyCode(), e.getKeyChar());
      m_cs.released(e.getModifiersEx(), e.getKeyCode(), e.getKeyChar());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      m_cs.moved(e.getModifiersEx(), e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
//            System.out.printf("MouseEvent:moved: mods:%d x:%d y:%d\n", 
//                e.getModifiersEx(), e.getX(), e.getY());
      m_cs.moved(e.getModifiersEx(), e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
      System.out.println("MouseEvent:pressed "+e);
      m_cs.pressed(e.getButton(), e.getModifiersEx(), e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      System.out.println("MouseEvent:released "+e);
      m_cs.released(e.getButton(), e.getModifiersEx(), e.getX(), e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

  }
}
