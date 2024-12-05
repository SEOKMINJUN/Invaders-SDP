package entity;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import engine.*;
import engine.DrawManager.SpriteType;
import lombok.Getter;
import lombok.Setter;
import screen.GameScreen;

import javax.swing.*;

/**
 * Implements a enemy ship, to be destroyed by the player.
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 */
public class EnemyShip extends Entity {
	/** Point value of a type A enemy. */
	private static final int A_TYPE_POINTS = 10;
	/** Point value of a type B enemy. */
	private static final int B_TYPE_POINTS = 20;
	/** Point value of a type C enemy. */
	private static final int C_TYPE_POINTS = 30;
	/** Point value of a type Explosive enemy. */
	private static final int EXPLOSIVE_TYPE_POINTS = 50; //Edited by Enemy
	/** Point value of a bonus enemy. */
	private static final int BONUS_TYPE_POINTS = 100;

    // Added by team Enemy
    /** EnemyShip's health point
     * -- SETTER --
     *  Setter for the Hp of the Enemy ship.
     *
     * @param hp
     * 			New hp of the Enemy ship.
     */
	@Getter @Setter
    private int hp; // Add by team Enemy
    // Add by team Enemy
    /** EnemyShip's Initial x-coordinate
     * -- GETTER --
     *  Getter for the Initial x-coordinate of this EnemyShip.
     *
     * @return Initial x-coordinate of the ship.
     **/
	@Getter
    private int x; // Add by team Enemy
    // Add by team Enemy
    /** EnemyShip's Initial y=coordinate
     * -- GETTER --
     *  Getter for the Initial y-coordinate of this EnemyShip.
     *
     * @return Initial x-coordinate of the ship.
     **/
	@Getter
    private int y; // Add by team Enemy

	/** Cooldown between sprite changes. */
	private Cooldown animationCooldown;
	/** Checks if the ship has been hit by a bullet. */
	private boolean isDestroyed;
	/** Checks if the ship is bombed */
	private boolean isChainExploded;
	/** Values of the ship, in points, when destroyed. */
	private int pointValue;

	/** Speed reduction or increase multiplier (1.0 means normal speed).
     * -- SETTER --
     *  Getter for the current speed multiplier.
     *
     */
	@Getter
    @Setter
    private double speedMultiplier = 1.0;
	private double defaultSpeedMultiplier;
	private Cooldown explosionCooldown;
	private int shipType;

	/**
	 * Constructor, establishes the ship's properties.
	 *
	 * @param positionX
	 *            Initial position of the ship in the X axis.
	 * @param positionY
	 *            Initial position of the ship in the Y axis.
	 * @param spriteType
	 *            Sprite type, image corresponding to the ship.
	 */


	public EnemyShip(final int positionX, final int positionY,
			final SpriteType spriteType, int hp,int x, int y) {// Edited by Enemy
		super(positionX, positionY, 12 * 2, 8 * 2, getColorByHP(hp));

		this.hp = hp;// Add by team Enemy
		this.spriteType = spriteType;
		this.animationCooldown = Core.getCooldown(500);
		this.isDestroyed = false;
		this.x = x; // Add by team enemy
		this.y = y; // Add by team enemy
		this.speedMultiplier=1.0; // default 1.0
		this.defaultSpeedMultiplier = 1.0;

		switch (this.spriteType) {
			case EnemyShipA1:
			case EnemyShipA2:
				this.pointValue = A_TYPE_POINTS;
				break;
			case EnemyShipB1:
			case EnemyShipB2:
				this.pointValue = B_TYPE_POINTS;
				break;
			case EnemyShipC1:
			case EnemyShipC2:
				this.pointValue = C_TYPE_POINTS;
				break;
			case ExplosiveEnemyShip1: //Edited by Enemy
			case ExplosiveEnemyShip2:
				super.setColor(new Color(237, 28, 36)); //set ExplosiveEnemyShip Color
				this.pointValue = EXPLOSIVE_TYPE_POINTS;
				break;
			default:
				this.pointValue = 0;
				break;
		}
		this.shipType = 0;
		this.explosionCooldown = Core.getCooldown(500);

		setClassName("EnemyShip");
		Globals.getCurrentScreen().addEntity(this);
		setEnabled(true);
	}

