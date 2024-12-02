package engine.Achievement;

import engine.Globals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AchievementManager {
    /*
        Variables
     */
    private static AchievementManager instance;
    private HashMap<AchievementType, ArrayList<Achievement>> achievementMap;
    private ArrayList<AchievementChangedCallback> achievementChangedCallbacks;

    private AchievementManager(){// HashMap<Achievement, Boolean>으로 초기화
        achievementMap = new HashMap<>();
        for(AchievementType type : AchievementType.values()){
            achievementMap.put(type, new ArrayList<>());
        }
        achievementChangedCallbacks = new ArrayList<>();
    }

    public static AchievementManager getInstance(){
        if (instance == null)
            instance = new AchievementManager();
        return instance;
    }

    public void checkAchievement(AchievementType achievementType, int value, int... var) {
        List<Achievement> achievements = new ArrayList<>(achievementMap.get(achievementType));
        for (Achievement achievement : achievements) {
            if (!achievement.isCompleted() && achievement.checkValue(value, var)) {
                completeAchievement(achievement);
            }
        }
    }

    /*
        Callbacks
     */
    @FunctionalInterface
    public interface AchievementChangedCallback {
        void onAchievementChanged(Achievement achievement, Boolean completed);
    }

    /*
        Functions
     */
    public void addAchievement(AchievementType type, Achievement achievement) {
        achievement.setIndex(achievementMap.get(type).size());
        achievementMap.get(type).add(achievement);
    }

    public void addAchievement(Achievement achievement) { addAchievement(achievement.getAchievementType(), achievement); }

    public boolean setAchievementComplete(Achievement achievement, Boolean completed) { // Object -> Achievement
        achievementMap.get(achievement.getAchievementType()).get(achievement.getIndex()).setCompleted(completed);

        for (AchievementChangedCallback callback : achievementChangedCallbacks) {
            callback.onAchievementChanged(achievement, completed);
        }
        return true;
    }

    /*
    Convenience function
    (Function added to make the code easier to use)
    */

    public boolean completeAchievement(Achievement achievement) { // Added Code
        if (!achievement.isCompleted()) {
            if(achievement.getGem() > 0) {
                try {
                    Globals.getCurrencyManager().addGem(achievement.getGem());
                } catch (IOException e) {
                    Globals.getLogger().warning("Couldn't load gem!");
                }
            }
            return setAchievementComplete(achievement, true);
        }
        return false;
    }

    public boolean isAllAchievementCompleted() {
        //TODO : Implement isAllAchievementCompleted
        Globals.getLogger().warning("isAllAchievementCompleted is not implemented yet");
        return false;
    }
}