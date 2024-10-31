package engine;

import CtrlS.UpgradeManager;
import Sound_Operator.SoundManager;
import clove.AchievementManager;
import CtrlS.CurrencyManager;
import screen.Screen;

import java.util.logging.Logger;

public class Globals {
    private AchievementManager achievementManager; // Team CLOVER
    public CurrencyManager currencyManager;
    public UpgradeManager upgradeManager;
    public SoundManager soundManager;
    public void Initialize(){

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

    public static SoundManager getSoundManager() { return SoundManager.getInstance(); }

    public static Screen getCurrentScreen() { getLogger().warning("TODO: Make current screen"); return new Screen(0,0,0); }
}
