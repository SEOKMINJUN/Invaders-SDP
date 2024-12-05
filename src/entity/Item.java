package entity;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

import engine.Core;
import engine.DrawManager;
import engine.DrawManager.SpriteType;
import engine.Globals;
import engine.SoundManager;
import screen.GameScreen;

import static engine.Globals.MAX_LIVES;

public class Item extends Entity {
    private final ShipUpgradeStatus shipUpgradeStatus = new ShipUpgradeStatus();
    private int speed;

    private static boolean isLoadProbability = false;
    private static double pierce_probability;
    private static double hearth_probability;
    private static double bomb_probability;
    private static double shield_probability;
    private static double feverTime_probability;
    private static double speedUp_probability;
    private static double speedSlow_probability;

    public Item(final int positionX, final int positionY, final int speed, final int type) {
        super(positionX, positionY, 3 * 2, 5 * 2, Color.yellow);
        setClassName("Item");
        this.speed = speed;
        setSprite();
        this.setEnabled(true);
    }

    public final void setSprite() {
        double rdItem = Math.random();

        loadProbability(); //Load only once
        double PierceP = pierce_probability;
        double HearthP = hearth_probability;
        double bombP = bomb_probability;
        double ShieldP = shield_probability;
        double FeverP = feverTime_probability;
        double SpeedUpP = speedUp_probability;
        double SpeedSlowP = speedSlow_probability;

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
                        ship.setBombBullet(true);
                        Globals.getCollectionManager().AddCollectionItemTypes(1);
                        SoundManager.playES("get_item");
                        break;
                    case ItemBarrier:
                        ship.activatebarrier();
                        Globals.getCollectionManager().AddCollectionItemTypes(3);
                        SoundManager.playES("get_item");
                        break;
                    case ItemHeart:
                        int health = ship.getHealth();
                        if (health < MAX_LIVES) { ship.setHealth(health + 1); }
                        Globals.getCollectionManager().AddCollectionItemTypes(2);
                        SoundManager.playES("get_item");
                        break;
                    case ItemFeverTime:
                        Globals.getCollectionManager().AddCollectionItemTypes(5);
                        screen.getFeverTimeItem().activate();
                        break;
                    case ItemPierce:
                        ship.getNumberOfBullet().pierceup();
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

    public static void loadProbability(){
        if(isLoadProbability){
            return;
        }
        Properties properties = new Properties();
        try {
            InputStream inputStream = ShipUpgradeStatus.class.getClassLoader().getResourceAsStream("StatusConfig.properties");
            if (inputStream == null) {
                System.out.println("FileNotFound");
                return;
            }

            properties.load(inputStream);

            pierce_probability = Double.parseDouble(properties.getProperty("pierce.probability"));
            hearth_probability = Double.parseDouble(properties.getProperty("hearth.probability"));
            bomb_probability = Double.parseDouble(properties.getProperty("bomb.probability"));
            shield_probability = Double.parseDouble(properties.getProperty("shield.probability"));
            feverTime_probability = Double.parseDouble(properties.getProperty("feverTime.probability"));
            speedUp_probability = Double.parseDouble(properties.getProperty("SpeedUp.probability"));
            speedSlow_probability = Double.parseDouble(properties.getProperty("SpeedSlow.probability"));

            isLoadProbability = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
