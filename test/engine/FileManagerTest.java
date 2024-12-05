package engine;

import engine.FileManager;
import engine.Score;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    private FileManager fileManager;

    @BeforeEach
    void setUp() {
        fileManager = FileManager.getInstance();
    }

    @Test
    void testLoadHighScores() {
        try {
            List<Score> scores = fileManager.loadHighScores();
            assertNotNull(scores, "Scores list should not be null");
            assertFalse(scores.isEmpty(), "Scores list should not be empty");
        } catch (IOException e) {
            fail("Exception should not occur: " + e.getMessage());
        }
    }

    @Test
    void testSaveHighScores() {
        try {
            List<Score> scores = new ArrayList<>();
            scores.add(new Score("TestPlayer", 100, 5000));
            fileManager.saveHighScores(scores);

            List<Score> loadedScores = fileManager.loadHighScores();
            assertEquals(scores.size(), loadedScores.size(), "Saved and loaded scores size should match");
            assertEquals(scores.get(0).getName(), loadedScores.get(0).getName(), "Player names should match");
        } catch (IOException e) {
            fail("Exception should not occur: " + e.getMessage());
        }
    }

    @Test
    void testLoadAndSaveRecentScores() {
        try {
            List<Score> recentScores = new ArrayList<>();
            recentScores.add(new Score("RecentPlayer", 200, "2023-12-01", 5, 50, 3, 90.5f,50));
            fileManager.saveRecentScores(recentScores);

            List<Score> loadedRecentScores = fileManager.loadRecentScores();
            assertEquals(recentScores.size(), loadedRecentScores.size(), "Saved and loaded recent scores size should match");
            assertEquals(recentScores.get(0).getScore(), loadedRecentScores.get(0).getScore(), "Scores should match");
        } catch (IOException e) {
            fail("Exception should not occur: " + e.getMessage());
        }
    }

    @Test
    void testLoadDefaultHighScores() {
        FileManager fileManager = FileManager.getInstance();

        try {
            // 리플렉션으로 private 메서드 호출
            Method method = FileManager.class.getDeclaredMethod("loadDefaultHighScores");
            method.setAccessible(true); // private 접근 허용

            @SuppressWarnings("unchecked")
            List<Score> defaultScores = (List<Score>) method.invoke(fileManager);

            assertNotNull(defaultScores, "Default scores list should not be null");
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    void testLoadUpgradeStatus() {
        try {
            var properties = fileManager.loadUpgradeStatus();
            assertNotNull(properties, "Properties should not be null");
            assertFalse(properties.isEmpty(), "Properties should not be empty");
        } catch (IOException e) {
            fail("Exception should not occur: " + e.getMessage());
        }
    }
}
