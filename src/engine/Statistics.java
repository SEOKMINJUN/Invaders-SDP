package engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import engine.Achievement.AchievementManager;
import engine.Achievement.AchievementType;
import entity.Bullet;
import lombok.Getter;

public class Statistics {

    private ScheduledExecutorService scheduler;
    /** Number of Player's Highest Reached Level */
    @Getter
    private int highestLevel;
    /** Number of Totally Fired Bullet */
    @Getter
    private int totalBulletsShot;
    /** Number of Totally Destroyed Ships*/
    @Getter
    private int totalShipsDestroyed;
    /** Number of ships destroyed consecutively */
    @Getter
    private int shipsDestructionStreak;
    /** Number of games played */
    @Getter
    private int playedGameNumber;
    /** Number of achievements cleared */
    @Getter
    private int clearAchievementNumber;
    /** Total playtime */
    @Getter
    private long totalPlaytime;
    /** Additional playtime */
    private long playTime;
    @Getter
    private float accuracy;

    /** Using for save statistics */
    private List<Statistics> playerStatistics = new ArrayList<>();
    private Statistics stat;

    /**
     *
     * Constructor for save Variables
     *
     * @param shipsDestructionStreak
     *              Number of ships destroyed consecutively
     * @param playedGameNumber
     *              Number of games played
     * @param clearAchievementNumber
     *              Number of achievements cleared
     * @param TotalPlaytime
     *              Total playtime
     * @param accuracy
     *              Current bullet hit accuracy
     */

