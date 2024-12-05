package screen;



import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

import engine.DrawManagerImpl;
import engine.Achievement.AchievementManager;
import engine.Achievement.AchievementType;
import engine.Globals;
import entity.*;
import engine.GameState;
import engine.GameSettings;


public class TwoPlayerGameScreen extends GameScreen {

    public Ship ship2;


    public TwoPlayerGameScreen(GameState gameState, GameSettings gameSettings, boolean bonusLife, int width, int height, int fps) {
        super(gameState, gameSettings, bonusLife, width, height, fps);
        // ship2 초기화
        this.ship2 = new Ship(this.width / 4, this.height - 30, Color.BLUE); // add by team HUD
        this.ship2.setKEY_LEFT(KeyEvent.VK_A);
        this.ship2.setKEY_RIGHT(KeyEvent.VK_D);
        this.ship2.setKEY_UP(KeyEvent.VK_W);
        this.ship2.setKEY_DOWN(KeyEvent.VK_S);
        this.ship2.setKEY_SHOOT(KeyEvent.VK_SPACE);
        this.ship2.setHealth(gameState.getLivesTwoRemaining());
        if(this.ship2.getHealth() <= 0){
            this.ship2.setDestroyed(true);
        }
        this.addEntity(this.ship2);

    }

    @Override
    public void initialize() {
        super.initialize(); // GameScreen의 초기화 로직 호출
    }

    @Override
    public GameState getGameState() {
        return new GameState(this.getLevel(), this.score, getShip1().getHealth(), ship2.getHealth(),
                this.bulletsShot, this.shipsDestroyed, this.accuracy, this.playTime, this.coin, this.gem, this.hitCount, this.coinItemsCollected, this.totalDistance); // Team-Ctrl-S(Currency)
    }

    @Override
    protected boolean update() {
        boolean gameProgress = inputDelay.checkFinished() && !isLevelFinished();
        ship2.setCanMove(gameProgress && ship2.getHealth() > 0  && ship2.getDestructionCooldown().checkFinished());

        super.update();
        if (this.isLevelFinished() && this.screenFinishedCooldown.checkFinished()) {
            //this.logger.info("Final Playtime: " + playTime + " seconds");    //clove
            if(this.getLevel() == Globals.NUM_LEVELS){
                AchievementManager.getInstance().checkAchievement(AchievementType.LIVES, ship2.getHealth());
            }
            AchievementManager.getInstance().checkAchievement(AchievementType.SCORE, score);
            try { //Team Clove
                this.statistics.comHighestLevel(this.getLevel());
                this.statistics.addBulletShot(bulletsShot);
                this.statistics.addShipsDestroyed(shipsDestroyed);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            this.isRunning = false;
        }
        return true;
    }

    @Override
    public void draw(){
        super.draw();
        DrawManagerImpl.drawBulletSpeed2P(this, ship2.getBulletSpeed());
        DrawManagerImpl.drawSpeed2P(this, ship2.getSpeed());
        DrawManagerImpl.drawLives2P(this, ship2.getHealth());
    }

    @Override
    protected void checkGameEnd() {
        /**
         * Wave counter condition added by the Level Design team*
         * Changed the conditions for the game to end  by team Enemy
         *
         * Checks if the intended number of waves for this level was destroyed
         * **/
        boolean clearRound = getRemainingEnemies() == 0 && !this.levelFinished && waveCounter == this.gameSettings.getWavesNumber();
        boolean shipDestroyed = ship1.isDestroyed() && ship2.isDestroyed() && !this.levelFinished;
        boolean selectGoToTitle = goToTitle; // Must be false, when this is set to true, returnCode is already set.
        if ((clearRound || shipDestroyed) && !selectGoToTitle) {
            this.levelFinished = true;
            this.screenFinishedCooldown.reset();
        }
    }
}

