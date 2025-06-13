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

import java.util.HashMap;
import java.util.Map;

public final class Task extends oop.tasks.Task {


  /*
   * Construct a new task, with the given name.
   */
  private Task(String name) {
    m_name = name;
    m_pump = EventPump.self();
  }

  @Override
  public Task newTask(String name) {
    return new Task(name);
  }

  @Override
  public void set(oop.tasks.Task task, Listener l) {
    m_l = l;
    m_ltask = (Task)task;
  }

  @Override
  public Object find(String name) {
    return services.get(name);
  }

  @Override
  public void register(String name, Object s) {
    services.put(name, s);
  }

  @Override
  public void post(Runnable r) {
    Event e = new Event(r);
    m_pump.post(e);
  }

  @Override
  public void post(Runnable r, int delay) {
    Event e = new Event(r);
    m_pump.post(e, delay);
  }

  @Override
  public void terminate() {
    terminate(null);
  }
  
  @Override
  public void terminate(Object o) {
    if (this!=Task.task())
      throw new IllegalStateException();
    if (m_terminated)
      throw new IllegalStateException();
    m_terminated = true;
    m_result = o;
    post(new Runnable() {
      public void run() {
        _dead();
      }
    });
  }

  @Override
  public boolean dead() {
    return m_dead;
  }

  private static Map<String, Object> services;

  private String m_name;
  private EventPump m_pump;
  private boolean m_terminated;
  private Object m_result;
  private boolean m_dead;
  private Listener m_l;
  private Task m_ltask;

  private Throwable m_th;
  
  static boolean CatchAll=true;

  Task(EventPump pump) {
    m_name = "root";
    m_pump = pump;
    services = new HashMap<String, Object>();
  }

  private void _dead() {
    if (!m_dead) {
      m_dead = true;
      if (m_l != null) {
        m_ltask.post(new Runnable() {
          public void run() {
            if (m_th != null)
              m_l.failed(Task.this, m_th);
            else
              m_l.completed(Task.this, m_result);
          }
        });
      }
    }
  }

  void failed(Throwable th) {
    m_th = th;
    if (th != null) {
      System.err.printf("Task %s: failed for %s\n", m_name, th.getMessage());
      th.printStackTrace(System.err);
    } else
      System.err.printf("Task %s: failed\n", m_name);
    _dead();
  }

  private boolean killed() {
    return m_dead;
  }

  public class Event {
    protected Runnable m_react;

    private Event(Runnable r) {
      m_react = r;
    }

    public Task task() {
      return Task.this;
    }

    public void react(EventPump ep) {
      if (!m_dead) {
        try {
          _task(Task.this);
          m_react.run();
        } catch (Throwable th) {
          if (CatchAll) {
            th.printStackTrace(System.err);
            failed(th);
          } else {
            m_pump.shutdown();
            throw th;
          }
        }
        _task(null);
      }
      if (m_dead)
        ep.cleanup(Task.this);
    }

  }

}
