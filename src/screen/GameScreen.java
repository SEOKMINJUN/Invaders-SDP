package screen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

import java.io.IOException;

import HUDTeam.DrawManagerImpl;
import engine.*;
import engine.Achievement.AchievementManager;
import engine.Achievement.AchievementType;
import entity.*;
// shield and heart recovery
// Sound Operator
import lombok.Getter;
import twoplayermode.TwoPlayerMode;

import static engine.Globals.NUM_LEVELS;
import static engine.Globals.getLogger;


/**
 * Implements the game screen, where the action happens.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class GameScreen extends Screen {

	/** Current game difficulty settings. */
	private GameSettings gameSettings;
	@Getter
	/** Current difficulty level number. */
	private int level;
	@Getter
	/** Formation of enemy ships. */
	private EnemyShipFormation enemyShipFormation;
	/** Player's ship1. */
	private Ship ship1;
	/** Bonus enemy ship1 that appears sometimes. */
    public EnemyShip enemyShipSpecial;
	/** Minimum time between bonus ship1 appearances. */
	private Cooldown enemyShipSpecialCooldown;
	/** Time until bonus ship1 explosion disappears. */
    public Cooldown enemyShipSpecialExplosionCooldown;
	/** Time from finishing the level to screen change. */
	private Cooldown screenFinishedCooldown;
	/** Add an itemManager Instance */
	public static ItemManager itemManager; //by Enemy team
	/** Shield item */
	private ItemBarrierAndHeart item;	// team Inventory
	private FeverTimeItem feverTimeItem;
	/** Speed item */
	private SpeedItem speedItem;
	/** Current score. */
	public int score;
	/** Player lives left. */
	private int lives;
	/** ship2 lives left.*/
	public int livestwo;
	/** Total bullets shot by the player. */
	public int bulletsShot;
	/** Total ships destroyed by the player. */
	public int shipsDestroyed;
	/** Moment the game starts. */
	private long gameStartTime;
	/** Checks if the level is finished. */
	@Getter
	private boolean levelFinished;
	/** Checks if a bonus life is received. */
	private boolean bonusLife;
	/**
	* Added by the Level Design team
	*
	* Counts the number of waves destroyed
	* **/
	private int waveCounter;

	/** ### TEAM INTERNATIONAL ### */
	/** Booleans for horizontal background movement */
    public boolean backgroundMoveLeft = false;
	public boolean backgroundMoveRight = false;



	// --- OBSTACLES
	private Cooldown obstacleSpawnCooldown; //control obstacle spawn speed
	/** Shield item */


	// Soomin Lee / TeamHUD
	/** Moment the user starts to play */
	private long playStartTime;
	/** Total time to play */
    protected int playTime = 0;
	/** Play time on previous levels */
	private int playTimePre = 0;

	// Team-Ctrl-S(Currency)
	/** Total coin **/
    protected int coin;
	/** Total gem **/
    protected int gem;
	/** Total hitCount **/		//CtrlS
    public int hitCount;
	/** Unique id for shot of bullets **/ //CtrlS
    public int fire_id;
	/** Set of fire_id **/
	public Set<Integer> processedFireBullet;

	/** Score calculation. */
    protected ScoreManager scoreManager;    //clove

	private Statistics statistics; //Team Clove
	private int fastKill;

	/** CtrlS: Count the number of coin collected in game */
    public int coinItemsCollected;

	/**
	 * Constructor, establishes the properties of the screen.
	 *
	 * @param gameState
	 *            Current game state.
	 * @param gameSettings
	 *            Current game settings.
	 * @param bonusLife
	 *            Checks if a bonus life is awarded this level.
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 */
	public GameScreen(final GameState gameState,
			final GameSettings gameSettings, final boolean bonusLife,
			final int width, final int height, final int fps) {
		super(width, height, fps);

		this.gameSettings = gameSettings;
		this.bonusLife = bonusLife;
		this.level = gameState.getLevel();
		this.score = gameState.getScore();
		this.bulletsShot = gameState.getBulletsShot();
		this.shipsDestroyed = gameState.getShipsDestroyed();
		this.item = new ItemBarrierAndHeart();   // team Inventory
		this.feverTimeItem = new FeverTimeItem(); // team Inventory
		this.speedItem = new SpeedItem();   // team Inventory
		this.coin = gameState.getCoin(); // Team-Ctrl-S(Currency)
		this.gem = gameState.getGem(); // Team-Ctrl-S(Currency)
		this.hitCount = gameState.getHitCount(); //CtrlS
		this.fire_id = 0; //CtrlS - fire_id means the id of bullet that shoot already. It starts from 0.
		this.processedFireBullet = new HashSet<>(); //CtrlS - initialized the processedFireBullet

		/**
		* Added by the Level Design team
		*
		* Sets the wave counter
		* **/
		this.waveCounter = 1;

		// Soomin Lee / TeamHUD
		this.playTime = gameState.getTime();
		this.scoreManager = new ScoreManager(level, score); //Team Clove
		this.statistics = new Statistics(); //Team Clove
		this.coinItemsCollected = gameState.getCoinItemsCollected(); // CtrlS

		this.ship1 = new Ship(this.width / 2, this.height - 30, Color.RED); // add by team HUD
		this.ship1.setKEY_LEFT(KeyEvent.VK_LEFT);
		this.ship1.setKEY_RIGHT(KeyEvent.VK_RIGHT);
		this.ship1.setKEY_SHOOT(KeyEvent.VK_ENTER);
		this.ship1.setHealth(gameState.getLivesRemaining());
		this.addEntity(ship1);
	}

	/**
	 * Initializes basic screen properties, and adds necessary elements.
	 */
	public void initialize() {
		super.initialize();
		/** initialize background **/
		drawManager.loadBackground(this.level);

		enemyShipFormation = new EnemyShipFormation(this.gameSettings);
		enemyShipFormation.attach(this);

		/** initialize itemManager */
		this.itemManager = new ItemManager(this.height, drawManager, this); //by Enemy team

		enemyShipFormation.setItemManager(this.itemManager);//add by team Enemy

		Set<EnemyShip> enemyShipSet = new HashSet<>();
		for (EnemyShip enemyShip : this.enemyShipFormation) {
			enemyShipSet.add(enemyShip);
		}
		this.itemManager.setEnemyShips(enemyShipSet);

		// Appears each 10-30 seconds.
		this.enemyShipSpecialCooldown = Core.getVariableCooldown(
				Globals.GAME_SCREEN_BONUS_SHIP_INTERVAL, Globals.GAME_SCREEN_BONUS_SHIP_VARIANCE);
		this.enemyShipSpecialCooldown.reset();
		this.enemyShipSpecialExplosionCooldown = Core
				.getCooldown(Globals.GAME_SCREEN_BONUS_SHIP_EXPLOSION);
		this.screenFinishedCooldown = Core.getCooldown(Globals.GAME_SCREEN_SCREEN_CHANGE_INTERVAL);

		// Special input delay / countdown.
		this.gameStartTime = System.currentTimeMillis();
		this.inputDelay = Core.getCooldown(Globals.GAME_SCREEN_INPUT_DELAY);
		this.inputDelay.reset();

		// Soomin Lee / TeamHUD
		this.playStartTime = gameStartTime + Globals.GAME_SCREEN_INPUT_DELAY;
		this.playTimePre = playTime;


		// 	// --- OBSTACLES - Initialize obstacles
		this.obstacleSpawnCooldown = Core.getCooldown(Math.max(2000 - (level * 200), 500)); // Minimum 0.5s
	}

	/**
	 * Starts the action.
	 *
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		this.score += Globals.GAME_SCREEN_LIFE_SCORE * (this.lives - 1);
		this.logger.info("Screen cleared with a score of " + this.scoreManager.getAccumulatedScore());

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected boolean update() {
		boolean gameProgress = inputDelay.checkFinished() && !isLevelFinished();
		ship1.setCanMove(gameProgress);

		boolean needDraw = super.update();

		if (gameProgress) {
			// --- OBSTACLES
			if (this.obstacleSpawnCooldown.checkFinished()) {
				// Adjust spawn amount based on the level
				int spawnAmount = Math.min(level, 3); // Spawn up to 3 obstacles at higher levels
				for (int i = 0; i < spawnAmount; i++) {
					int randomX = new Random().nextInt(this.width - 30);
					addEntity(new Obstacle(randomX, 50)); // Start each at the top of the screen
				}
				this.obstacleSpawnCooldown.reset();
			}

			if (this.enemyShipSpecial != null) {
				if (!this.enemyShipSpecial.isDestroyed())
					this.enemyShipSpecial.move(2, 0);
				else if (this.enemyShipSpecialExplosionCooldown.checkFinished())
					this.enemyShipSpecial = null;

			}
			if (this.enemyShipSpecial == null
					&& this.enemyShipSpecialCooldown.checkFinished()) {
				this.enemyShipSpecial = new EnemyShip();
				this.enemyShipSpecialCooldown.reset();
				//Sound Operator
				
				SoundManager.playES("UFO_come_up");
				this.logger.info("A special ship1 appears");
			}
			if (this.enemyShipSpecial != null
					&& this.enemyShipSpecial.getPositionX() > this.width) {
				this.enemyShipSpecial = null;
				this.logger.info("The special ship1 has escaped");
			}

			this.item.updateBarrierAndShip(this.ship1);   // team Inventory
			this.speedItem.update();         // team Inventory
			this.feverTimeItem.update();
			this.enemyShipFormation.update();
			this.enemyShipFormation.shoot();
		}


		needDraw = true;

		/**
		* Added by the Level Design team and edit by team Enemy
		* Changed the conditions for the game to end  by team Enemy
		*
		* Counts and checks if the number of waves destroyed match the intended number of waves for this level
		* Spawn another wave
		**/
		if (getRemainingEnemies() == 0 && waveCounter < this.gameSettings.getWavesNumber()) {

			waveCounter++;
			this.initialize();

		}

		/**
		* Wave counter condition added by the Level Design team*
		* Changed the conditions for the game to end  by team Enemy
		*
		* Checks if the intended number of waves for this level was destroyed
		* **/
		if ((getRemainingEnemies() == 0 || ship1.getHealth() == 0)
		&& !this.levelFinished
		&& waveCounter == this.gameSettings.getWavesNumber()) {
			this.levelFinished = true;
			this.screenFinishedCooldown.reset();
		}

		if (this.levelFinished && this.screenFinishedCooldown.checkFinished()) {
			//this.logger.info("Final Playtime: " + playTime + " seconds");    //clove
			if(level == NUM_LEVELS){
				AchievementManager.getInstance().checkAchievement(AchievementType.LIVES, ship1.getHealth());
			}
			AchievementManager.getInstance().checkAchievement(AchievementType.SCORE, score);
            try { //Team Clove
				statistics.comHighestLevel(level);
				statistics.addBulletShot(bulletsShot);
				statistics.addShipsDestroyed(shipsDestroyed);
				AchievementManager.getInstance().checkAchievement(AchievementType.FASTKILL, fastKill);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            this.isRunning = false;
		}
		return needDraw;
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	public void draw() {

		/** ### TEAM INTERNATIONAL ### */
		drawManager.drawBackground(backgroundMoveRight, backgroundMoveLeft);
		this.backgroundMoveRight = false;
		this.backgroundMoveLeft = false;

		DrawManagerImpl.drawRect(0, 0, this.width, Globals.GAME_SCREEN_SEPARATION_LINE_HEIGHT, Color.BLACK);
		DrawManagerImpl.drawRect(0, this.height - 70, this.width, 70, Color.BLACK); // by Saeum Jung - TeamHUD

		if (this.enemyShipSpecial != null)
			drawManager.drawEntity(this.enemyShipSpecial,
					this.enemyShipSpecial.getPositionX(),
					this.enemyShipSpecial.getPositionY());

		enemyShipFormation.draw();

		DrawManagerImpl.drawSpeed(this, ship1.getSpeed()); // Ko jesung / HUD team
		DrawManagerImpl.drawSeparatorLine(this,  this.height-65); // Ko jesung / HUD team


		// Interface.
//		drawManager.drawScore(this, this.scoreManager.getAccumulatedScore());    //clove -> edit by jesung ko - TeamHUD(to udjust score)
//		drawManager.drawScore(this, this.score); // by jesung ko - TeamHUD
		DrawManagerImpl.drawScore2(this,this.score); // by jesung ko - TeamHUD
		drawManager.drawLives(this, ship1.getHealth());
		drawManager.drawHorizontalLine(this, Globals.GAME_SCREEN_SEPARATION_LINE_HEIGHT - 1);
		DrawManagerImpl.drawRemainingEnemies(this, getRemainingEnemies()); // by HUD team SeungYun
		DrawManagerImpl.drawLevel(this, this.level);
		DrawManagerImpl.drawBulletSpeed(this, ship1.getBulletSpeed());
		// Call the method in DrawManagerImpl - Lee Hyun Woo TeamHud
		DrawManagerImpl.drawTime(this, this.playTime);
		// Call the method in DrawManagerImpl - Soomin Lee / TeamHUD
		drawManager.drawItem(this); // HUD team - Jo Minseo



		// Countdown to game start.
		if (!this.inputDelay.checkFinished()) {
			int countdown = (int) ((Globals.GAME_SCREEN_INPUT_DELAY
			- (System.currentTimeMillis()
			- this.gameStartTime)) / 1000);

			/**
			* Wave counter condition added by the Level Design team
			*
			* Display the wave number instead of the level number
			* **/
			if (waveCounter != 1) {
				drawManager.drawWave(this, waveCounter, countdown);
			} else {
				drawManager.drawCountDown(this, this.level, countdown,
				this.bonusLife);
			}

			drawManager.drawHorizontalLine(this, this.height / 2 - this.height
					/ 12);
			drawManager.drawHorizontalLine(this, this.height / 2 + this.height
					/ 12);
		}

		// Soomin Lee / TeamHUD
		if (this.inputDelay.checkFinished()) {
			playTime = (int) ((System.currentTimeMillis() - playStartTime) / 1000) + playTimePre;
		}

		super.draw();
	}


	/**
	 * Checks if two entities are colliding.
	 *
	 * @param a
	 *            First entity, the bullet.
	 * @param b
	 *            Second entity, the ship1.
	 * @return Result of the collision test.
	 */
	public static boolean checkCollision(final Entity a, final Entity b) {
		// Calculate center point of the entities in both axis.
		int centerAX = a.getPositionX() + a.getWidth() / 2;
		int centerAY = a.getPositionY() + a.getHeight() / 2;
		int centerBX = b.getPositionX() + b.getWidth() / 2;
		int centerBY = b.getPositionY() + b.getHeight() / 2;
		// Calculate maximum distance without collision.
		int maxDistanceX = a.getWidth() / 2 + b.getWidth() / 2;
		int maxDistanceY = a.getHeight() / 2 + b.getHeight() / 2;
		// Calculates distance.
		int distanceX = Math.abs(centerAX - centerBX);
		int distanceY = Math.abs(centerAY - centerBY);

		return distanceX < maxDistanceX && distanceY < maxDistanceY;
	}

	public int getBulletsShot(){    //clove
		return this.bulletsShot;    //clove
	}                               //clove

	/**
	 * Add playtime parameter - Soomin Lee / TeamHUD
	 * Returns a GameState object representing the status of the game.
	 *
	 * @return Current game state.
	 */
	public GameState getGameState() {
		return new GameState(this.level, this.scoreManager.getAccumulatedScore(), ship1.getHealth(), 0,
				this.bulletsShot, this.shipsDestroyed, this.playTime, this.coin, this.gem, this.hitCount, this.coinItemsCollected); // Team-Ctrl-S(Currency)
	}
	public int getLives() {
		return this.ship1.getHealth();
	}

	public void setLives(int lives) { this.ship1.setHealth(lives); }

	public Ship getShip1() {
		return ship1;
	}	// Team Inventory(Item)

	public ItemBarrierAndHeart getItem() {
		return item;
	}	// Team Inventory(Item)

	public FeverTimeItem getFeverTimeItem() {
		return feverTimeItem;
	} // Team Inventory(Item)
	/**
	 * Check remaining enemies
	 *
	 * @return remaining enemies count.
	 *
	 */
	private int getRemainingEnemies() {
		int remainingEnemies = 0;
		for (EnemyShip enemyShip : this.enemyShipFormation) {
			if (!enemyShip.isDestroyed()) {
				remainingEnemies++;
			}
		}
		return remainingEnemies;
	} // by HUD team SeungYun


	public SpeedItem getSpeedItem() {
		return this.speedItem;
	}
}
