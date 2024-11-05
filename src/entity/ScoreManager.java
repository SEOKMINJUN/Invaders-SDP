package entity;

import engine.Globals;
import lombok.Getter;

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
    public static void addScore(int enemyScore) {
        ScoreManager scoreManager = Globals.getScoreManager();
        if(scoreManager == null){
            return;
        }
        int scoreToAdd = enemyScore * scoreManager.level;
        if (Bomb.isBombExploded()){
            scoreToAdd += (250 + (Bomb.getTotalPoint() * scoreManager.level));
            Bomb.resetBombExploded();
        }
        scoreManager.levelScore += scoreToAdd;
        scoreManager.accumulatedScore += scoreToAdd;
        //System.out.println("Enemy destroyed. Score added: " + scoreToAdd + ", Level Score: " + scoreManager.levelScore);
    }

}
