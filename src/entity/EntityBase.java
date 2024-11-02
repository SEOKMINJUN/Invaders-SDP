package entity;

import engine.Globals;
import lombok.Getter;

/**
 * Implements a generic game entity.
 */
@Getter
public abstract class EntityBase {

	public int entIndex;
	/**
	 * -- SETTER --
	 *  Constructor, establishes the entity's generic properties.
	 *
	 */
	public EntityBase() { }

	public EntityBase(int entIndex) { this.entIndex = entIndex; }

	public void update() { }

	public void draw() { }
}
