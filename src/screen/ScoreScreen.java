package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import engine.Statistics; //Team Clove
import engine.*;
import entity.NumberOfBullet;

import static screen.GameScreen.goToTitle;

/**
 * Implements the score screen.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class ScoreScreen extends Screen {

	/** Milliseconds between changes in user selection. */
	private static final int SELECTION_TIME = 200;
	/** Maximum number of high scores. */
	private static final int MAX_HIGH_SCORE_NUM = 7;
	/** Code of first mayus character. */
	private static final int FIRST_CHAR = 65;
	/** Code of last mayus character. */
	private static final int LAST_CHAR = 90;
	/** Maximum number of recent scores / Team Clover */
	private static final int MAX_RECENT_SCORE_NUM = 10;

	/** Current score. */
	private int score;
	/** Player lives left. */
	private int health;
	/** Total bullets shot by the player. */
	private int bulletsShot;
	/** Total ships destroyed by the player. */
	private int shipsDestroyed;
	/** List of past high scores. */
	private List<Score> highScores;
	/** Checks if current score is a new high score. */
	private boolean isNewRecord;
	/** Player name for record input. */
	private char[] name;
	/** Character of players name selected for change. */
	private int nameCharSelected;
	/** Time between changes in user selection. */
	private Cooldown selectionCooldown;
	/** Total coin earned this game */
	private int coin; // Team-Ctrl-S(Currency)
	/** User's Final Reached Level */ //Team Clove
	private int level;

	/** List of user recent scores */
	private List<Score> recentScore; // Team Clove
	/** To symbolize current score is always the most recent score. value is always true */
	private final boolean isRecentScore = true; // Team Clove

	private Statistics statistics; //Team Clove

	private Statistics collections;
	private List<Statistics> collectionsList = new ArrayList<>();

	private long playTime; //Team Clove

	private GameState gameState; // Team-Ctrl-S(Currency)

	private boolean isGameClear; // CtrlS

	private int totalDistance;

	/**
	 * Constructor, establishes the properties of the screen.
	 * 
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 * @param gameState
	 *            Current game state.
	 */
	public ScoreScreen(final int width, final int height, final int fps,
			final GameState gameState) {
		super(width, height, fps);

		this.score = gameState.getScore();
		this.health = gameState.getLivesRemaining();
		this.bulletsShot = gameState.getBulletsShot();
		this.shipsDestroyed = gameState.getShipsDestroyed();
		this.isNewRecord = false;
		this.name = "AAA".toCharArray();
		this.nameCharSelected = 0;
		this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
		this.selectionCooldown.reset();
		this.coin = gameState.getCoin(); // Team-Ctrl-S(Currency)
		this.gameState = gameState; // Team-Ctrl-S(Currency)
		this.level = gameState.getLevel(); //Team Clove
		this.statistics = new Statistics(); //Team Clove
		this.isGameClear = this.health > 0 && this.level > 7; // CtrlS
		this.totalDistance = gameState.getTotalDistance();

		try {
			this.highScores = Globals.getFileManager().loadHighScores();
			if (highScores.size() < MAX_HIGH_SCORE_NUM
					|| highScores.get(highScores.size() - 1).getScore()
					< this.score)
				this.isNewRecord = true;

		} catch (IOException e) {
			logger.warning("Couldn't load high scores!");
		}
		try {																			// Team Clove added Exception
			this.recentScore = Globals.getFileManager().loadRecentScores();
		} catch (IOException e) {
			logger.warning("Couldn't load recent scores!");
		}

		try {
			this.collections = Globals.getFileManager().loadCollections();
			collectionsList.add(this.collections);
		}catch (IOException ex) {
			Logger.getLogger("Couldn't load Collection!");
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

		if (this.inputDelay.checkFinished()) {
			if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
				// Return to main menu.
				this.returnCode = 1;
				this.isRunning = false;
				if (this.isNewRecord) {
					saveScore();
				}
				if (this.isGameClear) {
					saveGem();
				} // CtrlS
				saveCoin(); // Team-Ctrl-S(Currency)
				saveStatistics(); //Team Clove
				saveRecentScore(); // Team Clove
				saveCollections();
			} else if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
				// Play again.
				this.returnCode = 2;
				this.isRunning = false;
				if (this.isNewRecord) {
					saveScore();
				}
				if (this.isGameClear) {
					saveGem();
				} // CtrlS
				saveCoin(); // Team-Ctrl-S(Currency)
				saveStatistics(); //Team Clove
				saveRecentScore(); // Team Clove
				saveCollections();
			}

			if (this.isNewRecord && this.selectionCooldown.checkFinished()) {
				if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
					this.nameCharSelected = this.nameCharSelected == 2 ? 0
							: this.nameCharSelected + 1;
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) {
					this.nameCharSelected = this.nameCharSelected == 0 ? 2
							: this.nameCharSelected - 1;
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_UP)) {
					this.name[this.nameCharSelected] =
							(char) (this.name[this.nameCharSelected]
									== LAST_CHAR ? FIRST_CHAR
							: this.name[this.nameCharSelected] + 1);
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_DOWN)) {
					this.name[this.nameCharSelected] =
							(char) (this.name[this.nameCharSelected]
									== FIRST_CHAR ? LAST_CHAR
							: this.name[this.nameCharSelected] - 1);
					this.selectionCooldown.reset();
				}
			}
			NumberOfBullet.ResetPierceLevel();
		}
		return true;
	}

	/**
	 * Saves the score as a high score.
	 */
	private void saveScore() {
		highScores.add(new Score(new String(this.name), score));
		Collections.sort(highScores);
		if (highScores.size() > MAX_HIGH_SCORE_NUM)
			highScores.remove(highScores.size() - 1);

		try {
			Globals.getFileManager().saveHighScores(highScores);
		} catch (IOException e) {
			logger.warning("Couldn't load high scores!");
		}
	}

	/** Saves the score as a recent score.
	 *  Team Clove
	 */
	private void saveRecentScore() {
		recentScore.add(new Score(null, score));
		if (recentScore.size() > MAX_RECENT_SCORE_NUM)
			recentScore.remove(0);
		try {
			Globals.getFileManager().saveRecentScores(recentScore);
		} catch (IOException e) {
			logger.warning("Couldn't load recent scores!");
		}
	}

	/**
	 *  Saves the Player's Statistics
	 */

	private void saveStatistics(){
		try{
			statistics.checkAndUpdateStreak();
			statistics.comShipsDestructionStreak(0);
			statistics.addShipsDestroyed(0);
			statistics.addPlayedGameNumber(0);
			statistics.comClearAchievementNumber(0);
			statistics.comDistance(0);
		} catch (IOException e) {
			logger.warning("Couldn't load Statistics!");
		}
	}

	private void saveCollections(){
		try{
			Globals.getFileManager().saveCollections(Globals.getCollectionManager().getCollectionList());
		} catch (IOException e) {
			logger.warning("Couldn't load Collections!");
		}
	}

	/**
	 * Saves the coin into currency file
	 */
	// Team-Ctrl-S(Currency)
	private void saveCoin() {
		try {
			Globals.getCurrencyManager().addCoin(coin);
			logger.info("You earned $" + coin);
		} catch (IOException e) {
			logger.warning("Couldn't load coin!");
        }
    }

	/**
	 * Saves the gem into currency file
	 */
	// CtrlS
	private void saveGem() {
		try {
			Globals.getCurrencyManager().addGem(1);
			logger.info("You earned 1 Gem for Game Clear");
		} catch (IOException e) {
			logger.warning("Couldn't load gem!");
		}
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	protected void draw() {

		drawManager.drawGameEnd(this, this.inputDelay.checkFinished(), this.isGameClear); // CtrlS
		drawManager.drawResults(this, this.score, this.health,
				this.shipsDestroyed, (float) this.gameState.getHitCount()
						/ this.bulletsShot, this.gameState, this.totalDistance);

		if (this.isNewRecord)
			drawManager.drawNameInput(this, this.name, this.nameCharSelected);

		super.drawPost();
	}
}
