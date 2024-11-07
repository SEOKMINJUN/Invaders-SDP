package screen;

import engine.Globals;

import java.awt.event.KeyEvent;
import java.io.IOException;

public class CollectionsScreen extends Screen {

    public CollectionsScreen(final int width, final int height, final int fps) {
        super(width, height, fps);

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
    }

    protected void draw() {
        drawManager.initDrawing(this);

        drawManager.drawCollectionsMenu(this);

        super.draw();
        drawManager.completeDrawing(this);
    }

}
