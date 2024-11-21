package engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Manages keyboard input for the provided screen.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public final class InputManager implements KeyListener {

	/** Number of recognised keys. */
	private static final int NUM_KEYS = 256;
	/** Array with the jeys marked as pressed or not. */
	private static boolean[] keys;
	/** Singleton instance of the class. */
	private static InputManager instance;

	private long lastRightTapTime = 0;
	private long lastLeftTapTime = 0;
	private boolean isRightKeyReleased = true;
	private boolean isLeftKeyReleased = true;
	private long isRightKeyPressed = 0;
	private long isLeftKeyPressed = 0;
	/**
	 * Private constructor.
	 */
	private InputManager() {
		keys = new boolean[NUM_KEYS];
	}

	/**
	 * Returns shared instance of InputManager.
	 *
	 * @return Shared instance of InputManager.
	 */
	protected static InputManager getInstance() {
		if (instance == null)
			instance = new InputManager();
		return instance;
	}

	/**
	 * Returns true if the provided key is currently pressed.
	 *
	 * @param keyCode
	 *            Key number to check.
	 * @return Key state.
	 */
	public boolean isKeyDown(final int keyCode) {
		return keys[keyCode];
	}

	/**
	 * Changes the state of the key to pressed.
	 *
	 * @param key
	 *            Key pressed.
	 */
	@Override
	public void keyPressed(final KeyEvent key) {
		if (key.getKeyCode() >= 0 && key.getKeyCode() < NUM_KEYS) { keys[key.getKeyCode()] = true; }
		if (key.getKeyCode() == KeyEvent.VK_RIGHT) { isRightKeyPressed = System.currentTimeMillis(); }
		if (key.getKeyCode() == KeyEvent.VK_LEFT) { isLeftKeyPressed = System.currentTimeMillis(); }
	}

	/**
	 * Changes the state of the key to not pressed.
	 *
	 * @param key
	 *            Key released.
	 */
	@Override
	public void keyReleased(final KeyEvent key) {
		if (key.getKeyCode() >= 0 && key.getKeyCode() < NUM_KEYS) { keys[key.getKeyCode()] = false; }
		if (key.getKeyCode() == KeyEvent.VK_RIGHT) { isRightKeyReleased = true; }
		if (key.getKeyCode() == KeyEvent.VK_LEFT) { isLeftKeyReleased = true; }
	}

	public boolean isDoubleTab(int keyCode) {
		long currentTime = System.currentTimeMillis();

		if (keyCode == KeyEvent.VK_RIGHT) {
			if (!isRightKeyReleased || currentTime - isRightKeyPressed > 500) { return false; }
			isRightKeyReleased = false;
			if(currentTime - lastRightTapTime < 500) {
				lastRightTapTime = 0;
				isRightKeyReleased = true;
				return true;
			}
			lastRightTapTime = currentTime;
		}
		if (keyCode == KeyEvent.VK_LEFT) {
			if (!isLeftKeyReleased || currentTime - isLeftKeyPressed > 500) { return false; }
			isLeftKeyReleased = false;
			if(currentTime - lastLeftTapTime < 500) {
				lastLeftTapTime = 0;
				isLeftKeyReleased = true;
				return true;
			}
			lastLeftTapTime = currentTime;
		}
		return false;
	}

	/**
	 * Does nothing.
	 *
	 * @param key
	 *            Key typed.
	 */
	@Override
	public void keyTyped(final KeyEvent key) {

	}
}