	/**
	 * Constructor, establishes the ship's properties for a special ship, with
	 * known starting properties.
	 */
	public EnemyShip() {
		super(-32, 60, 16 * 2, 7 * 2, Color.RED);
		setClassName("EnemyShip");

		this.hp = 1; // Add by team Enemy
		this.spriteType = SpriteType.EnemyShipSpecial;
		this.isDestroyed = false;
		this.pointValue = BONUS_TYPE_POINTS;
		this.x = -2;  // Add by team Enemy
		this.y = -2; // Add by team Enemy

		this.shipType = 1;
		this.explosionCooldown = Core.getCooldown(Globals.GAME_SCREEN_BONUS_SHIP_EXPLOSION);
		setClassName("EnemyShip");
		Globals.getCurrentScreen().addEntity(this);
		setEnabled(true);
	}


	/**
	 * Getter for the score bonus if this ship is destroyed.
	 *
	 * @return Value of the ship.
	 */
	public final int getPointValue() {
		return this.pointValue;
	}

	/**
	 * Moves the ship the specified distance.
	 *
	 * @param distanceX
	 *            Distance to move in the X axis.
	 * @param distanceY
	 *            Distance to move in the Y axis.
	 */
	public final void move(final int distanceX, final int distanceY) {
		this.positionX += distanceX * this.getSpeedMultiplier(); // team Inventory
		this.positionY += distanceY;
	}

	/**
	 * Updates attributes, mainly used for animation purposes.
	 */
	public final void update() {
		if(isDestroyed() && explosionCooldown.checkFinished()){
			remove();
		}
		if (this.shipType == 0 && this.animationCooldown.checkFinished()) {
			this.animationCooldown.reset();

			switch (this.spriteType) {
				case EnemyShipA1:
					this.spriteType = SpriteType.EnemyShipA2;
					break;
				case EnemyShipA2:
					this.spriteType = SpriteType.EnemyShipA1;
					break;
				case EnemyShipB1:
					this.spriteType = SpriteType.EnemyShipB2;
					break;
				case EnemyShipB2:
					this.spriteType = SpriteType.EnemyShipB1;
					break;
				case EnemyShipC1:
					this.spriteType = SpriteType.EnemyShipC2;
					break;
				case EnemyShipC2:
					this.spriteType = SpriteType.EnemyShipC1;
					break;
				case ExplosiveEnemyShip1: //Edited by Enemy
					this.spriteType = SpriteType.ExplosiveEnemyShip2;
					break;
				case ExplosiveEnemyShip2: //Edited by Enemy
					this.spriteType = SpriteType.ExplosiveEnemyShip1;
					break;
				default:
					break;
			}
		}
	}

