package entity;

import engine.DrawManager;
import engine.Globals;
import engine.SoundManager;
import lombok.Getter;
import lombok.Setter;
import screen.GameScreen;

/**
 * The PiercingBullet class extends the Bullet class to implement a bullet
 * that can pierce through multiple enemies based solely on piercing count.
 */
@Setter
@Getter
public class PiercingBullet extends Bullet {

    // Variable to track how many enemies the bullet can pierce.
    private int piercingCount;

    /**
     * Constructor for PiercingBullet.
     *
     * @param positionX Initial X position of the bullet.
     * @param positionY Initial Y position of the bullet.
     * @param speed Speed of the bullet, positive is down, negative is up.
     * @param piercingCount Number of enemies the bullet can pierce.
     */
    public PiercingBullet(final int positionX, final int positionY, final int speed, int piercingCount) {
        super(positionX, positionY, speed);  // Piercing bullets do not use isPiercing flag anymore.
        this.piercingCount = piercingCount;
        setSprite();    // team Inventory
    }

    /**
     * Handles the logic when the bullet collides with an entity.
     * Reduces the piercing count and destroys the bullet when piercing is exhausted.
     */
    public void onCollision() {
        Globals.getStatistics().setShipDestroyed();
        this.piercingCount--;
        if (this.piercingCount <= 0) {
            this.destroy(); // Destroys the bullet when it can no longer pierce.
        }
    }

    /**
     * Destroys the ship, causing an explosion.
     */
    public void destroy() {
        this.setEnabled(false);
        PiercingBulletPool.recycle(this);
    }

    public void testRemoveCondition(){
        GameScreen screen = (GameScreen) Globals.getCurrentScreen();

        // Remove when out of screen
        if (getPositionY() < Globals.GAME_SCREEN_SEPARATION_LINE_HEIGHT
                || getPositionY() > screen.getHeight()-70) // ko jesung / HUD team
            remove();
    }

    public void testCollision(){
        GameScreen screen = (GameScreen) Globals.getCurrentScreen();
        if (getSpeed() > 0) {
            //Enemy Bullet
            Ship ship = null;
            while((ship = (Ship)screen.findEntityByClassname(ship, "Ship")) != null){
                if (checkCollision(ship) && !screen.isLevelFinished()) {
                    remove();
                    if (!ship.isDestroyed() && !ship.isPlayDestroyAnimation() && !ship.isBarrierActive()) {	// team Inventory
                        ship.subtractHealth();
                    }
                    return;
                }
            }
        } else {
            EnemyShip enemyShip = null;
            while((enemyShip = (EnemyShip)screen.findEntityByClassname(enemyShip, "EnemyShip")) != null){
                if (!enemyShip.isDestroyed() && checkCollision(enemyShip)) {
                    enemyShip.subtractHP();
                    this.onCollision();
                    break;
                }
            }
            Obstacle obstacle = null;
            while((obstacle = (Obstacle) screen.findEntityByClassname(obstacle, "Obstacle")) != null){
                if (!obstacle.isDestroyed() && checkCollision(obstacle)) {
                    obstacle.destroy();  // Destroy obstacle
                    this.onCollision();
                    SoundManager.playES("obstacle_explosion");
                }
            }
        }

    }

    @Override
    public void update(){
        testRemoveCondition();
        if(!this.isEnabled()) return;

        this.positionY += this.getSpeed();
        testCollision();
    }
}
