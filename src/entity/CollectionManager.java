package entity;

import engine.Globals;
import engine.Statistics;
import lombok.Getter;
import lombok.Setter;

import java.io.FileNotFoundException;
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

    public void AddCollectionEnemyTypes(int i) {
        enemyTypes[i]++;
        collection.setEnemiesArray(enemyTypes);
        logger.info("Added enemy type " + i + " to collection : " + enemyTypes[i]);
    }

    public void AddCollectionAchievementTypes(int i) {
        achievementTypes[i]++;
        collection.setAchievementsArray(achievementTypes);
        logger.info("Added achievement type " + i + " to collection : " + achievementTypes[i]);
    }


}
