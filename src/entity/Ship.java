package entity;

import engine.*;
import engine.DrawManager.SpriteType;
import lombok.Getter;
import lombok.Setter;
import screen.GameScreen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Set;

import static engine.Globals.*;

class PlayerGrowth {

	//Player's base stats
	private int health;          //Health
	private static double moveSpeed = 1.5;       //Movement speed
	private static int bulletSpeed = -4;     // Bullet speed
	private static int shootingDelay = 750;   // Shooting delay

	//Constructor to set initial values
	public PlayerGrowth() {//  Base shooting delay is 750ms
		ResetBulletSpeed();
		// CtrlS: set player growth based on upgrade_status.properties
		try {
			moveSpeed = Globals.getUpgradeManager().getMovementSpeed();
			shootingDelay = Globals.getUpgradeManager().getAttackSpeed();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// Increases health
	public void increaseHealth(int increment) {
		this.health += increment;
	}

	//Increases movement speed
	public void increaseMoveSpeed(double increment) {
		this.moveSpeed += increment;
	}

	// Increases bullet speed (makes bullets faster)
	public void increaseBulletSpeed(int increment) {
		this.bulletSpeed -= increment; // Increase by subtracting (since negative speed)
	}

	// Decreases shooting delay (makes shooting faster)
	public void decreaseShootingDelay(int decrement) {
		this.shootingDelay -= decrement; //  Decrease delay for faster shooting
		if (this.shootingDelay < 100) {
			this.shootingDelay = 100; // Minimum shooting delay is 100ms
		}
	}

	// reset bullet speed
	//Edit by inventory
	public static void ResetBulletSpeed(){
		bulletSpeed = -4;
	}

	// Returns current health
	public int getHealth() {
		return this.health;
	}

	//  Returns current movement speed
	public double getMoveSpeed() {
		return this.moveSpeed;
	}

	// Returns current bullet speed
	public int getBulletSpeed() {
		return this.bulletSpeed;
	}

	//  Returns current shooting delay
	public int getShootingDelay() {
		return this.shootingDelay;
	}

	// Prints player stats (for debugging)
	public void printStats() {
		System.out.println("Health: " + this.health);
		System.out.println("MoveSpeed: " + this.moveSpeed);
		System.out.println("BulletSpeed: " + this.bulletSpeed);
		System.out.println("ShootingDelay: " + this.shootingDelay + "ms");
	}
}
/**
 * Implements a ship, to be controlled by the player.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
@Getter
public class Ship extends Entity {
	/** Minimum time between shots. */
	private Cooldown shootingCooldown;
	/** Time spent inactive between hits. */
	private Cooldown destructionCooldown;
	/** PlayerGrowth 인스턴스 / PlayerGrowth instance */
	private PlayerGrowth growth;
	/** ShipStaus instance*/
	private ShipUpgradeStatus shipUpgradeStatus;
	/** NumberOfBullet instance*/
	private NumberOfBullet numberOfBullet;
	/** */
	@Getter @Setter
	private boolean canMove;
	/** Key to move ship left */
	@Setter
	private int KEY_LEFT = KeyEvent.VK_LEFT;
	/** Key to move ship right */
	@Setter
	private int KEY_RIGHT = KeyEvent.VK_RIGHT;
	/** Key to move ship up */
	@Setter
	private int KEY_UP = KeyEvent.VK_UP;
	/** Key to move ship down */
	@Setter
	private int KEY_DOWN = KeyEvent.VK_DOWN;
	/** Key to shoot bullet */
	@Setter
	private int KEY_SHOOT = KeyEvent.VK_ENTER;
	/** Ship health */
	@Getter @Setter
	private int health;
	@Getter
	private int x;
	@Getter
	private int y;
	/**	Has bomb bullet item */
	@Getter @Setter
	private boolean bombBullet;
	/**	Is barrier activated */
	@Getter @Setter
	private boolean barrierActive = false;
	/**	Store barrier acivated time */
	@Getter @Setter
	private long barrierActivationTime;
	@Getter
	private Cooldown doubleTapCooldown = new Cooldown(3500);

	//TODO : Move health to ship from GameScreen, and Add immunity time

	/**
	 * Constructor, establishes the ship's properties.
	 *
	 * @param positionX
	 *            Initial position of the ship in the X axis.
	 * @param positionY
	 *            Initial position of the ship in the Y axis.
	 */
	//Edit by Enemy
	public Ship(final int positionX, final int positionY, final Color color) {
		super(positionX, positionY - 50, 13 * 2, 8 * 2, color); // add by team HUD
		setClassName("Ship");
		this.spriteType = SpriteType.Ship;

		// Create PlayerGrowth object and set initial stats
		this.growth = new PlayerGrowth();  // PlayerGrowth 객체를 먼저 초기화

		this.shipUpgradeStatus = new ShipUpgradeStatus();
		shipUpgradeStatus.loadStatus();

		//  Now use the initialized growth object
		this.shootingCooldown = Core.getCooldown(growth.getShootingDelay());

		this.destructionCooldown = Core.getCooldown(1000);

		this.numberOfBullet = new NumberOfBullet();
	}

	/**
	 * Moves the ship speed uni ts right, or until the right screen border is
	 * reached.
	 */
	public final void moveRight() {
		this.positionX += growth.getMoveSpeed(); //  Use PlayerGrowth for movement speed
	} //Edit by Enemy


	/**
	 * Moves the ship speed units left, or until the left screen border is
	 * reached.
	 */
	public final void moveLeft() {
		this.positionX -= growth.getMoveSpeed(); // Use PlayerGrowth for movement speed
	} //Edit by Enemy

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}


