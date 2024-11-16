package entity;

import engine.DrawManager.SpriteType;
import engine.Globals;
import engine.SoundManager;
import screen.GameScreen;

/**
 * Implements a bullet that moves vertically up or down.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class BombBullet extends PiercingBullet {


	/**
	 * Decide the bullet is enabled and show
	 */
	/**
	 * Constructor, establishes the bullet's properties.
	 *
	 * @param positionX
	 *            Initial position of the bullet in the X axis.
	 * @param positionY
	 *            Initial position of the bullet in the Y axis.
	 * @param speed
	 *            Speed of the bullet, positive or negative depending on
	 *            direction - positive is down.
	 */
	public BombBullet(final int positionX, final int positionY, final int speed) {
		super(positionX, positionY, speed, 0);
		setClassName("BombBullet");
		this.spriteType = SpriteType.ItemBomb;
	}

	@Override
	public void onCollision() {
		explosion();
		Globals.getStatistics().setShipDestroyed();
		this.destroy(); // Destroys the bullet when it can no longer pierce.
	}

	@Override
	public void destroy() {
		this.setEnabled(false);
		remove();
	}

	private void explosion() {
		SoundManager.playES("enemy_explosion");

		GameScreen screen = (GameScreen) Globals.getCurrentScreen();
		EnemyShip enemyShip = null;
		while((enemyShip = (EnemyShip)screen.findEntityByClassname(enemyShip, "EnemyShip")) != null){
			if (!enemyShip.isDestroyed() && checkCollisionWithRadius(enemyShip, 100)) {
				enemyShip.subtractHP();
			}
		}
	}
}
