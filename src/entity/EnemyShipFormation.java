package entity;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.Timer;

import engine.*;
import lombok.Getter;
import screen.Screen;
import engine.DrawManager.SpriteType;

import static java.lang.Math.*;


/**`
 * Groups enemy ships into a formation that moves together.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
@Getter
public class EnemyShipFormation implements Iterable<EnemyShip> {
	private boolean isCircle = false;
	/** Number of iteration of movement */
	private int iteration = 0;

	/** Initial position in the x-axis. */
	private static final int INIT_POS_X = 60;
	/** Initial position in the y-axis. */
	private static final int INIT_POS_Y = 100;
	/** Distance between ships. */
	private static final int SEPARATION_DISTANCE = 60;
	private static final int SEPARATION_DISTANCE_CIRCLE = 70;
	/** Radius of circle */
	private int RADIUS=0;
	private int MINIRADIUS= 0;

	/** Proportion of C-type ships. */
	private static final double PROPORTION_C = 0.2;
	/** Proportion of B-type ships. */
	private static final double PROPORTION_B = 0.4;
	/** Lateral speed of the formation. */
	private static final int X_SPEED = 8;
	/** Downwards speed of the formation. */
	private static final int Y_SPEED = 4;
	/** Speed of the bullets shot by the members. */
	private static final int BULLET_SPEED = 4;
	/** Proportion of differences between shooting times. */
	private static final double SHOOTING_VARIANCE = .2;
	/** Margin on the sides of the screen. */
	private static final int SIDE_MARGIN = 20;
	/** Margin on the bottom of the screen. */
	private static final int BOTTOM_MARGIN = 80;
	/** Distance to go down each pass. */
	private static final int DESCENT_DISTANCE = 20;
	/** Minimum speed allowed. */
	private static final int MINIMUM_SPEED = 10;

	/** DrawManager instance. */
	private DrawManager drawManager;
	/** Application logger. */
	private Logger logger;
	/** Screen to draw ships on. */
	private Screen screen;

	/** List of enemy ships forming the formation. */
	private List<List<EnemyShip>> enemyShips;
	/** Minimum time between shots. */
	private List<SpeedItem> activeSpeedItems;
	private Cooldown shootingCooldown;
	/** Number of ships in the formation - horizontally. */
	private int nShipsWide;
	/** Number of ships in the formation - vertically. */
	private int nShipsHigh;
	/** Time between shots. */
	private int shootingInterval;
	/** Variance in the time between shots. */
	private int shootingVariance;
	/** Initial ship speed. */
	private int baseSpeed;
	/** Speed of the ships. */
	private int movementSpeed;
	/** Current direction the formation is moving on. */
	private Direction currentDirection;
	/** Direction the formation was moving previously. */
	private Direction previousDirection;
	/** Interval between movements, in frames. */
	private int movementInterval;
	/** Total width of the formation. */
	private int width;
	/** Total height of the formation. */
	private int height;
	/** Position in the x-axis of the upper left corner of the formation. */
	private int positionX;
	/** Position in the y-axis of the upper left corner of the formation. */
	private int positionY;
	/** Width of one ship. */
	private int shipWidth;
	/** Height of one ship. */
	private int shipHeight;
	/** List of ships that are able to shoot. */
	private List<EnemyShip> shooters;
	/** Number of not destroyed ships. */
    public int shipCount;

	/** Directions the formation can move. */
	private enum Direction {
		/** Movement to the right side of the screen. */
		RIGHT,
		/** Movement to the left side of the screen. */
		LEFT,
		/** Movement to the bottom of the screen. */
		DOWN
	};

	/**
	 * Constructor, sets the initial conditions.
	 *
	 * @param gameSettings
	 *            Current game settings.
	 */
	public EnemyShipFormation(final GameSettings gameSettings) {
		this.drawManager = Globals.getDrawManager();
		this.logger = Core.getLogger();
		this.enemyShips = new ArrayList<List<EnemyShip>>();
		this.activeSpeedItems = new ArrayList<>();
		this.currentDirection = Direction.RIGHT;
		this.movementInterval = 0;
		this.nShipsWide = gameSettings.getFormationWidth();
		this.nShipsHigh = gameSettings.getFormationHeight();
		this.shootingInterval = gameSettings.getShootingFrecuency();
		this.shootingVariance = (int) (gameSettings.getShootingFrecuency()
				* SHOOTING_VARIANCE);
		this.baseSpeed = gameSettings.getBaseSpeed();
		this.movementSpeed = this.baseSpeed;
		this.positionX = INIT_POS_X;
		this.positionY = INIT_POS_Y;
		this.shooters = new ArrayList<EnemyShip>();
		this.shipCount = 0;
		SpriteType spriteType = null;
		int hp=1;// Edited by Enemy
		Random rand= new Random();
		int n = rand.nextInt(2);
		if(n%2==1){ isCircle=true;
			this.logger.info("cercle"+ 2);
		}
		else isCircle=false;

		if(isCircle){
			RADIUS=gameSettings.getFormationHeight()*6;
			MINIRADIUS= gameSettings.getFormationHeight()*2;}

		this.logger.info("Initializing " + nShipsWide + "x" + nShipsHigh
				+ " ship formation in (" + positionX + "," + positionY + ")");

		// Each sub-list is a column on the formation.
		for (int i = 0; i < this.nShipsWide; i++)
			this.enemyShips.add(new ArrayList<EnemyShip>());

		for (List<EnemyShip> column : this.enemyShips) {
			int x=0;
			int y=0;
			for (int i = 0; i < this.nShipsHigh; i++) {
				double angle = 2* PI * i / this.nShipsHigh;

				if (i / (float) this.nShipsHigh < PROPORTION_C)
					if (shipCount == (nShipsHigh*1)+1 ||shipCount == (nShipsHigh*3)+1) //Edited by Enemy
						spriteType = SpriteType.ExplosiveEnemyShip1;
					else if (i / (float) this.nShipsHigh < PROPORTION_C)
						spriteType = SpriteType.EnemyShipC1;
					else if (i / (float) this.nShipsHigh < PROPORTION_B + PROPORTION_C)
						spriteType = SpriteType.EnemyShipB1;
					else
						spriteType = SpriteType.EnemyShipA1;
				if(isCircle){
					x = (int) round(RADIUS * cos(angle) + positionX + ( SEPARATION_DISTANCE_CIRCLE* this.enemyShips.indexOf(column)));
					y = (int) (RADIUS * sin(angle)) + positionY;}
				else{
					x = positionX + (SEPARATION_DISTANCE * this.enemyShips.indexOf(column));
					y = positionY+ i*SEPARATION_DISTANCE;
				}

				if(shipCount == nShipsHigh*(nShipsWide/2))
					hp = 2; // Edited by Enemy, It just an example to insert EnemyShip that hp is 2.

				column.add(new EnemyShip(x, y, spriteType,hp,this.enemyShips.indexOf(column),i));// Edited by Enemy
				this.shipCount++;
				hp = 1;// Edited by Enemy
			}
		}


		this.shipWidth = this.enemyShips.get(0).get(0).getWidth();
		this.shipHeight = this.enemyShips.get(0).get(0).getHeight();

		this.width = (this.nShipsWide - 1) * SEPARATION_DISTANCE
				+ this.shipWidth;
		this.height = (this.nShipsHigh - 1) * SEPARATION_DISTANCE
				+ this.shipHeight;

		for (List<EnemyShip> column : this.enemyShips)
			this.shooters.add(column.get(column.size() - 1));

	}

	/**
	 * Associates the formation to a given screen.
	 * 
	 * @param newScreen
	 *            Screen to attach.
	 */
	public final void attach(final Screen newScreen) {
		screen = newScreen;
	}

	/**
	 * Updates the position of the ships.
	 */

	public void update() {
		if(this.shootingCooldown == null) {
			this.shootingCooldown = Core.getVariableCooldown(shootingInterval,
					shootingVariance);
			this.shootingCooldown.reset();
		}
		
		cleanUp();

		int movementX = 0;
		int movementY = 0;
		double remainingProportion = (double) this.shipCount
				/ (this.nShipsHigh * this.nShipsWide);
		this.movementSpeed = (int) (pow(remainingProportion, 2)
				* this.baseSpeed);
		this.movementSpeed += MINIMUM_SPEED;
		
		movementInterval++;
		if (movementInterval >= this.movementSpeed) {
			movementInterval = 0;

			int circleFormationPadding = 0;

			if (isCircle) {
				circleFormationPadding = 45;
			}

			boolean isAtBottom = positionY
					+ this.height + RADIUS > screen.getHeight() - BOTTOM_MARGIN;
			boolean isAtRightSide = positionX
					+ this.width + RADIUS >= screen.getWidth() - SIDE_MARGIN;
			boolean isAtLeftSide = positionX - RADIUS - circleFormationPadding <= SIDE_MARGIN;
			boolean isAtHorizontalAltitude = positionY % DESCENT_DISTANCE == 0;

			if (currentDirection == Direction.DOWN) {
				if (isAtHorizontalAltitude)
					if (previousDirection == Direction.RIGHT) {
						currentDirection = Direction.LEFT;
						this.logger.info("Formation now moving left 1");
					} else {
						currentDirection = Direction.RIGHT;
						this.logger.info("Formation now moving right 2");
					}
			} else if (currentDirection == Direction.LEFT) {
				if (isAtLeftSide)
					if (!isAtBottom) {
						previousDirection = currentDirection;
						currentDirection = Direction.DOWN;
						this.logger.info("Formation now moving down 3");
					} else {
						currentDirection = Direction.RIGHT;
						this.logger.info("Formation now moving right 4");
					}
			} else {
				if (isAtRightSide)
					if (!isAtBottom) {
						previousDirection = currentDirection;
						currentDirection = Direction.DOWN;
						this.logger.info("Formation now moving down 5");
					} else {
						currentDirection = Direction.LEFT;
						this.logger.info("Formation now moving left 6");
					}
			}

			if (currentDirection == Direction.RIGHT)
				movementX = X_SPEED;
			else if (currentDirection == Direction.LEFT)
				movementX = -X_SPEED;
			else
				movementY = Y_SPEED;


			positionX += movementX;
			positionY += movementY;

			double angle = (PI/this.nShipsHigh);
			int temp=0;
			iteration++;
			for (List<EnemyShip> column : this.enemyShips){
				temp=0;
				for (EnemyShip enemyShip : column) {
					double currentAngle = angle * (temp+iteration);
					int distanceX = movementX + (int) (MINIRADIUS * cos(currentAngle));
					int distanceY = movementY + (int) (MINIRADIUS * sin(currentAngle));

					if (distanceX + enemyShip.positionX > screen.getWidth() - SIDE_MARGIN || distanceX + enemyShip.positionX < SIDE_MARGIN) {
						distanceX = 0;

					} else if (distanceY + enemyShip.positionY > screen.getHeight() - BOTTOM_MARGIN) {
						distanceY = 0;
					}

					enemyShip.move(
							distanceX,
							distanceY
					);
					temp++;
				}
			}
		}
	}

	/**
	 * Cleans empty columns, adjusts the width and height of the formation.
	 */
	private void cleanUp() {
		Set<Integer> emptyColumns = new HashSet<Integer>();
		int maxColumn = 0;
		int minPositionY = Integer.MAX_VALUE;
		for (List<EnemyShip> column : this.enemyShips) {
			if (!column.isEmpty()) {
				// Height of this column
				int columnSize = column.get(column.size() - 1).positionY
						- this.positionY + this.shipHeight;
				maxColumn = max(maxColumn, columnSize);
				minPositionY = min(minPositionY, column.get(0)
						.getPositionY());
			} else {
				// Empty column, we remove it.
				emptyColumns.add(this.enemyShips.indexOf(column));
			}
		}
		for (int index : emptyColumns) {
			this.enemyShips.remove(index);
			logger.info("Removed column " + index);
		}

		int leftMostPoint = 0;
		int rightMostPoint = 0;
		
		for (List<EnemyShip> column : this.enemyShips) {
			if (!column.isEmpty()) {
				if (leftMostPoint == 0)
					leftMostPoint = column.get(0).getPositionX();
				rightMostPoint = column.get(0).getPositionX();
			}
		}

		this.width = rightMostPoint - leftMostPoint + this.shipWidth;
		this.height = maxColumn;

		this.positionX = leftMostPoint;
		this.positionY = minPositionY;
	}

	/**
	 * Shoots a bullet downwards.
	 *
	 */
	public final void shoot() { // Edited by Enemy
		// For now, only ships in the bottom row are able to shoot.
		if (!shooters.isEmpty()) { // Added by team Enemy
			int index = (int) (random() * this.shooters.size());
			EnemyShip shooter = this.shooters.get(index);
			if (this.shootingCooldown.checkFinished()) {
				this.shootingCooldown.reset();
				SoundManager.playES("Enemy_Gun_Shot_1_ES");
				PiercingBulletPool.getPiercingBullet( // Edited by Enemy
						shooter.getPositionX() + shooter.width / 2,
						shooter.getPositionY(),
						BULLET_SPEED,
						0); // Edited by Enemy
			}
		}
	}

	/**
	 * Gets the ship on a given column that will be in charge of shooting.
	 * 
	 * @param column
	 *            Column to search.
	 * @return New shooter ship.
	 */
	public final EnemyShip getNextShooter(final List<EnemyShip> column) {
		Iterator<EnemyShip> iterator = column.iterator();
		EnemyShip nextShooter = null;
		while (iterator.hasNext()) {
			EnemyShip checkShip = iterator.next();
			if (checkShip != null && !checkShip.isDestroyed())
				nextShooter = checkShip;
		}

		return nextShooter;
	}

	/**
	 * Check need to update next shooter and update shooter list when ship destroyed
	 *
	 * @param destroyedShip
	 *            Destroyed ship for update shooter
	 */
	//TODO : Fix this when dead by bomb
	public final void setNextShooterByDestroyedShip(EnemyShip destroyedShip) {
		if (this.shooters.contains(destroyedShip)) {
			int destroyedShipIndex = this.shooters.indexOf(destroyedShip);
			int destroyedShipColumnIndex = -1;

			for (List<EnemyShip> column : this.enemyShips)
				if (column.contains(destroyedShip)) {
					destroyedShipColumnIndex = this.enemyShips.indexOf(column);
					break;
				}

			EnemyShip nextShooter = getNextShooter(this.enemyShips
					.get(destroyedShipColumnIndex));

			if (nextShooter != null)
				this.shooters.set(destroyedShipIndex, nextShooter);
			else {
				this.shooters.remove(destroyedShipIndex);
				this.logger.info("Shooters list reduced to "
						+ this.shooters.size() + " members.");
			}
		}
	}

	/**
	 * Returns an iterator over the ships in the formation.
	 * 
	 * @return Iterator over the enemy ships.
	 */
	@Override
	public final Iterator<EnemyShip> iterator() {
		Set<EnemyShip> enemyShipsList = new HashSet<>();
		for (List<EnemyShip> column : this.enemyShips) {
			for (EnemyShip enemyShip : column) {
				enemyShipsList.add(enemyShip);
			}
		}
		return enemyShipsList.iterator();
	}

	/**
	 * Checks if there are any ships remaining.
	 * 
	 * @return True when all ships have been destroyed.
	 */
	public final boolean isEmpty() {
		return this.shipCount <= 0;
	}

	public final void BecomeCircle(boolean iscircle){
		this.isCircle=iscircle;
	}
}