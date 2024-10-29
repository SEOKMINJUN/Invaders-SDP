package engine;

import Sound_Operator.SoundManager;
import clove.AchievementManager;
import CtrlS.CurrencyManager;

public class Globals {
    public static SoundManager soundManager;
    public static AchievementManager achievementManager;
    public static CurrencyManager currencyManager;

    public static void Initialize(){
        achievementManager = new AchievementManager();
    }
}
