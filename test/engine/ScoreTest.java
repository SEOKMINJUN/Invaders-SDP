package engine;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    @Test
    void testConstructorAndGetters() {
        String name = "ABC";
        int scoreValue = 1000;
        boolean isTest = true;

        Score score = new Score(name, scoreValue, isTest);

        assertEquals(name, score.getName());
        assertEquals(scoreValue, score.getScore());
        assertNotNull(score.getcurrentDate());
        assertEquals(LocalDate.now(), score.getcurrentDate());
        assertNotNull(score.getDate());
    }

    @Test
    void testCustomConstructorWithStatistics() throws IOException {
        String name = "DEF";
        int scoreValue = 1500;
        String date = "2023-12-01";
        int highestLevel = 10;
        int totalShipsDestroyed = 50;
        int clearAchievementNumber = 5;
        float accuracy = 85.5f;
        int distance = 1000;

        Score score = new Score(name, scoreValue, date, highestLevel, totalShipsDestroyed, clearAchievementNumber, accuracy, distance);

        assertEquals(name, score.getName());
        assertEquals(scoreValue, score.getScore());
        assertEquals(date, score.getDate());
        assertEquals(highestLevel, score.getHighestLevel());
        assertEquals(totalShipsDestroyed, score.getShipDestroyed());
        assertEquals(clearAchievementNumber, score.getClearAchievementNumber());
        assertEquals(accuracy, score.getAccuracy());
        assertEquals(distance, score.getDistance());
    }

    @Test
    void testCompareTo() {
        boolean isTest = true;
        Score score1 = new Score("AAA", 2000, isTest);
        Score score2 = new Score("BBB", 1500, isTest);
        Score score3 = new Score("CCC", 2000, isTest);

        assertTrue(score1.compareTo(score2) < 0);
        assertTrue(score2.compareTo(score1) > 0);
        assertEquals(0, score1.compareTo(score3));
    }

    @Test
    void testStatisticsIntegration() throws IOException {
        String name = "GHI";
        int scoreValue = 1800;
        boolean isTest = true;

        Score score = new Score(name, scoreValue, isTest);

        assertTrue(score.getHighestLevel() >= 0);
        assertTrue(score.getShipDestroyed() >= 0);
        assertTrue(score.getClearAchievementNumber() >= 0);
        assertTrue(score.getPlayTime() >= 0);
        assertTrue(score.getAccuracy() >= 0);
        assertTrue(score.getDistance() >= 0);
    }

    @Test
    void testPlayTimeConstructor() {
        String name = "JKL";
        int scoreValue = 5000;
        long playTime = 3600L;

        Score score = new Score(name, scoreValue, playTime);

        assertEquals(name, score.getName());
        assertEquals(scoreValue, score.getScore());
        assertEquals(playTime, score.getPlayTime());
    }
}


