package engine;

import screen.Screen;
import entity.Entity;

import java.awt.*;

public class DrawManagerImpl extends DrawManager {

    // Add method
    public static void drawLevel(final Screen screen, final int level){
        String levelText = "Level: " + level;

        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.setColor(Color.WHITE);

        int xPosition = screen.getWidth() / 2;
        int yPosition = 25;

        backBufferGraphics.drawString(levelText, xPosition - fontRegularMetrics.stringWidth(levelText) / 2, yPosition); // edit by jesung ko - TeamHUD
    } // Lee Hyun Woo - level

    public static void drawBulletSpeed(final Screen screen, final int bulletSpeed) {
        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.setColor(Color.WHITE);
        String bulletSpeedText = String.format("BS : %d px/f ", bulletSpeed);
        backBufferGraphics.drawString(bulletSpeedText, 10, screen.getHeight() - 15);
    }

    public static void drawSpeed(final Screen screen, final double speed) {
        String speedString = "MS : " + speed;
        backBufferGraphics.setColor(Color.WHITE);
        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.drawString(speedString, 10, screen.getHeight() - 35);
    }

    public void drawLivesWithHeart(final Screen screen, final int lives) {
        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.setColor(Color.WHITE);

        Entity heart = new Entity(0, 0, 13 * 2, 8 * 2, Color.RED) {

        };
        heart.setSpriteType(SpriteType.Heart);

        for (int i = 0; i < lives; i++) {
            drawEntity(heart, 20 + 30 * i, 10);
        }
    } // Saeum Jung - heart graphic

    /**
     * Draws current score on screen.
     *
     * @param screen
     *            Screen to draw on.
     * @param playTime
     *            Current playtime.
     *
     * by Soomin Lee - TeamHUD
     */
    public static void drawTime(final Screen screen, final int playTime) {
        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.setColor(Color.WHITE);
        int playTimeMinutes = playTime / 60;
        int playTimeSeconds = playTime % 60;
        String playTimeString = String.format("%d"+"m "+"%d"+"s", playTimeMinutes, playTimeSeconds);
        int xPosition = (screen.getWidth() * 4) / 6; // position 4/6
        backBufferGraphics.drawString(playTimeString, xPosition - fontRegularMetrics.stringWidth(playTimeString) / 2, 25); // edit by jesung ko - TeamHUD
    }

    /**
     * Draws the player's score on the screen.
     * The score is displayed at the 2/6 position of the screen width.
     *
     * @param screen
     *          The screen to draw on.
     * @param score
     *          The current score to display.
     * by jesung Ko - TeamHUD
     */
    public static void drawScore2(final Screen screen, int score) {
        String scoreString = "Score: " + score;
        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.setColor(Color.WHITE);
        int xPosition = (screen.getWidth() * 2) / 6; // 2/6 지점
        backBufferGraphics.drawString(scoreString, xPosition - fontRegularMetrics.stringWidth(scoreString) / 2, 25);
    }
    /**
     * Show accomplished achievement
     *
     * @param screen
     *            Screen to draw on.
     * @param achievementText
     *            Accomplished achievement text.
     *
     * by Jo Minseo - HUD team
     */
    public static void drawAchievement(final Screen screen, String achievementText) {
        int width = screen.getWidth() / 4;
        int height = screen.getHeight() / 16;
        int fontWidth;

        backBufferGraphics.setColor(Color.white);
        backBufferGraphics.drawRect(screen.getWidth() - width - 3, screen.getHeight() - height - 68, width, height);

        //Modify the location of the window and the text - Jo Minseo/HUD team
        backBufferGraphics.setColor(Color.white);
        backBufferGraphics.setFont(fontRegular);
        if(achievementText.length() < 14){
            fontWidth = fontRegularMetrics.stringWidth(achievementText);
            backBufferGraphics.drawString(achievementText, screen.getWidth() - width / 2 - fontWidth / 2, screen.getHeight() - 85);
        }
        else if(achievementText.length() < 27){
            fontWidth = fontRegularMetrics.stringWidth(achievementText.substring(0,13));
            backBufferGraphics.drawString(achievementText.substring(0,13), screen.getWidth() - width / 2 - fontWidth / 2, screen.getHeight() - 93);
            fontWidth = fontRegularMetrics.stringWidth(achievementText.substring(13));
            backBufferGraphics.drawString(achievementText.substring(13), screen.getWidth() - width/2 - fontWidth / 2, screen.getHeight() - 75);
        }
        else{
            fontWidth = fontRegularMetrics.stringWidth(achievementText.substring(0,13));
            backBufferGraphics.drawString(achievementText.substring(0,13), screen.getWidth() - width / 2 - fontWidth / 2, screen.getHeight() - 93);
            fontWidth = fontRegularMetrics.stringWidth(achievementText.substring(13,25)+"...");
            backBufferGraphics.drawString(achievementText.substring(13,25)+"...", screen.getWidth() - width / 2 - fontWidth / 2, screen.getHeight() - 75);
        }
    }

