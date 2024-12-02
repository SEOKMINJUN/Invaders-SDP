package achievements;

import engine.Achievement.Achievement;
import engine.Achievement.AchievementManager;
import engine.Achievement.AchievementType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AchievementManagerTest {

    private AchievementManager achievementManager;

    @BeforeEach
    void setUp() {
        achievementManager = new AchievementManager();
        Achievement testAchievement = new Achievement("Test Kills", "Kill 15 enemies", 15, AchievementType.KILLS);
        achievementManager.addAchievement(AchievementType.KILLS, testAchievement);
    }

    @Test
    void testCheckAchievementUnlock() {
        achievementManager.checkAchievement(AchievementType.KILLS, 15);
        Achievement testAchievement = achievementManager.achievementMap.get(AchievementType.KILLS).get(0);
        assertTrue(testAchievement.isCompleted());
    }

    @Test
    void testCheckAchievementNotUnlock() {
        achievementManager.checkAchievement(AchievementType.KILLS, 10);
        Achievement testAchievement = achievementManager.achievementMap.get(AchievementType.KILLS).get(0);
        assertFalse(testAchievement.isCompleted());
    }
}
