package entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

import engine.GameState;
import engine.GameSettings;
import screen.GameScreen;

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
    @Test
    @DisplayName("Calculate distance moved by Ship using playerDistance")
    public void distanceCalculationTest() {
        // Basic initial setup
        GameSettings gameSettings = new GameSettings(500, 400, 5, 30, 3);
        GameState dummyGameState = new GameState(1, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        GameScreen gameScreen = new GameScreen(dummyGameState, gameSettings, false, 630, 720, 60);
        gameScreen.initialize();
        Ship ship = gameScreen.getShip1();
        ship.setPosition(125, 150);
        gameScreen.updatePlayerDistance();
        int lastLocation = gameScreen.getPlayerDistance();
        System.out.println("Initial Position X: " + ship.getPositionX() + ", Y: " + ship.getPositionY());
        System.out.println("Initial Distance: " + gameScreen.getPlayerDistance());

        //Test 1
        ship.setPosition(155, 190);
        gameScreen.updatePlayerDistance();
        int currentLocation1 = gameScreen.getPlayerDistance();
        System.out.println("Moved Position X: " + ship.getPositionX() + ", Y: " + ship.getPositionY());
        System.out.println("Calculated Distance: " + gameScreen.getPlayerDistance());
        int expectedDistance = (int) Math.sqrt(30 * 30 + 40 * 40);
        int calculatedDistance1 = currentLocation1 - lastLocation;
        assertEquals(expectedDistance, calculatedDistance1);

        //Test 2
        lastLocation = currentLocation1; //Update ship location
        ship.setPosition(150, 178);
        gameScreen.updatePlayerDistance();
        int currentLocation2 = gameScreen.getPlayerDistance();
        System.out.println("Moved Position X: " + ship.getPositionX() + ", Y: " + ship.getPositionY());
        System.out.println("Calculated Distance: " + gameScreen.getPlayerDistance());
        int expectedDistance2 = (int) Math.sqrt(5 * 5 + 12 * 12);
        int calculateDistance2 = currentLocation2 - lastLocation;
        assertEquals(expectedDistance2, calculateDistance2);
    }
}