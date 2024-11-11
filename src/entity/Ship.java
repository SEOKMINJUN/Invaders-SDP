package entity;

import java.awt.Color;
import java.io.IOException;
import java.util.Set;

import engine.*;
import engine.DrawManager.SpriteType;
// Import NumberOfBullet class
// Import ShipStatus class
import lombok.Getter;

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
	private ShipStatus shipStatus;
	/** NumberOfBullet instance*/
	private NumberOfBullet numberOfBullet;

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

		this.shipStatus = new ShipStatus();
		shipStatus.loadStatus();

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

	/**
	 * Shoots a bullet upwards.
	 *
	 * @param bullets
	 *            List of bullets on screen, to add the new bullet.
	 * @return Checks if the bullet was shot correctly.
	 *
	 * You can set Number of enemies the bullet can pierce at here.
	 */
	//Edit by Enemy and Inventory
	public final boolean shoot() {

		if (this.shootingCooldown.checkFinished()) {

			this.shootingCooldown.reset(); // Reset cooldown after shooting

			SoundManager.playES("My_Gun_Shot");

			// Use NumberOfBullet to generate bullets
			Set<PiercingBullet> newBullets = numberOfBullet.addBullet(
					positionX + this.width / 2,
					positionY,
					growth.getBulletSpeed(), // Use PlayerGrowth for bullet speed
					Bomb.getCanShoot()
			);

			// now can't shoot bomb
			Bomb.setCanShoot(false);
			return true;
		}
		return false;
	}





	/**
	 * Updates status of the ship.
	 */
	@Override
	public final void update() {
		if (!this.destructionCooldown.checkFinished())
			this.spriteType = SpriteType.ShipDestroyed;
		else
			this.spriteType = SpriteType.Ship;
	}

	@Override
	public final void draw(){
		DrawManager.drawEntity(this, getPositionX(), getPositionY());
	}

	/**
	 * Switches the ship to its destroyed state.
	 */
	public final void destroy() {
		this.destructionCooldown.reset();
		SoundManager.playES("ally_airship_damage");
	}

	/**
	 * Checks if the ship is destroyed.
	 *
	 * @return True if the ship is currently destroyed.
	 */
	public final boolean isDestroyed() {
		return !this.destructionCooldown.checkFinished();
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
		growth.increaseMoveSpeed(shipStatus.getSpeedIn());
	}

	// Increases bullet speed
	//Edit by Enemy
	public void increaseBulletSpeed() {
		growth.increaseBulletSpeed(shipStatus.getBulletSpeedIn());
	}

	//  Decreases shooting delay
	//Edit by Enemy
	public void decreaseShootingDelay() {
		growth.decreaseShootingDelay(shipStatus.getSuootingInIn());
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
	}	// Team Inventory(Item)
}