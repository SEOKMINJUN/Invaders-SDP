package engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import engine.Achievement.AchievementManager;
import engine.Achievement.AchievementType;
import lombok.Getter;
import lombok.Setter;

import static engine.CollectionManager.achievementTypes;

public class Statistics {

    private static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static boolean schedulerStarted = false;
    private static final Object streakLock = new Object();
    private final Object maxStreakLock = new Object();
    private static final Object statisticFileLock = new Object();
    private static Statistics instance = new Statistics();
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
    @Getter
    private int maxShipsDestructionStreak;

    private static volatile long lastDestructionTime = System.currentTimeMillis();
    private static volatile int currentStreakCount = 0;
    private static final long STREAK_TIMEOUT = 2000;
    private static boolean streakTimeoutCheckerStarted = false;
    private static boolean addingShipsDestroyedStarted = false;
    private static volatile boolean shipDestroyed = false;

    /** Using for save statistics */
    private List<Statistics> playerStatistics = new ArrayList<>();
    private Statistics stat;

    @Getter @Setter
    private int[] itemsArray = new int[8];
    @Getter @Setter
    private int[] enemiesArray = new int[9];
    @Getter @Setter
    private int[] achievementsArray = new int[20];

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

        scheduler = Executors.newSingleThreadScheduledExecutor();
        startStreakTimeoutChecker();
    }

    public Statistics(final int[] itemsArray, final int[] achievementArray, final int[] enemiesArray){
        this.itemsArray = itemsArray;
        this.achievementsArray = achievementArray;
        this.enemiesArray = enemiesArray;
    }

    /**
     * Public Constructor
     */

    public Statistics() {
        if (!schedulerStarted) {
            scheduler = Executors.newSingleThreadScheduledExecutor();
            System.out.println("Streak timeout checker started.");
            startStreakTimeoutChecker();
            schedulerStarted = true;
        }
        try {
            this.stat = loadUserData(stat);
            this.maxShipsDestructionStreak = stat.getShipsDestructionStreak();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Statistics getInstance(){
        if (instance == null)
            instance = new Statistics();
        return instance;
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
            saveUserData(playerStatistics);
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
        saveUserData(playerStatistics);
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
        saveUserData(playerStatistics);

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
        saveUserData(playerStatistics);

        AchievementManager.getInstance().checkAchievement(AchievementType.TRIALS, CurrentPlayedGameNumber);
    }

    public void setShipDestroyed() {
        shipDestroyed = true;
    }

    public int checkAndUpdateStreak() throws IOException {
        this.stat = loadUserData(stat);
        try {
            long currentTime = System.currentTimeMillis();
            synchronized (maxStreakLock) {
                if (shipDestroyed) {
                    currentStreakCount += 1;
                    lastDestructionTime = currentTime;
                    shipDestroyed = false;
                    if (currentStreakCount > maxShipsDestructionStreak) {
                        maxShipsDestructionStreak = currentStreakCount;

                        playerStatistics.clear();
                        playerStatistics.add(new Statistics(
                                stat.highestLevel, stat.totalBulletsShot, stat.totalShipsDestroyed, maxShipsDestructionStreak,
                                stat.playedGameNumber, stat.clearAchievementNumber, stat.totalPlaytime, stat.accuracy));
                        saveUserData(playerStatistics);

                        AchievementManager.getInstance().checkAchievement(AchievementType.KILLSTREAKS, maxShipsDestructionStreak);
                    }
                }
                if (currentStreakCount > 0 && currentTime - lastDestructionTime >= STREAK_TIMEOUT) {
                    currentStreakCount = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxShipsDestructionStreak;
    }

    public void comShipsDestructionStreak(int DestructionStreak) throws IOException {
        synchronized (maxStreakLock) {
            this.stat = loadUserData(stat);
            DestructionStreak = stat.getShipsDestructionStreak();

            if (maxShipsDestructionStreak > DestructionStreak) {
                playerStatistics.clear();
                playerStatistics.add(new Statistics(
                        stat.highestLevel, stat.totalBulletsShot, stat.totalShipsDestroyed, maxShipsDestructionStreak,
                        stat.playedGameNumber, stat.clearAchievementNumber, stat.totalPlaytime, stat.accuracy));
                saveUserData(playerStatistics);

                AchievementManager.getInstance().checkAchievement(AchievementType.KILLSTREAKS, maxShipsDestructionStreak);
            }

        }
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
            saveUserData(playerStatistics);
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
        saveUserData(playerStatistics);

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
        saveUserData(playerStatistics);
    }

    /**
     *  Load Statistic.property (userdata)
     *
     * @throws IOException
     *              In case of loading problems.
     */
    public Statistics loadUserData(Statistics stat) throws IOException {
        synchronized (statisticFileLock){
            stat = Globals.getFileManager().loadUserData();
            this.maxShipsDestructionStreak = stat.getShipsDestructionStreak();
            return stat;
        }
    }

    public void saveUserData(List<Statistics> stats) throws IOException {
        synchronized (statisticFileLock){
            System.out.println("Saving user data...");
            Globals.getFileManager().saveUserData(stats);
        }
    }

    public void resetStatistics() throws IOException {
        this.playerStatistics = new ArrayList<Statistics>();
        playerStatistics.add(new Statistics(0, 0, 0, 0,
                0, 0, 0, 0));
        saveUserData(playerStatistics);
    }

    private void startStreakTimeoutChecker() {
        synchronized (streakLock) {
            if (!streakTimeoutCheckerStarted) {
                scheduler.scheduleAtFixedRate(() -> {
                    try {
                        checkAndUpdateStreak();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 0, 100, TimeUnit.MILLISECONDS);
                streakTimeoutCheckerStarted = true;
            }
        }
    }

    public void stopScheduler() {
        if (!scheduler.isShutdown()) {
            scheduler.shutdownNow();
            streakTimeoutCheckerStarted = false;
            addingShipsDestroyedStarted = false;
            System.out.println("Scheduler stopped.");
        }
    }

    public void startAddingShipsDestroyed() {
        synchronized (streakLock) {
            if (!addingShipsDestroyedStarted) {
                scheduler.scheduleAtFixedRate(() -> {
                    try {
                        addShipsDestroyed(1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, 0, 1, TimeUnit.SECONDS);
                addingShipsDestroyedStarted = true;
            }
        }
    }

    public void updateAchievementsArray(int index) {
        if (index >= 0 && index < achievementTypes.length) {
            achievementTypes[index] = 1; // 상태를 완료로 표시
            this.setAchievementsArray(achievementTypes); // 배열 갱신
            Globals.getLogger().info("Achievement array updated at index: " + index);
        } else {
            Globals.getLogger().warning("Invalid achievement index: " + index);
        }
    }

    public void saveUserData(Statistics stat) throws IOException {
        List<Statistics> playerStatistics = new ArrayList<Statistics>();
        playerStatistics.add(stat);
        saveUserData(playerStatistics);
    }
}