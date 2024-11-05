package screen;

import java.awt.Insets;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import engine.Achievement.AchievementHud;
import engine.DrawManagerImpl;
import engine.*;
import entity.EntityBase;
import lombok.Getter;

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
    public Cooldown inputDelay;

	/** If the screen is running. */
	protected boolean isRunning;
	/** What kind of screen goes next. */
	protected int returnCode;

	@Getter
    private ArrayList<EntityBase> entityList;

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
		this.entityList = new ArrayList<>();
		Globals.setCurrentScreen(this);
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

			if(update());
			{
				drawManager.initDrawing(this);
				draw();
				drawManager.completeDrawing(this);
			}

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
	protected boolean update() {
		EntityBase entity = null;
		while((entity = findEntityByClassname(entity, "*")) != null){
			entity.update();
		}
		return false;
	}


	protected void draw() {
		EntityBase entity = null;
		while((entity = findEntityByLayer(entity, 0)) != null){
			entity.draw();
		}
		while((entity = findEntityByLayer(entity, 1)) != null){
			entity.draw();
		}
		while((entity = findEntityByLayer(entity, 2)) != null){
			entity.draw();
		}
	}

	/**
	 * Update the elements on screen after update all child screen
	 */
    protected void drawPost() {
		if(AchievementHud.getTimer() < 100) {
			DrawManagerImpl.drawAchievement(this, AchievementHud.getAchievementText());
			AchievementHud.addTimer();
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

	public final void addEntity(final EntityBase entity) {
		entity.entIndex = entityList.size();
		this.entityList.add(entity);
	}

	public final int getEntityIndex(final EntityBase entity) {
		return this.entityList.indexOf(entity);
	}

	public final void removeEntity(final EntityBase entity) {
		this.entityList.set(entity.entIndex, null);
	}

	public final void removeEntity(int entindex) {
		this.entityList.set(entindex, null);
	}

	public final EntityBase findEntityByLayer(int entindex, int layer){
		for(int i = entindex+1; i < entityList.size(); i++) {
			EntityBase entity = entityList.get(i);
			if(entity == null)
				continue;
			if(layer == entity.layer)
				return entity;
		}
		return null;
	}

	public final EntityBase findEntityByLayer(EntityBase entity, int layer){
		int entindex = -1;
		if(entity != null)
			entindex = entity.entIndex;
		return findEntityByLayer(entindex, layer);
	}

	public final EntityBase findEntityByClassname(int entindex, String classname){
		for(int i = entindex+1; i < entityList.size(); i++) {
			EntityBase entity = entityList.get(i);
			if(entity == null)
				continue;
			if(classname.equals("*") || entity.getClassName().equals(classname))
				return entity;
		}
		return null;
	}

	public final EntityBase findEntityByClassname(EntityBase entity, String classname){
		int entindex = -1;
		if(entity != null)
			entindex = entity.entIndex;
		return findEntityByClassname(entindex, classname);
	}
}