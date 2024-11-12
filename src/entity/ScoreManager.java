package entity;

import engine.Globals;
import lombok.Getter;
import screen.GameScreen;

public class ScoreManager extends EntityBase {

    private int levelScore;
    @Getter
    private int accumulatedScore = 0;
    @Getter
    private int level;

    // Constructor - initializes level
    public ScoreManager(int level, int score) {
        super();
        setClassName("ScoreManager");
        this.level = level;
        this.accumulatedScore = score;
        Globals.setScoreManager(this);
    }

    public static void setScore(int score){
        ScoreManager scoreManager = Globals.getScoreManager();
        if(scoreManager == null){
            return;
        }
        scoreManager.accumulatedScore = score;
    }

    // Adds score based on enemy score and level

}
