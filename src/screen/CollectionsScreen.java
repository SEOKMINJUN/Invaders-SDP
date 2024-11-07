package screen;

import engine.Core;
import engine.Globals;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Logger;

public class CollectionsScreen extends Screen {

    public static int CollectionsScreenCode;
    private boolean checkArrow;
    private boolean isFirst;

    public CollectionsScreen(final int width, final int height, final int fps) {
        super(width, height, fps);

        CollectionsScreenCode = 0;
        checkArrow = false;
        isFirst = true;
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
            if (CollectionsScreenCode >= 0) {
                CollectionsScreenCode--;
            }
            if (CollectionsScreenCode == -1) {
                CollectionsScreenCode = 2;
            }
            //System.out.println("Activate LEFT" + CollectionsScreenCode);
            checkArrow = true;
        }
        else if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                && this.inputDelay.checkFinished() && !checkArrow) {
            if (CollectionsScreenCode <= 2) {
                CollectionsScreenCode++;
            }
            if (CollectionsScreenCode == 3) {
                CollectionsScreenCode = 0;
            }
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

        super.draw();
        drawManager.completeDrawing(this);
    }
}
