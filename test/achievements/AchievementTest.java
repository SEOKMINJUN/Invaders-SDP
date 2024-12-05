package achievements;

import java.io.IOException;
import java.lang.reflect.Field;
import static org.mockito.Mockito.*;
import engine.Achievement.Achievement;
import engine.Achievement.AchievementType;
import engine.CollectionManager;
import engine.Statistics;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AchievementTest {

    private Achievement testAchievement;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException, IOException {
        Statistics.getInstance().resetStatistics();
        Statistics statistics = Statistics.getInstance();
        Field field = Statistics.class.getDeclaredField("totalShipsDestroyed");
        field.setAccessible(true);
        field.setInt(statistics, 15);
        testAchievement = new Achievement("Test Achievement", "Description", 15, AchievementType.KILLS);
    }

    @Test
    @DisplayName("Test if CheckValue returns true")
    void testCheckValueSuccess() {
        boolean result = testAchievement.checkValue(15, new int[]{});
        assertTrue(result);
    }

    @Test
    @DisplayName("Test if CompleteAchievement works")
    void testCompleteAchievement() {
        testAchievement.setCompleted(true);
        assertTrue(testAchievement.isCompleted());
    }
}
