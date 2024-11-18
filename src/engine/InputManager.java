package engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

	/**
	 * The threshold time (in milliseconds) to detect a double-tap event
	 */
	private Map<Integer, Long> lastKeyPress = new HashMap<>();
	private static final int DOUBLE_TAP_THRESHOLD = 300;
	private Set<Integer> pressedKeys = new HashSet<>();
	private static final int DOUBLE_TAP_COOLDOWN = 500;
	private long lastDoubleTapTime = 0;
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
		int keyCode = key.getKeyCode();
		if (key.getKeyCode() >= 0 && key.getKeyCode() < NUM_KEYS){
			keys[key.getKeyCode()] = true;
			pressedKeys.add(keyCode);

			long currentTime = System.currentTimeMillis();
			long lastTime = lastKeyPress.getOrDefault(keyCode, 0L);

			boolean isDoubleTap = (currentTime - lastTime) > DOUBLE_TAP_THRESHOLD;
			lastKeyPress.put(keyCode, currentTime);
		}
	}

	/**
	 * Changes the state of the key to not pressed.
	 *
	 * @param key
	 *            Key released.
	 */
	@Override
	public void keyReleased(final KeyEvent key) {
		int keyCode = key.getKeyCode();
		if (key.getKeyCode() >= 0 && key.getKeyCode() < NUM_KEYS)
			keys[key.getKeyCode()] = false;
		pressedKeys.remove(keyCode);
	}

	/**
	 * Checks if a given key code has been double-tapped within the defined threshold.
	 *
	 * @param keyCode The key code to check for a double-tap.
	 * @return True if the key was double-tapped, false otherwise.
	 */
	public boolean isDoubleTap(int keyCode) {
		if (keyCode != KeyEvent.VK_LEFT && keyCode != KeyEvent.VK_RIGHT) { return false; }
		long currentTime = System.currentTimeMillis();
		long lastPressTime = lastKeyPress.getOrDefault(keyCode, 0L);
		boolean isDoubleTap = (currentTime - lastDoubleTapTime > DOUBLE_TAP_COOLDOWN) &&
				(currentTime - lastPressTime > 0) &&
				(currentTime - lastPressTime <= DOUBLE_TAP_THRESHOLD);
		if (isDoubleTap){ lastDoubleTapTime = currentTime; }
		else { lastKeyPress.put(keyCode, currentTime); }

		return isDoubleTap;
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