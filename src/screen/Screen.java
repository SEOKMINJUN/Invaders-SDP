package screen;

import java.awt.Insets;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import HUDTeam.DrawAchievementHud;
import HUDTeam.DrawManagerImpl;
import engine.*;
import entity.EntityBase;

/**
 * Implements a generic screen.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Screen {
	
	/** Milliseconds until the screen accepts user input. */
	private static final int INPUT_DELAY = 1000;

	/** Draw Manager instance. */
	protected DrawManager drawManager;
	/** Input Manager instance. */
	protected InputManager inputManager;
	/** Application logger. */
	protected Logger logger;

	/** Screen width. */
	protected int width;
	/** Screen height. */
	protected int height;
	/** Frames per second shown on the screen. */
	protected int fps;
	/** Screen insets. */
	protected Insets insets;
	/** Time until the screen accepts user input. */
	protected Cooldown inputDelay;

	/** If the screen is running. */
	protected boolean isRunning;
	/** What kind of screen goes next. */
	protected int returnCode;
	/** Checks if the game is in 2 player mode **/
	private boolean isTwoPlayerMode;

	private ArrayList<EntityBase> entities;

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
	public Screen(final int width, final int height, final int fps) {
		this.width = width;
		this.height = height;
		this.fps = fps;

		this.drawManager = Globals.getDrawManager();
		this.inputManager = Globals.getInputManager();
		this.logger = Core.getLogger();
		this.inputDelay = Core.getCooldown(INPUT_DELAY);
		this.inputDelay.reset();
		this.returnCode = 0;
	}

	/**
	 * Initializes basic screen properties.
	 */
	public void initialize() {

	}

	/**
	 * Activates the screen.
	 * 
	 * @return Next screen code.
	 */
	public int run() {
		this.isRunning = true;

		while (this.isRunning) {
			long time = System.currentTimeMillis();

			update();

			time = (1000 / this.fps) - (System.currentTimeMillis() - time);
			if (time > 0) {
				try {
					TimeUnit.MILLISECONDS.sleep(time);
				} catch (InterruptedException e) {
					return 0;
				}
			}
		}

		return 0;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected void update() {
	}

	/**
	 * Update the elements on screen after update all child screen
	 */
    protected void draw() {
		if(DrawAchievementHud.getTimer() < 100) {
			DrawManagerImpl.drawAchievement(this, DrawAchievementHud.getAchievementText());
			DrawAchievementHud.addTimer();
		}
	}
	/**
	 * Getter for screen width.
	 * 
	 * @return Screen width.
	 */
	public final int getWidth() {
		return this.width;
	}

	/**
	 * Getter for screen height.
	 * 
	 * @return Screen height.
	 */
	public final int getHeight() {
		return this.height;
	}
	public final boolean isTwoPlayerMode() {
		return this.isTwoPlayerMode;
	}
	public final void setTwoPlayerMode(boolean isTwoPlayerMode) {
		this.isTwoPlayerMode = isTwoPlayerMode;
	}

	public ArrayList<EntityBase> getEntities() {
		return entities;
	}
}