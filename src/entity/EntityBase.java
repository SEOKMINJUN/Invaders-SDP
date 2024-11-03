package entity;

import engine.Globals;
import lombok.Getter;
import lombok.Setter;

/**
 * Implements a generic game entity.
 */
@Getter @Setter
public abstract class EntityBase {

	public int entIndex = -1;
	/*
		BACKGROUND = 0,
		HUD = 1,
		OBJECT = 2
	 */
	public int layer = 2;
	public String className = "EntityBase";

	public void update() { }

	public void draw() { }

	public void remove() {
		Globals.finest("Remove "+ className);
		Globals.getCurrentScreen().removeEntity(entIndex);
	}
}
