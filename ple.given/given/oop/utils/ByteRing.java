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
package oop.utils;

/**
 * This ring of bytes can be used to pass bytes between
 * a producer and a consumer. 
 * The ring is FIFO (First-In-First-Out), that is first byte 
 * pushed in is the first byte pulled out.
 * The ring is also lossless, all bytes pushed in the ring will
 * be pulled out.
 */
public class ByteRing {
  int m_tail, m_head;
  byte m_bytes[];

  public ByteRing(int capacity) {
    m_bytes = new byte[capacity];
    m_tail = m_head = 0;
  }

  /**
   * @return true if this buffer is full, false otherwise
   */
  public boolean full() {
    int next = (m_head + 1) % m_bytes.length;
    return (next == m_tail);
  }

  /**
   * @return true if this buffer is empty, false otherwise
   */
  public boolean empty() {
    return (m_tail == m_head);
  }

  /**
   * Pushes a new byte in this ring.
   * @param b: the byte to push in the buffer
   * @return the next available byte
   * @throws an IllegalStateException if full.
   */
  public void push(byte b) {
    int next = (m_head + 1) % m_bytes.length;
    if (next == m_tail)
      throw new IllegalStateException();
    m_bytes[m_head] = b;
    m_head = next;
  }

  /**
   * Pulls the next byte out of this ring.
   * @return the next available byte
   * @throws an IllegalStateException if empty.
   */
  public byte pull() {
    if (m_tail == m_head)
      throw new IllegalStateException();
    int next = (m_tail + 1) % m_bytes.length;
    byte bits = m_bytes[m_tail];
    m_tail = next;
    return bits;
  }

}
