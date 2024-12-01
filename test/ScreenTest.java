import engine.DrawManager;
import engine.Frame;
import engine.Globals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import screen.CollectionsScreen;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static screen.CollectionsScreen.CollectionsScreenCode;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ScreenTest {

    private static Frame frame;
    private int x;
    private int y;
    private CollectionsScreen collectionsScreen;

    @BeforeEach
    void setUp() {
        frame = new Frame(Globals.WIDTH, Globals.HEIGHT);
        x = frame.getWidth();
        y = frame.getHeight();

        collectionsScreen = new CollectionsScreen(800, 600, 60);
        DrawManager.getInstance().setFrame(new Frame(x, y) {
            @Override
            public Graphics getGraphics() {
                return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
            }
        });
    }

    @Test
    public void CollectionsScreenTest() {

    }
}
