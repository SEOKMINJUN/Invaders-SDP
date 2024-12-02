package achievements;

import engine.Achievement.Achievement;
import engine.Achievement.AchievementManager;
import engine.Achievement.AchievementType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AchievementManagerTest {

    private AchievementManager achievementManager;

    @BeforeEach
    void setUp() {
        achievementManager = new AchievementManager();
        Achievement testAchievement = new Achievement("Test Kills", "Kill 15 enemies", 15, AchievementType.KILLS);
        achievementManager.addAchievement(AchievementType.KILLS, testAchievement);
    }

    @Test
    @DisplayName("Test if achievements are added to achievementMap correctly")
    void testAddAchievementToMap() {
        Achievement achievement = new Achievement("Kill 50 Enemies", "Kill 50 enemies in total", 50, AchievementType.KILLS);
        achievementManager.addAchievement(achievement);

        List<Achievement> killsAchievements = achievementManager.achievementMap.get(AchievementType.KILLS);
        assertNotNull(killsAchievements, "Achievement map should contain KILLS type.");
        assertTrue(killsAchievements.contains(achievement), "Achievement should be added to the map under KILLS type.");
    }

    @Test
    void testCheckAchievementUnlock() {
        achievementManager.checkAchievement(AchievementType.KILLS, 15);
        Achievement testAchievement = achievementManager.achievementMap.get(AchievementType.KILLS).get(0);
        System.out.println("Achievement state: " + testAchievement);
        assertTrue(testAchievement.isCompleted());
        System.out.println("Achievement state: " + testAchievement);
    }

    @Test
    void testCheckAchievementNotUnlock() {
        achievementManager.checkAchievement(AchievementType.KILLS, 10);
        Achievement testAchievement = achievementManager.achievementMap.get(AchievementType.KILLS).get(0);
        assertFalse(testAchievement.isCompleted());
    }
}
