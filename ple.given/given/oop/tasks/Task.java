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
package oop.tasks;

/* 
 * A task, as its name suggests, represents
 * a something to be done, reacting to one or more 
 * events to carry out the steps necessary to accomplish
 * the task.
 * 
 * For simplicity, new events are posted on tasks
 * as runnable objects, that is, instances of classes that
 * implement the Java interface Runnable. That interface
 * defines a single method "run()void" that will carry
 * a step towards the completion of this task.
 * See methods:
 *   - post(Runnable r)void
 *   - post(Runnable r, int delay)void
 *
 * A task either completes normally or fails.
 * To complete normally a task, a runnable executing
 * on this task invokes the method terminate:
 *   - terminate()void
 *   - terminate(Object result)void
 *   
 * The normal termination of a task will result
 * in the asynchronous invocation of the method
 * "completed(Task,Object)void, on the provided 
 * life-cyle listener, if any.
 * 
 * A task completes abnormally either because
 * the currently executing runnable on that task
 * throws an uncaught exception or because it 
 * invokes the method failed:
 *   - failed(Throwable)void
 * The abnormal termination of a task will result
 * in the asynchronous invocation of the method
 * "failed(Task,Throwable)void, on the provided 
 * life-cyle listener, if any.
 */

public abstract class Task {

  /*
   * This is the task lifecycle listener,
   * allowing to be notified when a task 
   * has died. The notification will occur
   * on the task that was used to set the
   * listener or a given task. 
   * See the two public constructors for details.
   */
  public interface Listener {
    
    /* 
     * Invoked when this task has completed normally.
     * Invoked on the task given when the listener was set.
     */
    void completed(Task t, Object obj);

    /*
     * Invoked when this task has completed abnormally.
     * Invoked on the task given when the listener was set.
     */
    void failed(Task t, Throwable th);
  }

  /*
   * Construct a new task, with the given name.
   */
  public abstract Task newTask(String name);

  /*
   * Sets the life-cycle listener for this task.
   * The listener will be invoked on the given task.
   */
  public abstract void set(Task task, Listener l);

  /*
   * Finds the service registered under the given name,
   * if there is one. The name registry is global to all tasks.
   */
  public abstract Object find(String name);

  /*
   * Register the given service under the given name,
   * in the global registry of services.
   */
  public abstract void register(String name, Object s);

  /*
   * Posts a new runnable to be executed later as early
   * as possible by this task.
   */
  public abstract void post(Runnable r);

  /*
   * Posts a new runnable to be executed later by this 
   * task, at least after the given delay in milli-seconds.
   */
  public abstract void post(Runnable r, int delay);

  /*
   * Terminate this task without any result object.
   * This method must be invoked from a runnable 
   * executing on this task, in other words, 
   * this is invoked for self-termination.
   */
  public abstract void terminate();
  
  /*
   * Terminate this task with the given result object.
   * This method must be invoked from a runnable 
   * executing on this task, in other words, 
   * this is invoked for self-termination.
   */
  public abstract void terminate(Object o);
  /*
   * Returns true if this task is dead,
   * either because it terminated normally
   * or abnormally.
   */
  public abstract boolean dead();
  
  /*
   * Returns the current task, the one 
   * on which the current runnable executes.
   */
  public static Task task() {
    return task;
  }

  /* ================================================================
   * ================================================================
   * PRIVATE PART BELOW
   * ONLY LOOK IF YOU ARE INTERESTED BY THE DESIGN?IMPLEMENTATION
   * ================================================================
   * ================================================================
   */
  private static Task task;
  protected void _task(Task t) { task = t; }

}
