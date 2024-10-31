package HUDTeam;

import engine.Globals;

public class DrawAchievementHud {
    static int timer = 100;
    static String achievementText = null;

    public DrawAchievementHud() {}

    public static int getTimer(){return timer;}

    public static String getAchievementText(){return achievementText;}

    public static void addTimer(){timer++;}

    /**
     * Input accomplished achievement text.
     *
     * @param Text
     *            Accomplished achievement text.
     */
    public static void achieve(String Text){
        timer = 0;
        achievementText = Text;
        Globals.getSoundManager().playES("achievement");
    }
}
