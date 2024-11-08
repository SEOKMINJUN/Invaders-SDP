package entity;

import engine.*;
import screen.GameScreen;
import screen.ScoreScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

//import entity.Bomb;


public class ItemManager {

    public Set<Item> items;
    private int screenHeight;
    private DrawManager drawManager;
    private GameScreen gameScreen;
    protected Logger logger = Core.getLogger();
    private Set<Item> recyclableItems = new HashSet<>();
    private Set<EnemyShip> enemyShips;
    private ItemBarrierAndHeart Item2;
    private NumberOfBullet numberOfBullet;
    private SpeedItem speedItem;
    private Ship ship;
    private FeverTimeItem feverTimeItem;
    private int[] itemGainNumber;

    public ItemManager(int screenHeight, DrawManager drawManager, GameScreen gameScreen) {
        this.screenHeight = screenHeight;
        this.drawManager = drawManager;
        this.gameScreen = gameScreen;
        this.ship = gameScreen.getShip();       // Team Inventory
        this.Item2 = gameScreen.getItem();
        this.feverTimeItem = gameScreen.getFeverTimeItem();
        this.numberOfBullet = new NumberOfBullet();
        this.speedItem = gameScreen.getSpeedItem();
        this.enemyShips = new HashSet<>();
    }

    public ItemManager(){
        this.itemGainNumber = new int[8];
    }


    public void cleanItems() {
        Set<Item> recyclable = new HashSet<>();

    }

    public void dropItem(EnemyShip enemyShip, double probability, int enemyship_type) {
        if(Math.random() < probability) {
            Item item = ItemPool.getItem(enemyShip.getPositionX(), enemyShip.getPositionY(), 3, enemyship_type);
            item.setEnabled(true);
        }
    }

    public void setEnemyShips(Set<EnemyShip> enemyShips) {
        this.enemyShips = enemyShips;
    }

    // team Inventory
    public void OperateItem(Item item){
        if(item!= null) {

            DrawManager.SpriteType whatItem = item.getSpriteType();

            switch (whatItem) {     // Operates according to the SpriteType of the item.
                case ItemBomb:
                    Globals.getCollectionManager().AddCollectionItemTypes(0);
                    Bomb.setIsbomb(true);
                    Bomb.setCanShoot(true);
                    SoundManager.playES("get_item");
                    break;
                case ItemBarrier:
                    Globals.getCollectionManager().AddCollectionItemTypes(1);
                    Item2.activatebarrier();
                    SoundManager.playES("get_item");
                    break;
                case ItemHeart:
                    Globals.getCollectionManager().AddCollectionItemTypes(2);
                    Item2.activeheart(gameScreen);
                    SoundManager.playES("get_item");
                    break;
                case ItemFeverTime:
                    Globals.getCollectionManager().AddCollectionItemTypes(3);
                    feverTimeItem.activate();
                    break;
                case ItemPierce:
                    Globals.getCollectionManager().AddCollectionItemTypes(4);
                    numberOfBullet.pierceup();
                    ship.increaseBulletSpeed();
                    SoundManager.playES("get_item");
                    break;
                case ItemCoin:
                    Globals.getCollectionManager().AddCollectionItemTypes(5);
                    this.logger.info("You get coin!");
                    break;
                case ItemSpeedUp:
                    Globals.getCollectionManager().AddCollectionItemTypes(6);
                    speedItem.activate(true, enemyShips);
                    break;
                case ItemSpeedSlow:
                    Globals.getCollectionManager().AddCollectionItemTypes(7);
                    speedItem.activate(false, enemyShips);
                    break;
            }

            recyclableItems.add(item);
            String itemLog = item.getSpriteType().toString().toLowerCase().substring(4);
            // Sound Operator
            if (itemLog.equals("coin")){
                SoundManager.playES("item_coin");
            }

            if (!itemLog.equals("coin")) {
                this.logger.info("get " + itemLog + " item");   // Change log for each item
            }
        }
    }


    public void removeAllReItems(){
        this.items.removeAll(recyclableItems);
        ItemPool.recycle(recyclableItems);
    }
}