    public Statistics(int highestLevel, int totalBulletsShot, int totalShipsDestroyed, int shipsDestructionStreak,
                      int playedGameNumber, int clearAchievementNumber, long TotalPlaytime, float accuracy) {
        this.highestLevel = highestLevel;
        this.totalBulletsShot = totalBulletsShot;
        this.totalShipsDestroyed = totalShipsDestroyed;
        this.shipsDestructionStreak = shipsDestructionStreak;
        this.playedGameNumber = playedGameNumber;
        this.clearAchievementNumber = clearAchievementNumber;
        this.totalPlaytime = TotalPlaytime;
        this.accuracy = accuracy;

        //this.achievementConditions = new AchievementConditions();
        //this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Public Constructor
     */

    public Statistics() {
    }

    /**
     * Compare the previously highest reached level with the currently reached level.
     * @param Level
     *              current reached level
     * @throws IOException
     *              In case of saving problems.
     */

    public void comHighestLevel(int Level) throws IOException {
        this.stat = loadUserData(stat);
        int CurrentHighestLevel = stat.getHighestLevel();
        if(CurrentHighestLevel < Level){
            playerStatistics.clear();
            playerStatistics.add(new Statistics(Level, stat.totalBulletsShot, stat.totalShipsDestroyed, stat.shipsDestructionStreak,
                    stat.playedGameNumber, stat.clearAchievementNumber, stat.totalPlaytime, stat.accuracy));
            Globals.getFileManager().saveUserData(playerStatistics);
        }

        AchievementManager.getInstance().checkAchievement(AchievementType.STAGE, Level);
    }

    /**
     * Add the number of bullets fired so far to the previous record.
     * @param PlusBulletShot
     *              current fired bullets.
     * @throws IOException
     *              In case of saving problems.
     */

    public void addBulletShot(int PlusBulletShot) throws IOException{
        this.stat = loadUserData(stat);
        int CurrentBulletShot = stat.getTotalBulletsShot();
        CurrentBulletShot += PlusBulletShot;

        playerStatistics.clear();
        playerStatistics.add(new Statistics(stat.highestLevel, CurrentBulletShot, stat.totalShipsDestroyed, stat.shipsDestructionStreak,
                stat.playedGameNumber, stat.clearAchievementNumber, stat.totalPlaytime, stat.accuracy));
        Globals.getFileManager().saveUserData(playerStatistics);
    }

    /**
     * Add the number of ships destroyed so far to the previous record.
     * @param PlusShipsDestroyed
     *              The number of ships destroyed
     * @throws IOException
     *              In case of saving problems.
     */



    public void addShipsDestroyed(int PlusShipsDestroyed) throws IOException{
        this.stat = loadUserData(stat);
        int CurrentShipsDestroyed = stat.getTotalShipsDestroyed();
        CurrentShipsDestroyed += PlusShipsDestroyed;

        playerStatistics.clear();
        playerStatistics.add(new Statistics(stat.highestLevel, stat.totalBulletsShot, CurrentShipsDestroyed, stat.shipsDestructionStreak,
                stat.playedGameNumber, stat.clearAchievementNumber, stat.totalPlaytime, stat.accuracy));
        Globals.getFileManager().saveUserData(playerStatistics);

        AchievementManager.getInstance().checkAchievement(AchievementType.KILLS, CurrentShipsDestroyed);
    }

    /**
     * Add the number of games played.
     *
     * @param PlusPlayedGameNumber
     *              The number of times the game has been played
     *              until the program is executed and closed.
     * @throws IOException
     *              In case of saving problems.
     */

    public void addPlayedGameNumber(int PlusPlayedGameNumber) throws IOException {
        this.stat = loadUserData(stat);
        int CurrentPlayedGameNumber = stat.getPlayedGameNumber();
        CurrentPlayedGameNumber += PlusPlayedGameNumber;

        playerStatistics.clear();
        playerStatistics.add(new Statistics(stat.highestLevel, stat.totalBulletsShot, stat.totalShipsDestroyed, stat.shipsDestructionStreak,
                CurrentPlayedGameNumber, stat.clearAchievementNumber, stat.totalPlaytime, stat.accuracy));
        Globals.getFileManager().saveUserData(playerStatistics);

        AchievementManager.getInstance().checkAchievement(AchievementType.TRIALS, CurrentPlayedGameNumber);
    }

    /**
     * Compare the current game's destruction streak
     * with the high score for shipsDestructionStreak.
     *
     * @param DestroyedShipNumber
     *              current game score
     * @throws IOException
     *              In case of saving problems.
     */

    private Bullet lastBullet;
    private boolean lastBulletCheckCount = true;

    public void setLastBullet(Bullet bullet) {
        this.lastBullet = bullet;
    }

    public void comShipsDestructionStreak(int DestroyedShipNumber) throws IOException {
        this.stat = loadUserData(stat);
        Bullet bullet = this.lastBullet;

        if (lastBullet != null) {
            if (!lastBullet.isCheckCount() && lastBulletCheckCount) {
                DestroyedShipNumber += 1;
                lastBulletCheckCount = false;
            } else if (lastBullet.isCheckCount()) {
                DestroyedShipNumber = 0;
                lastBulletCheckCount = true;
            }
        }

        int CurrentShipsDestructionStreak = stat.getShipsDestructionStreak();
        if(CurrentShipsDestructionStreak < DestroyedShipNumber){
            playerStatistics.clear();
            playerStatistics.add(new Statistics(stat.highestLevel, stat.totalBulletsShot, stat.totalShipsDestroyed, DestroyedShipNumber,
                    stat.playedGameNumber, stat.clearAchievementNumber, stat.totalPlaytime, stat.accuracy));
            Globals.getFileManager().saveUserData(playerStatistics);
        }

        AchievementManager.getInstance().checkAchievement(AchievementType.KILLSTREAKS, DestroyedShipNumber);
    }

    /**
     * Compare the number of achievements cleared up to now with
     * the number of achievements cleared after the current game.
     *
     * @param ClearedAchievement
     *              current game score
     * @throws IOException
     *              In case of saving problems.
     */

    public void comClearAchievementNumber(int ClearedAchievement) throws IOException {
        this.stat = loadUserData(stat);
        int CurrentClearAchievementNumber = stat.getClearAchievementNumber();
        if(CurrentClearAchievementNumber < ClearedAchievement){
            playerStatistics.clear();
            playerStatistics.add(new Statistics(stat.highestLevel, stat.totalBulletsShot, stat.totalShipsDestroyed,stat.shipsDestructionStreak,
                    stat.playedGameNumber, ClearedAchievement, stat.totalPlaytime, stat.accuracy));
            Globals.getFileManager().saveUserData(playerStatistics);
        }
    }

    public void comAccuracy(float Accuracy) throws IOException {
        this.stat = loadUserData(stat);
        int shots = stat.getTotalBulletsShot();
        int hits = stat.getTotalShipsDestroyed();
        Accuracy = shots > 0 ? ((float) hits / shots) * 100 : 0;

        playerStatistics.clear();
        playerStatistics.add(new Statistics(stat.highestLevel, stat.totalBulletsShot, stat.totalShipsDestroyed, stat.shipsDestructionStreak,
                stat.playedGameNumber, stat.clearAchievementNumber, stat.totalPlaytime, Accuracy));
        Globals.getFileManager().saveUserData(playerStatistics);

        AchievementManager.getInstance().checkAchievement(AchievementType.ACCURACY, (int) Accuracy);
    }

    /**
     *  Add the current game's playtime to the previous total playtime.
     *
     * @param Playtime
     *              current playtime
     * @throws IOException
     *              In case of saving problems.
     */

    public void addTotalPlayTime(long Playtime) throws IOException {
        this.stat = loadUserData(stat);
        long CurrentPlaytime = stat.getTotalPlaytime();
        CurrentPlaytime += Playtime;

        playerStatistics.clear();
        playerStatistics.add(new Statistics(stat.highestLevel, stat.totalBulletsShot, stat.totalShipsDestroyed, stat.shipsDestructionStreak,
                stat.playedGameNumber, stat.clearAchievementNumber, CurrentPlaytime, stat.accuracy));
        Globals.getFileManager().saveUserData(playerStatistics);
    }

    /**
     *  Load Statistic.property (userdata)
     *
     * @throws IOException
     *              In case of loading problems.
     */
    public Statistics loadUserData(Statistics stat) throws IOException {
        stat = Globals.getFileManager().loadUserData();
        return stat;
    }

    public Statistics getStatisticsData() throws IOException {
        return Globals.getFileManager().loadUserData();
    }

    public void resetStatistics() throws IOException {
        this.playerStatistics = new ArrayList<Statistics>();
        playerStatistics.add(new Statistics(0, 0, 0, 0,
                0, 0, 0, 0));
        Globals.getFileManager().saveUserData(playerStatistics);
    }

    public void startAddingShipsDestroyed() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                addShipsDestroyed(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}