package screen;

import engine.RoundState;
import engine.GameState;

import java.awt.event.KeyEvent;

/**
 * Implements the high scores screen, it shows player records.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class ReceiptScreen extends Screen {
	
	private final RoundState roundState;
	private final GameState gameState;

	/**
	 * Constructor, establishes the properties of the screen.
	 *
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 */
	public ReceiptScreen(final int width, final int height, final int fps, final RoundState roundState, final GameState gameState) {
		super(width, height, fps);

		this.roundState = roundState;
		this.gameState = gameState;
		this.returnCode = 2;
	}

	/**
	 * Starts the action.
	 * 
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final boolean update() {
		super.update();

		if (inputManager.isKeyDown(KeyEvent.VK_SPACE)
				&& this.inputDelay.checkFinished())
			this.isRunning = false;
		return true;
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	protected void draw() {
		drawManager.drawReceipt(this, this.roundState, this.gameState);

		super.drawPost();
	}
}
