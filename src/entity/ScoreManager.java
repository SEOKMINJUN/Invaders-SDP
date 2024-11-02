package entity;

import engine.Globals;
import inventory_develop.Bomb;
import lombok.Getter;

import java.awt.*;

public class ScoreManager extends EntityBase {

    private int levelScore;
    @Getter
    private int accumulatedScore = 0;
    @Getter
    private int level;

    // Constructor - initializes level
    public ScoreManager(int level, int score) {
        this.level = level;
        this.accumulatedScore = score;
        Globals.setScoreManager(this);
    }

    public void setScore(int score){
        this.accumulatedScore = score;
    }

    // Adds score based on enemy score and level
    public void addScore(int enemyScore) {
        int scoreToAdd = enemyScore * this.level;
        if (Bomb.isBombExploded()){
            scoreToAdd += (250 + (Bomb.getTotalPoint() * this.level));
            Bomb.resetBombExploded();
        }
        this.levelScore += scoreToAdd;
        this.accumulatedScore += scoreToAdd;
        //System.out.println("Enemy destroyed. Score added: " + scoreToAdd + ", Level Score: " + this.levelScore);
    }

}
