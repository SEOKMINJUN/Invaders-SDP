package engine;

import engine.DrawManager.SpriteType;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CollectionManager {
    public static CollectionManager instance;

    private static Logger logger;

    @Getter
    private int[] itemTypes = new int[8];
    @Getter
    private int[] enemyTypes = new int[8];
    @Getter
    private int[] achievementTypes = new int[19];

    @Getter
    Statistics collection;
    @Getter
    List<Statistics> collectionList;

    public CollectionManager() {
        logger = Globals.getLogger();
        collectionList = new ArrayList<>();

        try{
            Statistics loadedCollection = Globals.getFileManager().loadCollections();
            if (loadedCollection != null) {
                this.collection = loadedCollection;
                this.itemTypes = collection.getItemsArray();
                this.enemyTypes = collection.getEnemiesArray();
                this.achievementTypes = collection.getAchievementsArray();
            } else {
                this.collection = new Statistics(itemTypes, enemyTypes, achievementTypes);
            }
        } catch (IOException e) {
            logger.info("Couldn't load collections");
        }
        collectionList.add(collection);
    }

    public static CollectionManager getInstance() {
        if (instance == null) {
            instance = new CollectionManager();
        }
        return instance;
    }

    public void AddCollectionItemTypes(int i) {
        itemTypes[i]++;
        collection.setItemsArray(itemTypes);
        logger.info("Added item type " + i + " to collection : " + itemTypes[i]);
    }

    public void AddCollectionEnemyTypes(SpriteType spriteType) {
        switch (spriteType){
            case EnemyShipA1:
                enemyTypes[0]++;
                break;
            case EnemyShipA2:
                enemyTypes[1]++;
                break;
            case EnemyShipB1:
                enemyTypes[2]++;
                break;
            case EnemyShipB2:
                enemyTypes[3]++;
                break;
            case EnemyShipC1:
                enemyTypes[4]++;
                break;
            case EnemyShipC2:
                enemyTypes[5]++;
                break;
            case ExplosiveEnemyShip1:
                enemyTypes[6]++;
                break;
            case ExplosiveEnemyShip2:
                enemyTypes[7]++;
                break;
            case EnemyShipSpecial:
                enemyTypes[8]++;
                break;
        }
        logger.info("Added enemy type " + spriteType + " to collection");
        collection.setEnemiesArray(enemyTypes);
    }

    public void AddCollectionAchievementTypes(int i) {
        achievementTypes[i]++;
        collection.setAchievementsArray(achievementTypes);
        logger.info("Added achievement type " + i + " to collection : " + achievementTypes[i]);
    }


}
