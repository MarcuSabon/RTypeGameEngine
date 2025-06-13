package game;

import Stunts.StuntPlayer;
import engine.IModel;
import engine.IView;
import engine.controller.Controller;
import engine.model.Player;
import engine.utils.Utils;
import oop.graphics.Canvas;
import oop.graphics.VirtualKeyCodes;
import oop.tasks.Task;

public class Controller0 extends Controller {

	private boolean leftButtonPressed;
	private boolean rightButtonPressed;
	private boolean inMotion;
	private boolean focus;
	private int currentDirection; // 1 for clockwise, -1 for counter-clockwise
	private Object rotationToken = null;

	private static final int ROTATION_DELAY = 33; // milliseconds
	private static final int MOVEMENT_DELAY = 33; // milliseconds
	private static final int ROTATION_ANGLE = 15; // degrees per step

	public Controller0(Canvas canvas, IModel model, IView view) {
		super(canvas, model, view);
	}

	@Override
	protected void pressed(Canvas canvas, int keyCode, char keyChar) {
		Player p = m_model.player();
		StuntPlayer sp = ((StuntPlayer) p.stunt);

		switch (keyCode) {
		case VirtualKeyCodes.VK_LEFT:
			if (m_shift)
				sp.rotateLeft();
			else
				sp.L(-1);
			break;

		case VirtualKeyCodes.VK_RIGHT:
			if (m_shift)
				sp.rotateRight();
			else
				sp.R(1);
			break;

		case VirtualKeyCodes.VK_UP:
			if (m_shift && !inMotion) {
				// Specialization : goes to the cardinal point where the player is facing
				inMotion = true;
				startContinuousMovement();
			} else
				sp.U(-1);

			break;

		case VirtualKeyCodes.VK_DOWN:
			// Specialization : stop the player
			if (m_shift)
				inMotion = false;
			else
				sp.D(1);
			break;

		case VirtualKeyCodes.VK_SPACE:
			sp.shoot();
			break;

		case VirtualKeyCodes.VK_F:
			// Toggle focus mode
			System.out.println("Focus mode disabled for now");
			// focus = !focus;
			break;
			
		}
	}

	@Override
	protected void released(Canvas canvas, int keyCode, char keyChar) {
		Player p = m_model.player();
		StuntPlayer sp = ((StuntPlayer) p.stunt);

		switch (keyCode) {
		case VirtualKeyCodes.VK_UP:
			sp.U(0);
			break;
		case VirtualKeyCodes.VK_DOWN:
			sp.D(0);
			break;
		case VirtualKeyCodes.VK_LEFT:
			sp.L(0);
			break;
		case VirtualKeyCodes.VK_RIGHT:
			sp.R(0);
			break;
		}

	}

	@Override
	protected void typed(Canvas canvas, char keyChar) {
		if(keyChar == '+') {
			m_view.zoom(1);
		}
		if(keyChar == '-') {
			m_view.zoom(-1);;
		}
		if(keyChar == '=') {
			m_view.reset();
		}

	}

	@Override
	protected void pressed(Canvas canvas, int bno, int x, int y) {
		switch (bno) {
		case 1: // Left mouse button
			leftButtonPressed = true;
			currentDirection = -1;
			startRotation();
			break;
		case 3: // Right mouse button
			rightButtonPressed = true;
			currentDirection = 1;
			startRotation();
			break;
		}

	}

	@Override
	protected void released(Canvas canvas, int bno, int x, int y) {
		switch (bno) {
		case 1: // Left mouse button
			leftButtonPressed = false;
			if (rightButtonPressed)
				currentDirection = 1;
			break;
		case 3: // Right mouse button
			rightButtonPressed = false;
			if (leftButtonPressed)
				currentDirection = -1;
			break;
		}
	}

	@Override
	protected void moved(Canvas canvas, int px, int py) {
		m_view.focus(px, py);

		// focusPlayer();

	}

	// ---------- Private methods ----------
	private void startRotation() {
		final Object token = new Object();
		rotationToken = token;

		Task.task().post(new Runnable() {
			@Override
			public void run() {
				if (token == rotationToken && (leftButtonPressed || rightButtonPressed)) {
					Player player = m_model.player();
					if (player != null) {
						// Rotate the player based on the current direction
						player.rotate(ROTATION_ANGLE * currentDirection);
					}

					Task.task().post(this, ROTATION_DELAY);
				}
			}
		}, ROTATION_DELAY);
	}

	private void movePlayerToCardinalPoint() {
		Player p = m_model.player();
		StuntPlayer sp = ((StuntPlayer) p.stunt);

		int theta = p.orientation();

		if ((315 <= theta && theta < 360) || (0 <= theta && theta < 45)) {
			sp.right();
		} else if (45 <= theta && theta < 135) {
			sp.down();
		} else if (135 <= theta && theta < 225) {
			sp.left();
		} else {
			sp.up();
		}
	}

	private void startContinuousMovement() {
		Task.task().post(new Runnable() {
			@Override
			public void run() {
				if (inMotion) {
					movePlayerToCardinalPoint();
					Task.task().post(this, MOVEMENT_DELAY);
				}
			}
		}, MOVEMENT_DELAY);
	}

	private void focusPlayer() {
		// FIXME : No longer work with non-continuous model
		Player player = m_model.player();
		if (player != null && m_view != null && focus) {
			double playerPixelX, playerPixelY;

			if (m_model.config().continuous) {
				playerPixelX = m_view.toMetricPixel(player.x());
				playerPixelY = m_view.toMetricPixel(player.y());
			} else {
				playerPixelX = m_view.toPixel(player.row());
				playerPixelY = m_view.toPixel(player.col());
			}

			double mouseX = m_view.mouseViewportX();
			double mouseY = m_view.mouseViewportY();

			// Calculer la différence de position
			float dx = (float) (mouseX - playerPixelX);
			float dy = (float) (mouseY - playerPixelY);

			// Calculer l'angle en degrés
			int theta = Utils.theta(dx, dy);

			// Mettre à jour l'orientation du joueur
			player.face(theta);
		}
	}
}
