package engine.controller;

import engine.IController;
import engine.IModel;
import engine.IView;
import oop.graphics.Canvas;
import oop.graphics.VirtualKeyCodes;

public abstract class Controller implements IController {
	protected Canvas m_canvas;
	protected IView m_view;
	protected IModel m_model;
	protected boolean m_shift;
	protected boolean m_control;
	protected boolean m_alt;

	public Controller(Canvas canvas, IModel model, IView view) {
		m_canvas = canvas;
		m_model = model;
		m_view = view;
		canvas.set(new MouseListener());
		canvas.set(new KeyListener());
	}

	class KeyListener implements Canvas.KeyListener {

		@Override
		public void pressed(Canvas canvas, int keyCode, char keyChar) {

			// Look at the class VirtualKeyCodes in the package oop.graphics
			// to know what the key codes.

			// Goals:
			// - arrow keys move the player's entity around, one step
			// left, right, up, and down.
			// - shift and left arrow key rotates counter-clock-wise the player's entity by
			// 90 degrees
			// - shift and right arrow key rotates clock-wise the player's entity by 90
			// degrees

			if (m_control) {
				switch (keyCode) {
				case VirtualKeyCodes.VK_LEFT:
					m_view.scrollLeft();
					break;
				case VirtualKeyCodes.VK_RIGHT:
					m_view.scrollRight();
					break;
				case VirtualKeyCodes.VK_UP:
					m_view.scrollUp();
					break;
				case VirtualKeyCodes.VK_DOWN:
					m_view.scrollDown();
					break;
				case VirtualKeyCodes.VK_D:
					m_view.debug();
					break;
				}
			} else {
				switch (keyCode) {
				case VirtualKeyCodes.VK_SHIFT:
					m_shift = true;
					break;

				case VirtualKeyCodes.VK_CONTROL:
					m_control = true;
					break;

				case VirtualKeyCodes.VK_ALT:
					m_alt = true;
					break;
				}
				Controller.this.pressed(canvas, keyCode, keyChar);
			}
		}

		@Override
		public void released(Canvas canvas, int keyCode, char keyChar) {
			switch (keyCode) {
			case VirtualKeyCodes.VK_SHIFT:
				m_shift = false;
				break;
			case VirtualKeyCodes.VK_CONTROL:
				m_control = false;
				break;
			case VirtualKeyCodes.VK_ALT:
				m_alt = false;
				break;
			}
			Controller.this.released(canvas, keyCode, keyChar);
		}

		@Override
		public void typed(Canvas canvas, char keyChar) {
			switch (keyChar) {
			case '+':
				m_view.zoomIn();
				break;
			case VirtualKeyCodes.VK_MINUS:
				m_view.zoomOut();
				break;
			case VirtualKeyCodes.VK_EQUALS:
				m_view.resetZoom();
				break;
			}
			Controller.this.typed(canvas, keyChar);
		}
	}

	class MouseListener implements Canvas.MouseListener {

		@Override
		public void moved(Canvas canvas, int px, int py) {

			// inform the model about the current focus,
			// that is, where the mouse is pointing at in the world.

			Controller.this.moved(canvas, px, py);
		}

		@Override
		public void pressed(Canvas canvas, int bno, int x, int y) {
			Controller.this.pressed(canvas, bno, x, y);
		}

		@Override
		public void released(Canvas canvas, int bno, int x, int y) {
			Controller.this.released(canvas, bno, x, y);
		}

	}

	protected abstract void pressed(Canvas canvas, int keyCode, char keyChar);

	protected abstract void released(Canvas canvas, int keyCode, char keyChar);

	protected abstract void typed(Canvas canvas, char keyChar);

	protected abstract void pressed(Canvas canvas, int bno, int x, int y);

	protected abstract void released(Canvas canvas, int bno, int x, int y);

	protected abstract void moved(Canvas canvas, int px, int py);

}
