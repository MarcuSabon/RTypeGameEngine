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
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

import oop.graphics.Graphics;
import oop.graphics.VirtualKeyCodes;
import oop.tasks.Task;

public class GameCanvasService extends CanvasService {
  KeyListener m_kl;
  Task m_kt;
  MouseListener m_ml;
  Task m_mt;
  PaintListener m_pl;
  Task m_pt;

  Task m_bt;
  Runnable m_br;
  boolean m_opened;
  boolean m_visible;
  Dimension m_dim;

  GameCanvas m_canvas;
  BufferedImage m_cursor;

  public GameCanvasService(Task t, Dimension d, Runnable r) {
    m_bt = t;
    m_br = r;
    m_dim = d;
    ProxyColor.init();
  }

  @Override
  public void setCursor(BufferedImage img) {
    m_cursor = img;
    if (m_canvas != null)
      m_canvas.setCursor(img);
  }

  private void post(Task t, Runnable r) {
    EventPump.self().crossPost(t, r);
  }

  @Override
  public int getWidth() {
    return m_dim.width;
  }

  @Override
  public int getHeight() {
    return m_dim.height;
  }

  @Override
  public void set(KeyListener l) {
    if (m_pl == null)
      throw new IllegalStateException();
    m_kt = Task.task();
    m_kl = l;
  }

  @Override
  public void set(MouseListener l) {
    if (m_pl == null)
      throw new IllegalStateException();
    m_mt = Task.task();
    m_ml = l;
  }

  @Override
  public void set(PaintListener l) {
    if (m_pl != null)
      throw new IllegalStateException();
    if (l == null)
      throw new IllegalArgumentException();
    m_pt = Task.task();
    m_pl = l;
    if (m_opened)
      visible();
  }

  private void visible() {
    if (m_pl != null) {
      post(m_pt, new Runnable() {
        @Override
        public void run() {
          if (m_opened && m_pl != null) {
            if (!m_visible) {
              m_visible = true;
              m_pl.visible(GameCanvasService.this);
            }
          }
        }
      });
    }
  }

  void windowOpened(GameCanvas gc) {
    m_opened = true;
    m_canvas = gc;
    if (m_cursor!= null)
      m_canvas.setCursor(m_cursor);

    post(m_bt, new Runnable() {
      @Override
      public void run() {
        m_br.run();
      }
    });
    visible();
  }

  void windowClosed() {
    if (m_opened) {
      m_opened = false;
      if (m_pt != null) {
        post(m_pt, new Runnable() {
          @Override
          public void run() {
            if (m_pl != null && !m_opened) {
              m_canvas = null;
              if (m_visible) {
                m_visible = false;
                m_pl.revoked(GameCanvasService.this);
              }
            }
          }
        });
      }
    }
  }

  boolean m_repaint;

  @Override
  public void repaint() {
    if (m_pl == null)
      throw new IllegalStateException();
    if (!m_repaint) {
      m_repaint = true;
      m_pt.post(new Runnable() {
        @Override
        public void run() {
          Graphics g;
          java.awt.Graphics2D ag = m_canvas.grabGraphics();
          if (ag == null) {
            m_pt.post(this);
            return;
          }
          g = new ProxyGraphics(GameCanvasService.this, ag);
          m_repaint = false;
          m_pl.paint(GameCanvasService.this, g);
          m_canvas.swap();
        }
      });
    }
  }

  @Override
  public String[] listFontNames() {
    String fonts[];
    fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    return fonts;
  }

  void setBounds(int width, int height) {
    m_dim.width = width;
    m_dim.height = height;
  }

  void pressed(int modifiers, int keyCode, char keyChar) {
    if (m_kl != null) {
      post(m_kt, new Runnable() {
        @Override
        public void run() {
          if (keyChar == 0x09)
            m_kl.pressed(GameCanvasService.this, VirtualKeyCodes.VK_TAB, VirtualKeyCodes.CHAR_UNDEFINED);
          else
            m_kl.pressed(GameCanvasService.this, keyCode, keyChar);
        }
      });
    }
  }

  void released(int modifiers, int keyCode, char keyChar) {
    if (m_kl != null) {
      post(m_kt, new Runnable() {
        @Override
        public void run() {
          m_kl.released(GameCanvasService.this, keyCode, keyChar);
        }
      });
    }
  }

  void typed(int modifiers, int keyCode, char keyChar) {
    // all the following characters are in fact virtual keys also
    // so avoid redundant notifications.
    switch (keyChar) {
    case '\n': // enter();
    case '\t': // tab();     
    case 0x08: // backspace();     
    case 0x7F: // delete();     
      return;
    }
    if (m_kl != null && keyChar != VirtualKeyCodes.CHAR_UNDEFINED) {
      post(m_kt, new Runnable() {
        @Override
        public void run() {
          m_kl.typed(GameCanvasService.this, keyChar);
        }
      });
    }
  }

  void moved(int modifiers, int x, int y) {
    if (m_ml != null) {
      post(m_mt, new Runnable() {
        @Override
        public void run() {
          m_ml.moved(GameCanvasService.this, x, y);
        }
      });
    }
  }

  void pressed(int bno, int modifiers, int x, int y) {
    if (m_ml != null) {
      post(m_mt, new Runnable() {
        @Override
        public void run() {
          m_ml.pressed(GameCanvasService.this, bno, x, y);
        }
      });
    }
  }

  void released(int bno, int modifiers, int x, int y) {
    if (m_ml != null) {
      post(m_mt, new Runnable() {
        @Override
        public void run() {
          m_ml.released(GameCanvasService.this, bno, x, y);
        }
      });
    }
  }
}
