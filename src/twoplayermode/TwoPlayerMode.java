package twoplayermode;



import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.HashSet;

import HUDTeam.DrawManagerImpl;
import engine.Globals;
import entity.*;
import screen.GameScreen;
import engine.GameState;
import engine.GameSettings;


public class TwoPlayerMode extends GameScreen {

    public Ship ship2;


    public TwoPlayerMode(GameState gameState, GameSettings gameSettings, boolean bonusLife, int width, int height, int fps) {
        super(gameState, gameSettings, bonusLife, width, height, fps);
        // ship2 초기화
        this.ship2 = new Ship(this.width / 4, this.height - 30, Color.BLUE); // add by team HUD
        this.ship2.setHealth(gameState.getLivesTwoRemaining());
        Globals.getLogger().info("TwoPlayerMode : " + ship2.getHealth());
        this.ship2.setKEY_LEFT(KeyEvent.VK_Z);
        this.ship2.setKEY_RIGHT(KeyEvent.VK_C);
        this.ship2.setKEY_SHOOT(KeyEvent.VK_X);
        this.addEntity(this.ship2);

    }

    @Override
    public void initialize() {
        super.initialize(); // GameScreen의 초기화 로직 호출
    }

    @Override
    public GameState getGameState() {
        return new GameState(this.getLevel(), this.scoreManager.getAccumulatedScore(), getShip1().getHealth(), ship2.getHealth(),
                this.bulletsShot, this.shipsDestroyed, this.playTime, this.coin, this.gem, this.hitCount, this.coinItemsCollected); // Team-Ctrl-S(Currency)
    }

    @Override
    protected boolean update() {
        boolean gameProgress = inputDelay.checkFinished() && !isLevelFinished();
        ship2.setCanMove(gameProgress);

        super.update();
        return true;
    }

    @Override
    public void draw(){
        super.draw();
        DrawManagerImpl.drawBulletSpeed2P(this, ship2.getBulletSpeed());
        DrawManagerImpl.drawSpeed2P(this, ship2.getSpeed());
        DrawManagerImpl.drawLives2P(this, ship2.getHealth());
    }
}

