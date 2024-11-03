package entity;

import engine.DrawManager;

import java.awt.*;

public class Coin extends Entity {
    public Coin() {
        super(0,0, 7 * 2, 5 * 2, Color.YELLOW); //Adjust the width value - Starter
        setClassName("Coin");
        this.spriteType = DrawManager.SpriteType.ItemCoin; //Change Coin to ItemCoin - Starter
    }


}
