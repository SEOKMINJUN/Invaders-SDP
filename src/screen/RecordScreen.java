package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import engine.Globals;
import engine.Score;

/**
 *  Implements past user recorded score
 */
public class RecordScreen extends Screen {

    /** List of user recent scores */
    private List<Score> recentScores;

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
    public RecordScreen(final int width, final int height, final int fps) {
        super(width, height, fps);

        this.returnCode = 1;

        try {
            this.recentScores = Globals.getFileManager().loadRecentScores();
        } catch (NumberFormatException | IOException e) {
            logger.warning("Couldn't load records!");
        }
    }

    /**
     * Starts the action.
     *
     * @return Next screen code.
     */
    public final int run() {
        super.run();

        return this.returnCode;
    }

    /**
     * Updates the elements on screen and checks for events.
     */
    protected final boolean update() {
        super.update();

        if (inputManager.isKeyDown(KeyEvent.VK_SPACE)
                && this.inputDelay.checkFinished())
            this.isRunning = false;
        return true;
    }

    /**
     * Draws the elements associated with the screen.
     */
    protected void draw() {

        drawManager.drawRecordMenu(this);
        drawManager.drawRecentScores(this, this.recentScores);

        super.drawPost();
    }
}
