package entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {
    @Test
    @DisplayName("Create Ship object")
    public void createTest() {
        int x = 125;
        int y = 300;
        Ship ship = new Ship(x, y+50, Color.BLUE);

        assertNotNull(ship);
        assertEquals(x, ship.getPositionX());
        assertEquals(y, ship.getPositionY());
        assertEquals("Ship", ship.getClassName());
    }

    @Test
    @DisplayName("Test ship's health")
    public void healthTest() {
        Ship ship = new Ship(120, 150, Color.BLUE);
        // Is ship destroyed when health been below 0
        int health = ship.getHealth();
        for(int i = 0;i < health; i++){
            ship.subtractHealth();
        }
        assertTrue(ship.isDestroyed());
    }
}