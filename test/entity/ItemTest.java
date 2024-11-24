package entity;

import engine.DrawManager;
import engine.GameState;
import engine.Globals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import screen.GameScreen;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    @Test
    @DisplayName("Create and test bomb item")
    public void bombTest(){
        int x = 10;
        int y = 10;
        Item item = new Item(x, y, 0, 0);
        item.spriteType = DrawManager.SpriteType.ItemBomb;

        GameState gameState = new GameState(1, 0
                , Globals.MAX_LIVES, Globals.MAX_LIVES, 0, 0, 0, 0, 0, 0, 0, 0);
        GameScreen gameScreen = new GameScreen(gameState, Globals.SETTINGS_LEVEL_1, false, 400, 500, 60);

        Ship ship = new Ship(x,y+50, Color.gray);
        gameScreen.addEntity(ship);
        gameScreen.addEntity(item);

        item.update();
        assertTrue(ship.isBombBullet());
    }
    @Test
    @DisplayName("Create and test heart item")
    public void heartTest(){
        int x = 10;
        int y = 10;
        Item item = new Item(x, y, 0, 0);
        item.spriteType = DrawManager.SpriteType.ItemHeart;

        GameState gameState = new GameState(1, 0
                , Globals.MAX_LIVES, Globals.MAX_LIVES, 0, 0, 0, 0, 0, 0, 0, 0);
        GameScreen gameScreen = new GameScreen(gameState, Globals.SETTINGS_LEVEL_1, false, 400, 500, 60);

        Ship ship = new Ship(x,y+50, Color.green);
        ship.setHealth(gameState.getLivesRemaining());
        gameScreen.addEntity(ship);
        gameScreen.addEntity(item);


        ship.subtractHealth();
        item.update();
        assertEquals(Globals.MAX_LIVES, ship.getHealth());
    }

    @Test
    @DisplayName("Create and test barrier item")
    public void barrierTest(){
        int x = 10;
        int y = 10;
        Item item = new Item(x, y, 0, 0);
        item.spriteType = DrawManager.SpriteType.ItemBarrier;

        GameState gameState = new GameState(1, 0
                , Globals.MAX_LIVES, Globals.MAX_LIVES, 0, 0, 0, 0, 0, 0, 0, 0);
        GameScreen gameScreen = new GameScreen(gameState, Globals.SETTINGS_LEVEL_1, false, 400, 500, 60);

        Ship ship = new Ship(x,y+50, Color.red);
        gameScreen.addEntity(ship);
        gameScreen.addEntity(item);


        item.update();
        assertTrue(ship.isBarrierActive());
    }
    @Test
    @DisplayName("Create and test fevertime item")
    public void feverTimeTest(){
        int x = 10;
        int y = 10;
        Item item = new Item(x, y, 0, 0);
        item.spriteType = DrawManager.SpriteType.ItemFeverTime;

        GameState gameState = new GameState(1, 0
                , Globals.MAX_LIVES, Globals.MAX_LIVES, 0, 0, 0, 0, 0, 0, 0, 0);
        GameScreen gameScreen = new GameScreen(gameState, Globals.SETTINGS_LEVEL_1, false, 400, 500, 60);

        Ship ship = new Ship(x,y+50, Color.yellow);
        gameScreen.addEntity(ship);
        gameScreen.addEntity(item);


        item.update();
        assertTrue(gameScreen.getFeverTimeItem().isActive());
    }
}