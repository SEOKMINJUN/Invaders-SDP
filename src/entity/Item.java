package entity;

import java.awt.Color;
import java.util.Collection;
import java.util.HashSet;

import engine.Core;
import engine.DrawManager;
import engine.DrawManager.SpriteType;
import engine.Globals;
import engine.SoundManager;
import screen.GameScreen;

public class Item extends Entity {
    private final ShipUpgradeStatus shipUpgradeStatus = new ShipUpgradeStatus();
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
        shipUpgradeStatus.loadProbability();
        double bombP = shipUpgradeStatus.getBomb_probability();
        double PierceP = shipUpgradeStatus.getPierce_probability();
        double ShieldP = shipUpgradeStatus.getShield_probability();
        double HearthP = shipUpgradeStatus.getHearth_probability();
        double FeverP = shipUpgradeStatus.getFeverTimeProbability();
        double SpeedUpP = shipUpgradeStatus.getSpeedUpProbability();
        double SpeedSlowP = shipUpgradeStatus.getSpeedSlowProbability();

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
                switch (getSpriteType()) {     // Operates according to the SpriteType of the item.
                    case ItemBomb:
                        Bomb.setIsbomb(true);
                        Bomb.setCanShoot(true);
                        Globals.getCollectionManager().AddCollectionItemTypes(1);
                        SoundManager.playES("get_item");
                        break;
                    case ItemBarrier:
                        screen.getItem().activatebarrier();
                        Globals.getCollectionManager().AddCollectionItemTypes(3);
                        SoundManager.playES("get_item");
                        break;
                    case ItemHeart:
                        screen.getItem().activeheart(ship);
                        Globals.getCollectionManager().AddCollectionItemTypes(2);
                        SoundManager.playES("get_item");
                        break;
                    case ItemFeverTime:
                        Globals.getCollectionManager().AddCollectionItemTypes(5);
                        screen.getFeverTimeItem().activate();
                        break;
                    case ItemPierce:
                        NumberOfBullet.pierceup();
                        ship.increaseBulletSpeed();
                        Globals.getCollectionManager().AddCollectionItemTypes(4);
                        SoundManager.playES("get_item");
                        break;
                    case ItemCoin:
                        Globals.getCollectionManager().AddCollectionItemTypes(0);
                        Core.getLogger().info("You get coin!");
                        break;
                    case ItemSpeedUp:
                        Globals.getCollectionManager().AddCollectionItemTypes(6);
                        screen.getSpeedItem().activate(true, new HashSet<>((Collection<EnemyShip>) screen.getEnemyShipFormation()));
                        break;
                    case ItemSpeedSlow:
                        Globals.getCollectionManager().AddCollectionItemTypes(7);
                        screen.getSpeedItem().activate(false, new HashSet<>((Collection<EnemyShip>) screen.getEnemyShipFormation()));
                        break;
                }

                remove();

                String itemLog = getSpriteType().toString().toLowerCase().substring(4);
                // Sound Operator
                if (itemLog.equals("coin")){
                    SoundManager.playES("item_coin");
                }

                if (!itemLog.equals("coin")) {
                    Core.getLogger().info("get " + itemLog + " item");   // Change log for each item
                }
                // CtrlS: Count coin
                if (getSpriteType() == DrawManager.SpriteType.ItemCoin) screen.coinItemsCollected++;
                Core.getLogger().info("coin: " + screen.coinItemsCollected);
            }
        }
    }

    public final void setSpeed(final int speed) {
        this.speed = speed;
    }
    public final int getSpeed() {
        return this.speed;
    }


}
