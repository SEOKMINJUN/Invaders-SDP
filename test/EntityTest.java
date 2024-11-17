import engine.DrawManager;
import engine.GameState;
import engine.Globals;
import entity.*;
import org.junit.jupiter.api.Test;
import screen.GameScreen;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    @Test
    public void ShipTest() {
        int x = 125;
        int y = 300;
        Ship ship = new Ship(x, y+50, Color.BLUE);

        assertNotNull(ship);
        assertEquals(x, ship.getPositionX());
        assertEquals(y, ship.getPositionY());
        assertEquals("Ship", ship.getClassName());

        // Is ship destroyed when health been below 0
        int health = ship.getHealth();
        for(int i = 0;i < health; i++){
            ship.subtractHealth();
        }
        assertTrue(ship.isDestroyed());
    }

    @Test
    public void BombTest(){
        int x = 10;
        int y = 300;
        int radius = 100;
        BombBullet bombBullet = new BombBullet(x, y, -3);

        assertNotNull(bombBullet);
        assertEquals(x, bombBullet.getPositionX());
        assertEquals(y, bombBullet.getPositionY());
        assertEquals("BombBullet", bombBullet.getClassName());

        GameState gamestate = new GameState(1, 0
                , Globals.MAX_LIVES, Globals.MAX_LIVES, 0, 0, 0, 0, 0, 0, 0, 0);
        GameScreen gameScreen = new GameScreen(gamestate, Globals.SETTINGS_LEVEL_1, false, 400, 500, 60);

        EnemyShip enemyShip;
        enemyShip = new EnemyShip(x, y, DrawManager.SpriteType.EnemyShipA1,1,-2,-2);
        assertTrue(bombBullet.checkCollisionWithRadius(enemyShip, radius));
        enemyShip = new EnemyShip(x, y + radius, DrawManager.SpriteType.EnemyShipA1,1,-2,-2);
        assertTrue(bombBullet.checkCollisionWithRadius(enemyShip, radius));
        enemyShip = new EnemyShip(x + radius, y, DrawManager.SpriteType.EnemyShipA1,1,-2,-2);
        assertTrue(bombBullet.checkCollisionWithRadius(enemyShip, radius));
        enemyShip = new EnemyShip(x + (int)Math.round(radius / Math.sqrt(2.0)), y + (int)Math.round(radius / Math.sqrt(2.0)), DrawManager.SpriteType.EnemyShipA1,1,-2,-2);
        assertTrue(bombBullet.checkCollisionWithRadius(enemyShip, radius));

        enemyShip = new EnemyShip(x + radius + enemyShip.getWidth() / 2, y, DrawManager.SpriteType.EnemyShipA1,1,-2,-2);
        assertFalse(bombBullet.checkCollisionWithRadius(enemyShip, radius));
        enemyShip = new EnemyShip(x, y + radius + enemyShip.getHeight() / 2, DrawManager.SpriteType.EnemyShipA1,1,-2,-2);
        assertFalse(bombBullet.checkCollisionWithRadius(enemyShip, radius));
    }
}
