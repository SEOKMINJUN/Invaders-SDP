package entity;

import engine.DrawManager;

import java.awt.*;

public class Gem extends Entity {
    DrawManager.SpriteType layeredSprite;

    public Gem() {
        super(0, 0, 7 * 2, 6 * 2, Color.cyan);
        setClassName("Gem");
        this.spriteType = DrawManager.SpriteType.Gem;
    }

}