package engine.Achievement;

import static engine.Globals.NUM_LEVELS;


public class AchievementList {
    Achievement ACHIEVEMENT_LIVE            = (new Achievement("Aerobatics","Maintain Maximum Life",3, AchievementType.LIVES));
    Achievement ACHIEVEMENT_KILL_25         = (new Achievement("Rookie Pilot", "Destroy 25 enemies", 25, AchievementType.KILLS, 1));
    Achievement ACHIEVEMENT_KILL_100        = (new Achievement("Space Hunter", "Destroy 50 enemies", 100, AchievementType.KILLS, 1));
    Achievement ACHIEVEMENT_KILL_250        = (new Achievement("Space Trooper", "Destroy 100 enemies", 250, AchievementType.KILLS, 1));
    Achievement ACHIEVEMENT_KILL_500        = (new Achievement("Guardian of Universe", "Destroy 200 enemies", 500, AchievementType.KILLS, 1));

    Achievement ACHIEVEMENT_TRIALS_1        = (new Achievement("Welcome Recruit", "Finished first game", 1, AchievementType.TRIALS, 1));
    Achievement ACHIEVEMENT_TRIALS_10       = (new Achievement("Skilled Solider", "Finished 10th game", 10, AchievementType.TRIALS, 1));
    Achievement ACHIEVEMENT_TRIALS_50       = (new Achievement("Veteran Pilot", "Finished 50th game", 50, AchievementType.TRIALS, 1));

    Achievement ACHIEVEMENT_KILLSTREAKS_10  = (new Achievement("Preheating", "Kill streak of 10", 10, AchievementType.KILLSTREAKS, 1));
    Achievement ACHIEVEMENT_KILLSTREAKS_30  = (new Achievement("Overheating", "Kill streak of 30", 30, AchievementType.KILLSTREAKS, 5));
    Achievement ACHIEVEMENT_KILLSTREAKS_60  = (new Achievement("Runaway","Kill streak of 60", 60, AchievementType.KILLSTREAKS, 10));

    Achievement ACHIEVEMENT_FASTKILL_3      = (new Achievement("Gunsliger","Kill 3 enemies 5 seconds", 3, AchievementType.FASTKILL,5));
    Achievement ACHIEVEMENT_FASTKILL_5      = (new Achievement("Fear the Enemy","Kill 5 enemies 5 seconds", 5, AchievementType.FASTKILL, 5));
    Achievement ACHIEVEMENT_FASTKILL_15     = (new Achievement("Genocide","Kill 15 enemies 5 seconds", 15, AchievementType.FASTKILL, 5));

    Achievement ACHIEVEMENT_SCORE_1000      = (new Achievement("First Milestone", "Reach 1,000 points", 6000, AchievementType.SCORE,1));
    Achievement ACHIEVEMENT_SCORE_15000     = (new Achievement("Score Hunter", "Reach 5,000 points", 15000, AchievementType.SCORE,3));
    Achievement ACHIEVEMENT_SCORE_30000     = (new Achievement("Score Master", "Reach 10,000 points", 30000, AchievementType.SCORE,5));

    Achievement ACHIEVEMENT_STAGE_MAX       = (new Achievement("Home Sweet Home","Cleared Final Stage", NUM_LEVELS, AchievementType.STAGE, 5));
    Achievement ACHIEVEMENT_ALL             = (new Achievement("Medal of Honor", "Complete all achievements", 0, AchievementType.ALL, 10));

    private static final int ACHIEVEMENT_FAST_KILL_TIME = 5;
}