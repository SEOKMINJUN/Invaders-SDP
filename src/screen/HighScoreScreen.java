package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import engine.Globals;
import engine.Score;
import engine.SoundManager;

/**
 * Implements the high scores screen, it shows player records.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class HighScoreScreen extends Screen {

	/** List of past high scores. */
	private List<Score> highScores;

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
	public HighScoreScreen(final int width, final int height, final int fps) {
		super(width, height, fps);

		this.returnCode = 1;

		// Sound Operator
		SoundManager.playBGM("highScore_bgm");

		try {
			this.highScores = Globals.getFileManager().loadHighScores();
		} catch (NumberFormatException | IOException e) {
			logger.warning("Couldn't load high scores!");
		}
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
		drawManager.drawHighScoreMenu(this);
		drawManager.drawHighScores(this, this.highScores);

		super.drawPost();
	}
}
