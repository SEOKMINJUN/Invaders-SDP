package engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

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

	private static final int DOUBLE_TAB_THRESHOLD = 1000;
	private static final int DOUBLE_TAB_COOLDOWN = 5000;

	private Map<Integer, Long> PressedTimes = new HashMap<>();
	private Map<Integer, Long> ReleasedTimes = new HashMap<>();

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
		if (key.getKeyCode() >= 0 && key.getKeyCode() < NUM_KEYS) {
			keys[key.getKeyCode()] = true;
			long currentTime = System.currentTimeMillis();
			PressedTimes.put(key.getKeyCode(), currentTime);
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
		if (key.getKeyCode() >= 0 && key.getKeyCode() < NUM_KEYS) {
			keys[key.getKeyCode()] = false;
			long currentTime = System.currentTimeMillis();
			ReleasedTimes.put(key.getKeyCode(), currentTime);
		}
	}


	public boolean isDoubleTab(final int keyCode) {
		if (keyCode >= 0 && keyCode < NUM_KEYS) {
			long currentTime = System.currentTimeMillis();

			if (ReleasedTimes.containsKey(keyCode) && PressedTimes.containsKey(keyCode)) {
				long timeSinceLastRelease = currentTime - ReleasedTimes.get(keyCode);
				long timeSinceLastPress = currentTime - PressedTimes.get(keyCode);

				// Double Tap 조건: Released -> Pressed 사이의 시간 간격
				if (timeSinceLastRelease <= DOUBLE_TAB_THRESHOLD && timeSinceLastPress >= DOUBLE_TAB_COOLDOWN) {
					// Double Tap 감지 후 상태 초기화
					ReleasedTimes.put(keyCode, currentTime);
					return true;
				}
			}
		}
		return false;
	}

	public void resetDoubleTab(final int keyCode) {
		if (keyCode >= 0 && keyCode < NUM_KEYS) {
			ReleasedTimes.remove(keyCode);
			PressedTimes.remove(keyCode);
		}
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