package screen;

import engine.Achievement.AchievementList;
import engine.Core;
import engine.Globals;
import engine.Statistics;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
public class CollectionsScreen extends Screen {

    public static int CollectionsScreenCode;
    private boolean checkArrow;
    private List<Statistics> collectionsStatistics;

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

    protected final void update() {
        super.update();

        draw();
        if (inputManager.isKeyDown(KeyEvent.VK_SPACE)
                && this.inputDelay.checkFinished())
            this.isRunning = false;
        if (inputManager.isKeyDown(KeyEvent.VK_LEFT)
                && this.inputDelay.checkFinished() && !checkArrow) {
            CollectionsScreenCode = (CollectionsScreenCode - 1 + 3) % 3;
            //System.out.println("Activate LEFT" + CollectionsScreenCode);
            checkArrow = true;
        }
        else if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                && this.inputDelay.checkFinished() && !checkArrow) {
            CollectionsScreenCode = (CollectionsScreenCode + 1 + 3) % 3;
            //System.out.println("Activate RIGHT" + CollectionsScreenCode);
            checkArrow = true;
        }

        if (!inputManager.isKeyDown(KeyEvent.VK_LEFT) && !inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
            checkArrow = false;
        }
    }

    protected void draw() {
        drawManager.initDrawing(this);

        drawManager.drawCollectionsMenu(this);

        switch (CollectionsScreenCode){
            case 0:
                drawManager.drawCollectionsData(this, collectionsStatistics);
                break;
            case 1:
                System.out.println(CollectionsScreenCode);
                break;
            case 2:
                System.out.println(CollectionsScreenCode);
                break;
            default:
                break;
        }

        super.draw();
        drawManager.completeDrawing(this);
    }
}