    /**
     * Draw remaining enemies
     *
     * @param screen
     *            Screen to draw on.
     * @param remainingEnemies
     *            remaining enemies count.
     */
    public static void drawRemainingEnemies(final Screen screen, final int remainingEnemies) {
        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.setColor(Color.WHITE);
        String remainingEnemiesString = "Enemies: " + remainingEnemies;
        int textWidth = fontRegularMetrics.stringWidth(remainingEnemiesString);

        int x = (screen.getWidth() - textWidth) / 2;
        int y = screen.getHeight() - 25;

        backBufferGraphics.drawString(remainingEnemiesString, x, y);
    } // by SeungYun

    public static void drawGoToTitleWarning(final Screen screen) {
        int width = screen.getWidth() / 2 + 20;
        int height = screen.getHeight() / 3 + 20;
        int rectX = (screen.getWidth() - width) / 2;
        int rectY = (screen.getHeight() - height) / 2;

        String warningString1 = "Current progress will be lost.";
        String warningString2 = "Are you sure?";
        int fontWidth1 = fontRegularMetrics.stringWidth(warningString1);
        int fontWidth2 = fontRegularMetrics.stringWidth(warningString2);
        int textX1 = rectX + (width - fontWidth1) / 2;
        int textX2 = rectX + (width - fontWidth2) / 2;
        int text1Y = rectY + height / 8 + fontRegularMetrics.getAscent();
        int text2Y = rectY + height / 4 + fontRegularMetrics.getAscent();

        backBufferGraphics.setColor(Color.black);
        backBufferGraphics.fillRect(rectX, rectY, width, height);

        backBufferGraphics.setColor(Color.WHITE);
        backBufferGraphics.drawRect(rectX, rectY, width, height);

        backBufferGraphics.setColor(Color.white);
        backBufferGraphics.setFont(fontRegular);

        backBufferGraphics.setColor(Color.white);
        backBufferGraphics.drawString(warningString1, textX1, text1Y);
        backBufferGraphics.drawString(warningString2, textX2, text2Y);

    }



    /**
     * Draws current score on screen.
     *
     * @param screen
     *            Screen to draw on.
     * @param positionY
     *            Position to display separator line.
     *
     * by Ko jesung - TeamHUD
     */
    public static void drawSeparatorLine(final Screen screen, final int positionY) {
        backBufferGraphics.setColor(Color.GREEN);
        backBufferGraphics.drawLine(0, positionY, screen.getWidth(), positionY);
        backBufferGraphics.drawLine(0, positionY + 1, screen.getWidth(),
                positionY + 1);
    }

