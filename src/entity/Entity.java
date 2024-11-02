package entity;

import java.awt.Color;

import engine.DrawManager.SpriteType;
import lombok.Setter;

/**
 * Implements a generic game entity.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public abstract class Entity extends EntityBase{

	/** Position in the x-axis of the upper left corner of the entity. */
	protected int positionX;
	/** Position in the y-axis of the upper left corner of the entity. */
	protected int positionY;
	/** Width of the entity. */
	protected int width;
	/** Height of the entity. */
	protected int height;
	/** Color of the entity. */
	private Color color;

	@Setter
	protected SpriteType spriteType;

	/**
     * -- SETTER --
     *  Constructor, establishes the entity's generic properties.
     *
     * @param positionX
     *            Initial position of the entity in the X axis.
     * @param positionY
     *            Initial position of the entity in the Y axis.
     * @param width
     *            Width of the entity.
     * @param height
     *            Height of the entity.
     * @param color
     *            Color of the entity.
     */
    public Entity(final int positionX, final int positionY, final int width,
			final int height, final Color color) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	/**
	 * Getter for the color of the entity.
	 * 
	 * @return Color of the entity, used when drawing it.
	 */
	public final Color getColor() {
		return color;
	}

	/**
	 * Setter for the X axis position of the entity.
	 *
	 * @param color
	 *            New color of the entity.
	 */
	public final void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Getter for the X axis position of the entity.
	 * 
	 * @return Position of the entity in the X axis.
	 */
	public final int getPositionX() {
		return this.positionX;
	}

	/**
	 * Getter for the Y axis position of the entity.
	 * 
	 * @return Position of the entity in the Y axis.
	 */
	public final int getPositionY() {
		return this.positionY;
	}

	/**
	 * Setter for the X axis position of the entity.
	 * 
	 * @param positionX
	 *            New position of the entity in the X axis.
	 */
	public final void setPositionX(final int positionX) {
		this.positionX = positionX;
	}

	/**
	 * Setter for the Y axis position of the entity.
	 * 
	 * @param positionY
	 *            New position of the entity in the Y axis.
	 */
	public final void setPositionY(final int positionY) {
		this.positionY = positionY;
	}

	/**
	 * Getter for the sprite that the entity will be drawn as.
	 * 
	 * @return Sprite corresponding to the entity.
	 */
	public final SpriteType getSpriteType() {
		return this.spriteType;
	}

	/**
	 * Getter for the width of the image associated to the entity.
	 * 
	 * @return Width of the entity.
	 */
	public final int getWidth() {
		return this.width;
	}

	/**
	 * Getter for the height of the image associated to the entity.
	 * 
	 * @return Height of the entity.
	 */
	public final int getHeight() {
		return this.height;
	}

	/**
	 * Checks if two entities are colliding.
	 *
	 * @param target
	 *            Target entity, the ship.
	 * @return Result of the collision test.
	 */
	public boolean checkCollision(final Entity target) {
		// Calculate center point of the entities in both axis.
		int centerAX = getPositionX() + getWidth() / 2;
		int centerAY = getPositionY() + getHeight() / 2;
		int centerBX = target.getPositionX() + target.getWidth() / 2;
		int centerBY = target.getPositionY() + target.getHeight() / 2;
		// Calculate maximum distance without collision.
		int maxDistanceX = getWidth() / 2 + target.getWidth() / 2;
		int maxDistanceY = getHeight() / 2 + target.getHeight() / 2;
		// Calculates distance.
		int distanceX = Math.abs(centerAX - centerBX);
		int distanceY = Math.abs(centerAY - centerBY);

		return distanceX < maxDistanceX && distanceY < maxDistanceY;
	}
}
