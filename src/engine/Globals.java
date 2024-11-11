package engine;

import engine.Achievement.AchievementManager;
import entity.ScoreManager;
import lombok.Getter;
import lombok.Setter;
import screen.Screen;

import java.util.logging.Logger;

public class Globals {
    //Settings
    /** Width of current screen. */
    public static final int WIDTH = 630;
    /** Height of current screen. */
    public static final int HEIGHT = 720;
    /** Max fps of current screen. */
    public static final int FPS = 60;
    /** Max lives. */
    public static final int MAX_LIVES = 3; // TEAM CLOVER: Fixed MAX_LIVES from public to public for usage in achievement
    /** Levels between extra life. */
    public static final int EXTRA_LIFE_FRECUENCY = 3;
    /** Total number of levels. */
    public static final int NUM_LEVELS = 7; // TEAM CLOVER : Fixed NUM_LEVELS from publicd to public for usage in achievement
    /** Difficulty settings for level 1. */
    public static final GameSettings SETTINGS_LEVEL_1 =
            new GameSettings(5, 4, 60, 2000, 1);
    /** Difficulty settings for level 2. */
    public static final GameSettings SETTINGS_LEVEL_2 =
            new GameSettings(5, 5, 50, 2500, 1);
    /** Difficulty settings for level 3. */
    public static final GameSettings SETTINGS_LEVEL_3 =
            new GameSettings(1, 1, -8, 500, 1);
    /** Difficulty settings for level 4. */
    public static final GameSettings SETTINGS_LEVEL_4 =
            new GameSettings(6, 6, 30, 1500, 2);
    /** Difficulty settings for level 5. */
    public static final GameSettings SETTINGS_LEVEL_5 =
            new GameSettings(7, 6, 20, 1000, 2);
    /** Difficulty settings for level 6. */
    public static final GameSettings SETTINGS_LEVEL_6 =
            new GameSettings(7, 7, 10, 1000, 3);
    /** Difficulty settings for level 7. */
    public static final GameSettings SETTINGS_LEVEL_7 =
            new GameSettings(8, 7, 2, 500, 1);
    /** Milliseconds until the screen accepts user input. */
    public static final int GAME_SCREEN_INPUT_DELAY = 6000;
    /** Bonus score for each life remaining at the end of the level. */
    public static final int GAME_SCREEN_LIFE_SCORE = 100;
    /** Minimum time between bonus ship's appearances. */
    public static final int GAME_SCREEN_BONUS_SHIP_INTERVAL = 20000;
    /** Maximum variance in the time between bonus ship's appearances. */
    public static final int GAME_SCREEN_BONUS_SHIP_VARIANCE = 10000;
    /** Time until bonus ship explosion disappears. */
    public static final int GAME_SCREEN_BONUS_SHIP_EXPLOSION = 500;
    /** Time from finishing the level to screen change. */
    public static final int GAME_SCREEN_SCREEN_CHANGE_INTERVAL = 1500;
    /** Height of the interface separation line. */
    public static final int GAME_SCREEN_SEPARATION_LINE_HEIGHT = 40;

    public static int currentMaxEntityIndex;
    @Getter @Setter
    public static ScoreManager scoreManager;

    @Getter @Setter
    private static Screen currentScreen;

    public void Initialize(){
        scoreManager = null;
    }

    /**
     * Controls access to the drawing manager.
     *
     * @return Application draw manager.
     */
    public static DrawManager getDrawManager() {
        return DrawManager.getInstance();
    }

    /**
     * Controls access to the input manager.
     *
     * @return Application input manager.
     */
    public static InputManager getInputManager() {
        return InputManager.getInstance();
    }

    /**
     * Controls access to the file manager.
     *
     * @return Application file manager.
     */
    public static FileManager getFileManager() { return FileManager.getInstance(); }

    public static Logger getLogger() { return Core.getLogger(); }

    public static CurrencyManager getCurrencyManager() { return CurrencyManager.getInstance(); }

    public static UpgradeManager getUpgradeManager() { return UpgradeManager.getInstance(); }

    public static AchievementManager getAchievementManager() { return AchievementManager.getInstance(); }

    public static Statistics getStatistics() { return Statistics.getInstance(); }

    public static SoundManager getSoundManager() { return SoundManager.getInstance(); }

    public static void severe(String msg){
        getLogger().severe(msg);
    }
    public static void warn(String msg){
        getLogger().warning(msg);
    }
    public static void info(String msg){
        getLogger().info(msg);
    }
    public static void fine(String msg){
        getLogger().fine(msg);
    }
    public static void finer(String msg){
        getLogger().finer(msg);
    }
    public static void finest(String msg){
        getLogger().finest(msg);
    }
}
