package entity;

import java.awt.Color;

import engine.Core;
import engine.DrawManager;
import engine.DrawManager.SpriteType;
import engine.Globals;
import screen.GameScreen;

public class Item extends Entity {
    private final ShipStatus shipStatus = new ShipStatus();
    private int speed;
    public Item(final int positionX, final int positionY, final int speed, final int type) {
        super(positionX, positionY, 3 * 2, 5 * 2, Color.yellow);
        setClassName("Item");
        this.speed = speed;
        setSprite();
        this.setEnabled(true);
    }

    public final void setSprite() {
        double rdItem = Math.random();
        shipStatus.loadProbability();
        double bombP = shipStatus.getBomb_probability();
        double PierceP = shipStatus.getPierce_probability();
        double ShieldP = shipStatus.getShield_probability();
        double HearthP = shipStatus.getHearth_probability();
        double FeverP = shipStatus.getFeverTimeProbability();
        double SpeedUpP = shipStatus.getSpeedUpProbability();
        double SpeedSlowP = shipStatus.getSpeedSlowProbability();

        // Import odds from properties file for easy balance patches
        if (rdItem < bombP) { // 30%
            this.spriteType = SpriteType.ItemBomb;
            this.setColor(Color.gray);
        } else if (rdItem < bombP + PierceP) { // 0% - Fixing error
            this.spriteType = SpriteType.ItemPierce;
            this.setColor(Color.white);
        } else if (rdItem < bombP + PierceP + ShieldP) { // 20%
            this.spriteType = SpriteType.ItemBarrier;
            this.setColor(Color.green);
        } else if (rdItem < bombP + PierceP + ShieldP + HearthP) { // 100%
            this.spriteType = SpriteType.ItemHeart;
            this.setColor(Color.red);
        } else if (rdItem < bombP + PierceP + ShieldP + HearthP + FeverP ) {
            this.spriteType = SpriteType.ItemFeverTime;
            this.setColor(Color.yellow);
        }
        else if (rdItem < bombP + PierceP + ShieldP + HearthP + SpeedUpP) {
            this.spriteType = SpriteType.ItemSpeedUp;
            this.setColor(Color.CYAN);
        }
        else if (rdItem < bombP + PierceP + ShieldP + HearthP + SpeedUpP + SpeedSlowP) {
            this.spriteType = SpriteType.ItemSpeedSlow;
            this.setColor(Color.ORANGE);
        }
        else {
            this.spriteType = SpriteType.ItemCoin;
            this.setColor(Color.yellow);
        }
    }
    @Override
    public final void update() {
        this.positionY += this.speed;
        GameScreen screen = (GameScreen)Globals.getCurrentScreen();

        /*
        Test remove condition
         */
        if (getPositionY() > screen.getHeight()) {
            remove();
        }


        /*
        Test collision
         */
        Ship ship = null;
        while((ship = (Ship) screen.findEntityByClassname(ship, "Ship")) != null){
            if (checkCollision(ship)) {
                screen.itemManager.OperateItem(this);
                // CtrlS: Count coin
                if (getSpriteType() == DrawManager.SpriteType.ItemCoin) screen.coinItemsCollected++;
                Core.getLogger().info("coin: " + screen.coinItemsCollected);
            }
            remove();
        }
    }

    @Override
    public void remove(){
        this.setEnabled(false);
        ItemPool.recycle(this);
    }

    public final void setSpeed(final int speed) {
        this.speed = speed;
    }
    public final int getSpeed() {
        return this.speed;
    }


}