	/**
	 * Destroys the ship, causing an explosion.
	 */
	public final void destroy() {
		GameScreen screen = (GameScreen) Globals.getCurrentScreen();

		// Check it is explosive type ship
		switch (this.spriteType){
			case ExplosiveEnemyShip1:
			case ExplosiveEnemyShip2:
				explosion();
				SoundManager.playES("enemy_explosion");
				break;
			default:
				if(hp > 0){
					Globals.getLogger().info("Enemy ship lost 1 HP in ("
							+ this.x + "," + y + ")");
				}else{
					Globals.getLogger().info("Destroyed ship in ("
							+ this.x + "," + y + ")");
				}
				break;
		}

		//Set status
		this.isDestroyed = true;

		// Check it is EnemyShip in formation
		if(this.shipType == 0){
			EnemyShipFormation enemyShipFormation = screen.getEnemyShipFormation();
			enemyShipFormation.setNextShooterByDestroyedShip(this);
			enemyShipFormation.shipCount--;
		}
		if(this.shipType == 0){
			EnemyShipFormation enemyShipFormation = screen.getEnemyShipFormation();
			List<List<EnemyShip>> enemyShips = enemyShipFormation.getEnemyShips();
			if(enemyShips.size() > this.x) {
				List<EnemyShip> column = enemyShips.get(this.x);
				if (!column.remove(this)) {
					Globals.getLogger().warning("EnemyShip from " + this.x + " " + this.y + " is already removed");
				}
			}
		}
		//Check point to add
		int point = this.getPointValue();
		if(screen.getFeverTimeItem().isActive())
			point *= 2;
		screen.addEnemyShipDestroyScore(point);
		screen.shipsDestroyed++;

		// Drop Item
		if(this.spriteType == SpriteType.EnemyShipSpecial){
			SoundManager.playES("special_enemy_die");
			GameScreen.dropItem(this,1,2);
		}else{
			SoundManager.playES("basic_enemy_die");
			if (getColor().equals(Color.MAGENTA)) GameScreen.dropItem(this, 1, 1);
		}
		Globals.getCollectionManager().AddCollectionEnemyTypes(this.spriteType);

		//Update Sprite to Explosion(DEAD)
		this.spriteType = SpriteType.Explosion;
		this.explosionCooldown.reset();
	}

	private void explosion() {
		GameScreen screen = (GameScreen) Globals.getCurrentScreen();
		EnemyShip enemyShip = null;

		Queue<EnemyShip> destroyedShips = new LinkedList<>();
		Timer timer = new Timer(500, null);

		while((enemyShip = (EnemyShip)screen.findEntityByClassname(enemyShip, "EnemyShip")) != null){
			if (!enemyShip.isDestroyed() && checkCollisionWithRadius(enemyShip, 100)) {
				destroyedShips.add(enemyShip);
				break;
			}
		}

		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				while(!destroyedShips.isEmpty()){
					EnemyShip targetShip = destroyedShips.poll();
					targetShip.subtractHP();
				}
				((Timer) e.getSource()).stop();
			}
		};

		timer.addActionListener(listener);
		timer.start();
	}

	/**
	 * Checks if the ship has been destroyed.
	 *
	 * @return True if the ship has been destroyed.
	 */
	public final boolean isDestroyed() {
		return this.isDestroyed;
	}

    public void resetSpeedMultiplier() {
		this.speedMultiplier = this.defaultSpeedMultiplier;
	}

    @Override
	public void draw() {
		DrawManager.drawEntity(this, getPositionX(), getPositionY());
	}

	/**
	 * When the EnemyShip is hit and its hp reaches 0, destroy the ship
	 */
	public void subtractHP() {
		this.hp -= 1;
		if(!this.getColor().equals(Color.magenta))
			this.setColor(getColorByHP(hp));
		if(hp <= 0)
			this.destroy();

		//DEBUG LOG
		switch (this.spriteType){
			case ExplosiveEnemyShip1:
			case ExplosiveEnemyShip2:
				Globals.getLogger().finer("Destroyed ExplosiveEnemyship in ("
						+ this.x + "," + this.y + ")");
				break;
			default:
				if(hp > 0){
					Globals.getLogger().finer("Enemy ship lost 1 HP in ("
							+ this.x + "," + y + ")");
				}else{
					Globals.getLogger().finer("Destroyed ship in ("
							+ this.x + "," + y + ")");
				}
				break;
		}
	}

	/**
	 * Determine the color of the ship according to hp
	 * @param hp
	 * 			The ship's hp
	 * @return if hp is 2, return yellow
	 * 		   if hp is 3, return orange
	 * 		   if hp is 1, return white
	 */
	public static Color getColorByHP(int hp) {
		Color color = Color.WHITE; // Declare a variable to store the color
		// set basic color WHITE
		if (hp == 2)
			return new Color(0xFFEB3B);
		else if (hp == 3)
			return new Color(0xFFA500);
		else if (hp == 1)
			return Math.random() < 0.1 ? Color.MAGENTA : Color.WHITE;
		return Color.WHITE;
	}
}
