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

import oop.graphics.Canvas;
import oop.runtime.Task.Event;

public class EventPump {

  private class Cell {
    Event m_event;
    long m_eta;
    Cell m_next;
    Queue m_queue;

    Cell(Queue queue, Event e) {
      m_event = e;
      m_queue = queue;
    }

    protected boolean react() {
      throw new Error("Must be overriden");
    }

    void run() {
      m_event.react(EventPump.this);
    }

  }

  class Queue {

    Cell m_head, m_last;

    boolean empty() {
      return m_head == null;
    }

    synchronized Cell pop() {
      Cell e = null;
      if (m_head != null) {
        e = m_head;
        if (m_head == m_last)
          m_head = m_last = null;
        else
          m_head = m_head.m_next;
        e.m_next = null;
      }
      return e;
    }

    synchronized void append(Cell e) {
      if (m_last == null) {
        m_head = m_last = e;
      } else {
        m_last.m_next = e;
        m_last = e;
      }
      e.m_next = null;
    }

    synchronized Cell prepend(Event e) {
      Cell cell = new Cell(this, e);
      if (m_head == null) {
        m_head = m_last = cell;
      } else {
        cell.m_next = m_head;
        m_head = cell;
      }
      return cell;
    }

    synchronized Cell append(Event e) {
      Cell cell = new Cell(this, e);
      if (m_last == null) {
        m_head = m_last = cell;
      } else {
        m_last.m_next = cell;
        m_last = cell;
      }
      return cell;
    }

    synchronized void append(Event e, long eta) {
      Cell cell = new Cell(this, e);
      cell.m_eta = eta;
      if (m_head == null) {
        m_head = m_last = cell;
      } else {
        Cell prev, pos = m_head;
        prev = null;
        while (pos != null && pos.m_eta < eta) {
          prev = pos;
          pos = pos.m_next;
        }
        if (prev == null) {
          cell.m_next = m_head;
          m_head = cell;
        } else {
          cell.m_next = prev.m_next;
          prev.m_next = cell;
          if (prev == m_last)
            m_last = cell;
        }
      }
    }

    void cleanup(Task task) {
      Cell prev, pos = m_head;
      prev = null;
      while (pos != null) {
        Event e = pos.m_event;
        if (e.task() == task) {
          if (prev == null) {
            m_head = pos.m_next;
          } else {
            prev.m_next = pos.m_next;
            if (pos == m_last)
              m_last = prev;
          }
        } else
          prev = pos;
        pos = pos.m_next;
      }
    }
  }

  Queue m_ready, m_delayed;
  private static EventPump self;

  public static EventPump self() {
    return self;
  }

  public EventPump() {
    self = this;
    m_ready = new Queue();
    m_delayed = new Queue();
  }

  public void cleanup(Task task) {
    m_ready.cleanup(task);
    m_delayed.cleanup(task);
  }

  private boolean m_shutdown;

  public void shutdown() {
    m_shutdown = true;
  }

  public interface CanvasExt extends Canvas {
    void set(oop.tasks.Task task, Runnable r);
  }

  /*
   * This boot is used when running our tests
   */
  public void boot(CanvasExt cs, Runnable r) {
    Task.CatchAll=false;
    Task task = new Task(this);
    cs.set(task, r);
    task.register("canvas", cs);
    loop();
    // the execution comes back here when 
    // the pump is shutdown. Do not system exit,
    // but just return.
    return;
  }

  /*
   * This bootstraps is with a canvas service.
   */
  public void boot(Dimension d, Runnable r) {
    Task task = new Task(this);
    CanvasService cs = new GameCanvasService(task, d, r);
    task.register("canvas", cs);
    GameCanvas canvas = new GameCanvas(cs);
    WindowFrame wf = new WindowFrame(d, canvas);
    loop();
    System.exit(0);
  }

  /*
   * This bootstraps is without a canvas service,
   * in other words a headless environment.
   */
  public void boot(Runnable r) {
    Task task = new Task(this);
    task.post(r);
    loop();
    System.exit(0);
  }

  private void loop() {
    Cell e;
    while (!m_shutdown) {
      checkDelayed();
      e = m_ready.pop();
      if (e == null) {
        sleep();
      } else {
        e.run();
      }
    }
  }

  /*
   * 
   */
  void crossPost(oop.tasks.Task t, Runnable r) {
    t.post(r);
    synchronized (this) {
      notify();
    }
  }

  public void post(Event e) {
    m_ready.append(e);
  }

  public void post(Event e, int delay) {
    long eta = System.currentTimeMillis() + delay;
    m_delayed.append(e, eta);
  }

  private synchronized void checkDelayed() {
    long now = System.currentTimeMillis();
    while (!m_delayed.empty()) {
      long eta = m_delayed.m_head.m_eta;
      if (now < eta)
        break;
      // System.out.println("popping delayed: "+delay);
      m_ready.append(m_delayed.pop());
    }
  }

  private synchronized void sleep() {
    checkDelayed();
    while (m_ready.empty()) {
      int delay = 0;
      if (!m_delayed.empty()) {
        long now = System.currentTimeMillis();
        long eta = m_delayed.m_head.m_eta;
        if (eta <= now)
          return;
        delay = (int) (eta - now);
      }
      try {
        if (delay == 0)
          wait();
        else
          wait(delay);
      } catch (InterruptedException e) {
        // nothing to do
      }
    }
  }

  void append(Event e) {
    m_ready.append(e);
    synchronized (this) {
      notify();
    }
  }

  void prepend(Event e) {
    m_ready.prepend(e);
    synchronized (this) {
      notify();
    }
  }

}
