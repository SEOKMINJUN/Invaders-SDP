package engine;

/**
 * Implements an object that stores the state of the game between levels.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class GameState {

	/** Current game level. */
	private int level;
	/** Current score. */
	public int score; // TEAM CLOVER : Changed score from private to public for usage in achievement
	/** Lives currently remaining. */
	public int livesRemaining; // TEAM CLOVER : Changed livesRemaining from private to public for usage in achievement
	/** Lives currently remaining for Player 2. */
	public int livestwoRemaining;
	/** Bullets shot until now. */
	private int bulletsShot;
	/** Ships destroyed until now. */
	public int shipsDestroyed; // TEAM CLOVER : Changed shipsDestroyed from private to public for usage in achievement
	/** Accuracy until now. */
	public float accuracy;
	// Soomin Lee / TeamHUD
	/** Total time to play. */
	private int playTime;
	/** Current coin **/
	// Team-Ctrl-S(Currency)
	private int coin;
	/** Current gem **/
	// Team-Ctrl-S(Currency)
	private int gem;
	/** Current hitCount **/
	private int hitCount;
	/** Current coinItemsCollected */
	private int coinItemsCollected;
	/** Player traveled total distance */
	private int totalDistance;

	/**
	 * Constructor.
	 *
	 * @param level
	 *            Current game level.
	 * @param score
	 *            Current score.
	 * @param livesRemaining
	 *            Lives currently remaining.
  	 * @param livestwoRemaining
	 * 	           Lives currently remaining for Player 2.
	 * @param bulletsShot
	 *            Bullets shot until now.
	 * @param shipsDestroyed
	 *            Ships destroyed until now.
	 * @param accuracy
	 * 			  Accuracy until now.
	 * Soomin Lee / TeamHUD
	 * @param playTime
	 * 	          Total time to play.
	 * @param coin
	 * 			  Current coin. // Team-Ctrl-S(Currency)
	 * @param gem
	 * 			  Current gem. // Team-Ctrl-S(Currency)
	 * @param hitCount
	 * 		   Current hitCount. // Team-Ctrl-S(Currency)
	 */
	public GameState(final int level, final int score,
					 final int livesRemaining, final int livestwoRemaining, final int bulletsShot,
					 final int shipsDestroyed, final float accuracy, final int playTime, final int coin, final int gem, final int hitCount, final int coinItemsCollected, final int playerDistance) {
		this.level = level;
		this.score = score;
		this.livesRemaining = livesRemaining;
		this.livestwoRemaining = livestwoRemaining;
		this.bulletsShot = bulletsShot;
		this.shipsDestroyed = shipsDestroyed;
		this.accuracy = accuracy;
		this.playTime = playTime;
		this.coin = coin; // Team-Ctrl-S(Currency)
		this.gem = gem; // Team-Ctrl-S(Currency)
		this.hitCount = hitCount; // Ctrl-S
		this.coinItemsCollected = coinItemsCollected; // Ctrl-S
		this.totalDistance += playerDistance;
	}

	/**
	 * @return the level
	 */
	public final int getLevel() {
		return level;
	}

	/**
	 * @return the score
	 */
	public final int getScore() {
		return score;
	}

	/**
	 * @return the livesRemaining
	 */
	public final int getLivesRemaining() {
		return livesRemaining;
	}
	/**
	 * @return the livestwoRemaining for Player 2
	 */
	public final int getLivesTwoRemaining() {
		return livestwoRemaining;
	}
	
	/**
	 * @return the bulletsShot
	 */
	public final int getBulletsShot() {
		return bulletsShot;
	}

	/**
	 * @return the shipsDestroyed
	 */
	public final int getShipsDestroyed() {
		return shipsDestroyed;
	}

	/**
	 * @return the accuracy
	 */
	public final float getAccuracy() {
		return accuracy;
	}

	/**
	 * Soomin Lee / TeamHUD
	 * @return the playTime
	 */
	public final int getTime() { return playTime; }

	/**
	 * @return the coin
	 */
	// Team-Ctrl-S(Currency)
	public final int getCoin() {
		return coin;
	}

	/**
	 * @return the gem
	 */
	// Team-Ctrl-S(Currency)
	public final int getGem() {
		return gem;
	}

	/**
	 * @return the hitCount
	 */
	// Team-Ctrl-S(Currency)
	public final int getHitCount() { return hitCount; }

	/**
	 * @return the coinItemsCollected
	 */
	// Team-Ctrl-S(Currency)
	public int getCoinItemsCollected() { return coinItemsCollected; }

	public final int getTotalDistance() { return totalDistance; }
}