    /**
     * Draws a filled rectangle with specified color at given coordinates.
     *
     * @param x
     *            The x-coordinate of the rectangle's top-left corner.
     * @param y
     *            The y-coordinate of the rectangle's top-left corner.
     * @param width
     *            The width of the rectangle.
     * @param height
     *            The height of the rectangle.
     * @param color
     *            The color to fill the rectangle.
     */
    public static void drawRect(final int x, final int y, final int width, final int height, final Color color) {
        backBufferGraphics.setColor(color);
        backBufferGraphics.fillRect(x, y, width, height);
    } // by Saeum Jung - TeamHUD

    /**
     * Draws 2P's lives on screen.
     *
     * @param screen
     *            Screen to draw on.
     * @param livestwo two
     *            2P's lives
     *
     * by Lee HyunWoo - TeamHUD
     */
    public static void drawLives2P(final Screen screen, final int livestwo) {
        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.setColor(Color.WHITE);
        Entity heart = new Entity(0, 0, 13 * 2, 8 * 2, Color.BLUE) {

        };
        heart.setSpriteType(SpriteType.Heart);

        int heartWidth = 13 * 2;
        int startingXPosition = screen.getWidth() - (heartWidth * livestwo) - 20;

        for (int i = 0; i < livestwo; i++)
            drawEntity(heart, startingXPosition + 30 * i, 10);
    }

    public static void drawPlayerDistance(Screen screen, int playerDistance) {
        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.setColor(Color.WHITE);

        String playerDistanceString = "Distance: " + (int)(playerDistance) + " M";
        int xPosition = screen.getWidth() - fontRegularMetrics.stringWidth(playerDistanceString) - 20;
        int yPosition = screen.getHeight() - 25;

        backBufferGraphics.drawString(playerDistanceString, xPosition, yPosition);
    }

    public static void drawCooldownCircle(Screen screen, final int centerX, final int centerY, float percentage, int remainingSeconds) {
        int radius = 20;
        int angle = (int) (360 * percentage);

        backBufferGraphics.setColor(Color.GREEN);
        backBufferGraphics.fillArc(centerX - radius, 0, 2 * radius, 2 * radius, 90, -360);
        backBufferGraphics.setColor(Color.BLACK);
        backBufferGraphics.fillArc(centerX-radius, 0, 2 * radius, 2 * radius, 90, -angle);
        backBufferGraphics.setColor(Color.WHITE);
        backBufferGraphics.drawArc(centerX - radius, 0, 2 * radius, 2 * radius, 90, -360);
        backBufferGraphics.setFont(fontRegular);
        String timeText = String.valueOf(remainingSeconds/1000);
        backBufferGraphics.setFont(fontRegular);
        int textWidth = fontRegularMetrics.stringWidth(timeText);
        backBufferGraphics.drawString(timeText, centerX - textWidth / 2, 25);

        String cooldownLabel = "Cooldown: ";
        backBufferGraphics.drawString(cooldownLabel,centerX - radius - fontRegularMetrics.stringWidth(cooldownLabel) - 10, 25);
    }

    /**
     * Draws 2P's bulletSpeed on screen.
     *
     * @param screen
     *            Screen to draw on.
     * @param bulletSpeed two
     *            2P's bulletSpeed
     *
     * by Lee HyunWoo - TeamHUD
     */
    public static void drawBulletSpeed2P(final Screen screen, final int bulletSpeed) {
        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.setColor(Color.WHITE);
        String bulletSpeedText = String.format("BS : %d px/f ", bulletSpeed);
        backBufferGraphics.drawString(bulletSpeedText, 500, screen.getHeight() - 15);
    }

    /**
     * Draws 2P's speed on screen.
     *
     * @param screen
     *            Screen to draw on.
     * @param speed two
     *            2P's speed
     *
     * by Lee HyunWoo - TeamHUD
     */
    public static void drawSpeed2P(final Screen screen, final double speed) {
        String speedString = "MS : " + speed;
        backBufferGraphics.setColor(Color.WHITE);
        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.drawString(speedString, 500, screen.getHeight() - 35);
    }
}
