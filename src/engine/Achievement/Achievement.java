package engine.Achievement;

import lombok.*;

@Getter @Setter
class AchievementBase {
    private String Name;
    private String Description;
    private AchievementType achievementType;
    private int gem = 0;
    private int id;

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

    public Achievement(String Name, String achievementDescription, int requiredValue, AchievementType type, int gem, int id) {
        this(Name, achievementDescription, requiredValue, type);
        this.setGem(gem);
        this.setId(id);
    }

    public void logCompleteAchievement() {
        this.setCompleted(true);
        System.out.println("Achievement completed: " + getName());
    }

    public boolean checkValue(int currentValue, int requiredValue, int[] var) {
        //Can be extended by AchievementType
        //TODO : Need check on gamescreen for FASTKILL
        if(getAchievementType() == AchievementType.LIVES) return currentValue == requiredValue;
        if(getAchievementType() == AchievementType.ALL) return AchievementManager.getInstance().isAllAchievementCompleted();
        return currentValue <= requiredValue;
    }

    public boolean checkValue(int currentValue, int[] var) { return checkValue(currentValue, requiredValue, var); }
    public boolean checkValue(int currentValue) { return checkValue(currentValue, requiredValue, new int[0]); }

    @Override
    public String toString() {
        return "Achievement{" + getName() + " - " + getDescription() + " : " + this.isCompleted() + "}";
    }
}