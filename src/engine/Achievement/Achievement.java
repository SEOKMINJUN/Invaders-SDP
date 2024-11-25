package engine.Achievement;

import engine.CollectionManager;
import engine.Globals;
import engine.Statistics;
import lombok.*;

import java.io.IOException;

@Getter @Setter
class AchievementBase {
    private String Name;
    private String Description;
    private AchievementType achievementType;
    private int gem = 0;

    public void Achievement() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AchievementBase that = (AchievementBase) o;
        return Name.equals(that.Name);
    }

    @Override
    public int hashCode() {
        return Name.hashCode();
    }

    @Override
    public String toString() {
        return "Achievement{" + Name + " - " + Description + "}";
    }
}

@Getter
public class Achievement extends AchievementBase{
    @Setter
    private int index;
    private int requiredValue;
    @Setter
    private boolean completed;

    public Achievement(String Name, String achievementDescription, int requiredValue, AchievementType type) {
        this.setName(Name);
        this.setDescription(achievementDescription);
        this.setAchievementType(type);
        this.completed = false;
        this.requiredValue = requiredValue;
        AchievementManager.getInstance().addAchievement(this);
    }

    public Achievement(String Name, String achievementDescription, int requiredValue, AchievementType type, int gem) {
        this(Name, achievementDescription, requiredValue, type);
        this.setGem(gem);
    }

    public void logCompleteAchievement() {
        System.out.println("Achievements completed: " + getName());
    }

    public boolean checkValue(int currentValue, int requiredValue, int[] var) {
        boolean result = false;
        switch (getAchievementType()) {
            case KILLS:
                result = Statistics.getInstance().getTotalShipsDestroyed() >= requiredValue;
                break;
            case ACCURACY:
                result = Statistics.getInstance().getAccuracy() >= requiredValue;
                break;
            case DISTANCE:
                result = Statistics.getInstance().getTotalPlaytime() / 1000 >= requiredValue;
                break;
            case STAGE:
                result = Statistics.getInstance().getHighestLevel() >= requiredValue;
                break;
            case KILLSTREAKS:
                result = Statistics.getInstance().getMaxShipsDestructionStreak() >= requiredValue;
                break;
            case TRIALS:
                result = Statistics.getInstance().getPlayedGameNumber() >= requiredValue;
                break;
            case ALL:
                result = AchievementManager.getInstance().isAllAchievementCompleted();
                break;
            default:
                result = currentValue >= requiredValue;
        }
        if (result && !completed) {
            completed = true;
            Globals.getLogger().info("Achievement unlocked: " + getAchievementType() + ", Required: " + requiredValue + ", Current: " + currentValue);

            try {
                int achievementIndex = CollectionManager.getInstance()
                        .getAchievementIndex(getAchievementType(), requiredValue);

                if (achievementIndex >= 0) {
                    Statistics.getInstance().updateAchievementsArray(achievementIndex);
                    CollectionManager.getInstance().AddCollectionAchievementTypes(achievementIndex);
                } else {
                    Globals.getLogger().warning("Achievement index not found for type: "
                            + getAchievementType() + ", requiredValue: " + requiredValue);
                }
            } catch (IOException e) {
                Globals.getLogger().warning("Failed to update achievement array: " + e.getMessage());
            }
        }
        return result;

        //if(getAchievementType() == AchievementType.LIVES) return currentValue == requiredValue;
        //if(getAchievementType() == AchievementType.ALL) return AchievementManager.getInstance().isAllAchievementCompleted();
        //return currentValue <= requiredValue;
    }

    public boolean checkValue(int currentValue, int[] var) { return checkValue(currentValue, requiredValue, var); }
    public boolean checkValue(int currentValue) { return checkValue(currentValue, requiredValue, new int[0]); }

    @Override
    public String toString() {
        return "Achievement{" + getName() + " - " + getDescription() + " : " + this.isCompleted() + "}";
    }
}