	public final void moveUP() {
		this.positionY -= growth.getMoveSpeed();
	}

	public final void moveDown() {
		this.positionY += growth.getMoveSpeed();
	}

	/**
	 * Shoots a bullet upwards.
	 *
	 * @return Checks if the bullet was shot correctly.
	 *
	 * You can set Number of enemies the bullet can pierce at here.
	 */
	//Edit by Enemy and Inventory
	public final boolean shoot() {

		if (this.shootingCooldown.checkFinished()) {

			this.shootingCooldown.reset(); // Reset cooldown after shooting

			SoundManager.playES("My_Gun_Shot");

			if(isBombBullet()) {
				BombBullet bombBullet = new BombBullet(positionX, positionY, growth.getBulletSpeed());
				getCurrentScreen().addEntity(bombBullet);
				this.setBombBullet(false);
			}
			else{
				// Use NumberOfBullet to generate bullets
				Set<PiercingBullet> newBullets = numberOfBullet.addBullet(
						positionX + this.width / 2,
						positionY,
						growth.getBulletSpeed() // Use PlayerGrowth for bullet speed
				);
			}
			return true;
		}
		return false;
	}





	/**
	 * Updates status of the ship.
	 */
	@Override
	public final void update() {
		this.spriteType = this.destructionCooldown.checkFinished() ? SpriteType.Ship : SpriteType.ShipDestroyed;

		if(!isPlayDestroyAnimation() && isDestroyed()){
			// Do not draw when ship destroyed
			this.setEnabled(false);
		}

		GameScreen screen = (GameScreen) getCurrentScreen();
		if (!isDestroyed() && this.canMove) {
			InputManager inputManager = getInputManager();

			boolean moveRight = inputManager.isKeyDown(KEY_RIGHT);
			boolean moveLeft = inputManager.isKeyDown(KEY_LEFT);
			boolean moveUp = inputManager.isKeyDown((KEY_UP));
			boolean moveDown = inputManager.isKeyDown((KEY_DOWN));

			boolean isRightBorder = getPositionX()
					+ this.getWidth() + this.getSpeed() > screen.getWidth() - 1;
			boolean isLeftBorder = getPositionX()
					- this.getSpeed() < 1;
			boolean isTopBorder = (getPositionY() - this.getSpeed())
					< screen.getHeight() * 0.6;
			boolean isBottomBorder = getPositionY() + this.getHeight() + this.getSpeed()
					> screen.getHeight() - 63;

			 if (moveRight && !isRightBorder) {
				this.moveRight();
				screen.backgroundMoveRight = false;
			}
			if (moveLeft && !isLeftBorder) {
				this.moveLeft();
				screen.backgroundMoveLeft = false;
			}
			if (moveUp && !isTopBorder) {
				this.moveUP();
			}
			if (moveDown && !isBottomBorder) {
				this.moveDown();
			}
			if (inputManager.isKeyDown(KEY_SHOOT))
				if (this.shoot()) {
					screen.bulletsShot++;
					screen.fire_id++;
					getLogger().fine("Bullet's fire_id is " + screen.fire_id);
				}
		}

		updateBarrier();
	}

