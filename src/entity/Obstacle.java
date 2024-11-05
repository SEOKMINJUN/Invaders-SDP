package entity;

import java.awt.Color;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager;
import engine.DrawManager.SpriteType;
import engine.Globals;
import screen.GameScreen;

/**
* Implements an obstacle, to be destroyed by the player.
*/
public class Obstacle extends Entity {
    
    /** Cooldown for obstacle movement or other features. */
    private Cooldown movementCooldown;
    /** Cooldown for the explosion animation. */
    private Cooldown explosionCooldown;
    /** Checks if the obstacle has been destroyed by a bullet. */
    private boolean isDestroyed;
    
    /**
    * Constructor, establishes the obstacle's properties.
    * 
    * @param positionX Initial position of the obstacle in the X axis.
    * @param positionY Initial position of the obstacle in the Y axis.
    */
    public Obstacle(final int positionX, final int positionY) {
        super(positionX, positionY, 14 * 2, 7 * 2, new Color(161, 142, 108)); // Use different size/color than EnemyShip
        setClassName("Obstacle");
        this.spriteType = SpriteType.Obstacle; // Ensure it's an obstacle shape sprite type
        this.isDestroyed = false;
        this.movementCooldown = Core.getCooldown(500); // Adjust the cooldown for obstacle movement
    }
    
    /**
    * Moves the obstacle a certain distance.
    * 
    * @param distanceX Distance to move in the X axis.
    * @param distanceY Distance to move in the Y axis.
    */
    public final void move(final int distanceX, final int distanceY) {
        this.positionX += distanceX;
        this.positionY += distanceY;
    }
    
    /**
    * Updates the obstacle's movement or other attributes.
    */
    @Override
    public final void update() {
        /*
        Update speed and Check remove condition
         */
        if (!this.isDestroyed) {
            // Set speed based on the level
            // And obstacle only create in GameScreen. it can't be null
            int speed = 2 + (((GameScreen)Globals.getCurrentScreen()).getLevel() / 2);  // Adjust so it increases by 1 every two levels
            this.positionY += speed; // Moves the obstacle down continuously
        } else {
            // If destroyed, check if the explosion animation should finish
            if (this.explosionCooldown != null && this.explosionCooldown.checkFinished()) {
                this.remove();
                return;
            }
        }

        if (getPositionY() > Globals.getCurrentScreen().getHeight() - 70 ||
                getPositionY() < Globals.GAME_SCREEN_SEPARATION_LINE_HEIGHT) {
            this.remove();
            return;
        }

        Ship ship = null;
        GameScreen screen = (GameScreen) Globals.getCurrentScreen();
        while((ship = (Ship)screen.findEntityByClassname(ship, "Ship")) != null){
            if(!isDestroyed() && checkCollision(ship)){
                //Obstacles ignored when barrier activated_team inventory
                int health = ship.getHealth();
                if (!screen.getItem().isbarrierActive()) {
                    health -= 1;
                    ship.setHealth(health);
                    if (!ship.isDestroyed()) {
                        ship.destroy();  // Optionally, destroy the ship or apply other effects.
                    }
                    this.destroy();  // Destroy obstacle
                    Globals.getLogger().info("Ship hit an obstacle, " + health + " lives remaining.");
                } else {
                    this.destroy();  // Destroy obstacle
                    Globals.getLogger().info("Shield blocked the hit from an obstacle, " + health + " lives remaining.");
                }
            }
            break;
        }
    }

    @Override
    public final void draw(){
        DrawManager.drawEntity(this, getPositionX(), getPositionY());
    }
    
    /**
    * Destroys the obstacle, causing it to explode.
    */
    public final void destroy() {
        if (!this.isDestroyed) {
            this.isDestroyed = true;
            this.spriteType = SpriteType.Explosion;  // Set sprite to explosion
            this.explosionCooldown = Core.getCooldown(500);  // Explosion lasts for 500 ms (adjust as needed)
            this.explosionCooldown.reset();
        }
    }
    
    /**
    * Checks if the obstacle has been destroyed.
    * 
    * @return True if the obstacle has been destroyed.
    */
    public final boolean isDestroyed() {
        return this.isDestroyed;
    }
    
    /**
    * Checks if the obstacle should be removed after explosion animation.
    * 
    * @return True if the obstacle can be removed.
    */
    public final boolean shouldBeRemoved() {
        return this.isDestroyed && this.spriteType == null;  // Remove when spriteType is null after explosion
    }
}