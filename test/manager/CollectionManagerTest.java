package manager;

import engine.CollectionManager;
import engine.DrawManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollectionManagerTest {
    private CollectionManager collectionManager;

    @BeforeEach
    void setUp() {
        collectionManager = CollectionManager.getInstance();
    }

    @Test
    void testAddCollectionItemTypes() {
        int initialCount = collectionManager.getItemTypes()[0];
        collectionManager.AddCollectionItemTypes(0);
        assertEquals(initialCount + 1, collectionManager.getItemTypes()[0], "Item count should increment");
    }

    @Test
    void testAddCollectionEnemyTypes() {
        int initialCount = collectionManager.getEnemyTypes()[0];
        collectionManager.AddCollectionEnemyTypes(DrawManager.SpriteType.EnemyShipA1);
        assertEquals(initialCount + 1, collectionManager.getEnemyTypes()[0], "Enemy count should increment");
    }

    @Test
    void testAddCollectionAchievementTypes() throws Exception {
        int initialCount = collectionManager.getAchievementTypes()[0];
        collectionManager.AddCollectionAchievementTypes(0);
        assertEquals(1, collectionManager.getAchievementTypes()[0], "Achievement should be marked as achieved");
    }
}
