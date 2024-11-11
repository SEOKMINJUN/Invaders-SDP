package screen;

import engine.DrawManager;
import engine.Globals;
import engine.Statistics;
import lombok.Getter;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CollectionsScreen extends Screen {
    /** Current Screen Code*/
    public static int CollectionsScreenCode;
    /** check isArrowDown*/
    private boolean checkArrow;
    /** Array List*/
    private List<Statistics> collectionsStatistics;

    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param width
     *            Screen width.
     * @param height
     *            Screen height.
     * @param fps
     *            Frames per second, frame rate at which the game is run.
     */
    public CollectionsScreen(final int width, final int height, final int fps) {
        super(width, height, fps);
        collectionsStatistics = new ArrayList<Statistics>();
        try {
            this.collectionsStatistics.add(Globals.getFileManager().loadCollections());
        } catch (NumberFormatException | IOException e) {
            logger.warning("Couldn't load records!");
        }

        CollectionsScreenCode = 0;
        checkArrow = false;
        this.returnCode = 1;
    }

    public final int run() {
        super.run();

        return this.returnCode;
    }

    /**
     * Updates the elements on screen and checks for events.
     * Press the arrow to determine the screen
     */
    protected final boolean update() {
        super.update();

        draw();
        if (inputManager.isKeyDown(KeyEvent.VK_SPACE)
                && this.inputDelay.checkFinished())
            this.isRunning = false;
        if (inputManager.isKeyDown(KeyEvent.VK_LEFT)
                && this.inputDelay.checkFinished() && !checkArrow) {
            CollectionsScreenCode = (CollectionsScreenCode + 7) % 8;
            //System.out.println("Activate LEFT" + CollectionsScreenCode);
            checkArrow = true;
        }
        else if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                && this.inputDelay.checkFinished() && !checkArrow) {
            CollectionsScreenCode = (CollectionsScreenCode + 1) % 8;
            //System.out.println("Activate RIGHT" + CollectionsScreenCode);
            checkArrow = true;
        }

        if (!inputManager.isKeyDown(KeyEvent.VK_LEFT) && !inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
            checkArrow = false;
        }
        return false;
    }

    protected void draw() {
        drawManager.initDrawing(this);

        drawManager.drawCollectionsMenu(this);
        drawManager.drawCollectionsData(this, collectionsStatistics);

        super.draw();
        drawManager.completeDrawing(this);
    }
}
