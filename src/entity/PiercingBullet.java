package entity;

import java.awt.Color;

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
//TODO : Merge PiercingBullet and Bullet
@Setter
@Getter
public class PiercingBullet extends Bullet {

    /**
     * -- GETTER --
     *  Getter for the number of remaining piercings.
     *
     *
     * -- SETTER --
     *  Setter for the piercing count.
     *
     @return The remaining piercings.
      * @param piercingCount The new number of piercings the bullet can perform.
     */
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
     *
     * @param entity The entity the bullet collided with.
     */
    public void onCollision(Entity entity) {
        this.piercingCount--;
        if (this.piercingCount <= 0) {
            this.destroy(); // Destroys the bullet when it can no longer pierce.
        }
    }

    /**
     * Destroys the ship, causing an explosion.
     */
    public void destroy() {
        this.spriteType = DrawManager.SpriteType.Explosion;
    }

    public void testRemoveCondition(){
        GameScreen screen = (GameScreen) Globals.getCurrentScreen();

        // Remove when out of screen
        if (getPositionY() < Globals.GAME_SCREEN_SEPARATION_LINE_HEIGHT
                || getPositionY() > screen.getHeight()-70) // ko jesung / HUD team
        {
            //Ctrl-S : set true of CheckCount if the bullet is planned to recycle.
            setCheckCount(true);
            remove();
        }
    }

    public void testCollision(){
        GameScreen screen = (GameScreen) Globals.getCurrentScreen();
        if (getSpeed() > 0) {
            //Enemy Bullet
            Ship ship = null;
            while((ship = (Ship)screen.findEntityByClassname(ship, "Ship")) != null){
                if (checkCollision(ship) && !screen.isLevelFinished()) {
                    remove();
                    if (!ship.isDestroyed() && !screen.getItem().isbarrierActive()) {	// team Inventory
                        ship.destroy();
                        int lives = screen.getLives()-1;
                        screen.setLives(lives);
                        Globals.getLogger().info("Hit on player ship, " + lives
                                + " lives remaining.");

                        // Sound Operator
                        if (lives == 0){
                            SoundManager.playShipDieSounds();
                        }
                    }
                    return;
                }
            }
        } else {
            //Player Bullet
            // CtrlS - set fire_id of bullet.
            setFire_id(fire_id);
            EnemyShipFormation enemyShipFormation = screen.getEnemyShipFormation();
            for (EnemyShip enemyShip : enemyShipFormation) {
                if (!enemyShip.isDestroyed()
                        && checkCollision(enemyShip)) {
                    int[] CntAndPnt = enemyShipFormation._destroy(this, enemyShip, false);
                    screen.shipsDestroyed += CntAndPnt[0];
                    int feverScore = CntAndPnt[0];

                    if(enemyShip.getHp() <= 0) {
                        //inventory_f fever time is activated, the score is doubled.
                        if(screen.getFeverTimeItem().isActive()) {
                            feverScore = feverScore * 10;
                        }
                        screen.shipsDestroyed++;
                    }

                    ScoreManager.addScore(feverScore); //clove
                    screen.score += CntAndPnt[1];

                    // CtrlS - If collision occur then check the bullet can process
                    if (!screen.processedFireBullet.contains(this.getFire_id())) {
                        // CtrlS - increase hitCount if the bullet can count
                        if (this.isCheckCount()) {
                            screen.hitCount++;
                            this.setCheckCount(false);
                            Globals.getLogger().info("Hit count!");
                            screen.processedFireBullet.add(this.getFire_id()); // mark this bullet_id is processed.
                        }
                    }

                    this.onCollision(enemyShip); // Handle bullet collision with enemy ship

                    // Check PiercingBullet piercing count and add to recyclable if necessary
                    if (this.getPiercingCount() <= 0) {
                        //Ctrl-S : set true of CheckCount if the bullet is planned to recycle.
                        this.setCheckCount(true);
                        remove();
                    }
                }
                // Added by team Enemy.
                // Enemy killed by Explosive enemy gives points too
                if (enemyShip.isChainExploded()) {
                    if (enemyShip.getColor() == Color.MAGENTA) {
                        screen.itemManager.dropItem(enemyShip, 1, 1);
                    }
                    screen.score += enemyShip.getPointValue();
                    screen.shipsDestroyed++;
                    enemyShip.setChainExploded(false); // resets enemy's chain explosion state.
                }
            }
            if (screen.enemyShipSpecial != null
                    && !screen.enemyShipSpecial.isDestroyed()
                    && checkCollision(screen.enemyShipSpecial)) {
                int feverSpecialScore = screen.enemyShipSpecial.getPointValue();
                // inventory - Score bonus when acquiring fever items
                if (screen.getFeverTimeItem().isActive()) { feverSpecialScore *= 10; } //TEAM CLOVE //Team inventory

                // CtrlS - If collision occur then check the bullet can process
                if (!screen.processedFireBullet.contains(this.getFire_id())) {
                    // CtrlS - If collision occur then increase hitCount and checkCount
                    if (this.isCheckCount()) {
                        screen.hitCount++;
                        this.setCheckCount(false);
                        Globals.getLogger().info("Hit count!");
                    }

                }
                ScoreManager.addScore(feverSpecialScore); //clove
                screen.shipsDestroyed++;
                screen.enemyShipSpecial.destroy();
                screen.enemyShipSpecialExplosionCooldown.reset();

                this.onCollision(screen.enemyShipSpecial); // Handle bullet collision with special enemy

                // Check PiercingBullet piercing count for special enemy and add to recyclable if necessary
                if (this.getPiercingCount() <= 0) {
                    //Ctrl-S : set true of CheckCount if the bullet is planned to recycle.
                    this.setCheckCount(true);
                    remove();
                }

                //// Drop item to 100%
                screen.itemManager.dropItem(screen.enemyShipSpecial,1,2);
            }

            Obstacle obstacle = null;
            while((obstacle = (Obstacle) screen.findEntityByClassname(obstacle, "Obstacle")) != null){
                if (!obstacle.isDestroyed() && checkCollision(obstacle)) {
                    obstacle.destroy();  // Destroy obstacle
                    remove();
                    SoundManager.playES("obstacle_explosion");
                }
            }
        }
    }

    @Override
    public void update(){
        if(!this.isEnabled()) return;

        this.positionY += this.getSpeed();

        testRemoveCondition();
        testCollision();
    }
}