	/**
	 * Switches the ship to its destroyed state.
	 */
	public final void playDestroyAnimation() {
		this.destructionCooldown.reset();
		SoundManager.playES("ally_airship_damage");
	}

	/**
	 * Checks if the ship is destroyed.
	 *
	 * @return True if the ship is currently destroyed.
	 */

	public final boolean isPlayDestroyAnimation() {
		return !this.destructionCooldown.checkFinished();
	}

	public final boolean isDestroyed() {
		return !(this.health > 0);
	}

	public final void setDestroyed(boolean destroyed) {
		if(destroyed) {
			this.destructionCooldown = Core.getCooldown(-1);
			this.setEnabled(false);
		}
		else this.destructionCooldown = Core.getCooldown(1000);
	}
	/**
	 * 스탯을 증가시키는 메서드들 (PlayerGrowth 클래스 사용)
	 * Methods to increase stats (using PlayerGrowth)
	 */

	// Increases health
	//Edit by Enemy
	public void increaseHealth(int increment) {
		growth.increaseHealth(increment);
	}

	//  Increases movement speed
	//Edit by Enemy
	public void increaseMoveSpeed() {
		growth.increaseMoveSpeed(shipUpgradeStatus.getSpeedIn());
	}

	// Increases bullet speed
	//Edit by Enemy
	public void increaseBulletSpeed() {
		growth.increaseBulletSpeed(shipUpgradeStatus.getBulletSpeedIn());
	}

	//  Decreases shooting delay
	//Edit by Enemy
	public void decreaseShootingDelay() {
		growth.decreaseShootingDelay(shipUpgradeStatus.getSuootingInIn());
		System.out.println(growth.getShootingDelay());
		this.shootingCooldown = Core.getCooldown(growth.getShootingDelay()); // Apply new shooting delay
	}

	/**
	 * Getter for the ship's speed.
	 *
	 * @return Speed of the ship.
	 */
	public final double getSpeed() {
		return growth.getMoveSpeed();
	}

	/**
	 * Calculates and returns the bullet speed in Pixels per frame.
	 *
	 * @return bullet speed (Pixels per frame).
	 */
	public final int getBulletSpeed() {
		int speed = growth.getBulletSpeed();
		return (speed >= 0) ? speed : -speed;
	}//by SeungYun TeamHUD

	public PlayerGrowth getPlayerGrowth() {
		return growth;
	}	// Team Inventory(Item)]

	public void subtractHealth(){
		this.playDestroyAnimation();
		this.health -= 1;
		getLogger().info("Hit on player ship, " + this.health
				+ " lives remaining.");

		// Sound Operator
		if (this.health == 0){
			SoundManager.playShipDieSounds();
		}
	}

	//barrier item stuffs
	public void updateBarrier() {
		if (this.barrierActive) {
			this.setSpriteType(SpriteType.ShipBarrierStatus);

			long currentTime = System.currentTimeMillis();

			if (currentTime - this.barrierActivationTime >= barrier_DURATION) {
				this.setSpriteType(SpriteType.Ship);
				deactivatebarrier();    // deactive barrier
			}
		}
	}

	public void moveToEdgeLeft(boolean isLeft) {
			if (isLeft) { this.positionX = getCurrentScreen().getWidth()-this.width; }
	}
	public void moveToEdgeRight(boolean isRight) {
			if (isRight) { this.positionX = 0; }
	}

	public void detectedDoubleTap(){
		if (Globals.getInputManager().isDoubleTab(KeyEvent.VK_RIGHT) && doubleTapCooldown.getRemainingTime() == 0 ) {
			moveToEdgeLeft(true);
			Globals.getLogger().info("Detected Double Tab Right");
			doubleTapCooldown.reset();
		}
		if (Globals.getInputManager().isDoubleTab(KeyEvent.VK_LEFT) && doubleTapCooldown.getRemainingTime() == 0) {
			moveToEdgeRight(true);
			Globals.getLogger().info("Detected Double Tab Left");
			doubleTapCooldown.reset();
		}
	}
	public void activatebarrier() {
		this.barrierActive = true;
		this.barrierActivationTime = System.currentTimeMillis();
	}

	public void deactivatebarrier() {
		this.barrierActive = false;
		getLogger().info("barrier effect ends");
	}
}